package com.example.musicplayer.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.musicplayer.utilities.Constants;

public class PreferenceManager {

    private SharedPreferences preference;

    public PreferenceManager(Context context){
        preference = (SharedPreferences) context.getSharedPreferences(Constants.KEY_PREFERENCE,Context.MODE_PRIVATE);
    }

    public void putString(String key,String value){
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getString(String key){
        return preference.getString(key,Constants.PLAY_MODE_LOOP);
    }

}
