package com.app.collow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.FollowingAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.CommunityTypebean;
import com.app.collow.beans.Followingbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
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
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by harmis on 12/1/17.
 */

public class FollowingActivity extends BaseActivity implements SetupViewInterface {

    public static ArrayList<Followingbean> followingbeanArrayList = new ArrayList<>();
    public static FollowingActivity followingActivity=null;
    protected Handler handler;
    View view_home = null;
    public static FollowingAdapter followingAdapter = null;
    CommonSession commonSession = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start = 0;
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;


    //Right menu data
    public  static BaseTextview baseTextview_state_right_menu = null, baseTextview_community_type_right_menu = null;
    RelativeLayout  relativeLayout_state_right_menu_new=null,relativeLayout_type_right_menu=null;
    BaseEdittext baseEdittext_city_right_menu = null;
    MyButtonView myButtonView_filter_apply = null,myButtonView_clear_filter=null;
    BaseTextview baseTextview_header_title = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retryParameterbean = new RetryParameterbean(FollowingActivity.this, getApplicationContext(), getIntent().getExtras(), FollowingActivity.class.getClass());
        commonSession = new CommonSession(FollowingActivity.this);
        //while activity open clear old data from this.
        followingbeanArrayList.clear();
        followingActivity=this;
        handler = new Handler();
        setupHeaderView();
        findViewByIDs();
        setupEvents();
        getFollowingListing();

    }

    public void setupHeaderView() {
        //right navigation
        //Right navigation iterms

        baseEdittext_city_right_menu = (BaseEdittext) findViewById(R.id.edittext_filter_city);
        baseTextview_state_right_menu = (BaseTextview) findViewById(R.id.textview_filter_state_right_menu);
        baseTextview_community_type_right_menu = (BaseTextview) findViewById(R.id.textview_community_type_right_menu);

        relativeLayout_state_right_menu_new= (RelativeLayout) findViewById(R.id.relativelayout_select_filter_state_right_menu_new);
        relativeLayout_type_right_menu= (RelativeLayout) findViewById(R.id.relativelayout_select_community_type_right_menu);


        baseTextview_state_right_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FollowingActivity.this,SelectStateActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        relativeLayout_type_right_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(FollowingActivity.this,SelectCommunityTypeActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        myButtonView_filter_apply = (MyButtonView) findViewById(R.id.mybuttonview_filter_apply);
        myButtonView_clear_filter= (MyButtonView) findViewById(R.id.mybuttonview_filter_clear);





        baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
        baseTextview_header_title.setText(getResources().getString(R.string.following));

        imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
        imageView_left_menu.setVisibility(View.VISIBLE);
        imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
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



        myButtonView_clear_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(baseTextview_state_right_menu!=null)
                {
                    Statesbean statesbean= (Statesbean) baseTextview_state_right_menu.getTag();
                    if(statesbean!=null)
                    {
                        commonSession.resetStateId();
                        commonSession.resetStateName();
                        requestParametersbean.setState("");
                    }
                    baseTextview_state_right_menu.setText(getResources().getString(R.string.state));
                }

                if(baseTextview_community_type_right_menu!=null)
                {
                    CommunityTypebean communityTypebean= (CommunityTypebean) baseTextview_community_type_right_menu.getTag();
                    if(communityTypebean!=null)
                    {
                        commonSession.resetCommunityTypeId();
                        commonSession.resetCommunityTypeName();
                        requestParametersbean.setType("");
                    }
                    baseTextview_community_type_right_menu.setText(getResources().getString(R.string.communtity_type));

                }


                if(baseEdittext_city_right_menu!=null)
                {
                    if(TextUtils.isEmpty(baseEdittext_city_right_menu.getText().toString()))
                    {

                    }
                    else
                    {
                        commonSession.resetCityName();
                        requestParametersbean.setCity("");

                    }
                    baseEdittext_city_right_menu.setHint(getResources().getString(R.string.enter_city));


                }

                drawerLayout.closeDrawer(Gravity.RIGHT);
                followingbeanArrayList.clear();
                followingAdapter = new FollowingAdapter(mRecyclerView,FollowingActivity.this);
                mRecyclerView.setAdapter(followingAdapter);
                current_start=0;
                getFollowingListing();
            }
        });



        myButtonView_filter_apply.setOnClickListener(new MyOnClickListener(FollowingActivity.this) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {





                            boolean isCityEmpty=false,isStateEmpty=false,isTypeEmpty=false;




                            if(baseTextview_state_right_menu!=null)
                            {

                                if(baseTextview_state_right_menu.getText().toString().equals(getResources().getString(R.string.state)))
                                {
                                    isStateEmpty=true;
                                }
                                else
                                {
                                    isStateEmpty=false;

                                    Statesbean statesbean= (Statesbean) baseTextview_state_right_menu.getTag();
                                    if(statesbean!=null)
                                    {
                                        commonSession.storeStateId(statesbean.getStateCode());
                                        commonSession.storeStateName(statesbean.getStateName());
                                        requestParametersbean.setState(statesbean.getStateCode());
                                    }
                                }



                            }

                            if(baseTextview_community_type_right_menu!=null)
                            {

                                if(baseTextview_community_type_right_menu.getText().toString().equals(getResources().getString(R.string.communtity_type)))
                                {
                                    isTypeEmpty=true;
                                }
                                else
                                {
                                    isTypeEmpty=false;

                                    CommunityTypebean communityTypebean= (CommunityTypebean) baseTextview_community_type_right_menu.getTag();
                                    if(communityTypebean!=null)
                                    {
                                        commonSession.storeCommunityTypeId(communityTypebean.getTypeID());
                                        commonSession.storeCommunityTypeName(communityTypebean.getTypeName());
                                        requestParametersbean.setType(communityTypebean.getTypeID());
                                    }
                                }




                            }


                            if(baseEdittext_city_right_menu!=null)
                            {
                                if(TextUtils.isEmpty(baseEdittext_city_right_menu.getText().toString()))
                                {
                                    isCityEmpty=true;

                                }
                                else
                                {
                                    commonSession.storeCityName(baseEdittext_city_right_menu.getText().toString());
                                    requestParametersbean.setCity(baseEdittext_city_right_menu.getText().toString());
                                    isCityEmpty=false;

                                }





                            }

                            if(isStateEmpty&&isTypeEmpty&&isCityEmpty)
                            {
                                CommonMethods.customToastMessage(getResources().getString(R.string.please_enter_any_search_criteria),FollowingActivity.this);

                            }
                            else
                            {
                                drawerLayout.closeDrawer(Gravity.RIGHT);
                                followingbeanArrayList.clear();
                                followingAdapter = new FollowingAdapter(mRecyclerView,FollowingActivity.this);
                                mRecyclerView.setAdapter(followingAdapter);
                                current_start=0;
                                getFollowingListing();
                            }



                        }










                else
                {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                }
            }
        });


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

        followingAdapter = new FollowingAdapter(mRecyclerView,FollowingActivity.this);
        mRecyclerView.setAdapter(followingAdapter);
        frameLayout_container.addView(view_home);



        followingAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {


                        current_start += 10;

                        //   searchbean_post_data.getStart_limit()+=10;

                        requestParametersbean.setStart_limit(current_start);
                        getFollowingListing();


                    }
                }, 2000);

            }
        });

    }

    private void setupEvents() {

    }


    // this method is used for following listing
    public void getFollowingListing() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            JSONObject jsonObjectGetPostParameterEachScreen =null;

            PassParameterbean passParameterbean=null;


                 jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex(), requestParametersbean);
                 passParameterbean = new PassParameterbean(this, FollowingActivity.this, getApplicationContext(), URLs.MY_FOLLOWINGS_LISTING, jsonObjectGetPostParameterEachScreen, ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex(), FollowingActivity.class.getClass());



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


                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.CommunityList)) {

                        JSONArray jsonArray = jsonObject_main.getJSONArray(JSONCommonKeywords.CommunityList);
                        ArrayList<Followingbean> followingbeanArrayList_local = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject_single_following = jsonArray.getJSONObject(i);
                            Followingbean followingbean = new Followingbean();

                            if (jsonObject_single_following.has(JSONCommonKeywords.CommunityID)) {
                                followingbean.setCommunityID(jsonObject_single_following.getString(JSONCommonKeywords.CommunityID));
                            }

                            if (jsonObject_single_following.has(JSONCommonKeywords.CommunityName)) {
                                followingbean.setCommunityName(jsonObject_single_following.getString(JSONCommonKeywords.CommunityName));
                            }
                            if (jsonObject_single_following.has(JSONCommonKeywords.IsHomeCommunity)) {
                                followingbean.setIsHomeCommunity(jsonObject_single_following.getString(JSONCommonKeywords.IsHomeCommunity));
                            }
                            if (jsonObject_single_following.has(JSONCommonKeywords.isFollowedCommunity)) {
                                followingbean.setIsFollowedCommunity(jsonObject_single_following.getString(JSONCommonKeywords.isFollowedCommunity));
                            }

                            if (jsonObject_single_following.has(JSONCommonKeywords.CommuntiyImageURL)) {
                                followingbean.setCommuntiyImageURL(jsonObject_single_following.getString(JSONCommonKeywords.CommuntiyImageURL));
                            }

                            if (jsonObject_single_following.has(JSONCommonKeywords.Latitude)) {
                                followingbean.setLatitude(jsonObject_single_following.getString(JSONCommonKeywords.Latitude));
                            }
                            if (jsonObject_single_following.has(JSONCommonKeywords.Longitude)) {
                                followingbean.setLongitude(jsonObject_single_following.getString(JSONCommonKeywords.Longitude));
                            }
                            if (jsonObject_single_following.has(JSONCommonKeywords.Longitude)) {
                                followingbean.setLongitude(jsonObject_single_following.getString(JSONCommonKeywords.Longitude));
                            } if (jsonObject_single_following.has(JSONCommonKeywords.Longitude)) {
                                followingbean.setLongitude(jsonObject_single_following.getString(JSONCommonKeywords.Longitude));
                            }



                            followingbean.setCommunityAccessbean(CommonMethods.getCommunityAccessFromJSON(jsonObject_single_following));




                            StringBuffer stringBuffer_bind_address = new StringBuffer();
                            String address = "", city = "", state = "", country = "";
                            if (jsonObject_single_following.has(JSONCommonKeywords.Address)) {
                                address = jsonObject_single_following.getString(JSONCommonKeywords.Address);
                                stringBuffer_bind_address.append(address);
                            }


                            if (jsonObject_single_following.has(JSONCommonKeywords.City)) {
                                city = jsonObject_single_following.getString(JSONCommonKeywords.City);
                                stringBuffer_bind_address.append(" ");
                                stringBuffer_bind_address.append(city);
                            }

                            if (jsonObject_single_following.has(JSONCommonKeywords.State)) {
                                state = jsonObject_single_following.getString(JSONCommonKeywords.State);
                                stringBuffer_bind_address.append(" ");
                                stringBuffer_bind_address.append(state);
                            }

                            if (jsonObject_single_following.has(JSONCommonKeywords.Country)) {
                                country = jsonObject_single_following.getString(JSONCommonKeywords.Country);
                                stringBuffer_bind_address.append(" ");
                                stringBuffer_bind_address.append(country);
                            }


                            followingbean.setFullAddress(stringBuffer_bind_address.toString());
                            followingbeanArrayList_local.add(followingbean);

                        }


                        if (current_start == 0) {
                            followingbeanArrayList = followingbeanArrayList_local;
                            followingAdapter.notifyDataSetChanged();

                        } else {

                            int start = followingbeanArrayList.size();

                            for (int i = 0; i < followingbeanArrayList_local.size(); i++) {

                                followingbeanArrayList.add(start, followingbeanArrayList_local.get(i));
                                followingAdapter.notifyItemInserted(followingbeanArrayList.size());
                                start++;

                            }
                            followingAdapter.setLoaded();

                        }
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
                baseTextview_error.setText(getResources().getString(R.string.no_data_founds));
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