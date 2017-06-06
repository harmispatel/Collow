package com.app.collow.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;

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
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

/**
 * Created by harmis on 8/1/17.
 */

public class ForgotPasswordMainActivity extends AppCompatActivity implements SetupViewInterface {
    RetryParameterbean retryParameterbean = null;
    BaseTextview textview_signin_create = null;
    BaseEdittext edittext_signin_email = null;

    MyButtonView myButtonView_reset = null;
    BaseTextview baseTextview_left_side = null;
    BaseTextview baseTextview_center_side = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotpassword_main);
        retryParameterbean = new RetryParameterbean(ForgotPasswordMainActivity.this, getApplicationContext(), getIntent().getExtras(), ForgotPasswordMainActivity.class.getClass());

        findViewByIDs();
        setupEvents();

    }


    private void findViewByIDs() {

        baseTextview_left_side = (BaseTextview) findViewById(R.id.textview_left_side_title);

        baseTextview_center_side = (BaseTextview) findViewById(R.id.textview_header_title);

        textview_signin_create = (BaseTextview) findViewById(R.id.textview_signin_create);

        edittext_signin_email = (BaseEdittext) findViewById(R.id.edittext_forgot_password_email);

        myButtonView_reset = (MyButtonView) findViewById(R.id.mybuttonview_reset);
        baseTextview_left_side.setText(getResources().getString(R.string.back));
        baseTextview_center_side.setText(getResources().getString(R.string.forgot_password_title));

    }

    private void setupEvents() {
        textview_signin_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             finish();
                if(SingUpActivity.singUpActivity!=null)
                {
                    SingUpActivity.singUpActivity.finish();
                }

                if(SignInActivity.signInActivity!=null)
                {
                    SignInActivity.signInActivity.finish();
                }
                Intent intent = new Intent(ForgotPasswordMainActivity.this, SingUpActivity.class);
                startActivity(intent);
            }
        });
        myButtonView_reset.setOnClickListener(new MyOnClickListener(ForgotPasswordMainActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {

                    if (TextUtils.isEmpty(edittext_signin_email.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_email), ForgotPasswordMainActivity.this);

                    } else if (!CommonMethods.isMorethenThreeCharacters(edittext_signin_email.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), ForgotPasswordMainActivity.this);

                    } else if (!CommonMethods.emailValidator(edittext_signin_email.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_valid_email), ForgotPasswordMainActivity.this);

                    } else {
                        requestParametersbean.setUser_email(edittext_signin_email.getText().toString());
                        forgotPassowrd();

                    }


                } else {
                    showInternetNotfoundMessage();
                }
            }
        });
        baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    finish();
                } catch (Exception e) {


                }
            }
        });
    }

    // this method is used for forgot password
    public void forgotPassowrd() {
        try {
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.FORGOT_PASSWORD.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, ForgotPasswordMainActivity.this, getApplicationContext(), URLs.FORGOT_PASSWORD, jsonObjectGetPostParameterEachScreen, ScreensEnums.FORGOT_PASSWORD.getScrenIndex(), ForgotPasswordMainActivity.class.getClass());
            new RequestToServer(passParameterbean, retryParameterbean).execute();
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    @Override
    public void setupUI(Responcebean responcebean) {




        if(responcebean.isServiceSuccess())
        {

            try {
                if(responcebean.isServiceSuccess())
                {
                    JSONObject jsonObject=new JSONObject(responcebean.getResponceContent());
                    if(jsonObject!=null)
                    {
                        if(jsonObject.has(JSONCommonKeywords.success))
                        {
                            String success=jsonObject.getString(JSONCommonKeywords.success);
                            if(jsonObject.has(JSONCommonKeywords.message))
                            {
                                responcebean.setErrorMessage(jsonObject.getString(JSONCommonKeywords.message));
                            }

                            if(responcebean.getErrorMessage()==null||responcebean.getErrorMessage().equals(""))
                            {

                            }
                            else
                            {
                                CommonMethods.customToastMessage(responcebean.getErrorMessage(),ForgotPasswordMainActivity.this);
                            }
                            if(success==null||success.equals(""))
                            {

                            }
                            else if(success.equals("1"))
                            {

                                Intent intent = new Intent(ForgotPasswordMainActivity.this, ForgotPasswordDoneActivity.class);
                                startActivity(intent);
                            }
                            else
                            {

                            }








                        }
                    }

                }
                else
                {
                    if(responcebean.getErrorMessage()==null||responcebean.getErrorMessage().equals(""))
                    {
                        CommonMethods.customToastMessage(getResources().getString(R.string.something_wrong),ForgotPasswordMainActivity.this);

                    }
                    else
                    {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(),ForgotPasswordMainActivity.this);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else
        {

        }

    }
}