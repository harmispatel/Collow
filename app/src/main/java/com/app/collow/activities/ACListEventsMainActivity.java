package com.app.collow.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.adapters.ACListEventAdapter;
import com.app.collow.allenums.ModificationOptions;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.EventTypebean;
import com.app.collow.beans.Eventbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.app.collow.R.string.events;
import static com.app.collow.R.string.search;

public class ACListEventsMainActivity extends BaseActivity implements SetupViewInterface, MyDatePickerInterface {

    public static ArrayList<Eventbean> aceventbeanArrayList_main = new ArrayList<>();
    public static ArrayList<Eventbean> aceventbeanArrayList_filtered = new ArrayList<>();

    public static ACListEventAdapter acListEventAdapter = null;
    public static BaseTextview baseTextview_error = null;
    public static RecyclerView mRecyclerView;
    public static ACListEventsMainActivity acListEventsMainActivity = null;
    public static BaseTextview baseTextview_event_type = null;
    protected Handler handler;
    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_left_side = null;
    FloatingActionButton floatingActionButton_create_events = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    EditText et_searchevent;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    String communityID = null;
    int current_start = 0;
    String communityText = null;
    CommunityAccessbean communityAccessbean = null;
    //Right menu data
    RelativeLayout relativeLayout_state_right_menu_new = null, relativeLayout_communitiy_type_right_menu = null, relativeLayout_select_event_type = null;
    BaseEdittext baseEdittext_city_right_mmenu = null;
    MyButtonView myButtonView_filter_apply = null, myButtonView_clear_filter = null;
    LinearLayout linearLayout_event_type = null;
    BaseTextview baseTextview_startDate = null, baseTextview_endDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(ACListEventsMainActivity.this, getApplicationContext(), null, ACListEventsMainActivity.class.getClass());
            commonSession = new CommonSession(ACListEventsMainActivity.this);
            acListEventsMainActivity = this;
            aceventbeanArrayList_main.clear();
            aceventbeanArrayList_filtered.clear();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityText = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT);
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);

                requestParametersbean.setCommunityID(communityID);
            }

            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            getEventList();

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
            baseTextview_event_type = (BaseTextview) findViewById(R.id.textview_event_type_right_menu);
            relativeLayout_select_event_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ACListEventsMainActivity.this, SelectEventTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.ACGETEVENTS.getScrenIndex());
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
                    aceventbeanArrayList_main.clear();
                    aceventbeanArrayList_filtered.clear();
                    acListEventAdapter = new ACListEventAdapter(ACListEventsMainActivity.this, mRecyclerView, communityID);
                    mRecyclerView.setAdapter(acListEventAdapter);
                    current_start = 0;

                    getEventList();
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
            myButtonView_filter_apply.setOnClickListener(new MyOnClickListener(ACListEventsMainActivity.this) {
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


                        if (isStartDate && isEndDate && isTypeEmpty) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.please_enter_any_search_criteria), ACListEventsMainActivity.this);

                        } else {
                            drawerLayout.closeDrawer(Gravity.RIGHT);
                            aceventbeanArrayList_main.clear();
                            aceventbeanArrayList_filtered.clear();
                            acListEventAdapter = new ACListEventAdapter(ACListEventsMainActivity.this, mRecyclerView, communityID);
                            mRecyclerView.setAdapter(acListEventAdapter);
                            current_start = 0;
                            getEventList();

                        }


                    } else {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                    }
                }
            });


            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.events));
            et_searchevent= (EditText) toolbar_header.findViewById(R.id.et_searchevent);
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
            imageView_search.setVisibility(View.VISIBLE);
            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.VISIBLE);
            //baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);
            imageview_right_foursquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openCommunityMenu(ACListEventsMainActivity.this, communityID, communityText, communityAccessbean);

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
            //adil code
            et_searchevent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    filter(s.toString());

                }
            });
            //adil code
            et_searchevent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;

                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        if(event.getRawX() >= (et_searchevent.getRight() - et_searchevent.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            et_searchevent.setText("");
                            et_searchevent.animate().alpha(0.0f);
                            et_searchevent.setVisibility(View.GONE);
                            imageView_left_menu.setVisibility(View.VISIBLE);
                            imageView_right_menu.setVisibility(View.VISIBLE);
                            imageView_search.setVisibility(View.VISIBLE);
                            imageview_right_foursquare.setVisibility(View.VISIBLE);
                            baseTextview_header_title.setVisibility(View.VISIBLE);


                            return true;
                        }
                    }
                    return false;
                }
            });
            imageView_search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   //adil code
                    imageView_left_menu.setVisibility(View.GONE);
                    imageView_right_menu.setVisibility(View.GONE);
                    imageView_search.setVisibility(View.GONE);
                    imageview_right_foursquare.setVisibility(View.GONE);
                    baseTextview_header_title.setVisibility(View.GONE);
                    et_searchevent.animate().alpha(1.0f);
                    et_searchevent.setVisibility(View.VISIBLE);

                }
            });
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }
    }
    //adil code
    void filter(String text) {
        ArrayList<Eventbean> temp = new ArrayList();
        for (Eventbean d : aceventbeanArrayList_main) {
            //or use .contains(text)
            if (d.getEvent_title().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        acListEventAdapter.updateList(temp);
    }
    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.acevent_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            floatingActionButton_create_events = (FloatingActionButton) view_home.findViewById(R.id.fab_create_new_acevent);
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);

            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);


            acListEventAdapter = new ACListEventAdapter(ACListEventsMainActivity.this, mRecyclerView, communityID);
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
                    Intent intent = new Intent(ACListEventsMainActivity.this, CreateNewEventActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                    bundle.putInt(BundleCommonKeywords.KEY_MODIFICATION_FORMAT, ModificationOptions.ADD.getOperationIndex());
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

            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setStart_limit(current_start);
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ACGETEVENTS.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, ACListEventsMainActivity.this, getApplicationContext(), URLs.EVENTLISTING, jsonObjectGetPostParameterEachScreen, ScreensEnums.ACGETEVENTS.getScrenIndex(), ACListEventsMainActivity.class.getClass());

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

                    ArrayList<Eventbean> acListEventBeen_local = new ArrayList<>();
                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.EventListing)) {
                        JSONArray jsonArray_events = jsonObject_main.getJSONArray(JSONCommonKeywords.EventListing);
                        for (int i = 0; i < jsonArray_events.length(); i++) {

                            JSONObject jsonObject_single_event = jsonArray_events.getJSONObject(i);
                            Eventbean eventbean = EventbeanBuild.getEventbeanFromJSON(jsonObject_single_event);
                            eventbean.setPosition(i);

                            acListEventBeen_local.add(eventbean);
                        }


                        aceventbeanArrayList_main.clear();
                        aceventbeanArrayList_filtered.clear();


                        if (current_start == 0) {
                            aceventbeanArrayList_main = acListEventBeen_local;
                            aceventbeanArrayList_filtered = acListEventBeen_local;

                            acListEventAdapter.notifyDataSetChanged();

                        } else {

                            int start = aceventbeanArrayList_main.size();

                            for (int i = 0; i < acListEventBeen_local.size(); i++) {

                                Eventbean eventbean = acListEventBeen_local.get(i);
                                eventbean.setPosition(start);
                                aceventbeanArrayList_main.add(start, eventbean);
                                // ad
                               // aceventbeanArrayList_filtered.add(start,eventbean);
                                acListEventAdapter.notifyItemInserted(aceventbeanArrayList_main.size());
                              //  acListEventAdapter.notifyItemInserted(aceventbeanArrayList_filtered.size());
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


    @Override
    public void selectedDate(String selectedDate, Date date) {

    }

    @Override
    public void selectedDate(String selectedDate, Date date, int index) {
        if (index == ScreensEnums.START_DATE.getScrenIndex()) {
            if (baseTextview_startDate != null) {
                baseTextview_startDate.setText(selectedDate);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                requestParametersbean.setStartDate(df.format(date));

            }
        } else if (index == ScreensEnums.END_DATE.getScrenIndex()) {
            if (baseTextview_endDate != null) {
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