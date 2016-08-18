package ru.korsander.simplersswidget;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by korsander on 18.08.16.
 */
public class SettingsFragment extends Fragment {
    public static final String TAG = "SettingsFragment";
    private static String EXTRA_TITLE = TAG + "_title";

    public static SettingsFragment newInstance(Context context) {
        final SettingsFragment fragment = new SettingsFragment();
        final Bundle arguments = new Bundle();

        arguments.putString(EXTRA_TITLE, context.getString(R.string.title_settings_fragment));

        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
