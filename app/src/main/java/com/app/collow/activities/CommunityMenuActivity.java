package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.MenuAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.Menubean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SubMenubean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harmis on 31/01/17.
 */

public class CommunityMenuActivity extends BaseActivity implements SetupViewInterface {

    public static CommunityMenuActivity communitySearchByNameActivity = null;
    public static CommunityMenuActivity communityMenuActivity;
    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    boolean isGroupOpened = false;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RetryParameterbean retryParameterbean = null;
    ImageView imageview_community_favouriteunfavourite = null;
    Button button_community_claim = null;
    List<Menubean> menubeanArrayList = new ArrayList<>();
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    String communityID = null;
    String communityText = null;
    CommonSession commonSession = null;
    CommunityAccessbean communityAccessbean = null;
    MenuAdapter menuAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            communityMenuActivity = this;
            retryParameterbean = new RetryParameterbean(CommunityMenuActivity.this, getApplicationContext(), getIntent().getExtras(), CommunityMenuActivity.class.getClass());
            commonSession = new CommonSession(this);

            communitySearchByNameActivity = this;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityText = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT);
                requestParametersbean.setCommunityID(communityID);

                //We are setting user as admin by claimed flag which comes from server
                if (communityAccessbean != null) {
                    if (communityAccessbean.isCommunityClaimedAndApproved()) {
                        commonSession.storeisUserAdminNow(true);
                    } else {
                        commonSession.storeisUserAdminNow(false);
                    }

                    if (communityAccessbean.isCommunityFollowed()) {
                        commonSession.storeUserFollow(true);
                    } else {
                        commonSession.storeUserFollow(false);
                    }
                }

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
            baseTextview_header_title.setText(getResources().getString(R.string.menu));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            imageView_right_menu.setImageResource(R.drawable.cross);

            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    if (CommunityMenuActivity.communityMenuActivity != null) {
                        CommunityMenuActivity.communityMenuActivity.finish();
                    }

                }
            });
            toolbar_header.setBackgroundColor(getResources().getColor(android.R.color.transparent));


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
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.community_menu, null);

