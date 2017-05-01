package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.adapters.NewsAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
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

import java.util.ArrayList;

/**
 * Created by harmis on 1/2/17.
 */

public class NewsMainActivity extends BaseActivity implements SetupViewInterface {

    public static NewsAdapter newsAdapter = null;
    public static ArrayList<Newsbean> newsbeanArrayList = new ArrayList<>();
    public static RecyclerView mRecyclerView;
    public static NewsMainActivity newsMainActivity = null;
    protected Handler handler;
    View view_home = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;

    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start = 0;
    FloatingActionButton floatingActionButton_create_news = null;
    CommunityAccessbean communityAccessbean = null;
    String communityID = null;
    public static BaseTextview baseTextview_error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            newsMainActivity = this;
            newsbeanArrayList.clear();
            retryParameterbean = new RetryParameterbean(NewsMainActivity.this, getApplicationContext(), getIntent().getExtras(), NewsMainActivity.class.getClass());
            commonSession = new CommonSession(NewsMainActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean = (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
            }
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            getNewsListing();

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.news));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                    drawerLayout.closeDrawer(Gravity.RIGHT);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);

                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);

                }
            };
            drawerLayout.setDrawerListener(drawerListener);

            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    MyUtils.openCommunityMenu(NewsMainActivity.this, communityID, baseTextview_header_title.getText().toString(), communityAccessbean);


                }
            });

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.news_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);
            floatingActionButton_create_news = (FloatingActionButton) view_home.findViewById(R.id.fab_create_news_annoucement);

            if (commonSession.isUserAdminNow()) {
                floatingActionButton_create_news.setVisibility(View.VISIBLE);
            } else {
                floatingActionButton_create_news.setVisibility(View.GONE);

            }

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);


            newsAdapter = new NewsAdapter(mRecyclerView, this);
            mRecyclerView.setAdapter(newsAdapter);
            frameLayout_container.addView(view_home);


            newsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            current_start += 10;

                            //   searchbean_post_data.getStart_limit()+=10;

                            requestParametersbean.setStart_limit(current_start);
                            //  getNewsListing();


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
        try {
            floatingActionButton_create_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NewsMainActivity.this, CreateNewsAndAnnouncementActivity.class);

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
    public void getNewsListing() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setCommunityID(communityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.NEWS.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, NewsMainActivity.this, getApplicationContext(), URLs.NEWSLISTING, jsonObjectGetPostParameterEachScreen, ScreensEnums.NEWS.getScrenIndex(), NewsMainActivity.class.getClass());

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


                    if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.NewsLists)) {

                        ArrayList<Newsbean> newsbeanArrayList_local = new ArrayList<>();
                        JSONArray jsonArray_news_list = jsonObject_main.getJSONArray(JSONCommonKeywords.NewsLists);
                        for (int i = 0; i < jsonArray_news_list.length(); i++) {

                            Newsbean newsbean = new Newsbean();
                            JSONObject jsonObject_single = jsonArray_news_list.getJSONObject(i);

                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.Newsid)) {
                                newsbean.setNews_id(jsonObject_single.getString(JSONCommonKeywords.Newsid));

                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.News)) {
                                newsbean.setNews_title(jsonObject_single.getString(JSONCommonKeywords.News));

                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.Description)) {
                                newsbean.setNews_description(jsonObject_single.getString(JSONCommonKeywords.Description));

                            }

                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.NewsDate)) {
                                newsbean.setNews_time(jsonObject_single.getString(JSONCommonKeywords.NewsDate));

                            }


                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.UserName)) {
                                newsbean.setNews_username(jsonObject_single.getString(JSONCommonKeywords.UserName));

                            }
                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.CommunityName)) {
                                newsbean.setCommunityName(jsonObject_single.getString(JSONCommonKeywords.CommunityName));

                            }


                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.ProfilePic)) {
                                newsbean.setNews_userprofilepic(jsonObject_single.getString(JSONCommonKeywords.ProfilePic));

                            }

                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.ActivityId)) {
                                newsbean.setActivityID(jsonObject_single.getString(JSONCommonKeywords.ActivityId));

                            }

                            SocialOptionbean socialOptionbean = CommonMethods.getSocialOptionbean(jsonObject_single);

                            if (socialOptionbean != null) {
                                newsbean.setSocialOptionbean(socialOptionbean);
                            }


                            if (jsonObject_single.has(JSONCommonKeywords.isLiked)) {


                                String islikedfeedString = jsonObject_single.getString(JSONCommonKeywords.isLiked);
                                if (islikedfeedString == null || islikedfeedString.equals("")) {
                                    newsbean.setLiked(false);
                                } else if (islikedfeedString.equals("1")) {
                                    newsbean.setLiked(true);

                                } else {
                                    newsbean.setLiked(false);

                                }


                            }

                            ArrayList<String> files_of_news = new ArrayList<>();


                            if (CommonMethods.handleKeyInJSON(jsonObject_single, JSONCommonKeywords.Image))

                            {
                                if (CommonMethods.checkJSONArrayHasData(jsonObject_single, JSONCommonKeywords.Image)) {
                                    JSONArray jsonArray_images = jsonObject_single.getJSONArray(JSONCommonKeywords.Image);
                                    for (int j = 0; j < jsonArray_images.length(); j++) {

                                        if (CommonMethods.isImageUrlValid(jsonArray_images.getString(j))) {
                                            files_of_news.add(jsonArray_images.getString(j));
                                        }
                                    }


                                }


                            } else {

                            }


                            newsbean.setStringArrayList_fileURLs(files_of_news);


                            newsbean.setPosition(i);
                            newsbeanArrayList_local.add(newsbean);
                        }

                        if (current_start == 0) {
                            newsbeanArrayList = newsbeanArrayList_local;
                            newsAdapter.notifyDataSetChanged();

                        } else {

                            int start = newsbeanArrayList.size();

                            for (int i = 0; i < newsbeanArrayList_local.size(); i++) {

                                Newsbean newsbean = newsbeanArrayList_local.get(i);
                                newsbean.setPosition(start);
                                newsbeanArrayList.add(start, newsbean);
                                newsAdapter.notifyItemInserted(newsbeanArrayList.size());

                                start++;

                            }
                            newsAdapter.setLoaded();

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
        try {
            if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                if (current_start == 0) {
                    baseTextview_error.setText(getResources().getString(R.string.no_news_found));
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
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }
    }
}
