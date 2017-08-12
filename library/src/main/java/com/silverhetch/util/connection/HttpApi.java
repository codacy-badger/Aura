package com.silverhetch.util.connection;

import com.silverhetch.util.connection.exception.HttpException;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    public final HttpResponse request() throws HttpException {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            setupConnection(connection);
            int responseCode = connection.getResponseCode();
            return handleResponse(responseCode, connection);
        } catch (Exception ex) {
            throw new HttpException(ex);
        }
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

    private byte[] convertInputStreamToByteArray(InputStream inputStream) throws Exception {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            channelStream(inputStream, result);
            return result.toByteArray();
        }
    }

    private void channelStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }
}
