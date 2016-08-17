package ru.korsander.simplersswidget;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by korsander on 16.08.16.
 */
public class RSSWidgetProvider extends AppWidgetProvider {
    public static final String ACTION_UPDATE = "ru.korsander.simplersswidget.ACTION_UPDATE";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            final int id = appWidgetIds[i];

        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_UPDATE.equals(intent.getAction())) {
            handleUpdate(context);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        scheduleUpdate(context);
    }

    @Override
    public void onDisabled(Context context) {
        clearUpdate(context);
    }



    private void scheduleUpdate(Context context) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final PendingIntent pendingIntent = getPendingIntent(context);
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),
                SettingsProvider.getInstance(context).getUpdateInterval(),
                pendingIntent);
    }

    private void clearUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    public PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(ACTION_UPDATE);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void handleUpdate(Context context) {
        final AppWidgetManager manager = AppWidgetManager.getInstance(context);
        final ComponentName provider = new ComponentName(context.getPackageName(), getClass().getName());
        final int[] ids = manager.getAppWidgetIds(provider);
        onUpdate(context, manager, ids);
    }

}