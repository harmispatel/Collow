package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.EventAdapter;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Eventbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserEventMainActivity extends BaseActivity implements SetupViewInterface {

    View view_home = null;
   public static EventAdapter eventAdapter = null;
    public static ArrayList<Eventbean> eventbeanArrayList = new ArrayList<>();
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    protected Handler handler;
    CommonSession commonSession = null;
    int current_start=0;
    BaseTextview baseTextview_header_title=null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;

    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    public static UserEventMainActivity eventMainActivity=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(UserEventMainActivity.this, getApplicationContext(), getIntent().getExtras(), UserEventMainActivity.class.getClass());
            commonSession = new CommonSession(UserEventMainActivity.this);
            eventMainActivity=this;
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
            imageView_search = (ImageView) toolbar_header.findViewById(R.id.imageview_search_items);
            imageView_search.setVisibility(View.GONE);
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

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }


    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.event_user_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);

            Eventbean eventbean = new Eventbean();
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            eventbeanArrayList.add(eventbean);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            eventAdapter = new EventAdapter(eventMainActivity, mRecyclerView);
            mRecyclerView.setAdapter(eventAdapter);
            frameLayout_container.addView(view_home);

           // getEventListing();


            eventAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
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

    }


    // this method is used for login user
   /* public void getEventListing() {
        try {



            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.EVENT.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, UserEventMainActivity.this, getApplicationContext(), URLs.EVENT, jsonObjectGetPostParameterEachScreen, ScreensEnums.EVENT.getScrenIndex(), UserEventMainActivity.class.getClass());

            passParameterbean.setNeedToFirstTakeFacebookProfilePic(false);

            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }*/

    @Override
    public void setupUI(Responcebean responcebean) {

        try {
            if (responcebean.isServiceSuccess()) {

                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                    //parse here data of following

                    ArrayList<Eventbean> eventbean_local = new ArrayList<>();
                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.Activities)) {
                        JSONArray jsonArray_events = jsonObject_main.getJSONArray(JSONCommonKeywords.Activities);
                        for (int i = 0; i < jsonArray_events.length(); i++) {

                            JSONObject jsonObject_single_event = jsonArray_events.getJSONObject(i);
                            Eventbean eventbean = new Eventbean();
                            eventbean_local.add(eventbean);
                        }

                        if (current_start == 0) {
                            eventbeanArrayList = eventbean_local;
                            eventAdapter.notifyDataSetChanged();

                        } else {

                            int start = eventbeanArrayList.size();

                            for (int i = 0; i < eventbean_local.size(); i++) {

                                eventbeanArrayList.add(start, eventbean_local.get(i));
                                eventAdapter.notifyItemInserted(eventbeanArrayList.size());
                                start++;

                            }
                            eventAdapter.setLoaded();

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

    public void handlerError(Responcebean responcebean)
    {
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