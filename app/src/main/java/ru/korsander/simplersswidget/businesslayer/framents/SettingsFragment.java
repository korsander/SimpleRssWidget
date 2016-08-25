package ru.korsander.simplersswidget.businesslayer.framents;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import ru.korsander.simplersswidget.R;
import ru.korsander.simplersswidget.businesslayer.SettingsAdapter;
import ru.korsander.simplersswidget.businesslayer.objects.ExtendedSetting;
import ru.korsander.simplersswidget.businesslayer.objects.Setting;
import ru.korsander.simplersswidget.datalayer.SettingsProvider;

/**
 * Created by korsander on 18.08.16.
 */
public class SettingsFragment extends Fragment implements SettingsAdapter.IClickListener, LinkDialogFragment.IDismissListener {
    public static final String TAG = "SettingsFragment";

    protected static String EXTRA_TITLE = TAG + "_title";
    protected static String EXTRA_WIDGET_ID = TAG + "_id";

    protected String mTitle;
    protected int mWidgetId;

    protected RecyclerView mRecycler;

    protected ArrayList<Setting> mItems = new ArrayList<>();

    public static SettingsFragment newInstance(Context context, int widgetId) {
        final SettingsFragment fragment = new SettingsFragment();
        final Bundle arguments = new Bundle();

        arguments.putString(EXTRA_TITLE, context.getString(R.string.title_settings_fragment));
        arguments.putInt(EXTRA_WIDGET_ID, widgetId);

        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if (bundle != null) {
            mTitle = bundle.getString(EXTRA_TITLE);
            mWidgetId = bundle.getInt(EXTRA_WIDGET_ID);
        }

        if (mItems.size() == 0) {
            mItems.add(new ExtendedSetting("Link", SettingsProvider.getInstance(getContext()).getRSSUrl(mWidgetId)));
            mItems.add(new Setting("About"));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mRecycler = (RecyclerView) view.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        final SettingsAdapter adapter = new SettingsAdapter(mItems);
        adapter.setOnClickListener(this);
        mRecycler.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

        setTitle();
        updateLink();
    }

    protected void setTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            return;
        }

        getActivity().setTitle(mTitle);
    }

    protected void updateLink() {
        if (mItems.size() > 1) {
            final ExtendedSetting extended = (ExtendedSetting) mItems.get(0);
            extended.desc = SettingsProvider.getInstance(getContext()).getRSSUrl(mWidgetId);
            mRecycler.getAdapter().notifyDataSetChanged();
        }
    }

    protected void showLinkDialog() {
        final LinkDialogFragment dialog = LinkDialogFragment.newInstance(mWidgetId);
        dialog.setDismissListener(this);
        dialog.show(getChildFragmentManager(), LinkDialogFragment.TAG);
    }

    @Override
    public void onClick(Setting item, int position) {
        if (item instanceof ExtendedSetting) {
            showLinkDialog();
        } else {
            Toast.makeText(getContext(), item.title, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDialogDismiss() {
        updateLink();
    }
}
