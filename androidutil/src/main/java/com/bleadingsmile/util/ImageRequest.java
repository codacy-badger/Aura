package com.bleadingsmile.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Larry Hsiao on 2016/9/27.
 */
public class ImageRequest {
    public byte[] request(String urlString) throws RequestFailedException, IOException {
        BufferedInputStream inputStream = null;
        try {

            URL               url        = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setUseCaches(false);
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                String errorMessage = new String(handleResponse(connection.getErrorStream()));
                throw new RequestFailedException(connection.getResponseCode(), errorMessage);
            }
            return handleResponse(connection.getInputStream());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException ex) {
            }
        }

    }

    private byte[] handleResponse(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = null;
        try {
            byte[] buffer = new byte[4 * 1024];
            outputStream = new ByteArrayOutputStream();
            int readCount = 0;
            while ((readCount = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, readCount);
            }
            return outputStream.toByteArray();
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static class RequestFailedException extends Exception {
        private final int    code;
        private final String message;

        public RequestFailedException(int code, String message) {
            this.code = code;
            this.message = message;
        }
    }
}
