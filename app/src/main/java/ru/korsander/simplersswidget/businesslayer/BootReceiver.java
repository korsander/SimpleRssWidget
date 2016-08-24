package ru.korsander.simplersswidget.businesslayer;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import ru.korsander.simplersswidget.datalayer.NetworkService;

/**
 * Created by korsander on 18.08.16.
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final AppWidgetManager manager = AppWidgetManager.getInstance(context);
        final int[] ids = manager.getAppWidgetIds(new ComponentName(context.getPackageName(), RSSWidgetProvider.class.getName()));
        for (int id : ids) {
            NetworkService.scheduleUpdate(context, id);
        }
    }
}
