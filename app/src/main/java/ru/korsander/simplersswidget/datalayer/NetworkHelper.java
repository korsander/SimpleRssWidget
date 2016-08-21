package ru.korsander.simplersswidget.datalayer;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by korsander on 20.08.16.
 */
public class NetworkHelper {
    public static final String TAG = "NetworkHelper";

    public static final int DEFAULT_CONNECT_TIMEOUT = 45 * 1000;
    public static final int DEFAULT_READ_TIMEOUT = 25 * 1000;
    public static final String HTTP_METHOD = "GET";

    public static final String PROPERTY_ACCEPT = "Accept";
    public static final String PROPERTY_CACHE_CONTROL = "Cache-Control";

    public static final String VALUE_ACCEPT = "application/rss+xml, application/rdf+xml;q=0.8, application/atom+xml;q=0.6, application/xml;q=0.4, text/xml;q=0.4";
    public static final String VALUE_MAX_AGE_ZERO = "max-age=0";

    public static HttpURLConnection openGetConnection(String link) throws MalformedURLException, IOException {
        final URL url = new URL(link);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(HTTP_METHOD);
        connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        connection.setReadTimeout(DEFAULT_READ_TIMEOUT);
        connection.setUseCaches(true);
        connection.setRequestProperty(PROPERTY_ACCEPT, VALUE_ACCEPT);
        connection.addRequestProperty(PROPERTY_CACHE_CONTROL, VALUE_MAX_AGE_ZERO);
        return connection;
    }
}
