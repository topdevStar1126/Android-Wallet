package com.alphawallet.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import org.web3j.crypto.Credentials;

public class SharedPreferencesManager {
    private static final String PREFERENCES_NAME = "PRIVATEKEY-STORE";
    private static SharedPreferencesManager instance;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static synchronized SharedPreferencesManager getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPreferencesManager(context.getApplicationContext());
        }
        return instance;
    }

    public void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    // Add other methods for different data types as needed
}
