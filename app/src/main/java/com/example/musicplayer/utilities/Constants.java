package com.example.musicplayer.utilities;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.musicplayer.model.AudioModel;

import java.util.ArrayList;

public class Constants {

    public static final String KEY_PREFERENCE = "musicPreference";
    public static final String PLAY_MODE_SHUFFLE = "shuffle";
    public static final String PLAY_MODE_REPEAT = "repeat";
    public static final String PLAY_MODE_LOOP = "loop";
    public static final String PLAY_MODE = "playMode";

    public static ArrayList<AudioModel> getAllAudios(Context context) {
        ArrayList<AudioModel> audioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION};
        Cursor cursor = context.getContentResolver()
                .query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                AudioModel audioModel = new AudioModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                audioList.add(audioModel);
            }
            cursor.close();
        }
        return audioList;
    }

    public static byte[] getAlbumArt(String uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public static String removeMP3FormString(String name) {
        return name.replace(".mp3", "");
    }

}
