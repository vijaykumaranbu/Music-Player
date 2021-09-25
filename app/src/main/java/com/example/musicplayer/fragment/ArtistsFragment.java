package com.example.musicplayer.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.R;
import com.example.musicplayer.activities.AlbumSongsActivity;
import com.example.musicplayer.adapter.ArtistAdapter;
import com.example.musicplayer.adapter.TrackAdapter;
import com.example.musicplayer.databinding.FragmentArtistsBinding;
import com.example.musicplayer.listener.ArtistListener;
import com.example.musicplayer.listener.AudioListener;
import com.example.musicplayer.model.ArtistModel;
import com.example.musicplayer.utilities.Constants;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment implements ArtistListener {

    public static ArrayList<ArtistModel> artistList;
    private FragmentArtistsBinding binding;
    private Context context;

    public ArtistsFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentArtistsBinding.inflate(inflater,container,false);
        artistList = Constants.getAllArtists(context);
        ArtistAdapter adapter = new ArtistAdapter(context,artistList,this);
        binding.artistRecyclerview.setAdapter(adapter);
        binding.artistRecyclerview.setHasFixedSize(true);
        return binding.getRoot();
    }

    @Override
    public void onArtistClicked(ArtistModel artist,int position) {
        Intent intent = new Intent(context, AlbumSongsActivity.class);
        intent.putExtra(Constants.KEY_FRAGMENT,Constants.KEY_ARTIST);
        intent.putExtra(Constants.KEY_ARTIST,artist.getArtist());
        intent.putExtra(Constants.KEY_TOTAL_SONGS,artist.getTotalSongs());
        startActivity(intent);
    }
}