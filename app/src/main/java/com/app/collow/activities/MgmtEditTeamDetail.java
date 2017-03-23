package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Handler;
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
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

public class MgmtEditTeamDetail extends BaseActivity implements SetupViewInterface {

    View view_home = null;

    BaseEdittext edittext_mgmteditteam_name= null;
    BaseEdittext edittext_mgmteditteam_address= null;
    BaseEdittext edittext_mgmteditteam_email= null;
    BaseEdittext edittext_mgmteditteam_fax= null;
    BaseEdittext edittext_mgmteditteam_phone= null;
    BaseEdittext edittext_mgmteditteam_url= null;
    BaseTextview textview_mgmteditteam_profilepic=null;
    ImageView  imageview_mgmteditteam_profilepic=null;
    MyButtonView button_mgmteditteam_save=null;
    BaseTextview baseTextview_left_side=null;

    BaseTextview baseTextview_header_title=null;
    private BaseTextview baseTextview_error = null;
    private RecyclerView mRecyclerView;
    protected Handler handler;
    CommonSession commonSession = null;

    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(MgmtEditTeamDetail.this, getApplicationContext(), getIntent().getExtras(), MgmtEditTeamDetail.class.getClass());
            commonSession = new CommonSession(MgmtEditTeamDetail.this);

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
            View headerview = getLayoutInflater().inflate(R.layout.header, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerview.setLayoutParams(layoutParams);
            baseTextview_header_title= (BaseTextview) headerview.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.news));

            imageView_left_menu = (ImageView) headerview.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) headerview.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            baseTextview_left_side = (BaseTextview) headerview.findViewById(R.id.textview_left_side_title);

             baseTextview_left_side.setText(getResources().getString(R.string.cancel));
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
            setSupportActionBar(toolbar_header);
            toolbar_header.addView(headerview);
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }



    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.recyleview_main, null);


            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);

            edittext_mgmteditteam_name=(BaseEdittext)view_home.findViewById(R.id.edittext_mgmteditteam_name);
            edittext_mgmteditteam_address=(BaseEdittext)view_home.findViewById(R.id.edittext_mgmteditteam_address);
            edittext_mgmteditteam_email=(BaseEdittext)view_home.findViewById(R.id.edittext_mgmteditteam_email);
            edittext_mgmteditteam_fax=(BaseEdittext)view_home.findViewById(R.id.edittext_mgmteditteam_fax);
            edittext_mgmteditteam_phone=(BaseEdittext)view_home.findViewById(R.id.edittext_mgmteditteam_phone);
            edittext_mgmteditteam_url=(BaseEdittext)view_home.findViewById(R.id.edittext_mgmteditteam_url);
            textview_mgmteditteam_profilepic=(BaseTextview) view_home.findViewById(R.id.textview_mgmteditteam_profilepicture);
            imageview_mgmteditteam_profilepic=(ImageView)view_home.findViewById(R.id.imageview_mgmgteditteam_profilepicture);
            button_mgmteditteam_save=(MyButtonView) view_home.findViewById(R.id.button_mgmteditteam_save);



            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);

        /*newsAdapter = new NewsAdapter(newsbeanArrayList, mRecyclerView);
        mRecyclerView.setAdapter(newsAdapter);
        */
            frameLayout_container.addView(view_home);

            acViewFAQ();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void setupEvents() {

    }


    // this method is used for login user
    public void acViewFAQ() {
        try {



            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums. EDITTEAM.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, MgmtEditTeamDetail.this, getApplicationContext(), URLs. EDITTEAM, jsonObjectGetPostParameterEachScreen, ScreensEnums. EDITTEAM.getScrenIndex(), MgmtEditTeamDetail.class.getClass());

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
            if (requestParametersbean.getStart_limit() == 0) {
                baseTextview_error.setText(getResources().getString(R.string.no_data_founds));
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            } else {

            }
        } else {
            if (requestParametersbean.getStart_limit() == 0) {
                baseTextview_error.setText(responcebean.getErrorMessage());
                mRecyclerView.setVisibility(View.GONE);
                baseTextview_error.setVisibility(View.VISIBLE);

            } else {

            }
        }
    }
}

