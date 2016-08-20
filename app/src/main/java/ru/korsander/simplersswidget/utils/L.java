package ru.korsander.simplersswidget.utils;

import android.text.TextUtils;
import android.util.Log;

import ru.korsander.simplersswidget.RSSWidgetApplication;

/**
 * Created by korsander on 20.08.16.
 */
public class L {
    public static enum Level {
        verbose, debug, info, warning, error;
    }

    public static final String EMPTY = "(empty)";
    public static final char NEW_LINE = '\n';

    public static final String MESSAGE_ERROR_CACHE_INSTALLATION_FAILED = "HTTP response cache installation failed: ";

    public static void v(String message) {
        v(null, message, null);
    }

    public static void v(String tag, String message) {
        v(tag, message, null);
    }

    public static void v(String tag, Throwable throwable) {
        v(tag, null, throwable);
    }

    public static void v(String tag, String message, Throwable throwable) {
        log(Level.verbose, tag, message, throwable);
    }

    public static void d(String message) {
        d(null, message, null);
    }

    public static void d(String tag, String message) {
        d(tag, message, null);
    }

    public static void d(String tag, Throwable throwable) {
        d(tag, null, throwable);
    }

    public static void d(String tag, String message, Throwable throwable) {
        log(Level.debug, tag, message, throwable);
    }

    public static void i(String message) {
        i(null, message, null);
    }

    public static void i(String tag, String message) {
        i(tag, message, null);
    }

    public static void i(String tag, Throwable throwable) {
        i(tag, null, throwable);
    }

    public static void i(String tag, String message, Throwable throwable) {
        log(Level.info, tag, message, throwable);
    }

    public static void w(String message) {
        w(null, message, null);
    }

    public static void w(String tag, String message) {
        w(tag, message, null);
    }

    public static void w(String tag, Throwable throwable) {
        w(tag, null, throwable);
    }

    public static void w(String tag, String message, Throwable throwable) {
        log(Level.warning, tag, message, throwable);
    }

    public static void e(String message) {
        e(null, message, null);
    }

    public static void e(String tag, String message) {
        e(tag, message, null);
    }

    public static void e(String tag, Throwable throwable) {
        e(tag, null, throwable);
    }

    public static void e(String tag, String message, Throwable throwable) {
        log(Level.error, tag, message, throwable);
    }

    protected static void log(Level level, String tag, String message, Throwable throwable) {
        final String t = TextUtils.isEmpty(tag) ? RSSWidgetApplication.TAG : tag;

        final StringBuilder builder = new StringBuilder();
        builder.append(message);
        if (!TextUtils.isEmpty(builder.toString()) && throwable != null) {
            builder.append(NEW_LINE);
        }
        builder.append(Log.getStackTraceString(throwable));

        final String m = TextUtils.isEmpty(builder.toString()) ? EMPTY : builder.toString();

        switch (level) {
            case verbose:
                Log.v(t,m);
                break;

            case debug:
                Log.d(t,m);
                break;

            case warning:
                Log.w(t,m);
                break;

            case error:
                Log.e(t,m);
                break;

            default:
            case info:
                Log.i(t,m);
                break;
        }
    }
}
