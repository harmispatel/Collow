package com.app.collow.activities;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.ACFollowersAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.ACFollowersbean;
import com.app.collow.beans.CommunityAccessbean;
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

import de.nitri.slidingtoggleswitch.SlidingToggleSwitchView;

public class
ACFollowerAwaitingManageActivity extends BaseActivity implements SetupViewInterface, SlidingToggleSwitchView.OnToggleListener {

    public static ArrayList<ACFollowersbean> acfollowersbeanArrayList_filtered = new ArrayList<>();
    public static ArrayList<ACFollowersbean> acfollowersbeanArrayList_all = new ArrayList<>();
    public static ACFollowerAwaitingManageActivity acFollowerAwaitingManageActivity = null;
    protected Handler handler;
    View view_home = null;
    ACFollowersAdapter acFollowersAdapter = null;
    BaseTextview baseTextview_header_title = null;
    CommonSession commonSession = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start = 0;
    boolean isApprovedOpenDefualt = true;
    SlidingToggleSwitchView slidingToggleSwitchView = null;
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    ImageView imageview_right_foursquare = null;
    String communityID = null,communityText=null;
    CommunityAccessbean communityAccessbean=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            retryParameterbean = new RetryParameterbean(ACFollowerAwaitingManageActivity.this, getApplicationContext(), getIntent().getExtras(), ACFollowerAwaitingManageActivity.class.getClass());
            commonSession = new CommonSession(ACFollowerAwaitingManageActivity.this);
            acFollowerAwaitingManageActivity = this;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean= (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
                communityText=bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT);
            }
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            getFollowers();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }


    public void setupHeaderView() {
        try {
            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.followers));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.VISIBLE);

            imageview_right_foursquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openCommunityMenu(ACFollowerAwaitingManageActivity.this,communityID,communityText,communityAccessbean);

                }
            });

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
                    MyUtils.leftMenuUpdateDataifOpenedDrawer(ACFollowerAwaitingManageActivity.this,drawerLayout,circularImageView_profile_pic,baseTextview_left_menu_unread_message,retryParameterbean);
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
            new BaseException(e, false, retryParameterbean);
        }

    }


    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.ac_followers_main, null);
            slidingToggleSwitchView = (SlidingToggleSwitchView) view_home.findViewById(R.id.sliding_toggle_switch);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);

            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view_followers);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            acFollowersAdapter = new ACFollowersAdapter(ACFollowerAwaitingManageActivity.this, mRecyclerView);
            mRecyclerView.setAdapter(acFollowersAdapter);
            frameLayout_container.addView(view_home);


            acFollowersAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            current_start += 10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);
                            getFollowers();
                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                        }
                    }, 2000);

                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }


    private void setupEvents() {
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(ACFollowerAwaitingManageActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        // TODO Handle item click
                        ACFollowersbean acFollowersbean1 = (ACFollowersbean) v.getTag();
                        showDialogForApproveOrReject(acFollowersbean1, position);

                    }
                })
        );

    }


    public void setupApprovedListing() {

        try {
            acfollowersbeanArrayList_filtered.clear();
            acFollowersAdapter.notifyDataSetChanged();


            for (int i = 0; i < acfollowersbeanArrayList_all.size(); i++) {
                ACFollowersbean acFollowersbean = acfollowersbeanArrayList_all.get(i);
                if (acFollowersbean.isApproved()) {
                    acfollowersbeanArrayList_filtered.add(acFollowersbean);
                } else {

                }
            }

            if (acfollowersbeanArrayList_filtered.size() == 0) {
                baseTextview_error.setText(getResources().getString(R.string.no_approved_records_found));
                baseTextview_error.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                acFollowersAdapter.notifyDataSetChanged();

                baseTextview_error.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    public void setupAwaitingdListing() {


        try {
            acfollowersbeanArrayList_filtered.clear();
            acFollowersAdapter.notifyDataSetChanged();


            for (int i = 0; i < acfollowersbeanArrayList_all.size(); i++) {
                ACFollowersbean acFollowersbean = acfollowersbeanArrayList_all.get(i);
                if (acFollowersbean.isApproved()) {
                } else {
                    acfollowersbeanArrayList_filtered.add(acFollowersbean);
                }
            }

            if (acfollowersbeanArrayList_filtered.size() == 0) {
                baseTextview_error.setText(getResources().getString(R.string.no_awaiting_records_found));
                baseTextview_error.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {

                acFollowersAdapter.notifyDataSetChanged();
                baseTextview_error.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    // this method is used for login user
    public void getFollowers() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ACFOLLOWERS.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, ACFollowerAwaitingManageActivity.this, getApplicationContext(), URLs.GETAPPROVEAWAITLIST, jsonObjectGetPostParameterEachScreen, ScreensEnums.ACFOLLOWERS.getScrenIndex(), ACFollowerAwaitingManageActivity.class.getClass());


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

                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.userList)) {
                        JSONArray jsonArray_followers_awating = jsonObject_main.getJSONArray(JSONCommonKeywords.userList);
                        ArrayList<ACFollowersbean> acFollowersbeanArrayList_local = new ArrayList<>();

                        for (int i = 0; i < jsonArray_followers_awating.length(); i++) {

                            JSONObject jsonObject_single_follower = jsonArray_followers_awating.getJSONObject(i);
                            ACFollowersbean acFollowersbean = new ACFollowersbean();

                            if (CommonMethods.handleKeyInJSON(jsonObject_single_follower, JSONCommonKeywords.UserName)) {
                                acFollowersbean.setFollowers_name(jsonObject_single_follower.getString(JSONCommonKeywords.UserName));
                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single_follower, JSONCommonKeywords.CommunityID)) {
                                acFollowersbean.setCommunityID(jsonObject_single_follower.getString(JSONCommonKeywords.CommunityID));
                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single_follower, JSONCommonKeywords.userid)) {
                                acFollowersbean.setFollowersUserID(jsonObject_single_follower.getString(JSONCommonKeywords.userid));
                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single_follower, JSONCommonKeywords.ProfilePic)) {
                                acFollowersbean.setFoloowers_profilepic(jsonObject_single_follower.getString(JSONCommonKeywords.ProfilePic));
                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single_follower, JSONCommonKeywords.Location)) {
                                acFollowersbean.setFollowers_address(jsonObject_single_follower.getString(JSONCommonKeywords.Location));
                            }
                            acFollowersbean.setPosition(i);

                            acFollowersbean.setPosition_main_item(i);
                            if (CommonMethods.handleKeyInJSON(jsonObject_single_follower, JSONCommonKeywords.isApproved)) {
                                int isApproved = jsonObject_single_follower.getInt(JSONCommonKeywords.isApproved);
                                if (isApproved == 1) {
                                    acFollowersbean.setApproved(true);
                                } else {
                                    acFollowersbean.setApproved(false);
                                }

                            }


                            acFollowersbeanArrayList_local.add(acFollowersbean);


                        }

                        if (current_start == 0) {
                            acfollowersbeanArrayList_all = acFollowersbeanArrayList_local;

                            if (isApprovedOpenDefualt) {
                                setupApprovedListing();

                            } else {
                                setupAwaitingdListing();
                            }


                        } else {

                            int start = acfollowersbeanArrayList_all.size();

                            for (int i = 0; i < acFollowersbeanArrayList_local.size(); i++) {


                                ACFollowersbean acFollowersbean = acFollowersbeanArrayList_local.get(i);
                                acFollowersbean.setPosition(start);
                                acFollowersbean.setPosition_main_item(i);

                                acfollowersbeanArrayList_all.add(start, acFollowersbean);
                                start++;

                            }
                            acFollowersAdapter.setLoaded();
                            if (isApprovedOpenDefualt) {
                                setupApprovedListing();

                            } else {
                                setupAwaitingdListing();
                            }

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





        if (isApprovedOpenDefualt) {
            setupApprovedListing();

        } else {
            setupAwaitingdListing();
        }
    }

    @Override
    public void onToggle(int result) {

        if (result == SlidingToggleSwitchView.LEFT_SELECTED) {
            isApprovedOpenDefualt = true;
            setupApprovedListing();
        } else {
            setupAwaitingdListing();

        }


    }

    private void showDialogForApproveOrReject(final ACFollowersbean acFollowersbean, final int position) {

        // custom dialog
        try {
            final Dialog dialog = new Dialog(ACFollowerAwaitingManageActivity.this, R.style.MyDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow()
                    .setLayout(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );


            dialog.setContentView(R.layout.ac_followers_approve_reject_dialog);
            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);
            MyButtonView myButtonView_approve = (MyButtonView) dialog.findViewById(R.id.mybuttonview_approve);
            MyButtonView myButtonView_reject = (MyButtonView) dialog.findViewById(R.id.mybuttonview_reject);

            if (!acFollowersbean.isApproved()) {
                myButtonView_approve.setVisibility(View.VISIBLE);

            } else {
                myButtonView_approve.setVisibility(View.GONE);
            }


            myButtonView_approve.setOnClickListener(new MyOnClickListener(ACFollowerAwaitingManageActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {

                        requestParametersbean.setApproveOrReject(1);
                        approveOrReject(dialog, acFollowersbean, position);


                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            myButtonView_reject.setOnClickListener(new MyOnClickListener(ACFollowerAwaitingManageActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        requestParametersbean.setApproveOrReject(0);

                        approveOrReject(dialog, acFollowersbean, position);


                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });

            imageView_close_dialopg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }


    }


    public void approveOrReject(final Dialog dialog, final ACFollowersbean acFollowersbean, final int position) {

        requestParametersbean.setCommunityID(acFollowersbean.getCommunityID());
        requestParametersbean.setFollowersuserId(acFollowersbean.getFollowersUserID());

        JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.APPROVE_OR_REJECT_FOLLOWERS_REQUEST.getScrenIndex(), requestParametersbean);
        PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
            @Override
            public void setupUI(Responcebean responcebean) {

                if (responcebean.isServiceSuccess()) {
                    try {
                        JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                        if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {

//this is for reject request comes successfully so remove item from list
                            if (requestParametersbean.getApproveOrReject() == 0) {


                                if (isApprovedOpenDefualt == true) {
                                    acFollowersAdapter.notifyItemRemoved(position);
                                    acfollowersbeanArrayList_all.remove(acFollowersbean.getPosition_main_item());
                                    setupApprovedListing();
                                } else {
                                    acFollowersAdapter.notifyItemRemoved(position);
                                    acfollowersbeanArrayList_all.remove(acFollowersbean.getPosition_main_item());
                                    setupAwaitingdListing();
                                }


                            }

                            //awaiting request sent to for make approved

                            else if (requestParametersbean.getApproveOrReject() == 1) {


                                isApprovedOpenDefualt = true;
                                acFollowersAdapter.notifyItemRemoved(position);
                                acFollowersbean.setApproved(true);
                                acfollowersbeanArrayList_all.set(acFollowersbean.getPosition_main_item(), acFollowersbean);
                                slidingToggleSwitchView.onClick(slidingToggleSwitchView);
                                setupApprovedListing();


                            }
                            if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                            }


                            if (responcebean.getErrorMessage() == null) {
                                CommonMethods.customToastMessage(getResources().getString(R.string.your_request_sent), ACFollowerAwaitingManageActivity.this);

                            } else {
                                CommonMethods.customToastMessage(responcebean.getErrorMessage(), ACFollowerAwaitingManageActivity.this);

                            }
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }


                        } else {
                            if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                            }


                            if (responcebean.getErrorMessage() == null) {
                                CommonMethods.customToastMessage(getResources().getString(R.string.your_request_sent_failed), ACFollowerAwaitingManageActivity.this);

                            } else {
                                CommonMethods.customToastMessage(responcebean.getErrorMessage(), ACFollowerAwaitingManageActivity.this);

                            }

                        }


                    } catch (Exception e) {
                        CommonMethods.customToastMessage(e.getMessage(), ACFollowerAwaitingManageActivity.this);

                    }

                }


            }
        }, ACFollowerAwaitingManageActivity.this, ACFollowerAwaitingManageActivity.this, URLs.APPROVEREJECT, jsonObjectGetPostParameterEachScreen, ScreensEnums.APPROVE_OR_REJECT_FOLLOWERS_REQUEST.getScrenIndex(), SignInActivity.class.getClass());
        new RequestToServer(passParameterbean, null).execute();

    }
}
