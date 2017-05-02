package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.CommunityActivitiesFeedAdapter;
import com.app.collow.adapters.ImageSlideAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.CommunityActivitiesFeedbean;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.recyledecor.SimpleDividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Harmis on 30/01/17.
 */

public class CommunityActivitiesFeedActivitiy extends BaseActivity implements SetupViewInterface {

    public static ArrayList<CommunityActivitiesFeedbean> communityActivitiesFeedbeanArrayList = new ArrayList<CommunityActivitiesFeedbean>();
    public static CommunityActivitiesFeedActivitiy communityActivitiesFeedActivitiy = null;
    protected Handler handler;
    View view_home = null;
    public static CommunityActivitiesFeedAdapter communityActivitiesFeedAdapter = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    CommunityAccessbean communityAccessbean = null;
    int current_start = 0;
    CommonSession commonSession = null;
    private BaseTextview baseTextview_empty_view;
    private RecyclerView mRecyclerView;
    private BaseTextview baseTextview_error = null;
    int screen_from_index =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            communityActivitiesFeedbeanArrayList.clear();
            retryParameterbean = new RetryParameterbean(CommunityActivitiesFeedActivitiy.this, getApplicationContext(), getIntent().getExtras(), CommunityActivitiesFeedActivitiy.class.getClass());
            communityActivitiesFeedActivitiy = this;
            commonSession = new CommonSession(CommunityActivitiesFeedActivitiy.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
                screen_from_index= bundle.getInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE);
                if (screen_from_index == ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex()) {
                    String communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                    String communityText = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT);

