package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.MgmtSelectTeamAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.MgmtCommuityAndAdminbean;
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
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

import java.util.ArrayList;

import de.nitri.slidingtoggleswitch.SlidingToggleSwitchView;

public class MgmtAdminAndCommunitiesListingofTeamActivity extends BaseActivity implements SetupViewInterface, SlidingToggleSwitchView.OnToggleListener {

    public static ArrayList<MgmtCommuityAndAdminbean> mgmtteambeanArrayList_all_main = new ArrayList<>();
    public static ArrayList<MgmtCommuityAndAdminbean> mgmtteambeanArrayList_communities = new ArrayList<>();
    public static ArrayList<MgmtCommuityAndAdminbean> mgmtteambeanArrayList_admins = new ArrayList<>();
    protected Handler handler;
    View view_home = null;
    MgmtSelectTeamAdapter mgmtTeamAdapter_communities = null, mgmtSelectTeamAdapter_admins = null;
    BaseTextview baseTextview_header_title = null;
    BaseTextview baseTextview_left_side = null;
    CommonSession commonSession = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    MgmtTeamListingbean mgmtTeamListingbean = null;
    int current_start = 0;
    boolean isCommunityTabOpened = true;
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView_communities = null, mRecyclerView_admins = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(MgmtAdminAndCommunitiesListingofTeamActivity.this, getApplicationContext(), getIntent().getExtras(), MgmtAdminAndCommunitiesListingofTeamActivity.class.getClass());
            commonSession = new CommonSession(MgmtAdminAndCommunitiesListingofTeamActivity.this);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                mgmtTeamListingbean = (MgmtTeamListingbean) bundle.getSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN);
            }

            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            //   getCommunitiesAndAdminsForSelectedTeam();


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            if (mgmtTeamListingbean != null) {
                if (mgmtTeamListingbean.getMgmtmanagecommunity_name() != null) {
                    baseTextview_header_title.setText(mgmtTeamListingbean.getMgmtmanagecommunity_name());

                }
            }

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);
          /*  imageView_delete = (ImageView) headerview.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.GONE);
            imageView_view = (ImageView) headerview.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.GONE);
            imageView_edit = (ImageView) headerview.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.VISIBLE);
            imageview_right_foursquare= (ImageView) headerview.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.GONE);

            imageView_search= (ImageView) headerview.findViewById(R.id.imageview_community_search);
            imageView_search.setVisibility(View.GONE);*/
            //baseTextview_left_side = (BaseTextview) headerview.findViewById(R.id.textview_left_side_title);

            // baseTextview_left_side.setText(getResources().getString(R.string.cancel));
            //baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);


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
            view_home = getLayoutInflater().inflate(R.layout.mgmt_admin_community_listing_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            MgmtCommuityAndAdminbean teambean = new MgmtCommuityAndAdminbean();
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);
            mgmtteambeanArrayList_all_main.add(teambean);

            mRecyclerView_communities = (RecyclerView) view_home.findViewById(R.id.my_recycler_view_commmunities);
            mRecyclerView_admins = (RecyclerView) view_home.findViewById(R.id.my_recycler_view_admins);


            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView_communities.setLayoutManager(mLayoutManager);
            mRecyclerView_communities.addItemDecoration(dividerItemDecoration);

            mRecyclerView_admins.setLayoutManager(mLayoutManager);
            mRecyclerView_admins.addItemDecoration(dividerItemDecoration);


            mgmtTeamAdapter_communities = new MgmtSelectTeamAdapter(MgmtAdminAndCommunitiesListingofTeamActivity.this, mRecyclerView_communities);
            mRecyclerView_admins.setAdapter(mgmtTeamAdapter_communities);


            mgmtSelectTeamAdapter_admins = new MgmtSelectTeamAdapter(MgmtAdminAndCommunitiesListingofTeamActivity.this, mRecyclerView_admins);
            mRecyclerView_admins.setAdapter(mgmtSelectTeamAdapter_admins);


            frameLayout_container.addView(view_home);


            mgmtTeamAdapter_communities.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            current_start += 10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);
                            getCommunitiesAndAdminsForSelectedTeam();
                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();

                        }
                    }, 2000);

                }
            });

            mgmtSelectTeamAdapter_admins.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            current_start += 10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);
                            getCommunitiesAndAdminsForSelectedTeam();
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

    }


    // this method is used for login user
    public void getCommunitiesAndAdminsForSelectedTeam() {
        try {


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.GET_ADMIN_COMMUNITITIES_BASED_ON_TEAM.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, MgmtAdminAndCommunitiesListingofTeamActivity.this, getApplicationContext(), URLs.GET_COMMUNITIES_AND_ADMINS_BASED_ON_TEAM, jsonObjectGetPostParameterEachScreen, ScreensEnums.GET_ADMIN_COMMUNITITIES_BASED_ON_TEAM.getScrenIndex(), MgmtAdminAndCommunitiesListingofTeamActivity.class.getClass());


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

    @Override
    public void onToggle(int result) {

        if (result == SlidingToggleSwitchView.LEFT_SELECTED) {
            isCommunityTabOpened = true;
            setupCommmunitiesListing();
        } else {
            isCommunityTabOpened = false;
            setupAdminListing();

        }


    }


    public void setupCommmunitiesListing() {

        try {

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    public void setupAdminListing() {


        try {

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void handlerError(Responcebean responcebean) {


        try {


            if (isCommunityTabOpened) {
                if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                    if (current_start == 0) {
                        baseTextview_error.setText(getResources().getString(R.string.no_communities_records_found));
                        mRecyclerView_communities.setVisibility(View.GONE);
                        baseTextview_error.setVisibility(View.VISIBLE);

                    } else {

                    }
                } else {
                    if (current_start == 0) {
                        baseTextview_error.setText(responcebean.getErrorMessage());
                        mRecyclerView_communities.setVisibility(View.GONE);
                        baseTextview_error.setVisibility(View.VISIBLE);

                    } else {

                    }
                }
            } else {


                if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                    if (current_start == 0) {
                        baseTextview_error.setText(getResources().getString(R.string.no_admins_records_found));
                        mRecyclerView_admins.setVisibility(View.GONE);
                        baseTextview_error.setVisibility(View.VISIBLE);

                    } else {

                    }
                } else {
                    if (current_start == 0) {
                        baseTextview_error.setText(responcebean.getErrorMessage());
                        mRecyclerView_admins.setVisibility(View.GONE);
                        baseTextview_error.setVisibility(View.VISIBLE);

                    } else {

                    }
                }
            }


        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }
    }
}

