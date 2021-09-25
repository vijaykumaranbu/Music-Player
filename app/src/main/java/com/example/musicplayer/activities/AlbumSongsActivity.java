package com.example.musicplayer.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
    private String album, albumArt, artist;
    public static ArrayList<AudioModel> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlbumSongsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadAlbumDetails();
        setListeners();
    }

    private void setListeners() {
        binding.playFloatButton.setOnClickListener(view -> {

        });
    }

    private void loadAlbumDetails() {
        album = getIntent().getStringExtra(Constants.KEY_ALBUM);
        albumArt = getIntent().getStringExtra(Constants.KEY_ALBUM_ART);
        artist = getIntent().getStringExtra(Constants.KEY_ARTIST);
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
        audioList = getAlbumSongsList(album);
        TrackAdapter adapter = new TrackAdapter(getApplicationContext(), audioList, this);
        binding.albumSongsRecyclerview.setAdapter(adapter);
        binding.albumSongsRecyclerview.setHasFixedSize(true);
    }

    private ArrayList<AudioModel> getAlbumSongsList(String album) {
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