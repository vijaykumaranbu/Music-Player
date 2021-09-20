package com.example.musicplayer.model;

import java.io.Serializable;

public class AudioModel implements Serializable {

    private String name;
    private String artist;
    private String path;
    private String album;
    private String duration;

    public AudioModel(String name, String artist, String path, String album, String duration) {
        this.name = name;
        this.artist = artist;
        this.path = path;
        this.album = album;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public String getPath() {
        return path;
    }

    public String getAlbum() {
        return album;
    }

    public String getDuration() {
        return duration;
    }
}

