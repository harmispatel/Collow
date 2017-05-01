package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.ClassifiedAdapter;
import com.app.collow.adapters.PollsAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Classifiedbean;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.Pollsbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beansgenerate.PollsbeanBuild;
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

public class PollsMainActivity extends BaseActivity implements SetupViewInterface {

    View view_home = null;
   public static PollsAdapter pollsAdapter = null;
    public static ArrayList<Pollsbean> pollsbeanArrayList = new ArrayList<>();
    public static BaseTextview baseTextview_error = null;
    public static RecyclerView mRecyclerView;
    protected Handler handler;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title=null;

    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start = 0;
    public static PollsMainActivity pollsMainActivity=null;
    FloatingActionButton floatingActionButton_create_polls=null;
    String communityID = null,communityText=null;
    CommunityAccessbean communityAccessbean=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(PollsMainActivity.this, getApplicationContext(), getIntent().getExtras(), PollsMainActivity.class.getClass());
            commonSession = new CommonSession(PollsMainActivity.this);
            pollsMainActivity=this;
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

            getPollsList();

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title= (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.polls));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);

            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setImageResource(R.drawable.community_main_menu);

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


                   MyUtils.openCommunityMenu(PollsMainActivity.this,communityID,communityText,communityAccessbean);



                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.polls_main, null);

            floatingActionButton_create_polls= (FloatingActionButton) view_home.findViewById(R.id.fab_create_polls);

            if(commonSession.isUserAdminNow())
            {
                floatingActionButton_create_polls.setVisibility(View.VISIBLE);
            }
            else
            {
                floatingActionButton_create_polls.setVisibility(View.GONE);
            }


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);


            pollsAdapter = new PollsAdapter(PollsMainActivity.this,mRecyclerView,communityID,communityText,communityAccessbean);
            mRecyclerView.setAdapter(pollsAdapter);
            frameLayout_container.addView(view_home);



            pollsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            current_start = +10;


                            requestParametersbean.setStart_limit(current_start);
                            getPollsList();


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
            floatingActionButton_create_polls.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(PollsMainActivity.this,CreatePollsActivity.class);

                    Bundle bundle = new Bundle();
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID,communityID);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }
    }


    // this method is used for login user
    public void getPollsList() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setCommunityID(communityID);
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.POLLS.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, PollsMainActivity.this, getApplicationContext(), URLs.POLLS, jsonObjectGetPostParameterEachScreen, ScreensEnums.POLLS.getScrenIndex(), PollsMainActivity.class.getClass());
            if(current_start==0)
            {
                passParameterbean.setNoNeedToDisplayLoadingDialog(false);

            }
            else
            {
                passParameterbean.setNoNeedToDisplayLoadingDialog(true);

            }

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
                    if(CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.Pollsdata))
                    {

                        ArrayList<Pollsbean> pollsbeen_local=new ArrayList<>();
                        JSONArray jsonArray_polls_list=jsonObject_main.getJSONArray(JSONCommonKeywords.Pollsdata);
                        for (int i = 0; i < jsonArray_polls_list.length(); i++) {

                            JSONObject jsonObject_single_polls=jsonArray_polls_list.getJSONObject(i);
                            Pollsbean  pollsbean= PollsbeanBuild.getPollsbean(jsonObject_single_polls,PollsMainActivity.this);
                            pollsbean.setPosition(i);
                            pollsbeen_local.add(pollsbean);
                        }

                        if (current_start == 0) {
                            pollsbeanArrayList = pollsbeen_local;
                            pollsAdapter.notifyDataSetChanged();
                        } else {

                            int start = pollsbeanArrayList.size();

                            for (int i = 0; i < pollsbeen_local.size(); i++) {

                                Pollsbean pollsbean=pollsbeen_local.get(i);
                                pollsbean.setPosition(start);
                                pollsbeanArrayList.add(start, pollsbean);
                                pollsAdapter.notifyItemInserted(pollsbeanArrayList.size());
                                start++;

                            }
                            pollsAdapter.setLoaded();

                        }
                    }
                    else
                    {
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

    public void handlerError(Responcebean responcebean)
    {
        if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals(""))
        {
            if(current_start==0)
            {
                baseTextview_error.setText(getResources().getString(R.string.no_polls_records_found));
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            }
            else
            {

            }
        }
        else
        {
            if(current_start==0)
            {
                baseTextview_error.setText(responcebean.getErrorMessage());
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            }
            else
            {

            }
        }
    }
}
