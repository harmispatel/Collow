package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.SearchResultsAdapter;
import com.app.collow.adapters.SearchResultsAdapterForNearBy;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.CommunityTypebean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.Searchbean;
import com.app.collow.beans.Statesbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.GPSTracker;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Harmis on 25/01/17. This will show search result in two form (1) Listing (2) Map in map if marker will click slider open from bottom,
 */

public class CommunitySearchResultsActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, SetupViewInterface {

    //Right menu data
    public static BaseTextview baseTextview_state_right_menu = null, baseTextview_community_type_right_menu = null;
    public static CommunitySearchResultsActivity communitySearchResultsActivity = null;
    public static ArrayList<Searchbean> searchbeanArrayList_new=new ArrayList<>();
    protected Handler handler;
    View view_home = null;
    SearchResultsAdapterForNearBy searchResultsAdapter = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_search_items = null;
    FrameLayout frameLayout_map_container = null;
    LinearLayout linearLayout_recyleview_container = null;
    FloatingActionButton floatingActionButton_search_result_list_or_map = null;
    boolean islistviewOpened = false;
    RetryParameterbean retryParameterbean = null;
    //if map displayed user will click on marter this view will open after tapping on this redirect to a screen.
    RelativeLayout relativeLayout_bottom_single_info = null;
    GPSTracker gpsTracker = null;
    Searchbean searchbean_post_data = new Searchbean();
    int current_start = 0;
    HashMap<Marker, Searchbean> markerSearchbeanHashMap = new HashMap<>();
    ImageView imageView_communtiy_for_map = null, imageView_defualt_home_icon = null;
    BaseTextview baseTextview_community_name_map = null, baseTextview_community_address_map = null;
    PassParameterbean passParameterbean = null;
    CommonSession commonSession = null;
    RelativeLayout relativeLayout_state_right_menu_new = null, relativeLayout_type_right_menu = null;
    BaseEdittext baseEdittext_city_right_menu = null, baseEdittext_zip_code = null;
    MyButtonView myButtonView_filter_apply = null, myButtonView_clear_filter = null;
    boolean pageTokenAvailable = false;
    String pageToken = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    private BaseTextview baseTextview_error;
    private RecyclerView mRecyclerView;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(CommunitySearchResultsActivity.this, getApplicationContext(), getIntent().getExtras(), CommunitySearchResultsActivity.class.getClass());
            commonSession = new CommonSession(this);
            communitySearchResultsActivity = this;
            handler = new Handler();
            searchbeanArrayList_new.clear();
            pageTokenAvailable = false;
            pageToken = null;
            setupHeaderView();
            findViewByIDs();
            setupEvents();

