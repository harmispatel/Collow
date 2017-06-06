package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.AmenitiesAdapter;
import com.app.collow.adapters.ImageSlideAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.httprequests.GetPostParameterEachScreen;
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
import java.util.List;

/**
 * Created by Harmis on 31/01/17.
 */

public class CommunityInformationActivity extends BaseActivity implements SetupViewInterface {

    public static CommunityInformationActivity CommunityInformationActivity = null;
    public static CommunityAccessbean communityAccessbean = null;
    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;

    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    BaseTextview baseTextview_left_side = null;
    BaseTextview textview_communityinformation_address = null;
    BaseTextview textview_communityinformation_type = null;
    BaseTextview textview_communityinformation_title;
    BaseTextview textview_communityinformation_contact;
    BaseTextview textview_communityinformation_email;
    BaseTextview textview_communityinformation_fax;
    BaseTextview textview_communityinformation_description;
    String communityID = null, communityName = "", comID = "", isFollow = null;
    CommonSession commonSession = null;
    RecyclerView recyclerView_amenities = null;
    BaseTextview baseTextview_empty_message_amenities = null;
    ViewPager viewPager_images = null;
    ImageView left_nav, right_nav;
    ArrayList<String> strings_images_url_community = new ArrayList<>();

    public  static ImageView imageview_follow = null;
    public  static boolean isFollowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(CommunityInformationActivity.this, getApplicationContext(), getIntent().getExtras(), CommunityInformationActivity.class.getClass());
            CommunityInformationActivity = this;
            commonSession = new CommonSession(this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
            }
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            getCommunityDetailsByID();
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
            imageView_right_menu.setImageResource(R.drawable.community_main_menu);

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
                    CommunityActivitiesFeedActivitiy.comId = comID;
                    //ad
                    // commonSession.storeUserFollow(false);
                    commonSession.storeSearchCom("1");
                    MyUtils.openCommunityMenu(CommunityInformationActivity.this, communityID, baseTextview_header_title.getText().toString(), communityAccessbean);


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
            view_home = getLayoutInflater().inflate(R.layout.community_information_main, null);

            textview_communityinformation_title = (BaseTextview) view_home.findViewById(R.id.textview_communityinformation_title);
            textview_communityinformation_contact = (BaseTextview) view_home.findViewById(R.id.textview_communityinformation_contact);
            textview_communityinformation_email = (BaseTextview) view_home.findViewById(R.id.textview_communityinformation_email);
            textview_communityinformation_description = (BaseTextview) view_home.findViewById(R.id.textview_communityinformation_description);
            textview_communityinformation_fax = (BaseTextview) view_home.findViewById(R.id.textview_communityinformation_fax);
            textview_communityinformation_type = (BaseTextview) view_home.findViewById(R.id.textview_communityinformation_type);
            textview_communityinformation_address = (BaseTextview) view_home.findViewById(R.id.textview_communityinformation_address);

            imageview_follow = (ImageView) view_home.findViewById(R.id.imageview_follow);
            imageview_follow.setVisibility(View.GONE);

            left_nav = (ImageView) view_home.findViewById(R.id.left_nav);
            right_nav = (ImageView) view_home.findViewById(R.id.right_nav);
            viewPager_images = (ViewPager) view_home.findViewById(R.id.viewpager);


            recyclerView_amenities = (RecyclerView) view_home.findViewById(R.id.my_recycler_view_amenities);
            baseTextview_empty_message_amenities = (BaseTextview) view_home.findViewById(R.id.empty_view);


