package com.app.collow.baseviews;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.collow.R;
import com.app.collow.beans.Communitybean;
import com.app.collow.beans.Interestbean;
import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Created by Harmis on 26/01/17.
 */

public class InterestAutoCompleteTextview extends TokenCompleteTextView<Interestbean> {

    public InterestAutoCompleteTextview(Context context) {
        super(context);
    }

    public InterestAutoCompleteTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterestAutoCompleteTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected View getViewForObject(Interestbean interestbean) {
        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TokenTextView token = (TokenTextView) l.inflate(R.layout.text_token, (ViewGroup) getParent(), false);
        token.setText(interestbean.getInterestName());

        if(interestbean.getColorcode()!=0)
        {
            Drawable tempDrawable_main_bg = getResources().getDrawable(R.drawable.button_transparent_background);
            LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
            GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);

            solidColor_main_bg.setColor(interestbean.getColorcode());
            solidColor_main_bg.setStroke(1, getResources().getColor(R.color.white));
            token.setBackground(tempDrawable_main_bg);
        }
        return token;
    }

    @Override
    protected Interestbean defaultObject(String completionText) {
        //Stupid simple example of guessing if we have an email or not
        Interestbean interestbean=new Interestbean();
        interestbean.setInterestName(completionText);

        return interestbean;
    }
}
