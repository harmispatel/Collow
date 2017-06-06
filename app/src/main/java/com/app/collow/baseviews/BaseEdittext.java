package com.app.collow.baseviews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by harmis on 6/1/17.
 */

public class BaseEdittext extends EditText {
    public BaseEdittext(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public BaseEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseEdittext(Context context) {
        super(context);
        init();
    }


    public void init() {


    }

}
