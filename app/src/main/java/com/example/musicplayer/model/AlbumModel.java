package com.example.musicplayer.model;

public class AlbumModel {

    private String album;
    private String artist;
    private String albumArt;
    private String totalSongs;

    public AlbumModel(String album, String artist,String albumArt, String totalSongs) {
        this.album = album;
        this.artist = artist;
        this.albumArt = albumArt;
        this.totalSongs = totalSongs;
    }

    public String getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbumArt() {
        return albumArt;
    }

    public String getTotalSongs() {
        return totalSongs;
    }
}
