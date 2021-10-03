package com.example.musicplayer.activities;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.adapter.TrackAdapter;
import com.example.musicplayer.databinding.ActivityAlbumSongsBinding;
import com.example.musicplayer.fragment.TracksFragment;
import com.example.musicplayer.listener.AudioListener;
import com.example.musicplayer.model.AudioModel;
import com.example.musicplayer.utilities.Constants;

import java.util.ArrayList;

public class AlbumSongsActivity extends AppCompatActivity implements AudioListener {

    private ActivityAlbumSongsBinding binding;
    private String album, albumArt, artist, totalSongs;
    public static ArrayList<AudioModel> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumSongsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        window.setStatusBarColor(Color.TRANSPARENT);
        loadAlbumDetails();
        setListeners();
    }

    private void setListeners() {
        binding.imageBack.setOnClickListener(view -> onBackPressed());
        binding.playFloatButton.setOnClickListener(view -> {
            if(PlayerActivity.mediaPlayer.isPlaying() && PlayerActivity.mediaPlayer != null){
                PlayerActivity.mediaPlayer.stop();
                PlayerActivity.mediaPlayer.release();
                binding.playFloatButton.setFabIcon(ResourcesCompat.getDrawable(getResources(),R.drawable.ic_play,getTheme()));
            }
            else {
                if (audioList != null) {
                    PlayerActivity.mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(audioList.get(0).getPath()));
                    PlayerActivity.mediaPlayer.start();
                    binding.playFloatButton.setFabIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, getTheme()));
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
}