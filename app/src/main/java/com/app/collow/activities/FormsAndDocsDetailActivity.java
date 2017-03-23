package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.MyUtils;

/**
 * Created by harmis on 1/2/17.
 */

public class FormsAndDocsDetailActivity extends BaseActivity {
    BaseTextview textview_formsanddocs_title;
    BaseTextview textview_formsanddocs_datetime;
    BaseTextview textview_formsanddocs_like;
    BaseTextview textview_formsanddocs_comment;
    BaseTextview textview_formsanddocs_view;
    BaseTextview textview_formsanddocs_description;
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    View view_home = null;
    BaseTextview baseTextview_left_side = null;
    BaseTextview baseTextview_header_title = null;
    RetryParameterbean retryParameterbean = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupHeaderView();
        findviewByIds();
    }

    public void setupHeaderView() {
        try {
            View headerview = getLayoutInflater().inflate(R.layout.header, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerview.setLayoutParams(layoutParams);
            baseTextview_header_title = (BaseTextview) headerview.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText("Title");
            imageView_left_menu = (ImageView) headerview.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) headerview.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            baseTextview_left_side = (BaseTextview) headerview.findViewById(R.id.textview_left_side_title);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);


            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // MyUtils.openCommunityMenu(FormsAndDocsDetailActivity.this,false,false,"1","1");


                }
            });

            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            setSupportActionBar(toolbar_header);
            toolbar_header.addView(headerview);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }
    private void findviewByIds() {
        view_home = getLayoutInflater().inflate(R.layout.formsanddocs_detail, null);




        textview_formsanddocs_title=(BaseTextview)view_home.findViewById(R.id.textview_formsanddocs_title);
        textview_formsanddocs_datetime=(BaseTextview)view_home.findViewById(R.id.textview_formsanddocs_date_time);
        textview_formsanddocs_like=(BaseTextview)view_home.findViewById(R.id.textview_formsanddocs_likes);
        textview_formsanddocs_comment=(BaseTextview)view_home.findViewById(R.id.textview_formsanddocs_comments);
        textview_formsanddocs_view=(BaseTextview)view_home.findViewById(R.id.textview_formsanddocs_views);
        textview_formsanddocs_description=(BaseTextview)view_home.findViewById(R.id.textview_formsanddocs_description);
        frameLayout_container.addView(view_home);


    }
}
