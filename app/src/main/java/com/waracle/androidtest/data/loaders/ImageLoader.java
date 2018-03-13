package com.waracle.androidtest.data.loaders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.LruCache;
import android.widget.ImageView;

import com.waracle.androidtest.utils.BitmapCache;
import com.waracle.androidtest.utils.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidParameterException;


public class ImageLoader {

    private LruCache<String, Bitmap> mCache;

    public ImageLoader() {
        mCache = BitmapCache.getInstance().getLruCache();
    }


    public void load(String url, final ImageView imageView) {

        if (TextUtils.isEmpty(url)) {
            throw new InvalidParameterException("URL is empty!");
        }

        final Bitmap targetBitmap = getBitmapFromCache(url);

        if (targetBitmap != null) {
            setImageView(imageView, targetBitmap);
        } else {
            LoadImageTask task = new LoadImageTask() {
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    setImageView(imageView, bitmap);
                }
            };

            task.execute(url);
        }
        // Can you think of a way to improve loading of bitmaps
        // that have already been loaded previously??

        // a memory cache to store bitmaps so that they're not fetched
        // multiple times
    }

    private static byte[] loadImageData(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        InputStream inputStream = null;
        try {
            try {
                // Read data from workstation
                inputStream = connection.getInputStream();
            } catch (IOException e) {
                // Read the error from the workstation
                inputStream = connection.getErrorStream();
            }

            // Can you think of a way to make the entire
            // HTTP more efficient using HTTP headers??

            return StreamUtils.readUnknownFully(inputStream);
        } finally {
            // Close the input stream if it exists.
            StreamUtils.close(inputStream);

            // Disconnect the connection
            connection.disconnect();
        }
    }

    private static Bitmap convertToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    private static void setImageView(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    private void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromCache(String key) {
        return mCache.get(key);
    }

    public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {

            byte[] data = null;

            try {
                data = loadImageData(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap output = convertToBitmap(data);

            if (output != null) {
                addBitmapToCache(strings[0], output);
            }
            return convertToBitmap(data);
        }
    }
}
