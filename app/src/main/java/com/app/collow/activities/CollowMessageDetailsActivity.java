package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CollowMessagebean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Harmis on 20/02/17.
 */

public class CollowMessageDetailsActivity extends BaseActivity {


    public static CollowMessageDetailsActivity collowMessageDetailsActivity = null;
    protected BaseTextview baseTextview_sender_name = null, baseTextview_content = null, baseTextview_date = null;
    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    CommonSession commonSession = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    String communityID = null;
    int current_start = 0;
    CollowMessagebean collowMessagebean = null;
    BaseTextview baseTextview_left_side = null;
    CircularImageView circularImageView_profile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            retryParameterbean = new RetryParameterbean(CollowMessageDetailsActivity.this, getApplicationContext(), getIntent().getExtras(), CollowMessageDetailsActivity.class.getClass());
            commonSession = new CommonSession(CollowMessageDetailsActivity.this);
            collowMessageDetailsActivity = this;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                collowMessagebean = (CollowMessagebean) bundle.getSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN);
            }
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
            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);
            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);

            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }
            };
            drawerLayout.setDrawerListener(drawerListener);

            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });
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
            view_home = getLayoutInflater().inflate(R.layout.collow_message_details, null);
            baseTextview_sender_name = (BaseTextview) view_home.findViewById(R.id.textview_message_sender_name);
            baseTextview_content = (BaseTextview) view_home.findViewById(R.id.textview_message_title);
            baseTextview_date = (BaseTextview) view_home.findViewById(R.id.textview_message_received_time);
            circularImageView_profile = (CircularImageView) view_home.findViewById(R.id.imageview_message_profileimage);
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }


    private void setupEvents() {


        try {
            if (CommonMethods.isTextAvailable(collowMessagebean.getUsername())) {
                baseTextview_sender_name.setText(collowMessagebean.getUsername());
            }

            if (CommonMethods.isTextAvailable(collowMessagebean.getContent())) {
                baseTextview_content.setText(collowMessagebean.getContent());

            }

            if (CommonMethods.isImageUrlValid(collowMessagebean.getProfilepic())) {
                Picasso.with(CollowMessageDetailsActivity.this)
                        .load(collowMessagebean.getProfilepic())
                        .into((circularImageView_profile), new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                circularImageView_profile.setImageResource(R.drawable.defualt_square);
                            }
                        });
            } else {
                circularImageView_profile.setImageResource(R.drawable.defualt_square);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

