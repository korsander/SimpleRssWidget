package ru.korsander.simplersswidget.businesslayer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ru.korsander.simplersswidget.businesslayer.objects.ExtendedSetting;
import ru.korsander.simplersswidget.businesslayer.objects.Setting;

/**
 * Created by korsander on 24.08.16.
 */
public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.Holder> {
    public static final String TAG = "SettingsAdapter";

    public interface IClickListener {
        public void onClick(Setting item, int position);
    }

    protected static final int ITEM_SINGLE = 0;
    protected static final int ITEM_DOUBLE = 1;

    protected ArrayList<Setting> mItems;

    private IClickListener mListener;

    public SettingsAdapter(ArrayList<Setting> items) {
        mItems = items;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final Holder holder;
        switch (viewType) {
            case ITEM_DOUBLE:
                holder = new DoubleHolder(inflater.inflate(android.R.layout.simple_list_item_2, parent, false), mListener);
                break;

            case ITEM_SINGLE:
            default:
                holder = new SingleHolder(inflater.inflate(android.R.layout.simple_list_item_1, parent, false), mListener);
                break;
        }


        return holder;
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.bind(mItems.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof ExtendedSetting) {
            return ITEM_DOUBLE;
        }
        return ITEM_SINGLE;
    }

    public void setOnClickListener(IClickListener listener) {
        mListener = listener;
    }

    public static abstract class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected IClickListener mClickListener;

        protected int mPosition;
        protected Setting mSetting;

        public Holder(View itemView, IClickListener listener) {
            super(itemView);
            mClickListener = listener;
            itemView.setOnClickListener(this);
        }

        public void bind(Setting setting, int position) {
            mSetting = setting;
            mPosition = position;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onClick(mSetting, mPosition);
            }
        }
    }

    public static class SingleHolder extends Holder {
        protected TextView title;

        public SingleHolder(View itemView, IClickListener listener) {
            super(itemView, listener);
            title = (TextView) itemView.findViewById(android.R.id.text1);
        }

        @Override
        public void bind(Setting setting, int position) {
            super.bind(setting, position);
            title.setText(setting.title);
        }
    }

    public static class DoubleHolder extends Holder {
        protected TextView title;
        protected TextView desc;

        public DoubleHolder(View itemView, IClickListener listener) {
            super(itemView, listener);
            title = (TextView) itemView.findViewById(android.R.id.text1);
            desc = (TextView) itemView.findViewById(android.R.id.text2);
        }

        @Override
        public void bind(Setting setting, int position) {
            super.bind(setting, position);
            final ExtendedSetting extended = (ExtendedSetting) setting;
            title.setText(extended.title);
            desc.setText(extended.desc);
        }
    }
}