            frameLayout_container.addView(view_home);


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    private void setupEvents() {
        leftRigtButtonHandler();
        left_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tab = viewPager_images.getCurrentItem();
                if (tab == 0) {
                    left_nav.setVisibility(View.GONE);
                    viewPager_images.setCurrentItem(tab);
                } else if (tab > 0) {
                    tab--;
                    viewPager_images.setCurrentItem(tab);
                    left_nav.setVisibility(View.VISIBLE);
                    if (tab == 0) {
                        left_nav.setVisibility(View.GONE);
                        viewPager_images.setCurrentItem(tab);
                    }
                }

                if (tab == strings_images_url_community.size() - 1) {
                    right_nav.setVisibility(View.GONE);
                } else {
                    right_nav.setVisibility(View.VISIBLE);
                }

            }
        });

        // Images right navigatin
        right_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int tab = viewPager_images.getCurrentItem();
                tab++;
                viewPager_images.setCurrentItem(tab);

                if (tab == strings_images_url_community.size() - 1) {
                    right_nav.setVisibility(View.GONE);
                } else {
                    right_nav.setVisibility(View.VISIBLE);
                    if (tab == strings_images_url_community.size() - 1) {
                        right_nav.setVisibility(View.GONE);
                    }
                }


                if (tab > 0) {

                    left_nav.setVisibility(View.VISIBLE);

                } else if (tab == 0) {
                    left_nav.setVisibility(View.GONE);
                }
            }
        });

        viewPager_images.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (strings_images_url_community.size() == 1)

                {
                    left_nav.setVisibility(View.GONE);
                    right_nav.setVisibility(View.GONE);
                } else if (position == 0) {
                    left_nav.setVisibility(View.GONE);
                    right_nav.setVisibility(View.VISIBLE);
                } else if (position == strings_images_url_community.size() - 1) {
                    left_nav.setVisibility(View.VISIBLE);
                    right_nav.setVisibility(View.GONE);
                } else {
                    left_nav.setVisibility(View.VISIBLE);
                    right_nav.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        imageview_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFollowed) {
                    unFollowCommunity();
                } else if (!isFollowed) {
                    commonSession.storeUserFollow(true);
                    followCommunity();
                }
            }
        });
    }


    @Override
    public void setupUI(Responcebean responcebean) {

        try {

            if (responcebean.getScreenIndex() == ScreensEnums.FOLLOW_COMMUNITY.getScrenIndex()) {  //follow communitry handler response
                if (responcebean.isServiceSuccess()) {

                    JSONObject jsonObject_follow = new JSONObject(responcebean.getResponceContent());
                    if (CommonMethods.checkSuccessResponceFromServer(jsonObject_follow)) {
                        //parse here data of following
                        isFollowed=true;
                        //enableButton();
                        imageview_follow.setImageResource(R.drawable.select_heart);
                        if (jsonObject_follow.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_follow.getString(JSONCommonKeywords.message));
                        }
                        communityAccessbean.setCommunityFollowed(true);
                        commonSession.storeUserFollow(true);
//                        menubeanArrayList.clear();
                        commonSession.resetSearchCom();
                        //isFromSearch="";
                        //setFollowerMenu();
                        // menuAdapter = new MenuAdapter(menubeanArrayList, CommunityInformationActivity.this, communityID, communityAccessbean);
                        //  mRecyclerView.setAdapter(menuAdapter);

                        CommunityInformationActivity.communityAccessbean = communityAccessbean;

                        //if user already set home communty no need to update community

                        CommunityActivitiesFeedActivitiy.comId = comID;

                        if (commonSession.isUserSetHomeCommunity()) {

                        } else {
                            //make home community
                            MyUtils.updateHomeCommunity(CommunityInformationActivity.this, comID, communityName);

                        }


                        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.following_request_sent), CommunityInformationActivity.this);
                        } else {
                            CommonMethods.customToastMessage(responcebean.getErrorMessage(), CommunityInformationActivity.this);
                        }

                    } else {
                        handlerError(responcebean);

                    }
                } else {
                    handlerError(responcebean);
                }
            } else if (responcebean.getScreenIndex() == ScreensEnums.UNFOLLOW_COMMUNITY.getScrenIndex()) {
                if (responcebean.isServiceSuccess()) {
                    isFollowed=false;
                    JSONObject jsonObject_unfollow = new JSONObject(responcebean.getResponceContent());
                    if (CommonMethods.checkSuccessResponceFromServer(jsonObject_unfollow)) {
                        communityAccessbean.setCommunityFollowed(false);
                        commonSession.storeUserFollow(false);
                        imageview_follow.setImageResource(R.drawable.unselect_heart);

                        if (jsonObject_unfollow.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_unfollow.getString(JSONCommonKeywords.message));
                        }
                    } else {
                        handlerError(responcebean);

                    }
                } else {
                    handlerError(responcebean);

                }
            } else {
                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                    //parse here data of following


                    JSONObject jsonObject_community = jsonObject_main.getJSONObject(JSONCommonKeywords.Community_Single);
                    if (jsonObject_community == null || jsonObject_community.equals("")) {

                    } else {


                        if (jsonObject_community.has(JSONCommonKeywords.CommunityID)) {

                        }
                        //set community name
                        if (CommonMethods.handleKeyInJSON(jsonObject_community, JSONCommonKeywords.CommunityName)) {
                            textview_communityinformation_title.setText(jsonObject_community.getString(JSONCommonKeywords.CommunityName));
                            textview_communityinformation_address.setText(jsonObject_community.getString(JSONCommonKeywords.Address));
                            textview_communityinformation_type.setText(jsonObject_community.getString(JSONCommonKeywords.CommunityType));

                            isFollow = jsonObject_community.getString(JSONCommonKeywords.isFollowedCommunity);

                            Log.e("ad", "isFollow=" + isFollow);
                            if (!TextUtils.isEmpty(isFollow) && isFollow.equalsIgnoreCase("1")) {
                                isFollowed=true;
                                imageview_follow.setImageResource(R.drawable.select_heart);
                                imageview_follow.setVisibility(View.VISIBLE);
                            } else if (!TextUtils.isEmpty(isFollow) && isFollow.equalsIgnoreCase("0")) {
                                isFollowed=false;
                                imageview_follow.setImageResource(R.drawable.unselect_heart);
                                imageview_follow.setVisibility(View.VISIBLE);
                            } else {
                                imageview_follow.setVisibility(View.GONE);
                            }

                            comID = jsonObject_community.getString(JSONCommonKeywords.CommunityID);
                            communityName = jsonObject_community.getString(JSONCommonKeywords.CommunityName);

                            baseTextview_header_title.setText(getResources().getString(R.string.info));
                            baseTextview_header_title.setSingleLine(true);
                            baseTextview_header_title.setEllipsize(TextUtils.TruncateAt.END);


                        }
                        //set community description
                        if (CommonMethods.handleKeyInJSON(jsonObject_community, JSONCommonKeywords.communityDescription)) {
                            textview_communityinformation_description.setText(jsonObject_community.getString(JSONCommonKeywords.communityDescription));
                        } else {

                        }
                        //set email
                        if (CommonMethods.handleKeyInJSON(jsonObject_community, JSONCommonKeywords.communityEmail)) {
                            textview_communityinformation_email.setText(jsonObject_community.getString(JSONCommonKeywords.communityEmail));
                        } else {
                            textview_communityinformation_email.setText("-");
                        }
                        //set mobile
                        if (CommonMethods.handleKeyInJSON(jsonObject_community, JSONCommonKeywords.CommunityPhone)) {
                            textview_communityinformation_contact.setText(jsonObject_community.getString(JSONCommonKeywords.CommunityPhone));
                        } else {
                            textview_communityinformation_contact.setText("-");

                        }

                        textview_communityinformation_contact.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", textview_communityinformation_contact.getText().toString(), null)));


                            }
                        });

                        //set fax
                        if (CommonMethods.handleKeyInJSON(jsonObject_community, JSONCommonKeywords.communityPhone2)) {
                            textview_communityinformation_fax.setText(jsonObject_community.getString(JSONCommonKeywords.communityPhone2));
                        } else {
                            textview_communityinformation_fax.setText("-");

                        }


                        communityAccessbean = CommonMethods.getCommunityAccessFromJSON(jsonObject_community);


                        //set amenities array
                        if (CommonMethods.checkJSONArrayHasData(jsonObject_community, JSONCommonKeywords.amenities)) {
                            List<String> stringList_amenities = new ArrayList<>();
                            JSONArray jsonArray = jsonObject_community.getJSONArray(JSONCommonKeywords.amenities);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                stringList_amenities.add(jsonArray.getJSONObject(i).getString(JSONCommonKeywords.name));
                            }

                            CommonMethods.displayLog("amen", stringList_amenities.toString());


                            // Partial code from https://developer.android.com/training/material/lists-cards.html#RecyclerView

                            // use this setting to improve performance if you know that changes
                            // in content do not change the layout size of the RecyclerView
                            recyclerView_amenities.setHasFixedSize(true);
                            LinearLayoutManager mLayoutManager;
                            // use a linear layout manager
                            mLayoutManager = new LinearLayoutManager(this);
                            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView_amenities.setLayoutManager(mLayoutManager);

                            // Disabled nested scrolling since Parent scrollview will scroll the content.
                            recyclerView_amenities.setNestedScrollingEnabled(false);

                            // specify an adapter (see also next example)

                            AmenitiesAdapter amenitiesAdapter = new AmenitiesAdapter(CommunityInformationActivity.this, stringList_amenities);
                            recyclerView_amenities.setAdapter(amenitiesAdapter);

                            recyclerView_amenities.setVisibility(View.VISIBLE);
                            baseTextview_empty_message_amenities.setVisibility(View.GONE);

                        } else {
                            recyclerView_amenities.setVisibility(View.GONE);
                            baseTextview_empty_message_amenities.setVisibility(View.GONE);
                        }


                        if (CommonMethods.handleKeyInJSON(jsonObject_community, JSONCommonKeywords.CommuntiyImageURL)) {
                            String imageURL = jsonObject_community.getString(JSONCommonKeywords.CommuntiyImageURL);
                            strings_images_url_community.add(imageURL);
                        }


                        ///set image url array
                        if (CommonMethods.checkJSONArrayHasData(jsonObject_community, JSONCommonKeywords.CommuntiyImageURL)) {
                            List<String> stringList_amenities = new ArrayList<>();
                            JSONArray jsonArray = jsonObject_community.getJSONArray(JSONCommonKeywords.CommuntiyImageURL);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                stringList_amenities.add(jsonArray.getJSONObject(i).getString(JSONCommonKeywords.name));
                            }

                        } else {

                        }

                        ImageSlideAdapter imageSlideAdapter = new ImageSlideAdapter(CommunityInformationActivity.this, strings_images_url_community, ScreensEnums.COMMUNITY_INFORMATION_SCREEN.getScrenIndex());
                        viewPager_images.setAdapter(imageSlideAdapter);
                        viewPager_images.setCurrentItem(0);
                        viewPager_images.setOffscreenPageLimit(strings_images_url_community.size());

                        leftRigtButtonHandler();

                    }


                } else {
                    handlerError(responcebean);

                }
            }


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }


    public void handlerError(Responcebean responcebean) {

        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {

        } else {


        }
    }


    public void getCommunityDetailsByID() {
        try {


            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());

            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.GET_COMMUNITY_DETAILS.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CommunityInformationActivity.this, getApplicationContext(), URLs.GETCOMMUNITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.GET_COMMUNITY_DETAILS.getScrenIndex(), CommunityInformationActivity.class.getClass());
            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    public void leftRigtButtonHandler() {
        if (strings_images_url_community.size() == 1)

        {
            left_nav.setVisibility(View.GONE);
            right_nav.setVisibility(View.GONE);
        } else if (strings_images_url_community.size() == 0)

        {
            left_nav.setVisibility(View.GONE);
            right_nav.setVisibility(View.GONE);
        } else {
            left_nav.setVisibility(View.VISIBLE);
            right_nav.setVisibility(View.VISIBLE);
        }
    }

    //

    public void followCommunity() {
        try {

            if (!TextUtils.isEmpty(comID)) {
                Loginbean loginbean = CommonMethods.convertJSONToLoginbean(commonSession.getLoginJsonContent());

                requestParametersbean.setUserId(commonSession.getLoggedUserID());
                requestParametersbean.setUser_email(loginbean.getEmail());
                requestParametersbean.setCommunityID(comID);
                requestParametersbean.setCommunityText("Home");
               // requestParametersbean.setCommunityText(communityName);


                JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.FOLLOW_COMMUNITY.getScrenIndex(), requestParametersbean);
                PassParameterbean passParameterbean = new PassParameterbean(this, CommunityInformationActivity.this, getApplicationContext(), URLs.FOLLOW_COMMUNITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.FOLLOW_COMMUNITY.getScrenIndex(), CommunityInformationActivity.class.getClass());

                passParameterbean.setNeedToFirstTakeFacebookProfilePic(false);

                new RequestToServer(passParameterbean, retryParameterbean).execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    private void unFollowCommunity() {
        try {

            if (!TextUtils.isEmpty(comID)) {

                requestParametersbean.setUserId(commonSession.getLoggedUserID());
                requestParametersbean.setCommunityID(comID);

                JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.UNFOLLOW_COMMUNITY.getScrenIndex(), requestParametersbean);
                PassParameterbean passParameterbean = new PassParameterbean(this, CommunityInformationActivity.this, getApplicationContext(), URLs.UNFOLLOW_COMMUNITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.UNFOLLOW_COMMUNITY.getScrenIndex(), CommunityInformationActivity.class.getClass());

                passParameterbean.setNeedToFirstTakeFacebookProfilePic(false);

                new RequestToServer(passParameterbean, retryParameterbean).execute();
            }

        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }
}