// get the listview

            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager;
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // Disabled nested scrolling since Parent scrollview will scroll the content.
            mRecyclerView.setNestedScrollingEnabled(false);


            imageview_community_favouriteunfavourite = (ImageView) view_home.findViewById(R.id.imageview_community_favouriteunfavourite);
            button_community_claim = (Button) view_home.findViewById(R.id.button_community_claimcommunity);


            if (communityAccessbean.isCommunityFollowed()) {
                enableButton();
                imageview_community_favouriteunfavourite.setVisibility(View.GONE);

                commonSession.storeUserFollow(true);
            } else {
                disableButton();
                commonSession.storeUserFollow(false);
                imageview_community_favouriteunfavourite.setVisibility(View.VISIBLE);

            }

            if (communityAccessbean.isClaimedCommunityRequestSent() || communityAccessbean.isCommunityClaimedAndApproved()) {
                button_community_claim.setVisibility(View.GONE);
            } else {
                button_community_claim.setVisibility(View.VISIBLE);
            }


            button_community_claim.setOnClickListener(new MyOnClickListener(this) {
                @Override
                public void onClick(View v) {
                    if (communityAccessbean.isCommunityFollowed()) {
                        if (isAvailableInternet()) {
                            Intent intent = new Intent(CommunityMenuActivity.this, ClaimeCommunityActivity.class);
                            Bundle bundle = new Bundle();
                            if (communityAccessbean == null) {
                                communityAccessbean = new CommunityAccessbean();
                            }
                            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

                            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, communityText);

                            intent.putExtras(bundle);
                            startActivity(intent);


                        } else {
                            showInternetNotfoundMessage();
                        }
                    } else {
                        CommonMethods.customToastMessage(getResources().getString(R.string.cannot_click_claim), CommunityMenuActivity.this);

                    }
                }
            });


            frameLayout_container.addView(view_home);


            imageview_community_favouriteunfavourite.setOnClickListener(new MyOnClickListener(this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        commonSession.storeUserFollow(true);
                        followCommunity();
                    } else {
                        showInternetNotfoundMessage();
                    }

                }
            });


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    private void setupEvents() {


        try {
            if (communityAccessbean.isCommunityClaimedAndApproved()) {
                setAdminMenu();
            } else {
                if (communityAccessbean.isCommunityFollowed()) {
                    setFollowerMenu();
                } else {
                    setNonFollowerMenu();
                }
            }

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(5);
            // use a linear layout manager
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            menuAdapter = new MenuAdapter(menubeanArrayList, CommunityMenuActivity.this, communityID, communityAccessbean);
            mRecyclerView.setAdapter(menuAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
           /* menuAdapter.setOnGroupClickListener(new OnGroupClickListener() {
                @Override
                public boolean onGroupClick(int flatPos) {

                    List<SubMenubean> artistArrayList = menubeanArrayList.get(flatPos).getItems();
                    if (artistArrayList.size() == 0) {
                        Menubean menubean1 = menubeanArrayList.get(flatPos);
                        requestParametersbean.setCommunityID(communityID);
                        MyUtils.menuItemOpenByItsName(CommunityMenuActivity.this, menubean1.getTitle(), 0, requestParametersbean, communityAccessbean);
                    }


                    return false;
                }
            });*/


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setNonFollowerMenu() {

        menubeanArrayList.clear();
        List<SubMenubean> subMenubeen_empty = new ArrayList<>();

        Menubean menubean_infor = new Menubean(subMenubeen_empty, getResources().getString(R.string.info), getResources().getColor(R.color.menu_info));
        Menubean menubean_gallery = new Menubean(subMenubeen_empty, getResources().getString(R.string.gallery), getResources().getColor(R.color.menu_gallery));
        Menubean menubean_classfied = new Menubean(subMenubeen_empty, getResources().getString(R.string.classified), getResources().getColor(R.color.menu_classfied));
        Menubean menubean_events = new Menubean(subMenubeen_empty, getResources().getString(R.string.events), getResources().getColor(R.color.menu_events));

        //if user already is following this community display all menu if not display following menus only info,gallery,classfied,events and for all feed,classfied,news,chat,events,gallery,polls,forms,info
        menubeanArrayList.add(menubean_infor);
        menubeanArrayList.add(menubean_gallery);
        menubeanArrayList.add(menubean_classfied);
        menubeanArrayList.add(menubean_events);


    }

    public void setFollowerMenu() {


        menubeanArrayList.clear();

        List<SubMenubean> subMenubeen_empty = new ArrayList<>();

        Menubean menubean_feed = new Menubean(subMenubeen_empty, getResources().getString(R.string.feed), getResources().getColor(R.color.menu_feed));
        Menubean menubean_classfied = new Menubean(subMenubeen_empty, getResources().getString(R.string.classified), getResources().getColor(R.color.menu_classfied));
        Menubean menubean_news = new Menubean(subMenubeen_empty, getResources().getString(R.string.news), getResources().getColor(R.color.menu_news));
        Menubean menubean_chat = new Menubean(subMenubeen_empty, getResources().getString(R.string.chat), getResources().getColor(R.color.menu_feed));
        Menubean menubean_events = new Menubean(subMenubeen_empty, getResources().getString(R.string.events), getResources().getColor(R.color.menu_events));
        Menubean menubean_gallery = new Menubean(subMenubeen_empty, getResources().getString(R.string.gallery), getResources().getColor(R.color.menu_gallery));
        Menubean menubean_polls = new Menubean(subMenubeen_empty, getResources().getString(R.string.polls), getResources().getColor(R.color.menu_polls));
        Menubean menubean_forms = new Menubean(subMenubeen_empty, getResources().getString(R.string.forms), getResources().getColor(R.color.menu_forms));
        Menubean menubean_infor = new Menubean(subMenubeen_empty, getResources().getString(R.string.info), getResources().getColor(R.color.menu_info));


        //if user already is following this community display all menu if not display following menus only info,gallery,classfied,events and for all feed,classfied,news,chat,events,gallery,polls,forms,info

        menubeanArrayList.add(menubean_feed);
        menubeanArrayList.add(menubean_classfied);
        menubeanArrayList.add(menubean_news);
        menubeanArrayList.add(menubean_chat);
        menubeanArrayList.add(menubean_events);
        menubeanArrayList.add(menubean_gallery);
        menubeanArrayList.add(menubean_polls);
        menubeanArrayList.add(menubean_forms);
        menubeanArrayList.add(menubean_infor);


    }

    public void setAdminMenu() {
        List<SubMenubean> subMenubeen_empty = new ArrayList<>();
        List<SubMenubean> subMenubeen_gallery = new ArrayList<>();

        SubMenubean subMenubean_gallery = new SubMenubean(getResources().getString(R.string.gallery), false, getResources().getColor(R.color.menu_gallery));

        subMenubeen_gallery.add(subMenubean_gallery);

        Menubean menubean_feed = new Menubean(subMenubeen_empty, getResources().getString(R.string.feed), getResources().getColor(R.color.menu_feed));

        Menubean menubean_classfied = new Menubean(subMenubeen_empty, getResources().getString(R.string.classified), getResources().getColor(R.color.menu_classfied));
        Menubean menubean_news = new Menubean(subMenubeen_empty, getResources().getString(R.string.news), getResources().getColor(R.color.menu_news));
        Menubean menubean_chat = new Menubean(subMenubeen_empty, getResources().getString(R.string.chat), getResources().getColor(R.color.menu_feed));
        Menubean menubean_events = new Menubean(subMenubeen_empty, getResources().getString(R.string.events), getResources().getColor(R.color.menu_events));
        Menubean menubean_polls = new Menubean(subMenubeen_empty, getResources().getString(R.string.polls), getResources().getColor(R.color.menu_polls));
        Menubean menubean_forms = new Menubean(subMenubeen_empty, getResources().getString(R.string.forms), getResources().getColor(R.color.menu_forms));
        Menubean menubean_infor = new Menubean(subMenubeen_gallery, getResources().getString(R.string.info), getResources().getColor(R.color.menu_info));


        Menubean menubean_admin = new Menubean(subMenubeen_empty, getResources().getString(R.string.admins), getResources().getColor(R.color.menu_info));
        Menubean menubean_followers = new Menubean(subMenubeen_empty, getResources().getString(R.string.followers), getResources().getColor(R.color.menu_info));
        Menubean menubean_feature = new Menubean(subMenubeen_empty, getResources().getString(R.string.feature), getResources().getColor(R.color.menu_info));

        Menubean menubean_address_units = new Menubean(subMenubeen_empty, getResources().getString(R.string.address_units), getResources().getColor(R.color.menu_feed));

        //if user already is following this community display all menu if not display following menus only info,gallery,classfied,events and for all feed,classfied,news,chat,events,gallery,polls,forms,info


        menubeanArrayList.add(menubean_admin);
        menubeanArrayList.add(menubean_infor);


        menubeanArrayList.add(menubean_feature);
        menubeanArrayList.add(menubean_news);
        menubeanArrayList.add(menubean_forms);
        menubeanArrayList.add(menubean_events);
        menubeanArrayList.add(menubean_classfied);
        menubeanArrayList.add(menubean_chat);
        menubeanArrayList.add(menubean_polls);
        menubeanArrayList.add(menubean_feed);
        menubeanArrayList.add(menubean_followers);


    }


    // this method is used for login user
    public void followCommunity() {
        try {

            Loginbean loginbean = CommonMethods.convertJSONToLoginbean(commonSession.getLoginJsonContent());

            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setUser_email(loginbean.getEmail());
            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setCommunityText(communityText);
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.FOLLOW_COMMUNITY.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CommunityMenuActivity.this, getApplicationContext(), URLs.FOLLOW_COMMUNITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.FOLLOW_COMMUNITY.getScrenIndex(), CommunityMenuActivity.class.getClass());

            passParameterbean.setNeedToFirstTakeFacebookProfilePic(false);

            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }


    @Override
    public void setupUI(Responcebean responcebean) {


        try {
            //follow communitry handler response
            if (responcebean.getScreenIndex() == ScreensEnums.FOLLOW_COMMUNITY.getScrenIndex()) {
                if (responcebean.isServiceSuccess()) {

                    JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                    if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                        //parse here data of following

                        enableButton();
                        imageview_community_favouriteunfavourite.setVisibility(View.GONE);
                        if (jsonObject_main.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                        }
                        communityAccessbean.setCommunityFollowed(true);

//                        menubeanArrayList.clear();
                        setFollowerMenu();
                        menuAdapter = new MenuAdapter(menubeanArrayList, CommunityMenuActivity.this, communityID, communityAccessbean);
                        mRecyclerView.setAdapter(menuAdapter);

                        CommunityInformationActivity.communityAccessbean = communityAccessbean;

                        //if user already set home communty no need to update community
                        if (commonSession.isUserSetHomeCommunity()) {

                        } else {
                            //make home community
                            MyUtils.updateHomeCommunity(CommunityMenuActivity.this, communityID, communityText);

                        }


                        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.following_request_sent), CommunityMenuActivity.this);
                        } else {
                            CommonMethods.customToastMessage(responcebean.getErrorMessage(), CommunityMenuActivity.this);
                        }

                    } else {
                        handlerError(responcebean);

                    }
                } else {
                    handlerError(responcebean);
                }
            }
            //claim community handler response
            else {

            }

        } catch (Exception e) {
            e.printStackTrace();
            handlerError(responcebean);
            new BaseException(e, false, retryParameterbean);

        }


    }

    public void handlerError(Responcebean responcebean) {
        //follow communitry handler response
        if (responcebean.getScreenIndex() == ScreensEnums.FOLLOW_COMMUNITY.getScrenIndex()) {
            if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                CommonMethods.customToastMessage(getResources().getString(R.string.following_request_failed), CommunityMenuActivity.this);
            } else {
                CommonMethods.customToastMessage(responcebean.getErrorMessage(), CommunityMenuActivity.this);
            }
        } else {
            if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                CommonMethods.customToastMessage(getResources().getString(R.string.claim_community_request_failed), CommunityMenuActivity.this);

            } else {
                CommonMethods.customToastMessage(responcebean.getErrorMessage(), CommunityMenuActivity.this);

            }
        }

    }

    public void enableButton() {
        button_community_claim.setBackground(getResources().getDrawable(R.drawable.enable_claim_comm_bg));
    }

    public void disableButton() {
        button_community_claim.setBackground(getResources().getDrawable(R.drawable.disable_claim_comm_bg));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }
    }
}