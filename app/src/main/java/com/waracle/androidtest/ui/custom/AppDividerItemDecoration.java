package com.waracle.androidtest.ui.custom;

import android.content.Context;
import android.support.v7.widget.DividerItemDecoration;

import com.waracle.androidtest.R;

/**
 * Created by joel on 3/12/18.
 */

public class AppDividerItemDecoration extends DividerItemDecoration {

    @SuppressWarnings("deprecation")
    public AppDividerItemDecoration(Context context) {
        super(context, VERTICAL);

        setDrawable(context.getResources().getDrawable(R.drawable.divider));
    }
}
