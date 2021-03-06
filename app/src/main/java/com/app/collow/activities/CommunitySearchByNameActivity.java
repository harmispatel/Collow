package com.app.collow.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.adapters.SearchHistoryAdapter;
import com.app.collow.adapters.SearchResultsAdapter;
import com.app.collow.allenums.CommunityInformationScreenEnum;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.CommunityTypebean;
import com.app.collow.beans.Locationbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.Searchbean;
import com.app.collow.beans.Statesbean;
import com.app.collow.collowinterfaces.GetLocationListener;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.database.CollowDatabaseHandler;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import smoothprogressbar.SmoothProgressBar;

/**
 * Created by Harmis on 27/01/17.In this user can enter any text to search community by  text.
 * then a progress bar can see and auto complete result will comes based on text searched.
 */

public class CommunitySearchByNameActivity extends BaseActivity implements SetupViewInterface, GetLocationListener {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static CommunitySearchByNameActivity communitySearchByNameActivity = null;
    public static SearchResultsAdapter searchResultsAdapter = null;
    public static ArrayList<Searchbean> searchbeanArrayList_new=new ArrayList<>();
    //Right menu data
    public static BaseTextview baseTextview_state_right_menu = null, baseTextview_community_type_right_menu = null;
    protected Handler handler;
    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_search_items = null;
    RetryParameterbean retryParameterbean = null;
    Searchbean searchbean_post_data = new Searchbean();
    int current_start = 0;
    CommonSession commonSession = null;
    int index_from_which_screen = 0;
    RelativeLayout relativeLayout_state_right_menu_new = null, relativeLayout_type_right_menu = null;
    BaseEdittext baseEdittext_city_right_menu = null, baseEdittext_zip_code = null;
    MyButtonView myButtonView_filter_apply = null, myButtonView_clear_filter = null;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    BaseTextview baseTextview_searched_item = null;
    LinearLayout linearLayout_searched_text = null;
    CollowDatabaseHandler collowDatabaseHandler = null;
    boolean pageTokenAvailable = false;
    String pageToken = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    //if map displayed user will click on marter this view will open after tapping on this redirect to a screen.
    private BaseTextview baseTextview_error;
    private RecyclerView mRecyclerView;
    private SmoothProgressBar smoothProgressBar;
    private Interpolator mCurrentInterpolator;
    private int mStrokeWidth = 4;
    private int mSeparatorLength = 1;
    private int mSectionsCount = 1;
    private float mFactor = 1f;
    private float mSpeed = 1f;
    public static boolean shouldDisplaythisActivity=false;

   public static SetupViewInterface setupViewInterface_for_place=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(CommunitySearchByNameActivity.this, getApplicationContext(), getIntent().getExtras(), CommunitySearchByNameActivity.class.getClass());

