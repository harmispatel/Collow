package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.ClaimCommunityAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.ClaimCommunitybean;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
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
import com.app.collow.utils.RecyclerItemClickListener;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Harmis on 16/02/17.
 */

public class ClaimeCommunityActivity extends BaseActivity implements SetupViewInterface {

    public static ClaimeCommunityActivity claimeCommunityActivity = null;
    public static ArrayList<ClaimCommunitybean> claimCommunitybeanArrayList = new ArrayList<>();
    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    String communityID = null;
    String communityText = null;
    CommonSession commonSession = null;
    CommunityAccessbean communityAccessbean = null;
    ClaimCommunityAdapter claimCommunityAdapter = null;
    int current_start = 0;
    MyButtonView myButtonView_procced = null;
    private RecyclerView mRecyclerView;
    private BaseTextview baseTextview_error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            claimeCommunityActivity = this;
            claimCommunitybeanArrayList.clear();
            retryParameterbean = new RetryParameterbean(ClaimeCommunityActivity.this, getApplicationContext(), getIntent().getExtras(), CommunityMenuActivity.class.getClass());
            commonSession = new CommonSession(this);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityText = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT);
                requestParametersbean.setCommunityID(communityID);
            }

            setupHeaderView();
            findViewByIDs();
            setupEvents();
            getMgmtListing();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.claime_community));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            imageView_right_menu.setImageResource(R.drawable.cross);
            DrawerLayout.DrawerListener drawerListener =new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    MyUtils.leftMenuUpdateDataifOpenedDrawer(ClaimeCommunityActivity.this,drawerLayout,circularImageView_profile_pic,baseTextview_left_menu_unread_message,retryParameterbean);


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
            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    if (ClaimeCommunityActivity.claimeCommunityActivity != null) {
                        ClaimeCommunityActivity.claimeCommunityActivity.finish();
                    }

                }
            });
            toolbar_header.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.claim_community, null);
            myButtonView_procced = (MyButtonView) view_home.findViewById(R.id.button_claimcommunity_proceed);


            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager;
            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mRecyclerView.setLayoutManager(mLayoutManager);

            // Disabled nested scrolling since Parent scrollview will scroll the content.
            mRecyclerView.setNestedScrollingEnabled(false);


            frameLayout_container.addView(view_home);


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    private void setupEvents() {
        myButtonView_procced.setOnClickListener(new MyOnClickListener(this) {
            @Override
            public void onClick(View v) {

                if (isAvailableInternet()) {
                    claimCommunity();
                } else {
                    showInternetNotfoundMessage();
                }
            }
        });
        claimCommunityAdapter = new ClaimCommunityAdapter(mRecyclerView, ClaimeCommunityActivity.this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(5);
        // use a linear layout manager
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mRecyclerView.setAdapter(claimCommunityAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ClaimeCommunityActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        ClaimCommunitybean claimCommunitybean = (ClaimCommunitybean) view.getTag();

                        if (claimCommunitybean.isNeedToTrueIconDisplay()) {
                            claimCommunitybean.setNeedToTrueIconDisplay(false);
                        } else {
                            claimCommunitybean.setNeedToTrueIconDisplay(true);

                        }


                        boolean isAnyTeamSelected = false;
                        for (int i = 0; i < claimCommunitybeanArrayList.size(); i++) {
                            ClaimCommunitybean claimCommunitybean1 = claimCommunitybeanArrayList.get(i);
                            if (claimCommunitybean1.isNeedToTrueIconDisplay()) {
                                isAnyTeamSelected = true;
                            }
                        }
                        if (isAnyTeamSelected) {
                            myButtonView_procced.setVisibility(View.VISIBLE);
                        } else {
                            myButtonView_procced.setVisibility(View.GONE);

                        }

                        claimCommunitybeanArrayList.set(claimCommunitybean.getPosition(), claimCommunitybean);
                        claimCommunityAdapter.notifyItemChanged(claimCommunitybean.getPosition());

                    }
                })
        );


    }

    // this method is used for following listing
    public void getMgmtListing() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());

            PassParameterbean passParameterbean = null;


            passParameterbean = new PassParameterbean(this, ClaimeCommunityActivity.this, getApplicationContext(), URLs.GETMANAGETEAMS, null, ScreensEnums.GET_MGMT_TEAM_LISTING.getScrenIndex(), FollowingActivity.class.getClass());


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

            if (responcebean.getScreenIndex() == ScreensEnums.CLAIM_COMMUNITY.getScrenIndex()) {
                if (responcebean.isServiceSuccess()) {

                    JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                    if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                        //parse here data of following

                        if (jsonObject_main.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                        }


                        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.claim_community_request_sent), ClaimeCommunityActivity.this);

                        } else {
                            CommonMethods.customToastMessage(responcebean.getErrorMessage(), ClaimeCommunityActivity.this);
                        }

                        communityAccessbean.setClaimedCommunityRequestSent(true);
                        finish();
                        MyUtils.openCommunityMenu(ClaimeCommunityActivity.this, communityID, communityText, communityAccessbean);


                    } else {
                        if (jsonObject_main.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                        }

                        handlerError(responcebean);

                    }
                } else {
                    handlerError(responcebean);
                }
            } else {
                if (responcebean.isServiceSuccess()) {

                    JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                    if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                        //parse here data of following
                        if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.teamsList)) {

                            JSONArray jsonArray = jsonObject_main.getJSONArray(JSONCommonKeywords.teamsList);
                            ArrayList<ClaimCommunitybean> claimCommunitybeen_local = new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject_single_claime = jsonArray.getJSONObject(i);
                                ClaimCommunitybean claimCommunitybean = new ClaimCommunitybean();

                                if (jsonObject_single_claime.has(JSONCommonKeywords.teamid)) {
                                    claimCommunitybean.setMgmtID(jsonObject_single_claime.getString(JSONCommonKeywords.teamid));
                                }


                                if (jsonObject_single_claime.has(JSONCommonKeywords.teamname)) {
                                    claimCommunitybean.setMgmtName(jsonObject_single_claime.getString(JSONCommonKeywords.teamname));
                                }


                                claimCommunitybean.setPosition(i);

                                claimCommunitybeen_local.add(claimCommunitybean);


                            }


                            if (current_start == 0) {
                                claimCommunitybeanArrayList = claimCommunitybeen_local;
                                claimCommunityAdapter.notifyDataSetChanged();

                            } else {

                                int start = claimCommunitybeanArrayList.size();

                                for (int i = 0; i < claimCommunitybeen_local.size(); i++) {

                                    ClaimCommunitybean claimCommunitybean = claimCommunitybeen_local.get(i);
                                    claimCommunitybean.setPosition(start);
                                    claimCommunitybeen_local.add(start, claimCommunitybean);
                                    claimCommunityAdapter.notifyItemInserted(claimCommunitybeanArrayList.size());

                                    start++;

                                }
                                claimCommunityAdapter.setLoaded();

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
            }


        } catch (Exception e) {
            e.printStackTrace();
            handlerError(responcebean);
            new BaseException(e, false, retryParameterbean);

        }

    }


    public void handlerError(Responcebean responcebean) {


        if (responcebean.getScreenIndex() == ScreensEnums.CLAIM_COMMUNITY.getScrenIndex()) {
            if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {

                CommonMethods.customToastMessage(getResources().getString(R.string.claim_community_request_sent_failed), ClaimeCommunityActivity.this);

            } else {

                CommonMethods.customToastMessage(responcebean.getErrorMessage(),ClaimeCommunityActivity.this);
            }
        }
        else
        {
            if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                if (current_start == 0) {
                    baseTextview_error.setText(getResources().getString(R.string.no_teams_found));
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


    // this method is used for login user
    public void claimCommunity() {
        try {


            Loginbean loginbean = CommonMethods.convertJSONToLoginbean(commonSession.getLoginJsonContent());

            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setUser_email(loginbean.getEmail());
            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setCommunityText(communityText);


            ArrayList<String> stringArrayList_teamIDs = new ArrayList<>();
            for (int i = 0; i < claimCommunitybeanArrayList.size(); i++) {

                ClaimCommunitybean claimCommunitybean = claimCommunitybeanArrayList.get(i);
                if (claimCommunitybean.isNeedToTrueIconDisplay()) {
                    stringArrayList_teamIDs.add(claimCommunitybean.getMgmtID());
                }
            }

            String ids = CommonMethods.getStringWithCommaFromList(stringArrayList_teamIDs);
            requestParametersbean.setTeamsMgmtCommaSeperatedIDs(ids);

            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.CLAIM_COMMUNITY.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, ClaimeCommunityActivity.this, getApplicationContext(), URLs.CLAIM_COMMUNITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.CLAIM_COMMUNITY.getScrenIndex(), ClaimeCommunityActivity.class.getClass());


            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }
}
