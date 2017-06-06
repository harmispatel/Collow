package com.app.collow.activities;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.app.collow.R;

import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.Activitiesbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.linkedin.platform.LISessionManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Harmis on 25/01/17.
 */

public class SettingActivity extends BaseActivity    {

    View view_home = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    BaseTextview baseTextview_signout=null;
    BaseTextview baseTextview_send_feedback=null;
    public static SettingActivity  settingActivity=null;
    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            settingActivity=this;
            retryParameterbean = new RetryParameterbean(SettingActivity.this, getApplicationContext(), getIntent().getExtras(), SettingActivity.class.getClass());
            setupHeaderView();
            findViewByIDs();
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }

    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.settings));

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

        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.settings_main, null);


            baseTextview_signout= (BaseTextview) view_home.findViewById(R.id.basetextview_signout);

            baseTextview_send_feedback= (BaseTextview) view_home.findViewById(R.id.basetextview_send_feedback);
           
            frameLayout_container.addView(view_home);

            baseTextview_signout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutDialog();
                }
            });

            baseTextview_send_feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendFeedback();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }

    }



    public void sendFeedback() {
        // custom dialog
        try {
            final Dialog dialog = new Dialog(SettingActivity.this, R.style.DialogCustomTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setContentView(R.layout.classisfied_dialog);

            RelativeLayout relativeLayout_contact_us_container = (RelativeLayout) dialog.findViewById(R.id.contact_us_layout);

            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);
            final BaseEdittext baseEdittext_message = (BaseEdittext) dialog.findViewById(R.id.edittext_classfied_contact_message);
            BaseTextview baseTextview_message = (BaseTextview) dialog.findViewById(R.id.textview_im_interest_messssage);
            relativeLayout_contact_us_container.setVisibility(View.VISIBLE);
            baseTextview_message.setVisibility(View.GONE);
            MyButtonView myButtonView_send = (MyButtonView) dialog.findViewById(R.id.mybuttonview_send_message);

            myButtonView_send.setOnClickListener(new MyOnClickListener(SettingActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        if (TextUtils.isEmpty(baseEdittext_message.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_message), SettingActivity.this);
                        } else if (!CommonMethods.isMorethenThreeCharacters(baseEdittext_message.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), SettingActivity.this);

                        } else {
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                        }
                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            imageView_close_dialopg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }

    }
    public void sendFeedback(final Dialog dialog) {
        try {
          

            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.CONTACT_CLASSIFIED.getScrenIndex(), requestParametersbean);


            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {


                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                                }


                                if (responcebean.getErrorMessage() == null) {
                                    CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_done), SettingActivity.this);

                                } else {
                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), SettingActivity.this);

                                }
                                if (dialog != null && dialog.isShowing()) {
                                    dialog.dismiss();
                                }


                            } else {
                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                                }


                                if (responcebean.getErrorMessage() == null) {
                                    CommonMethods.customToastMessage(getResources().getString(R.string.create_classfied_failed), SettingActivity.this);

                                } else {
                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), SettingActivity.this);

                                }

                            }


                        } catch (Exception e) {
                            CommonMethods.customToastMessage(e.getMessage(), SettingActivity.this);

                        }

                    }


                }
            }, SettingActivity.this, getApplicationContext(), URLs.CONTACTOWNER, jsonObjectGetPostParameterEachScreen, ScreensEnums.CONTACT_CLASSIFIED.getScrenIndex(), SignInActivity.class.getClass());


            new RequestToServer(passParameterbean, retryParameterbean).execute();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }
}
