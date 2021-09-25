package com.example.musicplayer.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.activities.PlayerActivity;
import com.example.musicplayer.adapter.TrackAdapter;
import com.example.musicplayer.databinding.FragmentTracksBinding;
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

public class TracksFragment extends Fragment implements AudioListener {

    private FragmentTracksBinding binding;
    private Context context;
    public static ArrayList<AudioModel> audioList;

    public TracksFragment() { }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding  = FragmentTracksBinding.inflate(inflater,container,false);
        runTimePermission();
        return binding.getRoot();
    }

    private void runTimePermission() {
        Dexter.withContext(context)
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

    public void loadAudios() {
       audioList = Constants.getAllAudios(context);
       if(!audioList.isEmpty()){
           TrackAdapter adapter = new TrackAdapter(context,audioList,this);
           binding.songsRecyclerview.setAdapter(adapter);
           binding.songsRecyclerview.setHasFixedSize(true);
       }
    }

    @Override
    public void onAudioClicked(int position) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra(Constants.KEY_FRAGMENT,Constants.KEY_TRACK);
        intent.putExtra(Constants.KEY_POSITION,position);
        startActivity(intent);
    }
}