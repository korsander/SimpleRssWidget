package ru.korsander.simplersswidget.businesslayer;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.SparseIntArray;
import android.widget.RemoteViews;

import ru.korsander.simplersswidget.R;
import ru.korsander.simplersswidget.businesslayer.objects.Channel;
import ru.korsander.simplersswidget.businesslayer.objects.Item;
import ru.korsander.simplersswidget.datalayer.NetworkService;
import ru.korsander.simplersswidget.utils.L;

/**
 * Created by korsander on 16.08.16.
 */
public class RSSWidgetProvider extends AppWidgetProvider {

    private Channel mChannel;

    private SparseIntArray mPositions = new SparseIntArray();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            final int id = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_rss_widget);

            int position = mPositions.get(id);

            if (mChannel != null) {
                views.setTextViewText(R.id.article_title, position + "/" + mChannel.getItems().size());
                final Item item = mChannel.getItems().get(position);
                views.setTextViewText(R.id.title, item.title);
                views.setTextViewText(R.id.time, item.pubDate);
                views.setTextViewText(R.id.desription, item.description);
            }

            appWidgetManager.updateAppWidget(id, views);

            L.d(mChannel == null ? "channel null" : mChannel.getItems().size() + "");
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkService.ACTION_LOAD_RSS_COMPLETE.equals(intent.getAction())) {
            mChannel = intent.getParcelableExtra(NetworkService.EXTRA_CHANNEL);
            handleUpdate(context);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        NetworkService.scheduleUpdate(context);
    }

    @Override
    public void onDisabled(Context context) {
        NetworkService.clearUpdate(context);
        final RSSWidgetApplication application = (RSSWidgetApplication) context.getApplicationContext();
        application.flushCache();
    }

   private void handleUpdate(Context context) {
        final AppWidgetManager manager = AppWidgetManager.getInstance(context);
        final ComponentName provider = new ComponentName(context.getPackageName(), getClass().getName());
        final int[] ids = manager.getAppWidgetIds(provider);
        onUpdate(context, manager, ids);
    }

}
