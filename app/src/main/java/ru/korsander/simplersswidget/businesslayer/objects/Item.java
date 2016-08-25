package ru.korsander.simplersswidget.businesslayer.objects;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by korsander on 20.08.16.
 */
public class Item implements Parcelable {
    public String guid;
    public String category;
    public String title;
    public String description;
    public String link;
    public String pubDate;

    protected static final SimpleDateFormat mExternal =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");

    protected static final SimpleDateFormat mInternal =
            new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

    public Item() {

    }

    protected Item(Parcel in) {
        guid = in.readString();
        category = in.readString();
        title = in.readString();
        description = in.readString();
        link = in.readString();
        pubDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(guid);
        dest.writeString(category);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(link);
        dest.writeString(pubDate);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getFormatedDateTime() {
        try {
            return mInternal.format(mExternal.parse(pubDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return pubDate;
    }
}
