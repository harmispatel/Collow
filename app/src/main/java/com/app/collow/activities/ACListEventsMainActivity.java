package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.ACListEventAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACListEventBean;
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
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ACListEventsMainActivity extends BaseActivity implements SetupViewInterface {

    public static ArrayList<ACListEventBean> aceventbeanArrayList = new ArrayList<>();
    protected Handler handler;
    View view_home = null;
    ACListEventAdapter acListEventAdapter = null;
    BaseTextview baseTextview_header_title = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_left_side = null;
    FloatingActionButton floatingActionButton_create_events=null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    String communityID = null;
    int current_start = 0;
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(ACListEventsMainActivity.this, getApplicationContext(), null, ACListEventsMainActivity.class.getClass());
            commonSession = new CommonSession(ACListEventsMainActivity.this);

            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    public void setupHeaderView() {
        try {
            /*View headerview = getLayoutInflater().inflate(R.layout.header, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerview.setLayoutParams(layoutParams);*/
            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.events));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);

            imageView_delete = (ImageView) toolbar_header.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.GONE);
            imageView_view = (ImageView) toolbar_header.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.GONE);
            imageView_edit = (ImageView) toolbar_header.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.GONE);
            imageView_search = (ImageView) toolbar_header.findViewById(R.id.imageview_community_search);
            imageView_search.setVisibility(View.VISIBLE);
            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.VISIBLE);
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
           /* setSupportActionBar(toolbar_header);
            toolbar_header.addView(toolbar_header);*/
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.acevent_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            floatingActionButton_create_events = (FloatingActionButton) view_home.findViewById(R.id.fab_create_new_acevent);
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            ACListEventBean eventbean = new ACListEventBean();
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            aceventbeanArrayList.add(eventbean);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            acListEventAdapter = new ACListEventAdapter(ACListEventsMainActivity.this, mRecyclerView);
            mRecyclerView.setAdapter(acListEventAdapter);
            frameLayout_container.addView(view_home);

            // getEventList(0);


            acListEventAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            current_start += 10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);


                        }
                    }, 2000);

                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    private void setupEvents() {
        try {
            floatingActionButton_create_events.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ACListEventsMainActivity.this, ACNewEventMainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }


    // this method is used for login user
    public void getEventList() {
        try {


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ACGETEVENTS.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, ACListEventsMainActivity.this, getApplicationContext(), URLs.ACGETEVENTS, jsonObjectGetPostParameterEachScreen, ScreensEnums.ACGETEVENTS.getScrenIndex(), ACListEventsMainActivity.class.getClass());


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

                    ArrayList<ACListEventBean> acListEventBeen_local = new ArrayList<>();
                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.Activities)) {
                        JSONArray jsonArray_events = jsonObject_main.getJSONArray(JSONCommonKeywords.Activities);
                        for (int i = 0; i < jsonArray_events.length(); i++) {

                            JSONObject jsonObject_single_event = jsonArray_events.getJSONObject(i);
                            ACListEventBean acListEventBean = new ACListEventBean();
                            acListEventBeen_local.add(acListEventBean);
                        }

                        if (current_start == 0) {
                            aceventbeanArrayList = acListEventBeen_local;
                            acListEventAdapter.notifyDataSetChanged();

                        } else {

                            int start = aceventbeanArrayList.size();

                            for (int i = 0; i < acListEventBeen_local.size(); i++) {

                                aceventbeanArrayList.add(start, acListEventBeen_local.get(i));
                                acListEventAdapter.notifyItemInserted(aceventbeanArrayList.size());
                                start++;

                            }
                            acListEventAdapter.setLoaded();

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
        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
            if (current_start == 0) {
                baseTextview_error.setText(getResources().getString(R.string.no_events_exist));
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