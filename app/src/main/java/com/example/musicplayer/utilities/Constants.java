package com.example.musicplayer.utilities;

import static android.content.ContentValues.TAG;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicplayer.R;
import com.example.musicplayer.model.AlbumModel;
import com.example.musicplayer.model.AudioModel;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Constants {

    public static final String KEY_PREFERENCE = "musicPreference";
    public static final String PLAY_MODE_SHUFFLE = "shuffle";
    public static final String PLAY_MODE_REPEAT = "repeat";
    public static final String PLAY_MODE_LOOP = "loop";
    public static final String PLAY_MODE = "playMode";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_ALBUM = "album";
    public static final String KEY_ALBUM_ART = "albumArt";
    public static final String KEY_POSITION = "position";
    public static final String KEY_AUDIO_LIST = "audioList";
    public static final String KEY_FRAGMENT = "fragment";
    public static final String KEY_TRACK = "trackFragment";

    public static ArrayList<AudioModel> getAllAudios(Context context){
        ArrayList<AudioModel> audioList = new ArrayList<>();
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = { MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION };
        final String where = MediaStore.Audio.Media.IS_MUSIC + "=1";
        final Cursor cursor = context.getContentResolver().query(uri,
                cursor_cols, where, null, null);

        while (cursor.moveToNext()) {
            String artist = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
            String album = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
            String track = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
            String data = cursor.getString(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
            long albumId = cursor.getLong(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

            int duration = cursor.getInt(cursor
                    .getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));

            Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);

            Log.d(TAG, "getAllAudios: " + albumArtUri);
            Bitmap bitmap = null;
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(
//                        context.getContentResolver(), albumArtUri);
//                bitmap = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
//
//            } catch (FileNotFoundException exception) {
//                exception.printStackTrace();
//                bitmap = BitmapFactory.decodeResource(context.getResources(),
//                        R.drawable.image_holder);
//            } catch (IOException e) {
//
//                e.printStackTrace();
//            }

            AudioModel audioModel = new AudioModel();
            audioModel.setName(track);
            audioModel.setArtist(artist);
            audioModel.setPath(data);
            audioModel.setAlbum(album);
            audioModel.setDuration(duration);
            audioModel.setAlbumArtUri(albumArtUri);
            audioModel.setBitmap(bitmap);

            audioList.add(audioModel);
        }
        return audioList;
    }

    public static ArrayList<AlbumModel> getAllAlbums(Context context){
        ArrayList<AlbumModel> albumList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if(cursor != null){
            while (cursor.moveToNext()){
                AlbumModel albumModel = new AlbumModel(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3)
                );
                albumList.add(albumModel);
            }
            cursor.close();
        }
        return albumList;
    }

    public static String removeMP3FormString(String name) {
        return name.replace(".mp3", "");
    }
}
