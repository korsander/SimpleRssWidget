package ru.korsander.simplersswidget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by korsander on 16.08.16.
 */
public class ConfigurationActivity extends AppCompatActivity {
    public static final String TAG = "ConfigurationActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_configuration_activity);
    }

    protected void replaceFragment(Fragment fragment, String tag, boolean allowBackStack) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_holder, fragment, tag)
                .addToBackStack(allowBackStack ? tag : null)
                .commitAllowingStateLoss();
    }
}