                    Log.e("ad","communityID="+communityID);
                    if (communityID != null) {
                        requestParametersbean.setCommunityID(communityID);
                        requestParametersbean.setCommunityText(communityText);

                        requestParametersbean.setIsFromFollowing("1");
                    }

                }

            }


            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            getMyActivities();
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }

    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.activity));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            imageView_right_menu.setImageResource(R.drawable.community_main_menu);


            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (screen_from_index == ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex()) {

                        MyUtils.openCommunityMenu(CommunityActivitiesFeedActivitiy.this, requestParametersbean.getCommunityID(), requestParametersbean.getCommunityText(), communityAccessbean);

                    }
                    else
                    {
                        Loginbean loginbean = CommonMethods.convertJSONToLoginbean(commonSession.getLoginJsonContent());
                        CommunityAccessbean communityAccessbean = loginbean.getCommunityAccessbean();
                        MyUtils.openCommunityMenu(CommunityActivitiesFeedActivitiy.this, loginbean.getHomeCommunityId(), loginbean.getName(), communityAccessbean);

                    }




                }
            });
            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    MyUtils.leftMenuUpdateDataifOpenedDrawer(CommunityActivitiesFeedActivitiy.this,drawerLayout,circularImageView_profile_pic,baseTextview_left_menu_unread_message,retryParameterbean);
                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);

                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);

                }
            };
            drawerLayout.setDrawerListener(drawerListener);

            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });

            //   setSupportActionBar(toolbar_header);


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.recyleview_main_without_margin, null);
            baseTextview_empty_view = (BaseTextview) view_home.findViewById(R.id.empty_view);
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            SimpleDividerItemDecoration simpleDividerItemDecoration = new SimpleDividerItemDecoration(this);

            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(simpleDividerItemDecoration);


            communityActivitiesFeedAdapter = new CommunityActivitiesFeedAdapter(mRecyclerView, CommunityActivitiesFeedActivitiy.this);
            mRecyclerView.setAdapter(communityActivitiesFeedAdapter);


            frameLayout_container.addView(view_home);

            communityActivitiesFeedAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //   remove progress item
                            current_start += 10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);
                            getMyActivities();
                        }
                    }, 2000);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }

    }


    // get communties activities  feed from server
    public void getMyActivities() {
        try {

            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setStart_limit(current_start);


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CommunityActivitiesFeedActivitiy.this, getApplicationContext(), URLs.GET_COMMUNITIES_FEEDS, jsonObjectGetPostParameterEachScreen, ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex(), SignInActivity.class.getClass());

            if (current_start == 0) {
                passParameterbean.setNoNeedToDisplayLoadingDialog(false);
            } else {
                passParameterbean.setNoNeedToDisplayLoadingDialog(true);

            }

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

                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                    //parse here data of following


                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.Activities)) {

                        JSONArray jsonArray_activities = jsonObject_main.getJSONArray(JSONCommonKeywords.Activities);
                        ArrayList<CommunityActivitiesFeedbean> communityActivitiesFeedbeanArrayList_Local = new ArrayList<>();
                        for (int i = 0; i < jsonArray_activities.length(); i++) {

                            JSONObject jsonObject_single_activity = jsonArray_activities.getJSONObject(i);
                            CommunityActivitiesFeedbean communityActivitiesFeedbean = new CommunityActivitiesFeedbean();

                            if (jsonObject_single_activity.has(JSONCommonKeywords.CommunityID)) {

                                communityActivitiesFeedbean.setCommunityID(jsonObject_single_activity.getString(JSONCommonKeywords.CommunityID));
                            }


                            if (jsonObject_single_activity.has(JSONCommonKeywords.ActivityId)) {

                                communityActivitiesFeedbean.setActivityID(jsonObject_single_activity.getString(JSONCommonKeywords.ActivityId));
                            }


                            if (jsonObject_single_activity.has(JSONCommonKeywords.Title)) {

                                communityActivitiesFeedbean.setTitle(jsonObject_single_activity.getString(JSONCommonKeywords.Title));
                            }


                            if (jsonObject_single_activity.has(JSONCommonKeywords.Category)) {

                                communityActivitiesFeedbean.setFeedcategory(jsonObject_single_activity.getString(JSONCommonKeywords.Category));
                            }


                            if (jsonObject_single_activity.has(JSONCommonKeywords.Description)) {

                                communityActivitiesFeedbean.setPostcontent(jsonObject_single_activity.getString(JSONCommonKeywords.Description));
                            }


                            if (jsonObject_single_activity.has(JSONCommonKeywords.NewsDate)) {

                                communityActivitiesFeedbean.setPosttime(jsonObject_single_activity.getString(JSONCommonKeywords.NewsDate));
                            }


                            if (jsonObject_single_activity.has(JSONCommonKeywords.ProfilePic)) {

                                communityActivitiesFeedbean.setUserprofilepic(jsonObject_single_activity.getString(JSONCommonKeywords.ProfilePic));
                            }

                            if (jsonObject_single_activity.has(JSONCommonKeywords.UserName)) {

                                communityActivitiesFeedbean.setUsername(jsonObject_single_activity.getString(JSONCommonKeywords.UserName));
                            }

                            if (jsonObject_single_activity.has(JSONCommonKeywords.CommunityName)) {

                                communityActivitiesFeedbean.setCommunityName(jsonObject_single_activity.getString(JSONCommonKeywords.CommunityName));
                            }


                            SocialOptionbean socialOptionbean = CommonMethods.getSocialOptionbean(jsonObject_single_activity);

                            if (socialOptionbean != null) {
                                communityActivitiesFeedbean.setSocialOptionbean(socialOptionbean);
                            }

                            if (jsonObject_single_activity.has(JSONCommonKeywords.isLiked)) {


                                String islikedfeedString = jsonObject_single_activity.getString(JSONCommonKeywords.isLiked);
                                if (islikedfeedString == null || islikedfeedString.equals("")) {
                                    communityActivitiesFeedbean.setLikedFeed(false);
                                } else if (islikedfeedString.equals("1")) {
                                    communityActivitiesFeedbean.setLikedFeed(true);

                                } else {
                                    communityActivitiesFeedbean.setLikedFeed(false);

                                }


                            }

                            if (jsonObject_single_activity.has(JSONCommonKeywords.Comments)) {

                            }

                            ArrayList<String> images_of_classified = new ArrayList<>();

                            if (CommonMethods.handleKeyInJSON(jsonObject_single_activity, JSONCommonKeywords.Image))

                            {
                                if (CommonMethods.checkJSONArrayHasData(jsonObject_single_activity, JSONCommonKeywords.Image)) {
                                    JSONArray jsonArray_images = jsonObject_single_activity.getJSONArray(JSONCommonKeywords.Image);
                                    for (int j = 0; j < jsonArray_images.length(); j++) {

                                        if (CommonMethods.isImageUrlValid(jsonArray_images.getString(j))) {
                                            images_of_classified.add(jsonArray_images.getString(j));
                                        }
                                    }

                                    communityActivitiesFeedbean.setStringArrayList_images_url(images_of_classified);

                                }
                            }

                            if (images_of_classified.size() == 0) {

                            } else {
                                ImageSlideAdapter imageSlideAdapter = new ImageSlideAdapter(CommunityActivitiesFeedActivitiy.this, images_of_classified, ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex());

                                communityActivitiesFeedbean.setImageSlideAdapter(imageSlideAdapter);
                            }
                            communityActivitiesFeedbean.setPosition(i);


                            communityActivitiesFeedbeanArrayList_Local.add(communityActivitiesFeedbean);

                        }


                        if (current_start == 0) {
                            communityActivitiesFeedbeanArrayList = communityActivitiesFeedbeanArrayList_Local;
                            communityActivitiesFeedAdapter.notifyDataSetChanged();

                        } else {

                            int start = communityActivitiesFeedbeanArrayList.size();

                            for (int i = 0; i < communityActivitiesFeedbeanArrayList_Local.size(); i++) {

                                CommunityActivitiesFeedbean communityActivitiesFeedbean = communityActivitiesFeedbeanArrayList_Local.get(i);
                                communityActivitiesFeedbean.setPosition(i);
                                communityActivitiesFeedbeanArrayList.add(start, communityActivitiesFeedbean);
                                communityActivitiesFeedAdapter.notifyItemInserted(communityActivitiesFeedbeanArrayList.size());
                                start++;

                            }
                            communityActivitiesFeedAdapter.setLoaded();

                        }
                    } else {
                        handlerError(responcebean);

                    }


                } else {
                    handlerError(responcebean);

                }
            } else {
                handlerError(responcebean);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handlerError(responcebean);
            new BaseException(e, false, retryParameterbean);

        }
    }


    public void handlerError(Responcebean responcebean) {
        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
            if (current_start == 0) {
                baseTextview_error.setText(getResources().getString(R.string.no_activties_feed_found));
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            } else {


            }
        } else {
            if (current_start == 0) {
                baseTextview_error.setText(responcebean.getErrorMessage());
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            } else {

            }
        }
    }
}