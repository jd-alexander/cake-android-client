package com.waracle.androidtest.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
public class StreamUtils {
    private static final String TAG = StreamUtils.class.getSimpleName();

    public static byte[] readUnknownFully(InputStream stream) throws IOException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[8192];
        int bytesRead;

        while ((bytesRead = stream.read(buffer)) != -1) {
           bos.write(buffer, 0, bytesRead);
        }

        bos.close();
        return bos.toByteArray();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }
}
