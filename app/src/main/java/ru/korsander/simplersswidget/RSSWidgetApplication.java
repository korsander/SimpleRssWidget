package ru.korsander.simplersswidget;

import android.app.Application;
import android.net.http.HttpResponseCache;

import java.io.File;
import java.io.IOException;

import ru.korsander.simplersswidget.utils.L;

/**
 * Created by korsander on 20.08.16.
 */
public class RSSWidgetApplication extends Application {
    public static final String TAG = "RSSWidgetApplication";

    public static final String HTTP_CACHE_DIR = "http";
    public static final int HTTP_CACHE_DIR_SIZE = 5 * 1024 * 1024;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            File httpCacheDir = new File(this.getCacheDir(), HTTP_CACHE_DIR);
            HttpResponseCache.install(httpCacheDir, HTTP_CACHE_DIR_SIZE);
        } catch (IOException e) {
            L.e(L.MESSAGE_ERROR_CACHE_INSTALLATION_FAILED, e);
        }
    }

    public void flushCache() {
        HttpResponseCache cache = HttpResponseCache.getInstalled();
        if (cache != null) {
            cache.flush();
        }
    }
}
