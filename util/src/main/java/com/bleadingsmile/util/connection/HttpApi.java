package com.bleadingsmile.util.connection;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.bleadingsmile.util.IOUtil.convertInputStreamToByteArray;

/**
 * Template class for quick implement http request.
 * Implement {@link HttpApi#setupConnection(HttpURLConnection)} for header, request body, etc.
 * <p>
 * All failed returns {@link HttpResponse} Object.Including exception cause with code -1.
 * Created by Larry Hsiao on 2016/11/16.
 */
public abstract class HttpApi implements HttpRequest {
    protected abstract void setupConnection(HttpURLConnection connection) throws Exception;

    private final URL url;

    public HttpApi(String urlString) {
        try {
            url = new URL(urlString);
        } catch (Exception e) {
            throw new RuntimeException("Url schema not correct url:" + urlString + "\n" + e);
        }
    }

    @Override
    public final HttpResponse request() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        setupConnection(connection);
        int responseCode = connection.getResponseCode();
        return handleResponse(responseCode, connection);
    }

    private HttpResponse handleResponse(int responseCode, HttpURLConnection urlConnection) throws Exception {
        if (responseCode >= 200 && responseCode < 300) {
            return handleResponse(responseCode, urlConnection.getInputStream());
        } else {
            return handleResponse(responseCode, urlConnection.getErrorStream());
        }
    }

    private HttpResponse handleResponse(int responseCode, InputStream responseStream) throws Exception {
        try (DataInputStream wrappedStream = new DataInputStream(responseStream)) {
            byte[] responseBytes = convertInputStreamToByteArray(wrappedStream);
            HttpResponse responseObject = new HttpResponse();
            responseObject.code = responseCode;
            responseObject.bodyBytes = responseBytes;
            return responseObject;
        }
    }
}
