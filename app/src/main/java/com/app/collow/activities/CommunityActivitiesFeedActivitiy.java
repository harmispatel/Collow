package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.CommunityActivitiesFeedAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityActivitiesFeedbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Harmis on 30/01/17.
 */

public class CommunityActivitiesFeedActivitiy extends BaseActivity implements SetupViewInterface {

    public ArrayList<CommunityActivitiesFeedbean> communityActivitiesFeedbeanArrayList = new ArrayList<CommunityActivitiesFeedbean>();
    protected Handler handler;
    View view_home = null;
    CommunityActivitiesFeedAdapter communityActivitiesFeedAdapter = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    private BaseTextview baseTextview_empty_view;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            retryParameterbean = new RetryParameterbean(CommunityActivitiesFeedActivitiy.this, getApplicationContext(), getIntent().getExtras(), CommunityActivitiesFeedActivitiy.class.getClass());

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
            View headerview = getLayoutInflater().inflate(R.layout.header, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerview.setLayoutParams(layoutParams);
            baseTextview_header_title = (BaseTextview) headerview.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.activity));

            imageView_left_menu = (ImageView) headerview.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) headerview.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            imageView_right_menu.setImageResource(R.drawable.community_main_menu);


            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    MyUtils.openCommunityMenu(CommunityActivitiesFeedActivitiy.this,false,false,"3",baseTextview_header_title.getText().toString());


                }
            });
            setSupportActionBar(toolbar_header);
            toolbar_header.addView(headerview);
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.recyleview_main_without_margin, null);
            baseTextview_empty_view = (BaseTextview) view_home.findViewById(R.id.empty_view);






            CommunityActivitiesFeedbean communityActivitiesFeedbean = new CommunityActivitiesFeedbean();
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            communityActivitiesFeedbeanArrayList.add(communityActivitiesFeedbean);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(10);

            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
         mRecyclerView.addItemDecoration(dividerItemDecoration);


            communityActivitiesFeedAdapter = new CommunityActivitiesFeedAdapter(communityActivitiesFeedbeanArrayList, mRecyclerView,CommunityActivitiesFeedActivitiy.this);
            mRecyclerView.setAdapter(communityActivitiesFeedAdapter);


            frameLayout_container.addView(view_home);

           /* communityActivitiesFeedAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom
                    communityActivitiesFeedbeanArrayList.add(null);
                    mRecyclerView.post(new Runnable() {
                        public void run() {
                            communityActivitiesFeedAdapter.notifyItemInserted(communityActivitiesFeedbeanArrayList.size() - 1);
                        }
                    });
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //   remove progress item
                            communityActivitiesFeedbeanArrayList.remove(communityActivitiesFeedbeanArrayList.size() - 1);
                            communityActivitiesFeedAdapter.notifyItemRemoved(communityActivitiesFeedbeanArrayList.size());
                            //add items one by one
                            int start = communityActivitiesFeedbeanArrayList.size();
                            int end = start + 20;

                            for (int i = start + 1; i <= end; i++) {
                                communityActivitiesFeedbeanArrayList.add(new CommunityActivitiesFeedbean());
                                communityActivitiesFeedAdapter.notifyItemInserted(communityActivitiesFeedbeanArrayList.size());
                            }
                            communityActivitiesFeedAdapter.setLoaded();
                            //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                        }
                    }, 2000);

                }
            });*/
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }

    }


    // get communties activities  feed from server
    public void getMyActivities() {
        try {


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CommunityActivitiesFeedActivitiy.this, getApplicationContext(), URLs.GET_COMMUNITIES_FEEDS, jsonObjectGetPostParameterEachScreen, ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex(), SignInActivity.class.getClass());


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

                JSONObject jsonObject=new JSONObject(responcebean.getResponceContent());
                if(jsonObject!=null)
                {
                    if(jsonObject.has(JSONCommonKeywords.success))
                    {
                        int success=jsonObject.getInt(JSONCommonKeywords.success);
                        if(success==1)
                        {

                        }
                        else
                        {

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