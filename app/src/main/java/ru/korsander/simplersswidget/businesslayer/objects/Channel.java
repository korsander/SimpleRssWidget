package ru.korsander.simplersswidget.businesslayer.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by korsander on 20.08.16.
 */
public class Channel implements Parcelable{
    public String title;
    public String description;
    public String language;
    public String link;

    protected ArrayList<Item> mItems = new ArrayList<>();

    public Channel() {
    }

    public void addItem(Item item) {
        mItems.add(item);
    }

    public ArrayList<Item> getItems() {
        return mItems;
    }

    protected Channel(Parcel in) {
        title = in.readString();
        description = in.readString();
        language = in.readString();
        link = in.readString();
        mItems = in.createTypedArrayList(Item.CREATOR);
    }

    public static final Creator<Channel> CREATOR = new Creator<Channel>() {
        @Override
        public Channel createFromParcel(Parcel in) {
            return new Channel(in);
        }

        @Override
        public Channel[] newArray(int size) {
            return new Channel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(language);
        dest.writeString(link);
        dest.writeTypedList(mItems);
    }
}
