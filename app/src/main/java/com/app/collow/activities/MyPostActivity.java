package com.app.collow.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.MyPostAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Activitiesbean;
import com.app.collow.beans.MyPostbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Harmis on 25/01/17.
 */

public class MyPostActivity extends BaseActivity implements SetupViewInterface {

    public static ArrayList<MyPostbean> myPostbeanArrayList = new ArrayList<>();
    public static MyPostActivity myPostActivity=null;
    protected Handler handler;
    View view_home = null;
    MyPostAdapter myPostAdapter = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    int current_start = 0;
    CommonSession commonSession=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            retryParameterbean = new RetryParameterbean(MyPostActivity.this, getApplicationContext(), getIntent().getExtras(), MyPostActivity.class.getClass());
            commonSession=new CommonSession(MyPostActivity.this);

            myPostActivity=this;
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            getMyPosts();
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }

    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.my_posts));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);


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
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.recyleview_main, null);
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);



            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(20);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);

            myPostAdapter = new MyPostAdapter(MyPostActivity.this, mRecyclerView);
            mRecyclerView.setAdapter(myPostAdapter);
            frameLayout_container.addView(view_home);

            myPostAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            current_start += 10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);
                            getMyPosts();

                        }
                    }, 2000);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }

    }


    // get activities from server
    public void getMyPosts() {
        try {

            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setStart_limit(current_start);

            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.MY_POST.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, MyPostActivity.this, getApplicationContext(), URLs.MY_POSTS, jsonObjectGetPostParameterEachScreen, ScreensEnums.MY_POST.getScrenIndex(), SignInActivity.class.getClass());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_post_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search_post).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_post_right_icon:
                drawerLayout.openDrawer(Gravity.RIGHT);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void setupUI(Responcebean responcebean) {

        try {
            if (responcebean.isServiceSuccess()) {

                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                    //parse here data of following


                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.mypostdata)) {

                        JSONArray jsonArray = jsonObject_main.getJSONArray(JSONCommonKeywords.mypostdata);
                        ArrayList<MyPostbean> myPostbeanArrayList_local = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject jsonObject_single_my_post = jsonArray.getJSONObject(i);
                            MyPostbean myPostbean = new MyPostbean();

                            if(CommonMethods.handleKeyInJSON(jsonObject_single_my_post,JSONCommonKeywords.Feedid))
                            {
                                myPostbean.setFeedid(jsonObject_single_my_post.getString(JSONCommonKeywords.Feedid));
                            }

                            if(CommonMethods.handleKeyInJSON(jsonObject_single_my_post,JSONCommonKeywords.CommunityID))
                            {
                                myPostbean.setCommunityID(jsonObject_single_my_post.getString(JSONCommonKeywords.CommunityID));
                            }


                            if(CommonMethods.handleKeyInJSON(jsonObject_single_my_post,JSONCommonKeywords.ActivityId))
                            {
                                myPostbean.setActivityId(jsonObject_single_my_post.getString(JSONCommonKeywords.ActivityId));
                            }



                            if(CommonMethods.handleKeyInJSON(jsonObject_single_my_post,JSONCommonKeywords.Category))
                            {
                                myPostbean.setCategory(jsonObject_single_my_post.getString(JSONCommonKeywords.Category));
                            }



                            if(CommonMethods.handleKeyInJSON(jsonObject_single_my_post,JSONCommonKeywords.Title))
                            {
                                myPostbean.setTitle(jsonObject_single_my_post.getString(JSONCommonKeywords.Title));
                            }


                            if(CommonMethods.handleKeyInJSON(jsonObject_single_my_post,JSONCommonKeywords.Date))
                            {
                                myPostbean.setNewsDate(jsonObject_single_my_post.getString(JSONCommonKeywords.Date));
                            }

                            if(CommonMethods.handleKeyInJSON(jsonObject_single_my_post,JSONCommonKeywords.Image))
                            {
                                myPostbean.setImage(jsonObject_single_my_post.getString(JSONCommonKeywords.Image));
                            }

                            myPostbean.setPosition(i);
                            myPostbeanArrayList_local.add(myPostbean);

                        }


                        if (current_start == 0) {
                            myPostbeanArrayList = myPostbeanArrayList_local;
                            myPostAdapter.notifyDataSetChanged();

                        } else {

                            int start = myPostbeanArrayList.size();

                            for (int i = 0; i < myPostbeanArrayList_local.size(); i++) {

                                MyPostbean myPostbean=myPostbeanArrayList_local.get(i);
                                myPostbean.setPosition(start);
                                myPostbeanArrayList.add(start, myPostbean);
                                myPostAdapter.notifyItemInserted(myPostbeanArrayList.size());
                                start++;

                            }
                            myPostAdapter.setLoaded();

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
                baseTextview_error.setText(getResources().getString(R.string.no_my_activities_found));
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