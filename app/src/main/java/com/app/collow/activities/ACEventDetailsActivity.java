package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACListEventBean;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

/**
 * Created by harmis on 6/2/17.
 */

public class ACEventDetailsActivity extends BaseActivity implements SetupViewInterface {

    View view_home = null;
    ImageView image_acviewevents= null;
    BaseTextview textview_acviewevent_title= null;
    BaseTextview textview_acviewevent_date= null;
    BaseTextview textview_acviewevent_description= null;
    BaseTextview textview_acviewevent_time= null;
    BaseTextview  baseTextview_header_title=null;
    private BaseTextview baseTextview_error = null;
    BaseTextview baseTextview_left_side=null;
    private RecyclerView mRecyclerView;
    String KEY_COMMUNITY_ID=null;
    int current_start = 0;
    CommunityAccessbean communityAccessbean=null;
    protected Handler handler;
    CommonSession commonSession = null;
    ACListEventBean aclisteventbean=null;
    String communityID=null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null,imageview_right_foursquare=null;
    ImageView imageView_delete = null,imageView_view=null,imageView_edit=null,imageView_search=null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(ACEventDetailsActivity.this, getApplicationContext(), getIntent().getExtras(), ACEventDetailsActivity.class.getClass());
            commonSession = new CommonSession(ACEventDetailsActivity.this);
            Bundle bundle = getIntent().getExtras();
           /* if (bundle != null) {
                KEY_COMMUNITY_ID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
                communityAccessbean= (CommunityAccessbean) bundle.getSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN);
            }*/
           /* if (bundle != null) {
                aclisteventbean = (ACListEventBean) bundle.getSerializable(BundleCommonKeywords.KEY_ACEVENT_BEAN);
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
            }*/

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
            /*View headerview = getLayoutInflater().inflate(R.layout.header, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerview.setLayoutParams(layoutParams);*/
            baseTextview_header_title= (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.view_event));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            imageView_delete = (ImageView) toolbar_header.findViewById(R.id.imageview_delete);
            imageView_delete.setVisibility(View.VISIBLE);
            imageView_view = (ImageView) toolbar_header.findViewById(R.id.imageview_view);
            imageView_view.setVisibility(View.VISIBLE);
            imageView_edit = (ImageView) toolbar_header.findViewById(R.id.imageview_edit);
            imageView_edit.setVisibility(View.VISIBLE);
            imageview_right_foursquare= (ImageView) toolbar_header.findViewById(R.id.imageview_community_menu);
            imageview_right_foursquare.setVisibility(View.GONE);

            imageView_search= (ImageView) toolbar_header.findViewById(R.id.imageview_search_items);
            imageView_search.setVisibility(View.GONE);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);

           // baseTextview_left_side.setText(getResources().getString(R.string.cancel));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);


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
           /* setSupportActionBar(toolbar_header);
            toolbar_header.addView(headerview);*/
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        try {
            //view_home = getLayoutInflater().inflate(R.layout.recyleview_main, null);

            view_home = getLayoutInflater().inflate(R.layout.aclistevent_viewevent, null);
           // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());


           // baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            //mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            textview_acviewevent_title=(BaseTextview) view_home.findViewById(R.id.viewevent_title);
            textview_acviewevent_date=(BaseTextview)view_home.findViewById(R.id.viewevent_date);
            textview_acviewevent_description=(BaseTextview)view_home.findViewById(R.id.viewevent_description);
            textview_acviewevent_time=(BaseTextview)view_home.findViewById(R.id.viewevent_time);
            image_acviewevents=(ImageView)view_home.findViewById(R.id.imageview_viewevent_image);

            image_acviewevents.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    Intent launchActivity1= new Intent(ACEventDetailsActivity.this,ACEditEventMainActivity.class);
                    startActivity(launchActivity1);

                }
            });

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
          /*  mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);
*/
        /*newsAdapter = new NewsAdapter(newsbeanArrayList, mRecyclerView);
        mRecyclerView.setAdapter(newsAdapter);
        */
            frameLayout_container.addView(view_home);

            acViewEvents();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void setupEvents() {
        imageView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ACEventDetailsActivity.this, ACEditEventMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
        baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    // this method is used for login user
    public void acViewEvents() {
        try {



            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums. ACLISTVIEWEVENT.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, ACEventDetailsActivity.this, getApplicationContext(), URLs. ACLISTVIEWEVENT, jsonObjectGetPostParameterEachScreen, ScreensEnums. ACLISTVIEWEVENT.getScrenIndex(), ACEventDetailsActivity.class.getClass());

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









