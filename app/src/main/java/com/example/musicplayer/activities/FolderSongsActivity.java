package com.example.musicplayer.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.adapter.TrackAdapter;
import com.example.musicplayer.databinding.ActivityFolderSongsBinding;
import com.example.musicplayer.fragment.TracksFragment;
import com.example.musicplayer.listener.AudioListener;
import com.example.musicplayer.model.AudioModel;
import com.example.musicplayer.utilities.Constants;

import java.util.ArrayList;

public class FolderSongsActivity extends AppCompatActivity implements AudioListener {

    private ActivityFolderSongsBinding binding;
    public static ArrayList<AudioModel> audioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFolderSongsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.name.setText(getIntent().getStringExtra(Constants.KEY_FOLDER));
        audioList = Constants.getFolderAudioList(TracksFragment.audioList,getIntent().getStringExtra(Constants.KEY_FOLDER_PATH));
        TrackAdapter adapter = new TrackAdapter(getApplicationContext(),audioList,this);
        binding.folderSongsRecyclerview.setAdapter(adapter);
        binding.folderSongsRecyclerview.setHasFixedSize(true);
        binding.imageBack.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onAudioClicked(int position) {
        Intent intent = new Intent(FolderSongsActivity.this,PlayerActivity.class);
        intent.putExtra(Constants.KEY_FRAGMENT,Constants.KEY_FOLDER);
        intent.putExtra(Constants.KEY_POSITION,position);
        startActivity(intent);
    }
}