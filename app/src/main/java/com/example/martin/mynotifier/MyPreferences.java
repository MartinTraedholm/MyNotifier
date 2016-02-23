package com.example.martin.mynotifier;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Objects;

/**
 * Created by Martin on 20/02/2016.
 */


public class MyPreferences
{
    public static final String MY_PREF = "com.example.martin.mynotifier.MyPreferences";
    public static final String MY_PREFS_LAST_BATERRY_LEVEL = "com.example.martin.mynotifier.MyPreferences.lastBaterryLevel";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public MyPreferences(Context context)
    {
        this.sharedPreferences = context.getSharedPreferences(MY_PREF,0);
        this.editor = this.sharedPreferences.edit();
    }
    public <T> boolean setObject(String key, T value ) {
        if(value instanceof String){
            this.editor.putString(key, (String)value);
        }else if( value instanceof Boolean){
            this.editor.putBoolean(key, (Boolean) value);
        }else if(value instanceof Float){
            this.editor.putFloat(key,(Float) value);
        }else{
            return false;
        }
        this.editor.commit();
        return true;
    }
    public Object get(String key, Object defaultVal) {
        Map getAllSharedPrefs = this.sharedPreferences.getAll();
        Object returnVal = null;
        if(false == this.sharedPreferences.contains(key))
        {
            if(false == setObject(key, defaultVal))
                return returnVal;
        }
        Object o = getAllSharedPrefs.get(key);

        if(o instanceof String)
        {
            returnVal = this.sharedPreferences.getString(key, "Error!!");
        }else if(o instanceof Float)
        {
            returnVal = this.sharedPreferences.getFloat(key, -1);
        }return returnVal;
    }

    public void clear(String key) {
        this.editor.remove(key);
        this.editor.commit();
    }

    public void clearAll() {
        this.editor.clear();
        this.editor.commit();
    }
}
