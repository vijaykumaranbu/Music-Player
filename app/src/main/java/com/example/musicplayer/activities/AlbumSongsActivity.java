package com.example.musicplayer.activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.TrackAdapter;
import com.example.musicplayer.database.PreferenceManager;
import com.example.musicplayer.databinding.ActivityAlbumSongsBinding;
import com.example.musicplayer.fragment.TracksFragment;
import com.example.musicplayer.listener.AudioListener;
import com.example.musicplayer.model.AudioModel;
import com.example.musicplayer.utilities.Constants;

import java.util.ArrayList;

public class AlbumSongsActivity extends AppCompatActivity implements AudioListener,MediaPlayer.OnCompletionListener{

    private ActivityAlbumSongsBinding binding;
    private String album, albumArt, artist, totalSongs;
    public static ArrayList<AudioModel> audioList;
    private int position = 0,albumPosition;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumSongsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(this);
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
        loadAlbumDetails();
        setListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Constants.mediaPlayer != null){
            if(Constants.mediaPlayer.isPlaying()){
                if(preferenceManager.getInt(Constants.KEY_CURRENT_ALBUM_POSITION) == albumPosition){
                    binding.playFloatButton.setFabIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, getTheme()));
                }
                else{
                    binding.playFloatButton.setFabIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, getTheme()));
                }
            }

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(Constants.mediaPlayer != null){
            if(!Constants.mediaPlayer.isPlaying())
                Constants.mediaPlayer = null;
        }
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
        binding.playFloatButton.setOnClickListener(view -> {
            if(Constants.mediaPlayer == null || preferenceManager.getInt(Constants.KEY_CURRENT_ALBUM_POSITION) != albumPosition){
               if(audioList != null){
                   if(Constants.mediaPlayer != null){
                       if(Constants.mediaPlayer.isPlaying()){
                           Constants.mediaPlayer.stop();
                           Constants.mediaPlayer.release();
                       }
                   }
                   Constants.mediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(audioList.get(position).getPath()));
                   binding.playFloatButton.setFabIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, getTheme()));
                   Constants.mediaPlayer.start();
                   Constants.mediaPlayer.setOnCompletionListener(this);
                   preferenceManager.putInt(Constants.KEY_CURRENT_ALBUM_POSITION,albumPosition);
               }
            }
            else {
                if(Constants.mediaPlayer.isPlaying()){
                    binding.playFloatButton.setFabIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_play,getTheme()));
                    Constants.mediaPlayer.pause();
                }
                else{
                    binding.playFloatButton.setFabIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, getTheme()));
                    Constants.mediaPlayer.start();
                }
            }
        });
    }

    private void loadAlbumDetails() {
        if(getIntent().getStringExtra(Constants.KEY_FRAGMENT).equals(Constants.KEY_ALBUM)){
            album = getIntent().getStringExtra(Constants.KEY_ALBUM);
            albumArt = getIntent().getStringExtra(Constants.KEY_ALBUM_ART);
            artist = getIntent().getStringExtra(Constants.KEY_ARTIST);
            totalSongs = getIntent().getStringExtra(Constants.KEY_TOTAL_SONGS);
            albumPosition = getIntent().getIntExtra(Constants.KEY_POSITION,-1);
            binding.textAlbum.setText(album);
            binding.textArtist.setText(artist);
            binding.textSongs.setText(String.format("%s Songs",totalSongs));
            if(albumArt != null){
                Glide.with(getApplicationContext())
                        .load(albumArt)
                        .into(binding.image);
            }
            else {
                Glide.with(getApplicationContext())
                        .load(R.drawable.image_holder)
                        .into(binding.image);
            }
            audioList = getAlbumSongList(album);
            TrackAdapter adapter = new TrackAdapter(getApplicationContext(), audioList, this);
            binding.albumSongsRecyclerview.setAdapter(adapter);
            binding.albumSongsRecyclerview.setHasFixedSize(true);
        }
        else if(getIntent().getStringExtra(Constants.KEY_FRAGMENT).equals(Constants.KEY_ARTIST)){
            artist = getIntent().getStringExtra(Constants.KEY_ARTIST);
            totalSongs = getIntent().getStringExtra(Constants.KEY_TOTAL_SONGS);
            albumPosition = getIntent().getIntExtra(Constants.KEY_POSITION,-1);
            binding.textAlbum.setText("");
            binding.textArtist.setText(artist);
            binding.textSongs.setText(String.format("%s Songs",totalSongs));
            Glide.with(getApplicationContext())
                    .load(R.drawable.artist_placeholder)
                    .into(binding.image);
            audioList = getArtistSongList(artist);
            TrackAdapter adapter = new TrackAdapter(getApplicationContext(), audioList, this);
            binding.albumSongsRecyclerview.setAdapter(adapter);
            binding.albumSongsRecyclerview.setHasFixedSize(true);
        }

    }

    private ArrayList<AudioModel> getArtistSongList(String artist) {
        ArrayList<AudioModel> audioList = new ArrayList<>();
        for (int i = 0; i < TracksFragment.audioList.size(); i++) {
            if (artist.equals(TracksFragment.audioList.get(i).getArtist())) {
                audioList.add(TracksFragment.audioList.get(i));
            }
        }
        return audioList;
    }

    private ArrayList<AudioModel> getAlbumSongList(String album) {
        ArrayList<AudioModel> audioList = new ArrayList<>();
        for (int i = 0; i < TracksFragment.audioList.size(); i++) {
            if (album.equals(TracksFragment.audioList.get(i).getAlbum())) {
                audioList.add(TracksFragment.audioList.get(i));
            }
        }
        return audioList;
    }

    @Override
    public void onAudioClicked(int position) {
        Intent intent = new Intent(AlbumSongsActivity.this, PlayerActivity.class);
        intent.putExtra(Constants.KEY_FRAGMENT,Constants.KEY_ALBUM);
        intent.putExtra(Constants.KEY_POSITION,position);
        startActivity(intent);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        position++;
        if(audioList.size() <= position){
            position = 0;
            Constants.mediaPlayer.stop();
            Constants.mediaPlayer.release();
            binding.playFloatButton.setFabIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_play,getTheme()));
        }
        else{
            Constants.mediaPlayer.stop();
            Constants.mediaPlayer.release();
            Constants.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(position).getPath()));
            Constants.mediaPlayer.start();
            Constants.mediaPlayer.setOnCompletionListener(this);
        }
    }
}