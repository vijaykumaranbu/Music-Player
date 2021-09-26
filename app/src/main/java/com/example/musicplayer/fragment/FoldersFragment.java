package com.example.musicplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.R;
import com.example.musicplayer.activities.FolderSongsActivity;
import com.example.musicplayer.adapter.FolderAdapter;
import com.example.musicplayer.databinding.FragmentFoldersBinding;
import com.example.musicplayer.listener.FolderListener;
import com.example.musicplayer.model.FolderModel;
import com.example.musicplayer.utilities.Constants;

import java.util.ArrayList;

public class FoldersFragment extends Fragment implements FolderListener {

    private FragmentFoldersBinding binding;
    private ArrayList<FolderModel> folderList;
    private Context context;

    public FoldersFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFoldersBinding.inflate(inflater,container,false);
        folderList = Constants.getAudioFolders(context);
        FolderAdapter adapter = new FolderAdapter(folderList,this);
        binding.folderRecyclerview.setAdapter(adapter);
        binding.folderRecyclerview.setHasFixedSize(true);
        return binding.getRoot();
    }

    @Override
    public void onFolderClicked(FolderModel folder) {
        Intent intent = new Intent(context, FolderSongsActivity.class);
        intent.putExtra(Constants.KEY_FOLDER,folder.getName());
        intent.putExtra(Constants.KEY_TOTAL_SONGS,folder.getTotalSongs());
        startActivity(intent);
    }
}