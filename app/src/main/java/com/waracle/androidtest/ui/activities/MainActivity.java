package com.waracle.androidtest.ui.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.LruCache;

import com.waracle.androidtest.ui.fragments.CakeFragment;
import com.waracle.androidtest.R;
import com.waracle.androidtest.utils.BitmapCache;


public class MainActivity extends AppCompatActivity {

    private LruCache<String, Bitmap> mCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CakeFragment placeholderFragment =
                CakeFragment.findOrCreateRetainInstance(getSupportFragmentManager());
        mCache = placeholderFragment.mRetainedCache;

        if (mCache == null) {
            mCache = BitmapCache.getInstance().getLruCache();
        }
        placeholderFragment.mRetainedCache = mCache;
    }
}
