package com.app.collow.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.ClassifiedAdapter;
import com.app.collow.adapters.PollsAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Classifiedbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.Pollsbean;
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

public class PollsMainActivity extends BaseActivity implements SetupViewInterface {

    View view_home = null;
    PollsAdapter pollsAdapter = null;
    public static ArrayList<Pollsbean> pollsbeanArrayList = new ArrayList<>();
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    protected Handler handler;
    CommonSession commonSession = null;

    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retryParameterbean = new RetryParameterbean(PollsMainActivity.this, getApplicationContext(), getIntent().getExtras(), PollsMainActivity.class.getClass());
        commonSession = new CommonSession(PollsMainActivity.this);

        handler = new Handler();
        setupHeaderView();
        findViewByIDs();
        setupEvents();

        BaseTextview baseTextview_classifed_name=(BaseTextview)findViewById(R.id.textview_polls_title);
        baseTextview_classifed_name.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                Intent launchActivity1= new Intent(PollsMainActivity.this,Polls_Detail.class);
                startActivity(launchActivity1);

            }
        });

    }

    public void setupHeaderView() {
        View headerview = getLayoutInflater().inflate(R.layout.header, null);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        headerview.setLayoutParams(layoutParams);


        imageView_left_menu = (ImageView) headerview.findViewById(R.id.imageview_left_menu);
        imageView_left_menu.setVisibility(View.VISIBLE);
        imageView_right_menu = (ImageView) headerview.findViewById(R.id.imageview_right_menu);
        imageView_right_menu.setVisibility(View.VISIBLE);


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


    }

    private void findViewByIDs() {
        view_home = getLayoutInflater().inflate(R.layout.recyleview_main, null);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
        mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
        // use a linear layout manager
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        pollsAdapter = new PollsAdapter(pollsbeanArrayList, mRecyclerView);
        mRecyclerView.setAdapter(pollsAdapter);
        frameLayout_container.addView(view_home);

        getPollsList(0);


        pollsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add null , so the adapter will check view_type and show progress bar at bottom
                pollsbeanArrayList.add(null);
                mRecyclerView.post(new Runnable() {
                    public void run() {
                        pollsAdapter.notifyItemInserted(pollsbeanArrayList.size() - 1);
                    }
                });
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //   remove progress item
                        pollsbeanArrayList.remove(pollsbeanArrayList.size() - 1);
                        pollsAdapter.notifyItemRemoved(pollsbeanArrayList.size());
                        //add items one by one
                        int start = pollsbeanArrayList.size();
                        int end = start + 20;
                        getPollsList(20);

                        for (int i = start + 1; i <= end; i++) {
                            pollsbeanArrayList.add(new Pollsbean());
                           pollsAdapter.notifyItemInserted(pollsbeanArrayList.size());
                        }
                        pollsAdapter.setLoaded();
                        //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                    }
                }, 2000);

            }
        });

    }

    private void setupEvents() {

    }


    // this method is used for login user
    public void getPollsList(int startLimit) {
        try {

            requestParametersbean.setStart_limit(startLimit);

            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.POLLS.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, PollsMainActivity.this, getApplicationContext(), URLs.POLLS, jsonObjectGetPostParameterEachScreen, ScreensEnums.POLLS.getScrenIndex(), PollsMainActivity.class.getClass());

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