            findDataFromServer();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    public void setupHeaderView() {
        try {
            //right navigation
            //Right navigation iterms

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
                    Intent intent = new Intent(CommunitySearchResultsActivity.this, SelectStateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.SEARCH_BY_COMMMUNITY_LATLONG.getScrenIndex());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            relativeLayout_type_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CommunitySearchResultsActivity.this, SelectCommunityTypeActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.SEARCH_BY_COMMMUNITY_LATLONG.getScrenIndex());
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

            imageview_search_items = (ImageView) toolbar_header.findViewById(R.id.imageview_search_items);
            imageview_search_items.setVisibility(View.VISIBLE);


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
            imageview_search_items.setOnClickListener(new MyOnClickListener(this) {
                @Override
                public void onClick(View v) {
                    if (CommunitySearchByNameActivity.communitySearchByNameActivity != null) {
                        CommunitySearchByNameActivity.communitySearchByNameActivity.finish();
                    }
                    Intent intent = new Intent(CommunitySearchResultsActivity.this, CommunitySearchByNameActivity.class);
                    startActivity(intent);
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
                    if (baseEdittext_zip_code != null) {

                        baseEdittext_zip_code.setHint(getResources().getString(R.string.zipcode));


                    }
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    searchbeanArrayList_new.clear();
                    searchResultsAdapter = new SearchResultsAdapterForNearBy(CommunitySearchResultsActivity.this, mRecyclerView, false);
                    mRecyclerView.setAdapter(searchResultsAdapter);
                    current_start = 0;
                    //this lines hide map and display listview
                    linearLayout_recyleview_container.setVisibility(View.VISIBLE);
                    frameLayout_map_container.setVisibility(View.GONE);
                    floatingActionButton_search_result_list_or_map.setImageResource(R.drawable.search_map);
                    pageTokenAvailable = false;
                    pageToken = null;
                    islistviewOpened = true;
                    //set defualt current latlong
                    gpsTracker = new GPSTracker(CommunitySearchResultsActivity.this);

                    Location location = gpsTracker.getLocation();
                    if (location != null) {
                        searchbean_post_data.setCurrentlat(String.valueOf(location.getLatitude()));
                        searchbean_post_data.setCurrentlong(String.valueOf(location.getLongitude()));

                    } else {
                        if (GPSTracker.location_from_gps != null) {
                            searchbean_post_data.setCurrentlat(String.valueOf(GPSTracker.location_from_gps.getLatitude()));
                            searchbean_post_data.setCurrentlong(String.valueOf(GPSTracker.location_from_gps.getLongitude()));
                        }
                    }
                    searchbean_post_data.setPageToken("");

                    findDataFromServer();
                }
            });


            myButtonView_filter_apply.setOnClickListener(new MyOnClickListener(CommunitySearchResultsActivity.this) {
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
                                searchbean_post_data.setCommunityTypeID("");
                                searchbean_post_data.setCommunityTypeText("");
                                isTypeEmpty = true;
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

                        /*if (baseEdittext_zip_code != null) {
                            if (TextUtils.isEmpty(baseEdittext_zip_code.getText().toString())) {
                                isZipEmpty = true;

                            } else {
                                requestParametersbean.setZipCode(baseEdittext_zip_code.getText().toString());
                                isZipEmpty = false;

                            }


                        }*/

                        if (isStateEmpty && isTypeEmpty && isCityEmpty) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.please_enter_any_search_criteria), CommunitySearchResultsActivity.this);

                        } else {
                            drawerLayout.closeDrawer(Gravity.RIGHT);
                            searchbeanArrayList_new.clear();
                            searchResultsAdapter = new SearchResultsAdapterForNearBy(CommunitySearchResultsActivity.this, mRecyclerView, false);
                            mRecyclerView.setAdapter(searchResultsAdapter);
                            current_start = 0;
                            if (mMap != null) {
                                mMap.clear();
                            } else {
                            }
                            //this lines hide map and display listview
                            linearLayout_recyleview_container.setVisibility(View.VISIBLE);
                            frameLayout_map_container.setVisibility(View.GONE);
                            floatingActionButton_search_result_list_or_map.setImageResource(R.drawable.search_map);
                            islistviewOpened = true;
                            pageTokenAvailable = false;
                            pageToken = null;
                            searchbean_post_data.setPageToken("");
                            searchbean_post_data.setCurrentlat("");
                            searchbean_post_data.setCurrentlong("");
                            if(relativeLayout_bottom_single_info!=null)
                            {
                                relativeLayout_bottom_single_info.setVisibility(View.GONE);

                            }

                            findDataFromServer();
                        }


                    } else {
                        drawerLayout.closeDrawer(Gravity.RIGHT);
                        CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                    }
                }
            });
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.search_main, null);

            //map fields
            relativeLayout_bottom_single_info = (RelativeLayout) view_home.findViewById(R.id.relativelayout_single_marker_selected);
            relativeLayout_bottom_single_info.setVisibility(View.GONE);

            baseTextview_community_name_map = (BaseTextview) view_home.findViewById(R.id.textview_search_community_name_from_map);
            baseTextview_community_address_map = (BaseTextview) view_home.findViewById(R.id.textview_search_community_address_from_map);
            imageView_communtiy_for_map = (ImageView) view_home.findViewById(R.id.imageview_search_item_from_map);
            imageView_defualt_home_icon = (ImageView) view_home.findViewById(R.id.imageview_home_in_search_result);


            frameLayout_map_container = (FrameLayout) view_home.findViewById(R.id.framelayout_map_container);
            linearLayout_recyleview_container = (LinearLayout) view_home.findViewById(R.id.linerlayout_list_cotainer);
            floatingActionButton_search_result_list_or_map = (FloatingActionButton) view_home.findViewById(R.id.fab_search_listview_or_map);
            islistviewOpened = true;
            linearLayout_recyleview_container.setVisibility(View.VISIBLE);
            frameLayout_map_container.setVisibility(View.GONE);
            floatingActionButton_search_result_list_or_map.setOnClickListener(new MyOnClickListener(CommunitySearchResultsActivity.this) {
                @Override
                public void onClick(View v) {
                    if (islistviewOpened) {

                        if (searchbeanArrayList_new.size() == 0) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.no_search_result_found), CommunitySearchResultsActivity.this);
                        } else {
                            linearLayout_recyleview_container.setVisibility(View.GONE);
                            frameLayout_map_container.setVisibility(View.VISIBLE);
                            floatingActionButton_search_result_list_or_map.setImageResource(R.drawable.search_listview);
                            islistviewOpened = false;
                        }

                    } else {
                        linearLayout_recyleview_container.setVisibility(View.VISIBLE);
                        frameLayout_map_container.setVisibility(View.GONE);
                        floatingActionButton_search_result_list_or_map.setImageResource(R.drawable.search_map);

                        islistviewOpened = true;
                    }
                }
            });


            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            Searchbean searchbean = new Searchbean();

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(20);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);

            searchResultsAdapter = new SearchResultsAdapterForNearBy(CommunitySearchResultsActivity.this, mRecyclerView, false);
            mRecyclerView.setAdapter(searchResultsAdapter);
            frameLayout_container.addView(view_home);

