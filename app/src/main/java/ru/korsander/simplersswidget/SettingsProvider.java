package ru.korsander.simplersswidget;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by korsander on 17.08.16.
 */
public class SettingsProvider {
    public static final String TAG = "SettingsProvider";
    public static final String PREFERENCE_INTERVAL = TAG + "_interval";

    public static final int DEFAULT_UPDATE_INTERVAL = 60 * 1000;

    private static SettingsProvider sInstance;
    private SharedPreferences mPreferences;

    public static SettingsProvider getInstance(Context context) {
        SettingsProvider localInstance = sInstance;
        if(localInstance == null) {
            synchronized (SettingsProvider.class) {
                localInstance = sInstance;
                if(localInstance == null) {
                    sInstance = localInstance = new SettingsProvider(context);
                }
            }
        }

        return localInstance;
    }

    private SettingsProvider(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int getUpdateInterval() {
        return mPreferences.getInt(PREFERENCE_INTERVAL, DEFAULT_UPDATE_INTERVAL);
    }

    public void setUpdateInterval(int interval) {
        mPreferences.edit().putInt(PREFERENCE_INTERVAL, interval).apply();
    }
}
