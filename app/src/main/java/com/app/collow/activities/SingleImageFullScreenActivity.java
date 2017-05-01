package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.PollOptionbean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

/**
 * Created by Harmis on 09/02/17.
 */

public class SingleImageFullScreenActivity extends BaseActivity {
    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null,baseTextview_left_side;
    String url=null;
    ImageView imageView_fullscreen=null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            retryParameterbean = new RetryParameterbean(SingleImageFullScreenActivity.this, getApplicationContext(), getIntent().getExtras(), SingleImageFullScreenActivity.class.getClass());
            commonSession = new CommonSession(SingleImageFullScreenActivity.this);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                url=bundle.getString(BundleCommonKeywords.KEY_URL);
            }

            setupHeaderView();
            findViewByIDs();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }


    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
          //  baseTextview_header_title.setText(getResources().getString(R.string.new_poll));
            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);
            baseTextview_left_side.setVisibility(View.VISIBLE);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            baseTextview_left_side.setText(getResources().getString(R.string.back));


            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            drawerLayout.setEnabled(true);

            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.single_image_full_screen, null);

            imageView_fullscreen= (ImageView) view_home.findViewById(R.id.imageview_full_screen);
            frameLayout_container.addView(view_home);

            if(url!=null)
            {
                Picasso.with(this)
                        .load(url)
                        .into(imageView_fullscreen, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                imageView_fullscreen.setImageResource(R.drawable.defualt_square);
                            }
                        });
            }

            //  getNewsListing(0);
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }
}