            commonSession = new CommonSession(CommunitySearchByNameActivity.this);
            // getCityAndState = new GetCityAndState(CommunitySearchByNameActivity.this, getApplicationContext(), this, retryParameterbean, this);
            communitySearchByNameActivity = this;
            searchbeanArrayList_new.clear();
            handler = new Handler();
            searchbean_post_data.setStart_limit(0);
            collowDatabaseHandler = new CollowDatabaseHandler(CommunitySearchByNameActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                index_from_which_screen = bundle.getInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX);
            }

            pageTokenAvailable = false;
            pageToken = null;
            setupHeaderView();
            findViewByIDs();
            setupEvents();

            if (index_from_which_screen == CommunityInformationScreenEnum.FROM_COMMUNITY_SEARCH_HISTORY.getIndexFromWhereCalledCommunityInformation()) {
                String searchedCommunity = getIntent().getExtras().getString(BundleCommonKeywords.KEY_COMMUNITY_SEARCH_HISTORY_ITEM_NAME);
                smoothProgressBar.setVisibility(View.VISIBLE);
                searchbeanArrayList_new.clear();
                current_start = 0;
                searchbean_post_data.setStart_limit(0);
                searchbean_post_data.setSearchText(searchedCommunity);
                if (searchResultsAdapter != null) {
                    searchResultsAdapter.notifyDataSetChanged();
                }

                linearLayout_searched_text.setVisibility(View.VISIBLE);
                baseTextview_searched_item.setVisibility(View.VISIBLE);
                baseTextview_searched_item.setText(searchedCommunity);

                findDataFromServer();
            } else {
                openGooglePlaceIntent(null);

            }


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    public void setupHeaderView() {
        try {
            //right navigation
            //Right navigation iterms
            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.search));
            baseEdittext_city_right_menu = (BaseEdittext) findViewById(R.id.edittext_filter_city);
            baseTextview_state_right_menu = (BaseTextview) findViewById(R.id.textview_filter_state_right_menu);
            baseTextview_community_type_right_menu = (BaseTextview) findViewById(R.id.textview_community_type_right_menu);
            baseEdittext_zip_code = (BaseEdittext) findViewById(R.id.edittext_filter_zip);
            baseEdittext_zip_code.setVisibility(View.GONE);
            relativeLayout_state_right_menu_new = (RelativeLayout) findViewById(R.id.relativelayout_select_filter_state_right_menu_new);
            relativeLayout_type_right_menu = (RelativeLayout) findViewById(R.id.relativelayout_select_community_type_right_menu);


            baseTextview_state_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CommunitySearchByNameActivity.this, SelectStateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            relativeLayout_type_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CommunitySearchByNameActivity.this, SelectCommunityTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

            myButtonView_filter_apply = (MyButtonView) findViewById(R.id.mybuttonview_filter_apply);
            myButtonView_clear_filter = (MyButtonView) findViewById(R.id.mybuttonview_filter_clear);


            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.results));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            imageview_search_items = (ImageView) toolbar_header.findViewById(R.id.imageview_search_items);
            imageview_search_items.setVisibility(View.VISIBLE);


            imageview_search_items.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openGooglePlaceIntent(null);


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

            myButtonView_clear_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (baseTextview_state_right_menu != null) {
                        Statesbean statesbean = (Statesbean) baseTextview_state_right_menu.getTag();
                        if (statesbean != null) {
                            commonSession.resetStateId();
                            commonSession.resetStateName();
                            searchbean_post_data.setStateID("");
                            searchbean_post_data.setStateText("");
                        }
                        baseTextview_state_right_menu.setText(getResources().getString(R.string.state));
                    }

                    if (baseTextview_community_type_right_menu != null) {
                        CommunityTypebean communityTypebean = (CommunityTypebean) baseTextview_community_type_right_menu.getTag();
                        if (communityTypebean != null) {
                            commonSession.resetCommunityTypeId();
                            commonSession.resetCommunityTypeName();
                            searchbean_post_data.setCommunityTypeID("");
                            searchbean_post_data.setCommunityTypeText("");
                        }
                        baseTextview_community_type_right_menu.setText(getResources().getString(R.string.communtity_type));

                    }


                    if (baseEdittext_city_right_menu != null) {
                        if (TextUtils.isEmpty(baseEdittext_city_right_menu.getText().toString())) {

                        } else {
                            commonSession.resetCityName();
                            searchbean_post_data.setCityText("");

                        }
                        baseEdittext_city_right_menu.setHint(getResources().getString(R.string.enter_city));


                    }


                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    searchbeanArrayList_new.clear();
                    searchResultsAdapter = new SearchResultsAdapter(CommunitySearchByNameActivity.this, mRecyclerView, false);
                    mRecyclerView.setAdapter(searchResultsAdapter);
                    current_start = 0;
                    pageTokenAvailable = false;
                    pageToken = null;
                    searchbean_post_data.setPageToken("");

                    findDataFromServer();
                }
            });


            myButtonView_filter_apply.setOnClickListener(new MyOnClickListener(CommunitySearchByNameActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {


                        boolean isCityEmpty = false, isStateEmpty = false, isTypeEmpty = false;


                        if (baseTextview_state_right_menu != null) {

                            if (baseTextview_state_right_menu.getText().toString().equals(getResources().getString(R.string.state))) {
                                isStateEmpty = true;
                                searchbean_post_data.setStateID("");
                                searchbean_post_data.setStateText("");

                            } else {
                                isStateEmpty = false;

                                Statesbean statesbean = (Statesbean) baseTextview_state_right_menu.getTag();
                                if (statesbean != null) {
                                    commonSession.storeStateId(statesbean.getStateCode());
                                    commonSession.storeStateName(statesbean.getStateName());

                                    searchbean_post_data.setStateID(statesbean.getStateCode());
                                    searchbean_post_data.setStateText(statesbean.getStateName());
                                }
                            }


                        }

                        if (baseTextview_community_type_right_menu != null) {

                            if (baseTextview_community_type_right_menu.getText().toString().equals(getResources().getString(R.string.communtity_type))) {
                                isTypeEmpty = true;
                                searchbean_post_data.setCommunityTypeID("");
                                searchbean_post_data.setCommunityTypeText("");
                            } else {
                                isTypeEmpty = false;

                                CommunityTypebean communityTypebean = (CommunityTypebean) baseTextview_community_type_right_menu.getTag();
                                if (communityTypebean != null) {
                                    commonSession.storeCommunityTypeId(communityTypebean.getTypeID());
                                    commonSession.storeCommunityTypeName(communityTypebean.getTypeName());

                                    searchbean_post_data.setCommunityTypeID(communityTypebean.getTypeID());
                                    searchbean_post_data.setCommunityTypeText(communityTypebean.getTypeName());
                                }
                            }


                        }


                        if (baseEdittext_city_right_menu != null) {
                            if (TextUtils.isEmpty(baseEdittext_city_right_menu.getText().toString())) {
                                isCityEmpty = true;
                                searchbean_post_data.setCityText("");

                            } else {
                                commonSession.storeCityName(baseEdittext_city_right_menu.getText().toString());
                                searchbean_post_data.setCityText(baseEdittext_city_right_menu.getText().toString());
                                isCityEmpty = false;

                            }


                        }

                      /*  if (baseEdittext_zip_code != null) {
                            if (TextUtils.isEmpty(baseEdittext_zip_code.getText().toString())) {
                                isZipEmpty = true;

                            } else {
                                requestParametersbean.setZipCode(baseEdittext_zip_code.getText().toString());
                                isZipEmpty = false;

                            }


                        }*/


                        if (isStateEmpty && isTypeEmpty && isCityEmpty) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.please_enter_any_search_criteria), CommunitySearchByNameActivity.this);

                        } else {
                            drawerLayout.closeDrawer(Gravity.RIGHT);
                            searchbeanArrayList_new.clear();
                            searchResultsAdapter = new SearchResultsAdapter(CommunitySearchByNameActivity.this, mRecyclerView, false);
                            mRecyclerView.setAdapter(searchResultsAdapter);
                            current_start = 0;
                            pageTokenAvailable = false;
                            pageToken = null;
                            searchbean_post_data.setPageToken("");
                            findDataFromServer();
                        }


                    } else {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                    }
                }
            });

            setSupportActionBar(toolbar_header);

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.search_by_communtiy_name_main, null);
            baseTextview_searched_item = (BaseTextview) view_home.findViewById(R.id.textview_searched_item_display);
            linearLayout_searched_text = (LinearLayout) view_home.findViewById(R.id.layout_searched_text);
            smoothProgressBar = (SmoothProgressBar) view_home.findViewById(R.id.progressbar);
            smoothProgressBar.setVisibility(View.GONE);

            setInterpolator();


            // Obtain the SupportMapFragment and get notified when the map is ready to be used.

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);

            searchResultsAdapter = new SearchResultsAdapter(CommunitySearchByNameActivity.this, mRecyclerView, true);
            mRecyclerView.setAdapter(searchResultsAdapter);
            frameLayout_container.addView(view_home);


            searchResultsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom
                    if (searchbeanArrayList_new.size() != 0) {


                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //   remove progress item


                                //add items one by one

                                if (pageTokenAvailable) {
                                    smoothProgressBar.setVisibility(View.VISIBLE);

                                    current_start += 10;

                                    //   searchbean_post_data.getStart_limit()+=10;

                                    searchbean_post_data.setStart_limit(current_start);


                                    findDataFromServer();


                                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();
                                }


                            }
                        }, 2000);

                    }

                }
            });

            setupViewInterface_for_place=new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    updateData(responcebean.getSearchedText());
                }
            };
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    private void setupEvents() {

    }

    private void setInterpolator() {


        mCurrentInterpolator = new AccelerateDecelerateInterpolator();

        smoothProgressBar.setSmoothProgressDrawableInterpolator(mCurrentInterpolator);
        smoothProgressBar.setSmoothProgressDrawableColors(getResources().getIntArray(R.array.gplus_colors));


        smoothProgressBar.setSmoothProgressDrawableSpeed(mSpeed);
        smoothProgressBar.setSmoothProgressDrawableProgressiveStartSpeed(mSpeed);
        smoothProgressBar.setSmoothProgressDrawableProgressiveStopSpeed(mSpeed);
        smoothProgressBar.setSmoothProgressDrawableStrokeWidth(dpToPx(mStrokeWidth));
        smoothProgressBar.setSmoothProgressDrawableSeparatorLength(dpToPx(mSeparatorLength));
        smoothProgressBar.setSmoothProgressDrawableSectionsCount(mSectionsCount);


    }


    public int dpToPx(int dp) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
        return px;
    }


    public void openGooglePlaceIntent(String searchKeyword) {


        if (searchKeyword == null) {



                Intent intent=new Intent(CommunitySearchByNameActivity.this,SearchPlaceActivity.class);
                startActivity(intent);


               /* Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                .build(CommunitySearchByNameActivity.this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
*/



        } else {

                linearLayout_searched_text.setVisibility(View.VISIBLE);
                baseTextview_searched_item.setVisibility(View.VISIBLE);
                baseTextview_searched_item.setText(getResources().getString(R.string.searched_item) + " " + searchKeyword);
                /*Intent intent =
                        new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).zzeZ(searchKeyword)
                                .build(CommunitySearchByNameActivity.this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);*/

        }
    }


    @Override
    public void setupUI(Responcebean responcebean) {

        try {
            smoothProgressBar.setVisibility(View.GONE);


            if (responcebean.getScreenIndex() == ScreensEnums.GET_LATLONG_BY_GOOGLE.getScrenIndex()) {
                if (responcebean.isServiceSuccess()) {
                    //   final Locationbean locationbean = getCityAndState.getAddress(responcebean.getResponceContent());


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///  handleLocationDetails(locationbean);

                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //   baseTextview_city_country_name_current_location.setText(getResources().getString(R.string.no_found_location));

                        }
                    });

                }
            } else {
                if (responcebean.isServiceSuccess()) {

                    JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                    if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                        //parse here data of following


                        if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.CommunityList)) {

                            ArrayList<Searchbean> searchbeanArrayList_local = new ArrayList<>();
                            mRecyclerView.setVisibility(View.VISIBLE);
                            baseTextview_error.setVisibility(View.GONE);

                            if (jsonObject_main.has(JSONCommonKeywords.PageToken)) {

                                if (CommonMethods.isTextAvailable(jsonObject_main.getString(JSONCommonKeywords.PageToken))) {
                                    pageTokenAvailable = true;
                                    pageToken = jsonObject_main.getString(JSONCommonKeywords.PageToken);
                                } else {
                                    pageTokenAvailable = false;

                                }
                            } else {
                                pageTokenAvailable = false;

                            }


                            JSONArray jsonArray_communities = jsonObject_main.getJSONArray(JSONCommonKeywords.CommunityList);
                            for (int i = 0; i < jsonArray_communities.length(); i++) {

                                JSONObject jsonObject_single_community = jsonArray_communities.getJSONObject(i);
                                Searchbean searchbean = new Searchbean();

                                if (jsonObject_single_community.has(JSONCommonKeywords.CommunityID)) {
                                    searchbean.setCommunityID(jsonObject_single_community.getString(JSONCommonKeywords.CommunityID));
                                }


                                if (jsonObject_single_community.has(JSONCommonKeywords.CommuntiyImageURL)) {
                                    searchbean.setCommuntiyImageURL(jsonObject_single_community.getString(JSONCommonKeywords.CommuntiyImageURL));
                                }
                                if (jsonObject_single_community.has(JSONCommonKeywords.CommunityName)) {
                                    searchbean.setCommunityName(jsonObject_single_community.getString(JSONCommonKeywords.CommunityName));
                                }
                                if (jsonObject_single_community.has(JSONCommonKeywords.Latitude)) {
                                    searchbean.setLatitude(jsonObject_single_community.getString(JSONCommonKeywords.Latitude));
                                }
                                if (jsonObject_single_community.has(JSONCommonKeywords.Longitude)) {
                                    searchbean.setLongitude(jsonObject_single_community.getString(JSONCommonKeywords.Longitude));
                                }
                                if (jsonObject_single_community.has(JSONCommonKeywords.Address)) {
                                    searchbean.setAddress(jsonObject_single_community.getString(JSONCommonKeywords.Address));
                                }
                                if (jsonObject_single_community.has(JSONCommonKeywords.City)) {
                                    searchbean.setCity(jsonObject_single_community.getString(JSONCommonKeywords.City));
                                }
                                if (jsonObject_single_community.has(JSONCommonKeywords.State)) {
                                    searchbean.setState(jsonObject_single_community.getString(JSONCommonKeywords.State));
                                }
                                if (jsonObject_single_community.has(JSONCommonKeywords.Country)) {
                                    searchbean.setCountry(jsonObject_single_community.getString(JSONCommonKeywords.Country));
                                }
                                if (jsonObject_single_community.has(JSONCommonKeywords.CommunityType)) {
                                    searchbean.setCommunityTypeText(jsonObject_single_community.getString(JSONCommonKeywords.CommunityType));
                                }
                                if (jsonObject_single_community.has(JSONCommonKeywords.placeid)) {
                                    searchbean.setPlaceid(jsonObject_single_community.getString(JSONCommonKeywords.placeid));
                                }


                                //handle community access
                                searchbean.setCommunityAccessbean(CommonMethods.getCommunityAccessFromJSON(jsonObject_single_community));


                                searchbeanArrayList_local.add(searchbean);


                            }


                            if (current_start == 0) {
                                searchbeanArrayList_new = searchbeanArrayList_local;
                                searchResultsAdapter.notifyDataSetChanged();

                            } else {

                                int start = searchbeanArrayList_new.size();

                                for (int i = 0; i < searchbeanArrayList_local.size(); i++) {

                                    searchbeanArrayList_new.add(start, searchbeanArrayList_local.get(i));
                                    searchResultsAdapter.notifyItemInserted(searchbeanArrayList_new.size());
                                    start++;

                                }
                                searchResultsAdapter.setLoaded();

                            }


                            // searchbeanArrayList_all.add(new Searchbean());
                            //  searchResultsAdapter.notifyItemInserted(searchbeanArrayList_all.size());


                        } else {
                            handlerError(responcebean);

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
                if (searchbeanArrayList_new.size() == 0) {
                    baseTextview_error.setText(getResources().getString(R.string.no_search_result_found));
                    mRecyclerView.setVisibility(View.GONE);
                    baseTextview_error.setVisibility(View.VISIBLE);

                } else {
                    baseTextview_error.setText(getResources().getString(R.string.no_data_founds));
                    baseTextview_error.setVisibility(View.VISIBLE);

                }

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


    // this method is used for login user
    public void findDataFromServer() {
        try {
            JSONObject jsonObjectGetPostParameterEachScreen = null;
            PassParameterbean passParameterbean = null;
            if (pageTokenAvailable) {
                searchbean_post_data.setPageToken(pageToken);
            }

            if (TextUtils.isEmpty(baseTextview_searched_item.getText().toString())) {

            } else {
               String name = URLEncoder.encode(baseTextview_searched_item.getText().toString(), "utf-8");
                searchbean_post_data.setSearchText(name);
            }


            searchbean_post_data.setIsCommuntiySearchByName("1");
            searchbean_post_data.setUser_id(commonSession.getLoggedUserID());
            requestParametersbean.setSearchbeanPassPostData(searchbean_post_data);

            jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex(), requestParametersbean);
            passParameterbean = new PassParameterbean(this, CommunitySearchByNameActivity.this, getApplicationContext(), URLs.SEARCH_BY_COMMUNITYNAME, jsonObjectGetPostParameterEachScreen, ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex(), CommunitySearchByNameActivity.class.getClass());


              /*  requestParametersbean.setUserId(commonSession.getLoggedUserID());
                requestParametersbean.setStart_limit(current_start);

                jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.FILTER_COMMUNITY.getScrenIndex(), requestParametersbean);
                passParameterbean = new PassParameterbean(this, CommunitySearchByNameActivity.this, getApplicationContext(), URLs.FILTERCOMMUNITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.FILTER_COMMUNITY.getScrenIndex(), CommunitySearchByNameActivity.class.getClass());
*/


            passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean checkAndRequestPermissions() {

        int access_fine_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);


        List<String> listPermissionsNeeded = new ArrayList<>();


        if (access_fine_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (
                            perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                            ) {
                        // process the normal flow
                        //else any one or both the permissions are not granted


                        try {
                            //   Locationbean locationbean = getCityAndState.callAndGetIfAllPermissionAvailabe();
                            //   handleLocationDetails(locationbean);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)


                                ) {
                            showDialogOK("Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            //   getCityAndState.callAndGetIfAllPermissionAvailabe();

            if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);

                    smoothProgressBar.setVisibility(View.VISIBLE);


                    StringBuffer stringBuffer = new StringBuffer();

                    if (place != null) {
                        if (place.getName() != null) {
                            stringBuffer.append(place.getName());
                        }
                        stringBuffer.append(" ");

                        if (place.getAddress() != null) {
                            stringBuffer.append(place.getAddress());
                        }
                        searchbeanArrayList_new.clear();
                        current_start = 0;
                        searchbean_post_data.setStart_limit(0);
                        searchbean_post_data.setSearchText(stringBuffer.toString());
                        if (searchResultsAdapter != null) {
                            searchResultsAdapter.notifyDataSetChanged();
                        }

                        linearLayout_searched_text.setVisibility(View.VISIBLE);
                        baseTextview_searched_item.setVisibility(View.VISIBLE);
                        baseTextview_searched_item.setText(stringBuffer.toString());


                        if (collowDatabaseHandler.isThisCommunityExist(stringBuffer.toString(), commonSession.getLoggedUserID())) {

                        } else {
                            collowDatabaseHandler.saveSearchedHistory(stringBuffer.toString(), commonSession.getLoggedUserID());

                        }
                        if (CommunitySearchFilterOptionsActivity.recyclerView_recent_searches != null) {
                            List<Searchbean> searchbeanList_search_history = collowDatabaseHandler.getSearchedCommunities(commonSession.getLoggedUserID());
                            CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.removeAllViews();

                            SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(CommunitySearchByNameActivity.this, searchbeanList_search_history);
                            CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.setAdapter(searchHistoryAdapter);
                            if (CommunitySearchFilterOptionsActivity.recyclerView_recent_searches != null) {
                                CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.setVisibility(View.VISIBLE);
                            }
                            if (CommunitySearchFilterOptionsActivity.baseTextview_empty_recent_searches != null) {
                                CommunitySearchFilterOptionsActivity.baseTextview_empty_recent_searches.setVisibility(View.GONE);
                            }

                        }
                        pageTokenAvailable = false;
                        pageToken = null;
                        searchbean_post_data.setPageToken("");
                        findDataFromServer();

                    }


                } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                    Status status = PlaceAutocomplete.getStatus(this, data);
                    // TODO: Handle the error.
                    if (status != null) {
                        CommonMethods.customToastMessage(status.getStatusMessage(), CommunitySearchByNameActivity.this);

                    }
                    linearLayout_searched_text.setVisibility(View.GONE);
                    finish();

                } else if (resultCode == RESULT_CANCELED) {
                    // The user canceled the operation.
                    linearLayout_searched_text.setVisibility(View.GONE);
                    finish();


                }
            } else {
                //getCityAndState.callAndGetIfAllPermissionAvailabe();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void updateData(String keytext)
    {
        searchbeanArrayList_new.clear();
        current_start = 0;
        searchbean_post_data.setStart_limit(0);
        searchbean_post_data.setSearchText(keytext);
        if (searchResultsAdapter != null) {
            searchResultsAdapter.notifyDataSetChanged();
        }

        linearLayout_searched_text.setVisibility(View.VISIBLE);
        baseTextview_searched_item.setVisibility(View.VISIBLE);
        baseTextview_searched_item.setText(keytext);


        if (collowDatabaseHandler.isThisCommunityExist(keytext, commonSession.getLoggedUserID())) {

        } else {
            collowDatabaseHandler.saveSearchedHistory(keytext, commonSession.getLoggedUserID());

        }
        if (CommunitySearchFilterOptionsActivity.recyclerView_recent_searches != null) {
            List<Searchbean> searchbeanList_search_history = collowDatabaseHandler.getSearchedCommunities(commonSession.getLoggedUserID());
            CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.removeAllViews();
            ;
            SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(CommunitySearchByNameActivity.this, searchbeanList_search_history);
            CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.setAdapter(searchHistoryAdapter);
            if (CommunitySearchFilterOptionsActivity.recyclerView_recent_searches != null) {
                CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.setVisibility(View.VISIBLE);
            }
            if (CommunitySearchFilterOptionsActivity.baseTextview_empty_recent_searches != null) {
                CommunitySearchFilterOptionsActivity.baseTextview_empty_recent_searches.setVisibility(View.GONE);
            }

        }
        pageTokenAvailable = false;
        pageToken = null;
        searchbean_post_data.setPageToken("");
        smoothProgressBar.setVisibility(View.VISIBLE);
        findDataFromServer();
    }

    public void handleLocationDetails(Locationbean locationbean) {

        if (locationbean != null) {
            if (locationbean.getCity() != null && !locationbean.getCity().equals("")) {
                baseEdittext_city_right_menu.setText(locationbean.getCity());


            }

            if (locationbean.getState() != null && !locationbean.getState().equals("")) {
                baseTextview_state_right_menu.setText(locationbean.getState());
            }

        } else {

        }


    }

    @Override
    public void updateLocation(final Locationbean locationbean) {
        if (locationbean != null)

        {
            if (locationbean.isServiceSuccess()) {
                runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        handleLocationDetails(locationbean);

                    }
                });

            }
        }
    }
}