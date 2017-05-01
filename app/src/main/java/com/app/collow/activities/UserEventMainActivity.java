package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.adapters.EventAdapter;
import com.app.collow.adapters.SearchResultsAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.CommunityTypebean;
import com.app.collow.beans.EventTypebean;
import com.app.collow.beans.Eventbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.Statesbean;
import com.app.collow.beansgenerate.EventbeanBuild;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.commondialogs.MyDatePicker;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.httprequests.MyDatePickerInterface;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserEventMainActivity extends BaseActivity implements SetupViewInterface , MyDatePickerInterface {

    public static EventAdapter eventAdapter = null;
    public static ArrayList<Eventbean> eventbeanArrayList = new ArrayList<>();
    public static UserEventMainActivity eventMainActivity = null;
    protected Handler handler;
    View view_home = null;
    CommonSession commonSession = null;
    int current_start = 0;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    CustomCalendarView calendarView;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    String communityID = null;
    //Right menu data
    RelativeLayout relativeLayout_state_right_menu_new = null, relativeLayout_communitiy_type_right_menu = null, relativeLayout_select_event_type = null;
    BaseEdittext baseEdittext_city_right_mmenu = null;
    MyButtonView myButtonView_filter_apply = null, myButtonView_clear_filter = null;
    LinearLayout linearLayout_event_type = null;
    BaseTextview baseTextview_startDate = null, baseTextview_endDate = null;
    public static BaseTextview  baseTextview_event_type=null;
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    CommunityAccessbean communityAccessbean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(UserEventMainActivity.this, getApplicationContext(), getIntent().getExtras(), UserEventMainActivity.class.getClass());
            commonSession = new CommonSession(UserEventMainActivity.this);
            eventMainActivity = this;
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                requestParametersbean.setCommunityID(communityID);
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);

            }
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();

            Date  date=Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            requestParametersbean.setStartDate(df.format(date));
            getEventListing();


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    public void setupHeaderView() {
        try {


            //right navigation
            //Right navigation iterms
            myButtonView_filter_apply = (MyButtonView) findViewById(R.id.mybuttonview_filter_apply);
            myButtonView_clear_filter = (MyButtonView) findViewById(R.id.mybuttonview_filter_clear);
            baseEdittext_city_right_mmenu = (BaseEdittext) findViewById(R.id.edittext_filter_city);
            baseEdittext_city_right_mmenu.setVisibility(View.GONE);

            relativeLayout_state_right_menu_new = (RelativeLayout) findViewById(R.id.relativelayout_select_filter_state_right_menu_new);
            relativeLayout_state_right_menu_new.setVisibility(View.GONE);

            relativeLayout_communitiy_type_right_menu = (RelativeLayout) findViewById(R.id.relativelayout_select_community_type_right_menu);
            relativeLayout_communitiy_type_right_menu.setVisibility(View.GONE);


            relativeLayout_select_event_type = (RelativeLayout) findViewById(R.id.relativelayout_select_event_type_right_menu);


            linearLayout_event_type = (LinearLayout) findViewById(R.id.layout_event_filter);
            linearLayout_event_type.setVisibility(View.VISIBLE);


            baseTextview_startDate = (BaseTextview) findViewById(R.id.textview_start_date_right_menu);
            baseTextview_endDate = (BaseTextview) findViewById(R.id.textview_end_date_right_menu);
            baseTextview_event_type= (BaseTextview) findViewById(R.id.textview_event_type_right_menu);
            relativeLayout_select_event_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(UserEventMainActivity.this, SelectEventTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.EVENT.getScrenIndex());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            myButtonView_clear_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (baseTextview_event_type != null) {

                        baseTextview_event_type.setText(getResources().getString(R.string.select_event_type));
                    }
                    if (baseTextview_startDate != null) {

                        baseTextview_startDate.setText(getResources().getString(R.string.start_date));
                    }
                    if (baseTextview_endDate != null) {

                        baseTextview_endDate.setText(getResources().getString(R.string.end_date));
                    }

                    requestParametersbean.setTypeID("");
                    requestParametersbean.setStartDate("");
                    requestParametersbean.setEndDate("");


                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    eventbeanArrayList.clear();
                    eventAdapter = new EventAdapter(eventMainActivity, mRecyclerView,communityID,communityAccessbean);
                    mRecyclerView.setAdapter(eventAdapter);
                    current_start = 0;

                    getEventListing();
                }
            });
            baseTextview_startDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new MyDatePicker();
                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.START_DATE.getScrenIndex());
                    newFragment.setArguments(bundle);
                    newFragment.show(getSupportFragmentManager(), "StartDate");
                }
            });
            baseTextview_endDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new MyDatePicker();

                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.END_DATE.getScrenIndex());
                    newFragment.setArguments(bundle);
                    newFragment.show(getSupportFragmentManager(), "EndDate");
                }
            });
            myButtonView_filter_apply.setOnClickListener(new MyOnClickListener(UserEventMainActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {


                        boolean isStartDate = false, isEndDate = false, isTypeEmpty = false;


                        if (baseTextview_startDate != null) {

                            if (baseTextview_startDate.getText().toString().equals(getResources().getString(R.string.start_date))) {
                                isStartDate = true;
                            } else {
                                isStartDate = false;

                                String startdate = baseTextview_startDate.getText().toString();

                            }


                        }


                        if (baseTextview_endDate != null) {

                            if (baseTextview_endDate.getText().toString().equals(getResources().getString(R.string.end_date))) {
                                isEndDate = true;
                            } else {
                                isEndDate = false;

                                String enddate = baseTextview_endDate.getText().toString();

                            }


                        }


                        if (baseTextview_event_type != null) {

                            if (baseTextview_event_type.getText().toString().equals(getResources().getString(R.string.select_event_type))) {
                                isTypeEmpty = true;
                            } else {
                                isTypeEmpty = false;

                                EventTypebean eventTypebean = (EventTypebean) baseTextview_event_type.getTag();
                                if (eventTypebean != null) {
                                    requestParametersbean.setTypeID(eventTypebean.getTypeID());
                                }
                            }


                        }




                        if (isStartDate && isEndDate && isTypeEmpty ) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.please_enter_any_search_criteria), UserEventMainActivity.this);

                        } else {
                            drawerLayout.closeDrawer(Gravity.RIGHT);
                            eventbeanArrayList.clear();
                            eventAdapter = new EventAdapter(eventMainActivity, mRecyclerView,communityID,communityAccessbean);
                            mRecyclerView.setAdapter(eventAdapter);
                            current_start = 0;
                            getEventListing();
                        }


                    } else {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                    }
                }
            });


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
            imageview_right_foursquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openCommunityMenu(UserEventMainActivity.this,communityID,"",communityAccessbean);

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

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }


    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.event_user_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);


            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

            eventAdapter = new EventAdapter(eventMainActivity, mRecyclerView,communityID,communityAccessbean);
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


            //Initialize CustomCalendarView from layout
            calendarView = (CustomCalendarView) view_home.findViewById(R.id.calendar_view);

            //Initialize calendar with date
            Calendar currentCalendar = Calendar.getInstance(Locale.getDefault());

            //Show monday as first date of week
            calendarView.setFirstDayOfWeek(Calendar.MONDAY);

            //Show/hide overflow days of a month
            calendarView.setShowOverflowDate(true);

            //call refreshCalendar to update calendar the view
            calendarView.refreshCalendar(currentCalendar);

            //Handling custom calendar events
            calendarView.setCalendarListener(new CalendarListener() {
                @Override
                public void onDateSelected(Date date) {
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                    requestParametersbean.setStartDate(df.format(date));
                    requestParametersbean.setEndDate("");
                    requestParametersbean.setTypeID("");
                    eventbeanArrayList.clear();
                    eventAdapter = new EventAdapter(eventMainActivity, mRecyclerView,communityID,communityAccessbean);
                    mRecyclerView.setAdapter(eventAdapter);
                    current_start = 0;
                    getEventListing();

                }

                @Override
                public void onMonthChanged(Date date) {
                    SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                    Toast.makeText(UserEventMainActivity.this, df.format(date), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }

    }

    private void setupEvents() {

    }


    // this method is used for login user
    public void getEventListing() {
        try {


            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setStart_limit(current_start);
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ACGETEVENTS.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, UserEventMainActivity.this, getApplicationContext(), URLs.EVENTLISTING, jsonObjectGetPostParameterEachScreen, ScreensEnums.ACGETEVENTS.getScrenIndex(), ACListEventsMainActivity.class.getClass());


            if(current_start==0)
            {
                passParameterbean.setNoNeedToDisplayLoadingDialog(false);

            }
            else
            {
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

                    ArrayList<Eventbean> eventbean_local = new ArrayList<>();
                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.EventListing)) {
                        JSONArray jsonArray_events = jsonObject_main.getJSONArray(JSONCommonKeywords.EventListing);
                        for (int i = 0; i < jsonArray_events.length(); i++) {

                            JSONObject jsonObject_single_event = jsonArray_events.getJSONObject(i);
                            Eventbean eventbean = EventbeanBuild.getEventbeanFromJSON(jsonObject_single_event);
                            eventbean.setPosition(i);
                            eventbean_local.add(eventbean);
                        }

                        if (current_start == 0) {
                            eventbeanArrayList = eventbean_local;
                            eventAdapter.notifyDataSetChanged();

                            mRecyclerView.setVisibility(View.VISIBLE);
                            baseTextview_error.setVisibility(View.GONE);

                        } else {

                            int start = eventbeanArrayList.size();

                            for (int i = 0; i < eventbean_local.size(); i++) {

                                Eventbean eventbean = eventbean_local.get(i);
                                eventbean.setPosition(start);
                                eventbean_local.add(start, eventbean);
                                eventAdapter.notifyItemInserted(eventbean_local.size());
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




    @Override
    public void selectedDate(String selectedDate, Date date) {

    }

    @Override
    public void selectedDate(String selectedDate, Date date, int index) {
        if(index==ScreensEnums.START_DATE.getScrenIndex())
        {
            if(baseTextview_startDate!=null)
            {
                baseTextview_startDate.setText(selectedDate);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                requestParametersbean.setStartDate(df.format(date));

            }
        }
        else if(index==ScreensEnums.END_DATE.getScrenIndex())
        {
            if(baseTextview_endDate!=null)
            {
                baseTextview_endDate.setText(selectedDate);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

                requestParametersbean.setEndDate(df.format(date));
            }
        }
    }

    @Override
    public void selectedTime(int hourOfDay, int minute, int forwhichFieldNeedToSet, boolean is24HOursFormat) {

    }
}