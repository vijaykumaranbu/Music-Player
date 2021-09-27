package com.example.musicplayer.model;

public class FolderModel {

    private String name;
    private String path;
    private String totalSongs;

    public FolderModel(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTotalSongs() {
        return totalSongs;
    }

    public void setTotalSongs(String totalSongs) {
        this.totalSongs = totalSongs;
    }
}
