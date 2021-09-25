package com.example.musicplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.musicplayer.activities.AlbumSongsActivity;
import com.example.musicplayer.adapter.AlbumAdapter;
import com.example.musicplayer.databinding.FragmentAlbumsBinding;
import com.example.musicplayer.listener.AlbumListener;
import com.example.musicplayer.model.AlbumModel;
import com.example.musicplayer.utilities.Constants;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment implements AlbumListener {

    private FragmentAlbumsBinding binding;
    private Context context;

    public AlbumsFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAlbumsBinding.inflate(inflater,container,false);
        binding.albumRecyclerview.setHasFixedSize(true);
        binding.albumRecyclerview.setLayoutManager(new GridLayoutManager(getContext(),2));
        AlbumAdapter adapter = new AlbumAdapter(getContext(), Constants.getAllAlbums(context),this);
        binding.albumRecyclerview.setAdapter(adapter);
        return binding.getRoot();
    }

    @Override
    public void onAlbumClicked(AlbumModel album, int position) {
        Intent intent = new Intent(context, AlbumSongsActivity.class);
        intent.putExtra(Constants.KEY_ALBUM,album.getAlbum());
        intent.putExtra(Constants.KEY_ARTIST,album.getArtist());
        intent.putExtra(Constants.KEY_ALBUM_ART,album.getAlbumArt());
        startActivity(intent);
    }
}