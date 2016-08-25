package ru.korsander.simplersswidget.businesslayer.framents;

import android.app.Dialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import ru.korsander.simplersswidget.R;
import ru.korsander.simplersswidget.datalayer.SettingsProvider;

/**
 * Created by korsander on 25.08.16.
 */
public class LinkDialogFragment extends DialogFragment {
    public static final String TAG = "LinkDialogFragment";

    public interface IDismissListener {
        public void onDialogDismiss();
    }

    protected static final String EXTRA_WIDGET_ID = TAG + "_widget_id";

    protected IDismissListener mListener;

    public static LinkDialogFragment newInstance(int widgetId) {
        final LinkDialogFragment fragment = new LinkDialogFragment();
        final Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_WIDGET_ID, widgetId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());

        final int widgetId = getArguments().getInt(EXTRA_WIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        final View view = inflater.inflate(R.layout.layout_dialog_rss_link, null);
        final EditText editText = (EditText) view.findViewById(R.id.edit);
        editText.setText(SettingsProvider.getInstance(getContext()).getRSSUrl(widgetId));

        final TextView textView = (TextView) view.findViewById(R.id.example_lenta);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
                editText.setText(textView.getText());
            }
        });



        builder.setTitle(getString(R.string.title_link_dialog));
        builder.setView(view);
        builder.setPositiveButton(getString(R.string.title_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final String text = editText.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    SettingsProvider.getInstance(getContext()).setRSSUrl(widgetId, text);
                }
            }
        });
        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (mListener != null) {
            mListener.onDialogDismiss();
        }
    }

    public void setDismissListener(IDismissListener listener) {
        mListener = listener;
    }
}