//set defualt current latlong
            gpsTracker = new GPSTracker(CommunitySearchResultsActivity.this);

            Location location = gpsTracker.getLocation();
            if (location != null) {
                searchbean_post_data.setCurrentlat(String.valueOf(location.getLatitude()));
                searchbean_post_data.setCurrentlong(String.valueOf(location.getLongitude()));

            } else {
                if (GPSTracker.location_from_gps != null) {
                    searchbean_post_data.setCurrentlat(String.valueOf(GPSTracker.location_from_gps.getLatitude()));
                    searchbean_post_data.setCurrentlong(String.valueOf(GPSTracker.location_from_gps.getLongitude()));
                }
            }

            searchResultsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            if (pageTokenAvailable) {
                                current_start = +10;

                                //   searchbean_post_data.getStart_limit()+=10;


                                searchbean_post_data.setStart_limit(current_start);


                                findDataFromServer();

                            }


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_search_result, menu);


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            gpsTracker = new GPSTracker(CommunitySearchResultsActivity.this);
            Location location = gpsTracker.getLocation();

            /*// Add a marker in Sydney and move the camera

            if (searchbeanArrayList_new.size() == 0) {
                if (location != null) {
                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());

                    mMap.addMarker(new MarkerOptions().position(current).title(""));
                    mMap.moveCamera(center);
                    mMap.animateCamera(zoom);
                }
            } else {
                mMap.setOnMarkerClickListener(this);
                setupMarkerOnMap(searchbeanArrayList_new);

            }*/


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search_results:
                if (CommunitySearchByNameActivity.communitySearchByNameActivity != null) {
                    CommunitySearchByNameActivity.communitySearchByNameActivity.finish();
                }
                Intent intent = new Intent(CommunitySearchResultsActivity.this, CommunitySearchByNameActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_post_right_icon:
                drawerLayout.openDrawer(Gravity.RIGHT);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        try {
            if (marker != null) {
                if (markerSearchbeanHashMap.containsKey(marker)) {
                    final Searchbean searchbean = markerSearchbeanHashMap.get(marker);


                    if (searchbean.getCommunityName() == null || searchbean.getCommunityName().equals("")) {

                    } else {
                        baseTextview_community_name_map.setText(searchbean.getCommunityName());
                    }


                    StringBuffer stringBuffer = new StringBuffer();

                    if (CommonMethods.isTextAvailable(searchbean.getAddress())) {
                        stringBuffer.append(searchbean.getAddress());
                        stringBuffer.append(" ");

                    }

                    if (CommonMethods.isTextAvailable(searchbean.getCity())) {
                        stringBuffer.append(searchbean.getCity());
                        stringBuffer.append(" ");

                    }

                    if (CommonMethods.isTextAvailable(searchbean.getState())) {
                        stringBuffer.append(searchbean.getState());
                        stringBuffer.append(" ");

                    }

                    if (CommonMethods.isTextAvailable(searchbean.getCountry())) {
                        stringBuffer.append(searchbean.getCountry());
                    }


                    baseTextview_community_address_map.setText(stringBuffer.toString());


                    if (CommonMethods.isImageUrlValid(searchbean.getCommuntiyImageURL())) {
                        Picasso.with(CommunitySearchResultsActivity.this)
                                .load(searchbean.getCommuntiyImageURL())
                                .into(imageView_communtiy_for_map, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        imageView_communtiy_for_map.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });
                    }

                    if (searchbean.getCommunityAccessbean().isHomeDefualtCommunity()) {
                        imageView_defualt_home_icon.setImageResource(R.drawable.homeselected);
                    } else {
                        imageView_defualt_home_icon.setImageResource(R.drawable.homeunselected);

                    }


                    relativeLayout_bottom_single_info.setVisibility(View.VISIBLE);
                    Animation bottomUp = AnimationUtils.loadAnimation(CommunitySearchResultsActivity.this,
                            R.anim.bottom_up);
                    relativeLayout_bottom_single_info.startAnimation(bottomUp);
                    relativeLayout_bottom_single_info.setVisibility(View.VISIBLE);

                    relativeLayout_bottom_single_info.setOnClickListener(new MyOnClickListener(CommunitySearchResultsActivity.this) {
                        @Override
                        public void onClick(View v) {
                            if (isAvailableInternet()) {
                                MyUtils.openCommunityFeedActivity(CommunitySearchResultsActivity.this, ScreensEnums.MRKER_MAP_COMMUNNITY_FEED.getScrenIndex(), searchbean.getCommunityID(), searchbean.getCommunityName(), searchbean.getCommunityAccessbean());
                            } else {
                                showInternetNotfoundMessage();
                            }
                        }
                    });

                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


        return false;
    }

    // this method is used for login user
    public void findDataFromServer() {
        try {
            JSONObject jsonObjectGetPostParameterEachScreen = null;
            PassParameterbean passParameterbean = null;
            searchbean_post_data.setIsCommuntiySearchByName("0");
            searchbean_post_data.setUser_id(commonSession.getLoggedUserID());
            if (pageTokenAvailable) {
                searchbean_post_data.setPageToken(pageToken);
            }




            requestParametersbean.setSearchbeanPassPostData(searchbean_post_data);
            jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex(), requestParametersbean);
            passParameterbean = new PassParameterbean(this, CommunitySearchResultsActivity.this, getApplicationContext(), URLs.SEARCH_BY_COMMUNITYNAME, jsonObjectGetPostParameterEachScreen, ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex(), CommunitySearchResultsActivity.class.getClass());


                /*requestParametersbean.setUserId(commonSession.getLoggedUserID());
                requestParametersbean.setStart_limit(current_start);
                jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.FILTER_COMMUNITY.getScrenIndex(), requestParametersbean);
                passParameterbean = new PassParameterbean(this, CommunitySearchResultsActivity.this, getApplicationContext(), URLs.FILTERCOMMUNITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.FILTER_COMMUNITY.getScrenIndex(), CommunitySearchByNameActivity.class.getClass());
*/


            if (current_start == 0) {

            } else {
                if (passParameterbean != null) {
                    passParameterbean.setNoNeedToDisplayLoadingDialog(true);

                }
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


                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.CommunityList)) {


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

                        ArrayList<Searchbean> searchbeanArrayList_local = new ArrayList<>();
                        mRecyclerView.setVisibility(View.VISIBLE);
                        baseTextview_error.setVisibility(View.GONE);
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

                            if (searchbeanArrayList_new.size() == 0) {
                                gpsTracker = new GPSTracker(CommunitySearchResultsActivity.this);
                                Location location = gpsTracker.getLocation();
                                if (location != null) {
                                    CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                                    CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);
                                    LatLng current = new LatLng(location.getLatitude(), location.getLongitude());

                                    mMap.addMarker(new MarkerOptions().position(current).title(""));
                                    mMap.moveCamera(center);
                                    mMap.animateCamera(zoom);
                                }
                            } else {
                                mMap.setOnMarkerClickListener(this);
                                setupMarkerOnMap(searchbeanArrayList_local);

                            }
                        } else {

                            int start = searchbeanArrayList_new.size();
                            if (searchbeanArrayList_local.size() != 0) {
                                setupMarkerOnMap(searchbeanArrayList_local);
                            }
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


    public void setupMarkerOnMap(List<Searchbean> searchbeanArrayList) {
        mMap.setOnMarkerClickListener(this);

        for (int i = 0; i < searchbeanArrayList.size(); i++) {

            Searchbean searchbean = searchbeanArrayList.get(i);
            double latitude = Double.parseDouble(searchbean.getLatitude());
            double longitude = Double.parseDouble(searchbean.getLongitude());

            LatLng current = new LatLng(latitude, longitude);
            Marker marker = mMap.addMarker(new MarkerOptions().position(current).title(""));

            markerSearchbeanHashMap.put(marker, searchbean);


            if (i == searchbeanArrayList.size() - 1) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(current) // Center Set
                        .zoom(11.0f)                // Zoom
                        .bearing(90)                // Orientation of the camera to east
                        .tilt(30)                   // Tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        }


    }


}