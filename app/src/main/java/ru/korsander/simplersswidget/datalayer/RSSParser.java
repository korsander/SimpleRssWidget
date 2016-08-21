package ru.korsander.simplersswidget.datalayer;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import ru.korsander.simplersswidget.businesslayer.objects.Channel;
import ru.korsander.simplersswidget.businesslayer.objects.Item;
import ru.korsander.simplersswidget.utils.L;

/**
 * Created by korsander on 20.08.16.
 */
public class RSSParser {
    public static final String TAG = "RSSParser";

    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String LINK = "link";
    private static final String DESC = "description";
    private static final String DATE = "pubDate";
    private static final String GUID = "guid";
    private static final String CATEGORY = "category";
    private static final String LANG = "language";
    private static final String IMAGE = "image";
    private static final String CHANNEL = "channel";



    public RSSParser() {

    }

    public static Channel parse(InputStream stream) throws XmlPullParserException, IOException {
        Channel result = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(stream, null);
        int eventType = parser.getEventType();
        Item currentItem = null;
        boolean done = false;
        while (eventType != XmlPullParser.END_DOCUMENT && !done) {
            String name = "";
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    L.d(TAG, "start parsing");
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase(CHANNEL)) {
                        result = new Channel();
                    } else if (name.equalsIgnoreCase(ITEM)) {
                        currentItem = new Item();
                    } else if (currentItem != null) {
                        handleItem(parser, name, currentItem);
                    } else if (result != null) {
                        handleChannel(parser, name, result);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (result != null && name.equalsIgnoreCase(ITEM)) {
                        result.addItem(currentItem);
                    } else if (name.equalsIgnoreCase(CHANNEL)) {
                        done = true;
                        L.d(TAG, "finish parsing");
                    }
                    break;
            }
            eventType = parser.next();
        }
        return result;
    }

    protected static void handleItem(XmlPullParser parser, String name, Item item) throws IOException, XmlPullParserException {
        switch (name) {
            case TITLE:
                item.title = parser.nextText();
                break;

            case GUID:
                item.guid = parser.nextText();
                break;

            case CATEGORY:
                item.category = parser.nextText();
                break;

            case DESC:
                item.description = parser.nextText();
                break;

            case LINK:
                item.link = parser.nextText();
                break;

            case DATE:
                item.pubDate = parser.nextText();
                break;

            default:
                break;
        }
    }

    protected static void handleChannel(XmlPullParser parser, String name, Channel channel) throws IOException, XmlPullParserException {
        switch (name) {
            case TITLE:
                channel.title = parser.nextText();
                break;

            case DESC:
                channel.description = parser.nextText();
                break;

            case LANG:
                channel.language = parser.nextText();
                break;

            case LINK:
                channel.link = parser.nextText();
                break;

            case IMAGE:
                channel.image = parser.nextText();
                break;

            default:
                break;
        }
    }
}