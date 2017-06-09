package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityActivitiesFeedbean;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonSession;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class FeedsUserProfileActivity  extends BaseActivity {
    public static FeedsUserProfileActivity feedsUserProfileActivity = null;
    CommonSession commonSession = null;

    String fname=""; // 004
    String lname=""; // 034556
    String image="";
    String email="";
    String userid = "";


    CircularImageView circularImageView_profile_edit_profile = null;
    BaseEdittext edittext_editprofile_firstname = null;
    BaseEdittext edittext_editprofile_lastname = null;
    BaseEdittext edittext_editprofile_email = null;
    ProgressBar profile_pgb = null;

    BaseTextview baseTextview_header_title = null;
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    BaseTextview baseTextview_left_side = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_user_profile_activity);
        commonSession = new CommonSession(FeedsUserProfileActivity.this);
        feedsUserProfileActivity = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //activitiesFeedbean = bundle.getString(BundleCommonKeywords.KEY_ID);

            /*intent.putExtra("fname", fname);
        intent.putExtra("lname", lname);
        intent.putExtra("image", image);
        intent.putExtra("email", email);*/
            fname=bundle.getString("fname");
            lname=bundle.getString("lname");
            image=bundle.getString("image");
            email=bundle.getString("email");


            Log.e("ad", "fname==" + fname);
            Log.e("ad", "lname==" + lname);
            Log.e("ad", "image==" + image);
            Log.e("ad", "fname==" + fname);
            Log.e("ad", "email==" + email);
            setupHeaderView();
            findViewID();
             setupData();
        } else {
            Log.e("ad", "FeedsUserProfileActivity---KEY_COMMUNITY_FEADBEAN may null");
            finish();
        }
    }
    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.user_profile));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);
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

            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
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


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();


        }


    }
    private void setupData() {

        if (!TextUtils.isEmpty(fname)) {
            edittext_editprofile_firstname.setVisibility(View.VISIBLE);
            edittext_editprofile_firstname.setText(fname);
        } else {
            edittext_editprofile_firstname.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(lname)) {
            edittext_editprofile_lastname.setVisibility(View.VISIBLE);
            edittext_editprofile_lastname.setText(lname);
        } else {
            edittext_editprofile_lastname.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(email)) {
            edittext_editprofile_email.setVisibility(View.VISIBLE);
            edittext_editprofile_email.setText(email);
        } else {
            edittext_editprofile_email.setVisibility(View.INVISIBLE);
        }



        if (image == null || image.equalsIgnoreCase("")) {
            circularImageView_profile_edit_profile.setImageResource(R.drawable.user_defualt_icon);
            profile_pgb.setVisibility(View.GONE);
        } else {
            final ProgressBar finalProfile_pgb = profile_pgb;
            final CircularImageView finalCircularImageView_profile_edit_profile = circularImageView_profile_edit_profile;
            Picasso.with(feedsUserProfileActivity)
                    .load(image)
                    .into(circularImageView_profile_edit_profile, new Callback() {
                        @Override
                        public void onSuccess() {
                            finalProfile_pgb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            finalCircularImageView_profile_edit_profile.setImageResource(R.drawable.user_defualt_icon);
                            finalProfile_pgb.setVisibility(View.GONE);
                        }
                    });
        }
    }

    private void findViewID() {
        try {
            circularImageView_profile_edit_profile = (CircularImageView) findViewById(R.id.profile_image);
            circularImageView_profile_edit_profile.setVisibility(View.VISIBLE);
            edittext_editprofile_firstname = (BaseEdittext) findViewById(R.id.edittext_editprofile_firstname);
            edittext_editprofile_lastname = (BaseEdittext) findViewById(R.id.edittext_editprofile_lastname);
            edittext_editprofile_email = (BaseEdittext) findViewById(R.id.edittext_editprofile_email);
            profile_pgb = (ProgressBar) findViewById(R.id.profile_pgb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
