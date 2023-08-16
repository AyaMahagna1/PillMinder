package com.application.pillminderplus.database;

import android.content.Context;
import android.content.SharedPreferences;
//If you have a relatively small collection of key-values that you'd like to save, you should use the SharedPreferences APIs. A SharedPreferences object points to a file containing key-value pairs and provides simple methods to read and write them. Each SharedPreferences file is managed by the framework and can be private or shared.
public class SharedPreferenceManager {
    private static SharedPreferenceManager sharedPreferenceManager;
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private SharedPreferenceManager(Context context, String fileName) {
        sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferenceManager getInstance(Context context, String fileName) {
        if (sharedPreferenceManager == null) {
            sharedPreferenceManager = new SharedPreferenceManager(context, fileName);
        }
        return sharedPreferenceManager;
    }

    public void setValue(String key, Object value) {
        if (value instanceof Integer) {
            editor.putInt(key, (int) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (boolean) value);
        }
        editor.apply();
    }

    public boolean getBooleanValue(String keyFlag) {
        return sharedPreferences.getBoolean(keyFlag, false);
    }

    public String getStringValue(String key) {
        return sharedPreferences.getString(key, "null");
    }
}
