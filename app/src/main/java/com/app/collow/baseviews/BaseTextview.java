package com.app.collow.baseviews;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by harmis on 5/1/17.
 */

public class BaseTextview extends TextView {
    public BaseTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BaseTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseTextview(Context context) {
        super(context);
        init();
    }


    public void init() {


    }

}
