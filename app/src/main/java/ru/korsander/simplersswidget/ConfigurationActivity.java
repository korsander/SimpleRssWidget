package ru.korsander.simplersswidget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by korsander on 16.08.16.
 */
public class ConfigurationActivity extends AppCompatActivity {
    public static final String TAG = "ConfigurationActivity";

    protected int mWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResult(RESULT_OK); //TODO для теста. после реализации добавления адреса RSS ленты нужно поменять на cancel

        setContentView(R.layout.layout_configuration_activity);

        if (getIntent() == null) {
            handleIntent(false);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(true);
    }



    protected void handleIntent(boolean isFromNewIntent) {
        final Intent intent = getIntent();
        final Bundle extras = intent.getExtras();

        if (extras != null) {
            mWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        replaceFragment(SettingsFragment.newInstance(this), SettingsFragment.TAG, false);
    }

    protected void replaceFragment(Fragment fragment, String tag, boolean allowBackStack) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_holder, fragment, tag)
                .addToBackStack(allowBackStack ? tag : null)
                .commitAllowingStateLoss();
    }
}
