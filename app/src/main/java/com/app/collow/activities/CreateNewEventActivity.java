package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

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
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.URLs;

import org.json.JSONObject;

public class CreateNewEventActivity extends BaseActivity implements SetupViewInterface {

    View view_home = null;
    BaseTextview baseTextview_header_title=null;
    BaseEdittext edittext_acnewevent_addimage=null;
    BaseEdittext edittext_acnewevent_date=null;
    BaseEdittext edittext_acnewevent_starttime=null;
    BaseEdittext edittext_acnewevent_endtime=null;
    BaseEdittext edittext_acnewevent_eventlocation=null;
    BaseEdittext edittext_acnewevent_description=null;
    BaseTextview baseTextview_left_side=null;
    MyButtonView button_acnewevent_addimage=null;
    MyButtonView button_acnewevent_save=null;
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
            retryParameterbean = new RetryParameterbean(CreateNewEventActivity.this, getApplicationContext(), getIntent().getExtras(), CreateNewEventActivity.class.getClass());
            commonSession = new CommonSession(CreateNewEventActivity.this);

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

            baseTextview_header_title= (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.new_event));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);



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

        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.activity_acnew_event_main, null);



            edittext_acnewevent_addimage=(BaseEdittext) view_home.findViewById(R.id.edittext_acnewevent_addimage);
            edittext_acnewevent_date=(BaseEdittext) view_home.findViewById(R.id.edittext_acnewevent_date);
            edittext_acnewevent_starttime=(BaseEdittext) view_home.findViewById(R.id.edittext_acnewevent_starttime);
            edittext_acnewevent_endtime=(BaseEdittext)  view_home.findViewById(R.id.edittext_acnewevent_endtime);
            edittext_acnewevent_eventlocation=(BaseEdittext)  view_home.findViewById(R.id.edittext_acnewevent_eventlocation);
            edittext_acnewevent_description=(BaseEdittext)  view_home.findViewById(R.id.edittext_acnewevent_description);
            button_acnewevent_addimage=(MyButtonView)view_home.findViewById(R.id.button_acnewevent_addimage);
            button_acnewevent_save=(MyButtonView)view_home.findViewById(R.id.button_acnewevent_save);


            button_acnewevent_save.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v1) {
                    Intent launchActivity1= new Intent(CreateNewEventActivity.this,ACEventDetailsActivity.class);
                    startActivity(launchActivity1);

                }
            });

            frameLayout_container.addView(view_home);

            getacNewEvent();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void setupEvents() {
        baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    // this method is used for login user
    public void getacNewEvent() {
        try {



            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.ACNEWEVENT.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CreateNewEventActivity.this, getApplicationContext(), URLs.ACEDITADMIN, jsonObjectGetPostParameterEachScreen, ScreensEnums.ACNEWEVENT.getScrenIndex(), CreateNewEventActivity.class.getClass());

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
        try {
            if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                if (requestParametersbean.getStart_limit() == 0) {


                } else {

                }
            } else {
                if (requestParametersbean.getStart_limit() == 0) {


                } else {

                }
            }
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);


        }
    }
}
