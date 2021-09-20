package com.example.musicplayer.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.adapter.AudioListAdapter;
import com.example.musicplayer.databinding.ActivityMainBinding;
import com.example.musicplayer.listener.AudioListener;
import com.example.musicplayer.model.AudioModel;
import com.example.musicplayer.utilities.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AudioListener {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        runTimePermission();
    }

    private void runTimePermission() {
        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        loadAudios();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    private void loadAudios() {
        ArrayList<AudioModel> audioList = Constants.getAllAudios(getApplicationContext());
        AudioListAdapter adapter = new AudioListAdapter(getApplicationContext(), audioList, this);
        binding.songsRecyclerview.setAdapter(adapter);

    }

    @Override
    public void onAudioClicked(int position) {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.putExtra("position", position);
        startActivity(intent);
    }
}