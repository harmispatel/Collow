package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.MgmtTeamListingAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ClaimCommunitybean;
import com.app.collow.beans.MgmtTeamListingbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MgmtTeamListingActivity extends BaseActivity implements SetupViewInterface {

    View view_home = null;
    MgmtTeamListingAdapter mgmtTeamListingAdapter = null;
    public static MgmtTeamListingActivity mgmtTeamListingActivity=null;
    public static ArrayList<MgmtTeamListingbean> mgmtcommunitybeanArrayList = new ArrayList<>();
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    BaseTextview baseTextview_header_title=null;
    protected Handler handler;
    CommonSession commonSession = null;
    BaseTextview baseTextview_left_side=null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null,imageview_right_foursquare=null;
    ImageView imageView_delete = null,imageView_view=null,imageView_edit=null,imageView_search=null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(MgmtTeamListingActivity.this, getApplicationContext(), getIntent().getExtras(), MgmtTeamListingActivity.class.getClass());
            commonSession = new CommonSession(MgmtTeamListingActivity.this);
            mgmtTeamListingActivity=this;
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();


           // getTeamListing();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title= (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.select_team));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

           /* imageView_delete = (ImageView) headerview.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.GONE);
            imageView_view = (ImageView) headerview.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.GONE);
            imageView_edit = (ImageView) headerview.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.VISIBLE);
            imageview_right_foursquare= (ImageView) headerview.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.GONE);

            imageView_search= (ImageView) headerview.findViewById(R.id.imageview_community_search);
            imageView_search.setVisibility(View.GONE);*/
            // baseTextview_left_side = (BaseTextview) headerview.findViewById(R.id.textview_left_side_title);

            // baseTextview_left_side.setText(getResources().getString(R.string.cancel));
            // baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);

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

                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);

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

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.recyleview_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            MgmtTeamListingbean communitybean = new MgmtTeamListingbean();
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mgmtcommunitybeanArrayList.add(communitybean);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            mgmtTeamListingAdapter = new MgmtTeamListingAdapter(MgmtTeamListingActivity.this, mRecyclerView);
            mRecyclerView.setAdapter(mgmtTeamListingAdapter);
            frameLayout_container.addView(view_home);



            mgmtTeamListingAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            current_start += 10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);
                            getTeamListing();

                        }
                    }, 2000);

                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    private void setupEvents() {

    }


    // this method is used for login user
    public void getTeamListing() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());

            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.MGMT_TEAM_LISTING.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, MgmtTeamListingActivity.this, getApplicationContext(), URLs.GET_TEAM_LISTING, jsonObjectGetPostParameterEachScreen, ScreensEnums.MGMT_TEAM_LISTING.getScrenIndex(), MgmtTeamListingActivity.class.getClass());


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

                    if(CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.teamsList))
                    {

                        JSONArray jsonArray = jsonObject_main.getJSONArray(JSONCommonKeywords.teamsList);
                        ArrayList<MgmtTeamListingbean> mgmtTeamListingbeanArrayList_local = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject_single_claime = jsonArray.getJSONObject(i);
                            MgmtTeamListingbean mgmtTeamListingbean = new MgmtTeamListingbean();

                            if (jsonObject_single_claime.has(JSONCommonKeywords.teamid)) {
                                mgmtTeamListingbean.setMgmtmanagecommunity_name(jsonObject_single_claime.getString(JSONCommonKeywords.teamid));
                            }


                            if (jsonObject_single_claime.has(JSONCommonKeywords.teamname)) {
                                mgmtTeamListingbean.setMgmtmanagecommunity_name(jsonObject_single_claime.getString(JSONCommonKeywords.teamname));
                            }


                            mgmtTeamListingbean.setPosition(i);

                            mgmtTeamListingbeanArrayList_local.add(mgmtTeamListingbean);


                        }

                        if (current_start == 0) {
                            mgmtcommunitybeanArrayList = mgmtTeamListingbeanArrayList_local;
                            mgmtTeamListingAdapter.notifyDataSetChanged();

                        } else {

                            int start = mgmtcommunitybeanArrayList.size();

                            for (int i = 0; i < mgmtTeamListingbeanArrayList_local.size(); i++) {

                                mgmtcommunitybeanArrayList.add(start, mgmtTeamListingbeanArrayList_local.get(i));
                                mgmtTeamListingAdapter.notifyItemInserted(mgmtcommunitybeanArrayList.size());
                                start++;

                            }
                            mgmtTeamListingAdapter.setLoaded();

                        }

                    }
                    else
                    {
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
                baseTextview_error.setText(getResources().getString(R.string.no_teams_record_found));
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
