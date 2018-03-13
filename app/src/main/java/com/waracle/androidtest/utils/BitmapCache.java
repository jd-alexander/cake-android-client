package com.waracle.androidtest.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

public class BitmapCache {

    private static BitmapCache sInstance;
    private LruCache<String, Bitmap> mLruCache;

    private BitmapCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount() / 1024;
            }
        };
    }

    public static BitmapCache getInstance() {
        if (sInstance == null) {
            sInstance = new BitmapCache();
        }
        return sInstance;
    }

    public LruCache<String, Bitmap> getLruCache() {
        return mLruCache;
    }
}
