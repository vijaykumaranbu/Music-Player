package com.example.musicplayer.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.musicplayer.fragment.AlbumsFragment;
import com.example.musicplayer.fragment.ArtistsFragment;
import com.example.musicplayer.fragment.FoldersFragment;
import com.example.musicplayer.fragment.TracksFragment;

public class ViewStateAdapter extends FragmentStateAdapter {

    public ViewStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0)
            return new TracksFragment();
        else if (position == 1)
            return new AlbumsFragment();
        else if (position == 2)
            return new ArtistsFragment();
        else
            return new FoldersFragment();
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
