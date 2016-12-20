package com.bleadingsmile.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Larry Hsiao on 2016/11/2.
 */
public class IOUtil {
    public static byte[] convertInputStreamToByteArray(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            channelStream(inputStream, result);
            return result.toByteArray();
        }
    }

    public static void channelStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }
}
