package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.ClassifiedAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Classifiedbean;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.GridSpacingItemDecoration;
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

import java.util.ArrayList;

public class ClassifiedMainActivity extends BaseActivity implements SetupViewInterface {

    public static ArrayList<Classifiedbean> classifiedbeanArrayList = new ArrayList<>();
    public static ClassifiedMainActivity classifiedMainActivity = null;
    protected Handler handler;
    View view_home = null;
  public static  ClassifiedAdapter classifiedAdapter = null;
    CommonSession commonSession = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    FloatingActionButton floatingActionButton_create_classfied = null;
    int current_start = 0;
    public static BaseTextview baseTextview_error = null;
    public static RecyclerView mRecyclerView;
    BaseTextview baseTextview_header_title = null;
    String communityID = null,communityText=null;
    CommunityAccessbean communityAccessbean=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        try {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            retryParameterbean = new RetryParameterbean(ClassifiedMainActivity.this, getApplicationContext(), getIntent().getExtras(), ClassifiedMainActivity.class.getClass());
            commonSession = new CommonSession(ClassifiedMainActivity.this);
            classifiedMainActivity = this;
            classifiedbeanArrayList.clear();
            handler = new Handler();
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean= (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
                communityText=bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT);


            }
            setupHeaderView();
            findViewByIDs();
            setupEvents();

            getClassified();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setupHeaderView() {
        try {


            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.classified));
            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);


            imageview_right_foursquare = (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.VISIBLE);
            imageview_right_foursquare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyUtils.openCommunityMenu(ClassifiedMainActivity.this,communityID,communityText,communityAccessbean);
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
            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    MyUtils.leftMenuUpdateDataifOpenedDrawer(ClassifiedMainActivity.this,drawerLayout,circularImageView_profile_pic,baseTextview_left_menu_unread_message,retryParameterbean);
                }

                @Override
                public void onDrawerClosed(View drawerView) {

                }

                @Override
                public void onDrawerStateChanged(int newState) {

                }
            };
            drawerLayout.setDrawerListener(drawerListener);

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.classfied_main, null);

            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            mRecyclerView.setLayoutManager(gaggeredGridLayoutManager);
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            floatingActionButton_create_classfied = (FloatingActionButton) view_home.findViewById(R.id.fab_create_classfied);

            if(commonSession.isUserAdminNow())
            {
                floatingActionButton_create_classfied.setVisibility(View.VISIBLE);
            }
            else
            {
                floatingActionButton_create_classfied.setVisibility(View.GONE);

            }

            if(commonSession.isUserFollow())
            {
                floatingActionButton_create_classfied.setVisibility(View.VISIBLE);
            }
            else
            {
                floatingActionButton_create_classfied.setVisibility(View.GONE);

            }


            int spanCount = 2; // 3 columns
            int spacing = 15; // 50px
            boolean includeEdge = true;
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
            // use a linear layout manager
            mRecyclerView.setLayoutManager(gaggeredGridLayoutManager);


            classifiedAdapter = new ClassifiedAdapter( mRecyclerView, ClassifiedMainActivity.this,communityID);
            mRecyclerView.setAdapter(classifiedAdapter);
            frameLayout_container.addView(view_home);


            classifiedAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            current_start += 10;


                            getClassified();

                        }
                    }, 2000);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupEvents() {
        floatingActionButton_create_classfied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClassifiedMainActivity.this, CreateClassifiedActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID,communityID);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


    // this method is used for login user
    public void getClassified() {
        try {


            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setCommunityID(communityID);


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.CLASSIFIED.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, ClassifiedMainActivity.this, getApplicationContext(), URLs.CLASSIFIEDLISTING, jsonObjectGetPostParameterEachScreen, ScreensEnums.CLASSIFIED.getScrenIndex(), ClassifiedMainActivity.class.getClass());


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

                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.ClassifiedList)) {
                        ArrayList<Classifiedbean> classifiedbeanArrayList_local = new ArrayList<>();
                        JSONArray jsonArray_classified_listing = jsonObject_main.getJSONArray(JSONCommonKeywords.ClassifiedList);
                        for (int i = 0; i < jsonArray_classified_listing.length(); i++) {
                            Classifiedbean classifiedbean = new Classifiedbean();

                            JSONObject json_single_classified = jsonArray_classified_listing.getJSONObject(i);

                            if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.id))

                            {
                                classifiedbean.setClassifiedID(json_single_classified.getString(JSONCommonKeywords.id));
                            }


                            if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.PropertyTitle))

                            {
                                classifiedbean.setClassified_name(json_single_classified.getString(JSONCommonKeywords.PropertyTitle));
                            }


                            if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.Description))

                            {
                                classifiedbean.setClassifiedDescription(json_single_classified.getString(JSONCommonKeywords.Description));
                            }

                            if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.Price))

                            {
                                classifiedbean.setClssified_price(json_single_classified.getString(JSONCommonKeywords.Price));
                            }
                            if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.Status))

                            {
                                classifiedbean.setStatus(json_single_classified.getString(JSONCommonKeywords.Status));
                            }
                            if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.PostedDate))

                            {
                                classifiedbean.setClassified_date(json_single_classified.getString(JSONCommonKeywords.PostedDate));
                            }
                            if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.OwnerId))

                            {
                                classifiedbean.setOwnerId(json_single_classified.getString(JSONCommonKeywords.OwnerId));
                            }
                            if (CommonMethods.handleKeyInJSON(json_single_classified, JSONCommonKeywords.Image))

                            {
                                ArrayList<String> images_of_classified = new ArrayList<>();
                                if (CommonMethods.checkJSONArrayHasData(json_single_classified, JSONCommonKeywords.Image)) {
                                    JSONArray jsonArray_images = json_single_classified.getJSONArray(JSONCommonKeywords.Image);
                                    for (int j = 0; j < jsonArray_images.length(); j++) {

                                        if (CommonMethods.isImageUrlValid(jsonArray_images.getString(j))) {
                                            images_of_classified.add(jsonArray_images.getString(j));
                                        }
                                    }

                                    classifiedbean.setStringArrayList_images(images_of_classified);

                                }
                            }
                            classifiedbeanArrayList_local.add(classifiedbean);


                        }
                        mRecyclerView.setVisibility(View.VISIBLE);
                        baseTextview_error.setVisibility(View.GONE);
                        if (current_start == 0) {

                            classifiedbeanArrayList = classifiedbeanArrayList_local;
                            classifiedAdapter.notifyDataSetChanged();

                        } else {

                            int start = classifiedbeanArrayList.size();
                            for (int i = 0; i < classifiedbeanArrayList_local.size(); i++) {

                                classifiedbeanArrayList.add(start, classifiedbeanArrayList_local.get(i));
                                classifiedAdapter.notifyItemInserted(classifiedbeanArrayList.size());
                                start++;

                            }
                            classifiedAdapter.setLoaded();

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
                baseTextview_error.setText(getResources().getString(R.string.no_classified_found));
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