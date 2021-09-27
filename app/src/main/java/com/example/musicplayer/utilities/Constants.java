package com.example.musicplayer.utilities;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.provider.MediaStore;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.musicplayer.model.AlbumModel;
import com.example.musicplayer.model.ArtistModel;
import com.example.musicplayer.model.AudioModel;
import com.example.musicplayer.model.FolderModel;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

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
    public static final String KEY_FRAGMENT = "fragment";
    public static final String KEY_TRACK = "trackFragment";
    public static final String KEY_TOTAL_SONGS = "totalSongs";
    public static final String KEY_FOLDER_PATH = "folderPath" ;
    private static final String TAG = "Constants";
    public static final String KEY_FOLDER = "folder";

    public static ArrayList<AudioModel> getAllAudios(Context context) {
        ArrayList<AudioModel> audioList = new ArrayList<>();
        final Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        final String[] cursor_cols = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION};
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

            String parentPath = new File(data).getParent();
            Log.d(TAG, "getAllAudios: dirName : " + parentPath);

            AudioModel audioModel = new AudioModel();
            audioModel.setName(track);
            audioModel.setArtist(artist);
            audioModel.setPath(data);
            audioModel.setAlbum(album);
            audioModel.setParentPath(parentPath);
            audioModel.setDuration(duration);
            audioModel.setAlbumArtUri(albumArtUri);

            audioList.add(audioModel);
        }
        return audioList;
    }

    public static ArrayList<AlbumModel> getAllAlbums(Context context) {
        ArrayList<AlbumModel> albumList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
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

    public static ArrayList<ArtistModel> getAllArtists(Context context) {
        ArrayList<ArtistModel> artistList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ArtistModel artistModel = new ArtistModel();
                artistModel.setArtist(cursor.getString(0));
                artistModel.setTotalSongs(cursor.getString(1));
                artistList.add(artistModel);
            }
            cursor.close();
        }
        return artistList;
    }

    public static ArrayList<FolderModel> getAudioFolders(Context context) {

        ArrayList<FolderModel> folderList = new ArrayList<>();
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();

        File file = new File(root);
        File fileSD = SDCard.findSdCardPath(context);

        Log.d(TAG, "root: " + root);
        Log.d(TAG, "root SD: " + fileSD.getAbsolutePath());

        File[] list = file.listFiles();
        File[] listSD = fileSD.listFiles();


        assert list != null;
        for (File value : list) {
            File mFile = new File(file,value.getName());
            File[] dirList = mFile.listFiles();
            if (dirList == null) continue;
            for (File item : dirList) {
                if (item.getName().toLowerCase(Locale.getDefault()).endsWith(".mp3")) {
                    FolderModel folderModel = new FolderModel();
                    folderModel.setName(value.getName());
                    folderModel.setPath(value.getPath());
                    folderModel.setTotalSongs(getAudioFileCount(context, value.getPath()));
                    Log.d(TAG, "folder Path: " + value.getPath());
                    folderList.add(folderModel);
                    break;
                }
            }
        }

        assert listSD != null;
        for (File value : listSD) {
            File mFile = new File(fileSD, value.getName());
            File[] dirList = mFile.listFiles();
            if (dirList == null) continue;
            for (File item : dirList) {
                if (item.getName().toLowerCase(Locale.getDefault()).endsWith(".mp3")) {
                    FolderModel folderModel = new FolderModel();
                    folderModel.setName(value.getName());
                    folderModel.setPath(value.getPath());
                    folderModel.setTotalSongs(getAudioFileCount(context, value.getPath()));
                    Log.d(TAG, "folder SD Path: " + value.getPath());
                    folderList.add(folderModel);
                    break;
                }
            }
        }

        return folderList;

    }

    private static String getAudioFileCount(Context context, String dirPath) {
        String selection = MediaStore.Audio.Media.DATA + " like ?";
        String[] projection = {MediaStore.Audio.Media.DATA};
        String[] selectionArgs = {dirPath + "%"};
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                selectionArgs,
                null);
        return String.valueOf(cursor.getCount());
    }

    public static ArrayList<AudioModel> getFolderAudioList(ArrayList<AudioModel> audioList,String parent) {
        ArrayList<AudioModel> audios = new ArrayList<>();
        if (audioList != null) {
            for (int i = 0; i < audioList.size(); i++) {
                if (audioList.get(i).getParentPath().equals(parent)) {
                    audios.add(audioList.get(i));
                }
            }
        }
        return audios;
    }

    public static String removeMP3FormString(String name) {
        return name.replace(".mp3", "");
    }

    private static String getExternalStoragePath(Context mContext, boolean is_removable) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removable == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
