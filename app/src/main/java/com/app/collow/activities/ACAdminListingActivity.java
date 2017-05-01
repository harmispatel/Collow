package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.ACListCommunityAdminAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACListCommunityAdminbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

import java.util.ArrayList;

public class ACAdminListingActivity extends BaseActivity implements SetupViewInterface {

    public static ACAdminListingActivity acAdminListingActivity = null;
    public static ArrayList<ACListCommunityAdminbean> acListCommunityAdminbeanArrayList = new ArrayList<>();
    protected Handler handler;
    View view_home = null;
    ACListCommunityAdminAdapter acListCommunityAdminAdapter = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    CommonSession commonSession = null;
    FloatingActionButton floatingActionButton_create_new_admin = null;
    private BaseTextview baseTextview_empty_view;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            acAdminListingActivity = this;
            retryParameterbean = new RetryParameterbean(ACAdminListingActivity.this, getApplicationContext(), getIntent().getExtras(), ACAdminListingActivity.class.getClass());
            commonSession = new CommonSession(ACAdminListingActivity.this);
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.my_posts));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.VISIBLE);

            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.RIGHT);


                }
            });
            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    MyUtils.leftMenuUpdateDataifOpenedDrawer(ACAdminListingActivity.this,drawerLayout,circularImageView_profile_pic,baseTextview_left_menu_unread_message,retryParameterbean);
                }

                @Override
                public void onDrawerClosed(View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            };
            drawerLayout.setDrawerListener(drawerListener);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.ac_admins_main, null);
            baseTextview_empty_view = (BaseTextview) view_home.findViewById(R.id.empty_view);


            floatingActionButton_create_new_admin = (FloatingActionButton) view_home.findViewById(R.id.fab_create_new_admin);

            floatingActionButton_create_new_admin.setOnClickListener(new MyOnClickListener(this) {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ACAdminListingActivity.this, ACNewAdmin.class);
                    startActivity(intent);
                }
            });


            ACListCommunityAdminbean admislistbean = new ACListCommunityAdminbean();
            acListCommunityAdminbeanArrayList.add(admislistbean);
            acListCommunityAdminbeanArrayList.add(admislistbean);
            acListCommunityAdminbeanArrayList.add(admislistbean);
            acListCommunityAdminbeanArrayList.add(admislistbean);
            acListCommunityAdminbeanArrayList.add(admislistbean);
            acListCommunityAdminbeanArrayList.add(admislistbean);
            acListCommunityAdminbeanArrayList.add(admislistbean);
            acListCommunityAdminbeanArrayList.add(admislistbean);
            acListCommunityAdminbeanArrayList.add(admislistbean);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(20);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);

            acListCommunityAdminAdapter = new ACListCommunityAdminAdapter(ACAdminListingActivity.this, mRecyclerView);
            mRecyclerView.setAdapter(acListCommunityAdminAdapter);
            frameLayout_container.addView(view_home);

            acListCommunityAdminAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                        }
                    }, 2000);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }

    }


    // get activities from server
    public void getMyActivities() {
        try {


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ADMINLIST.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, ACAdminListingActivity.this, getApplicationContext(), URLs.ADMINLIST, jsonObjectGetPostParameterEachScreen, ScreensEnums.ADMINLIST.getScrenIndex(), ACAdminListingActivity.class.getClass());


            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }


    @Override
    public void setupUI(Responcebean responcebean) {
        try {

            if (responcebean.isServiceSuccess()) {

                JSONObject jsonObject = new JSONObject(responcebean.getResponceContent());
                if (jsonObject != null) {
                    if (jsonObject.has(JSONCommonKeywords.success)) {
                        int success = jsonObject.getInt(JSONCommonKeywords.success);
                        if (success == 1) {

                        } else {

                        }
                    }
                }

            } else {
                if (responcebean.getErrorMessage() != null) {
                    baseTextview_empty_view.setText(responcebean.getErrorMessage());

                } else {
                    baseTextview_empty_view.setText(getResources().getString(R.string.no_records_found));

                }
            }


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }
}
