package ru.korsander.simplersswidget.datalayer;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import ru.korsander.simplersswidget.businesslayer.RSSWidgetProvider;
import ru.korsander.simplersswidget.businesslayer.objects.Channel;
import ru.korsander.simplersswidget.utils.L;

/**
 * Created by korsander on 21.08.16.
 */
public class NetworkService extends IntentService {
    public static final String TAG = "NetworkService";

    public static final String ACTION_LOAD_RSS = "ru.korsander.simplersswidget.datalayer.action.LOAD_RSS";
    public static final String ACTION_LOAD_RSS_COMPLETE = "ru.korsander.simplersswidget.datalayer.action.LOAD_RSS_COMPLETE";

    public static final String EXTRA_RSS_LINK = "ru.korsander.simplersswidget.datalayer.extra.RSS_LINK";
    public static final String EXTRA_CHANNEL = "ru.korsander.simplersswidget.datalayer.extra.CHANNEL";

    public NetworkService() {
        super(TAG);
    }

    public static void startRSSLoading(Context context, String link) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(ACTION_LOAD_RSS);
        intent.putExtra(EXTRA_RSS_LINK, link);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            switch (action) {
                case ACTION_LOAD_RSS:
                    final String link = intent.getStringExtra(EXTRA_RSS_LINK);
                    if (!TextUtils.isEmpty(link)) {
                        handleRSSLoading(link);
                    }
                    break;

                default:
                    L.d(TAG, String.format(L.MESSAGE_WARNING_CANT_HANDLE_ACTION_PATTERN, action));
                    break;
            }
        }
    }

    private void handleRSSLoading(String link) {
        try {
            final HttpURLConnection connection = NetworkHelper.openGetConnection(link);
            final Channel result = RSSParser.parse(new BufferedInputStream(connection.getInputStream()));

            Intent intent = new Intent(getBaseContext(), RSSWidgetProvider.class);
            intent.setAction(ACTION_LOAD_RSS_COMPLETE);
            intent.putExtra(EXTRA_CHANNEL, result);
            sendBroadcast(intent);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public static void scheduleUpdate(Context context) {
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        final PendingIntent pendingIntent = getPendingIntent(context);
        alarmManager.cancel(pendingIntent);
        alarmManager.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis(),
                SettingsProvider.getInstance(context).getUpdateInterval(),
                pendingIntent);
    }

    public static PendingIntent getPendingIntent(Context context) {
        Intent intent = new Intent(context, NetworkService.class);
        intent.setAction(NetworkService.ACTION_LOAD_RSS);
        intent.putExtra(EXTRA_RSS_LINK, SettingsProvider.getInstance(context).getRSSUrl());
        return PendingIntent.getService(context, 0, intent, 0);
    }



    public static void clearUpdate(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }
}
