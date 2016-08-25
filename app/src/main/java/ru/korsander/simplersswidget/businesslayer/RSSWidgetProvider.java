package ru.korsander.simplersswidget.businesslayer;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
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
    public static final String TAG = "RSSWidgetProvider";
    public enum ClickType {
        left, right;

        public static ClickType fromString(String src) {
            try {
                return ClickType.valueOf(src);
            } catch (IllegalArgumentException e) {
                return right;
            }
        }
    }
    public static final String ACTION_RSS_WIDGET_BUTTON_CLICK = "ru.korsander.simplersswidget.datalayer.action.RSS_WIDGET_BUTTON_CLICK";

    public static final String EXTRA_CLICK_TYPE = TAG + "_click_type";

    public static final String WIDGET_TITLE_PATTERN = "%d/%d - %s";

    private static final SparseArray<Channel> mChannels = new SparseArray<>();
    private static final SparseIntArray mPositions = new SparseIntArray();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0; i < appWidgetIds.length; i++) {
            final int id = appWidgetIds[i];

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_rss_widget);

            int position = mPositions.get(id);
            final Channel channel = mChannels.get(id);
            L.d("id = " + id + " position = " + position);

            if (channel != null) {
                views.setTextViewText(R.id.widget_title, String.format(WIDGET_TITLE_PATTERN, position + 1, channel.getItems().size(), channel.title));
                final Item item = channel.getItems().get(position);
                views.setTextViewText(R.id.title, item.title);
                views.setTextViewText(R.id.time, item.getFormatedDateTime());
                views.setTextViewText(R.id.desription, item.description);
                views.setOnClickPendingIntent(R.id.button_left, getClickIntent(context, ClickType.left, id));
                views.setOnClickPendingIntent(R.id.button_right, getClickIntent(context, ClickType.right, id));
            }


            appWidgetManager.updateAppWidget(id, views);
            if(channel == null) {
                NetworkService.scheduleUpdate(context, id);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (NetworkService.ACTION_LOAD_RSS_COMPLETE.equals(intent.getAction())) {
            final Channel channel = intent.getParcelableExtra(NetworkService.EXTRA_CHANNEL);
            final int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            if(id == AppWidgetManager.INVALID_APPWIDGET_ID) {
                return;
            }

            mChannels.put(id, channel);
            handleUpdate(context);
        } else if (ACTION_RSS_WIDGET_BUTTON_CLICK.equals(intent.getAction())) {
            final String extraType = intent.getStringExtra(EXTRA_CLICK_TYPE);
            final int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if (TextUtils.isEmpty(extraType) || id == AppWidgetManager.INVALID_APPWIDGET_ID) {
                return;
            }

            int position = mPositions.get(id);
            final Channel channel = mChannels.get(id);

            if (channel == null) {
                return;
            }

            switch (ClickType.fromString(extraType)) {
                case left:
                    if (position > 0) {
                        position--;
                    }
                    break;
                case right:
                default:
                    if (position < channel.getItems().size() - 1) {
                        position++;
                    }
                    break;
            }

            mPositions.put(id, position);
            handleUpdate(context);
        } else {
            super.onReceive(context, intent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        final int[] ids = getWidgetsIds(context);
        for (int id : ids) {
            NetworkService.scheduleUpdate(context, id);
        }

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        NetworkService.scheduleUpdate(context, appWidgetId);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        final int[] ids = getWidgetsIds(context);
        for (int id : ids) {
            NetworkService.clearUpdate(context, id);
        }

        final RSSWidgetApplication application = (RSSWidgetApplication) context.getApplicationContext();
        application.flushCache();
    }

    private void handleUpdate(Context context) {
        final int[] ids = getWidgetsIds(context);
        onUpdate(context, AppWidgetManager.getInstance(context), ids);
    }

    private int[] getWidgetsIds(Context context) {
        final AppWidgetManager manager = AppWidgetManager.getInstance(context);
        final ComponentName provider = new ComponentName(context.getPackageName(), getClass().getName());
        return manager.getAppWidgetIds(provider);
    }

    protected static PendingIntent getClickIntent(Context context, ClickType type, int id) {
        final Intent intent = new Intent(context, RSSWidgetProvider.class);
        intent.setAction(ACTION_RSS_WIDGET_BUTTON_CLICK);
        intent.putExtra(EXTRA_CLICK_TYPE, type.name());
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        return PendingIntent.getBroadcast(context, id * 10 + type.ordinal(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
