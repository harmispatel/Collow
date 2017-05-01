package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.MyUtils;

/**
 * Created by Harmis on 13/02/17.
 */

public class CommentSettingActivity extends BaseActivity {

    public static CommentSettingActivity commentSettingActivity = null;
    protected Handler handler;
    View view_home = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;

    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start = 0;
    CommunityAccessbean communityAccessbean = null;
    String communityID = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            commentSettingActivity = this;
            retryParameterbean = new RetryParameterbean(CommentSettingActivity.this, getApplicationContext(), getIntent().getExtras(), CommentSettingActivity.class.getClass());
            commonSession = new CommonSession(CommentSettingActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
            }
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    public void setupHeaderView() {
        try {
              baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.news));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });



        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.upload_picure_with_text, null);


            frameLayout_container.addView(view_home);


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void setupEvents() {
        try {

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }
    }


}
