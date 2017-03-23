package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.MgmtSelectTeamAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.MgmtSelectTeambean;
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
import com.app.collow.utils.URLs;

import org.json.JSONObject;

import java.util.ArrayList;

public class MgmtAdminAndCommunitiesListingofTeamActivity extends BaseActivity implements SetupViewInterface {

    View view_home = null;
    MgmtSelectTeamAdapter mgmtTeamAdapter = null;
    public static ArrayList<MgmtSelectTeambean> mgmtteambeanArrayList = new ArrayList<>();
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    BaseTextview baseTextview_header_title=null;
    BaseTextview baseTextview_left_side=null;
    protected Handler handler;
    CommonSession commonSession = null;

    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null,imageview_right_foursquare=null;
    ImageView imageView_delete = null,imageView_view=null,imageView_edit=null,imageView_search=null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(MgmtAdminAndCommunitiesListingofTeamActivity.this, getApplicationContext(), getIntent().getExtras(), MgmtAdminAndCommunitiesListingofTeamActivity.class.getClass());
            commonSession = new CommonSession(MgmtAdminAndCommunitiesListingofTeamActivity.this);

            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();

            BaseTextview baseTextview_mgmtmanageteam_name=(BaseTextview)findViewById(R.id.textview_mgmtmanageteam_teamname);
            baseTextview_mgmtmanageteam_name.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    Intent launchActivity1= new Intent(MgmtAdminAndCommunitiesListingofTeamActivity.this,MgmtTeamListingActivity.class);
                    startActivity(launchActivity1);

                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    public void setupHeaderView() {
        try {
            View headerview = getLayoutInflater().inflate(R.layout.header, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerview.setLayoutParams(layoutParams);
            baseTextview_header_title= (BaseTextview) headerview.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.news));

            imageView_left_menu = (ImageView) headerview.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) headerview.findViewById(R.id.imageview_right_menu);
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
            setSupportActionBar(toolbar_header);
            toolbar_header.addView(headerview);
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.recyleview_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            MgmtSelectTeambean teambean = new MgmtSelectTeambean();
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mgmtteambeanArrayList.add(teambean);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            mgmtTeamAdapter = new MgmtSelectTeamAdapter(mgmtteambeanArrayList, mRecyclerView);
            mRecyclerView.setAdapter(mgmtTeamAdapter);
            frameLayout_container.addView(view_home);

            getTeamList(0);


            mgmtTeamAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom
                    mgmtteambeanArrayList.add(null);
                    mRecyclerView.post(new Runnable() {
                        public void run() {
                            mgmtTeamAdapter.notifyItemInserted(mgmtteambeanArrayList.size() - 1);
                        }
                    });
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //   remove progress item
                            mgmtteambeanArrayList.remove(mgmtteambeanArrayList.size() - 1);
                            mgmtTeamAdapter.notifyItemRemoved(mgmtteambeanArrayList.size());
                            //add items one by one
                            int start = mgmtteambeanArrayList.size();
                            int end = start + 20;
                            getTeamList(20);

                            for (int i = start + 1; i <= end; i++) {
                                mgmtteambeanArrayList.add(new MgmtSelectTeambean());
                                mgmtTeamAdapter.notifyItemInserted(mgmtteambeanArrayList.size());
                            }
                            mgmtTeamAdapter.setLoaded();
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
    public void getTeamList(int startLimit) {
        try {

            requestParametersbean.setStart_limit(startLimit);

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

    public void handlerError(Responcebean responcebean)
    {
        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals(""))
        {
            if(requestParametersbean.getStart_limit()==0)
            {
                baseTextview_error.setText(getResources().getString(R.string.no_data_founds));
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            }
            else
            {

            }
        }
        else
        {
            if(requestParametersbean.getStart_limit()==0)
            {
                baseTextview_error.setText(responcebean.getErrorMessage());
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            }
            else
            {

            }
        }
    }
}
