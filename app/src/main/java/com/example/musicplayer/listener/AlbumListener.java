package com.example.musicplayer.listener;

import com.example.musicplayer.model.AlbumModel;

public interface AlbumListener {
    void onAlbumClicked(AlbumModel album, int position);
}
