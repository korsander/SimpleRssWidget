package ru.korsander.simplersswidget.datalayer;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by korsander on 17.08.16.
 */
public class SettingsProvider {
    public static final String TAG = "SettingsProvider";
    public static final String PREFERENCE_INTERVAL = TAG + "_interval";
    public static final String PREFERENCE_LAST_SUCCESS_UPDATE = TAG + "_last_success_update";
    public static final String PREFERENCE_RSS_URL = TAG + "_rss_url";

    public static final long DEFAULT_UPDATE_INTERVAL = 60 * 1000;

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

    public long getUpdateInterval() {
        return mPreferences.getLong(PREFERENCE_INTERVAL, DEFAULT_UPDATE_INTERVAL);
    }

    public void setUpdateInterval(long interval) {
        mPreferences.edit().putLong(PREFERENCE_INTERVAL, interval).apply();
    }

    public long getLastSucessUpdate() {
        return mPreferences.getLong(PREFERENCE_LAST_SUCCESS_UPDATE, 0);
    }

    public void setRSSUrl(int widgetId, String url) {
        mPreferences.edit().putString(PREFERENCE_RSS_URL + widgetId, url).apply();
    }

    public String getRSSUrl(int widgetId) {
        return mPreferences.getString(PREFERENCE_RSS_URL + widgetId, null);
    }

    public void setUpdateInterval(String url) {
        mPreferences.edit().putString(PREFERENCE_RSS_URL, url).apply();
    }

}
