package ru.korsander.simplersswidget.datalayer;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

import ru.korsander.simplersswidget.businesslayer.objects.Channel;
import ru.korsander.simplersswidget.utils.L;

/**
 * Created by korsander on 21.08.16.
 */
public class NetworkService extends IntentService {
    public static final String TAG = "NetworkService";

    private static final String ACTION_LOAD_RSS = "ru.korsander.simplersswidget.datalayer.action.LOAD_RSS";
    private static final String ACTION_LOAD_RSS_COMPLETE = "ru.korsander.simplersswidget.datalayer.action.LOAD_RSS_COMPLETE";

    private static final String EXTRA_RSS_LINK = "ru.korsander.simplersswidget.datalayer.extra.RSS_LINK";

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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }
}