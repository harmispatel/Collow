package com.app.collow.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.app.collow.R;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.beans.CommunityTypebean;
import com.app.collow.beans.EventTypebean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.Statesbean;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;


public class SplashActvitiy extends Activity {

    public static boolean isNeedToDisplayLog = true;
    public static HashMap<String, CommunityTypebean> stringCommunityTypebeanHashMap_globle = new HashMap<>();
    public static HashMap<String, Statesbean> stringStatesbeanHashMap_globle = new HashMap<>();
    public static ArrayList<Integer> integerArrayList_colors = new ArrayList<>();
    CommonSession commonSession = null;
    private final int SPLASH_DISPLAY_LENGTH = 1000;
    public static HashMap<String,EventTypebean> stringEventTypebeanHashMap_globle=new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawable(null);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity_screen);
        commonSession = new CommonSession(SplashActvitiy.this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                needToLoadDataFromServer();
                getCommunityTypesData();
                getStatesListing();
                getEventTypeFromServer();
                integerArrayList_colors.add(getResources().getColor(R.color.menu_feed));
                integerArrayList_colors.add(getResources().getColor(R.color.menu_classfied));
                integerArrayList_colors.add(getResources().getColor(R.color.menu_news));
                integerArrayList_colors.add(getResources().getColor(R.color.menu_chat));
                integerArrayList_colors.add(getResources().getColor(R.color.menu_events));
                integerArrayList_colors.add(getResources().getColor(R.color.menu_gallery));
                integerArrayList_colors.add(getResources().getColor(R.color.menu_feed));
                integerArrayList_colors.add(getResources().getColor(R.color.menu_feed));
                integerArrayList_colors.add(getResources().getColor(R.color.menu_feed));
            }
        });

/* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                finish();

                if (commonSession.getLoggedUserID() == null) {
                    Intent intent = new Intent(SplashActvitiy.this, GettingStartedActivity.class);
                    startActivity(intent);
                } else {
                    if (commonSession.isUserSetHomeCommunity()) {
                        Intent intent = new Intent(SplashActvitiy.this, CommunityActivitiesFeedActivitiy.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActvitiy.this, CommunitySearchFilterOptionsActivity.class);
                        startActivity(intent);
                    }

                }
            }
        }, SPLASH_DISPLAY_LENGTH);


    }

    //need to load data for app requiremnts
    private void needToLoadDataFromServer() {

    }


    public void hashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                CommonMethods.displayLog("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void generateHashkey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                Log.e("Hash key", "" + Base64.encodeToString(md.digest(), Base64.NO_WRAP));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public void hashkey2() {

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                // writtenToFile("FB_KEY_HASH.txt",
                // Base64.encodeToString(md.digest(),
                // Base64.DEFAULT).toString(), false);


                CommonMethods.displayLog("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getCommunityTypesData() {

        try {

            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {

                                if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.typeList)) {
                                    JSONArray jsonArray_typelisting = jsonObject_main.getJSONArray(JSONCommonKeywords.typeList);
                                    for (int i = 0; i < jsonArray_typelisting.length(); i++) {

                                        JSONObject jsonObject_single_type = jsonArray_typelisting.getJSONObject(i);

                                        CommunityTypebean communityTypebean = new CommunityTypebean();

                                        if (jsonObject_single_type.has(JSONCommonKeywords.TypeId)) {
                                            communityTypebean.setTypeID(jsonObject_single_type.getString(JSONCommonKeywords.TypeId));

                                        }

                                        if (jsonObject_single_type.has(JSONCommonKeywords.TypeName)) {
                                            communityTypebean.setTypeName(jsonObject_single_type.getString(JSONCommonKeywords.TypeName));

                                        }

                                        stringCommunityTypebeanHashMap_globle.put(communityTypebean.getTypeID(), communityTypebean);


                                    }
                                }


                            } else {

                            }


                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GETCOMMUNITYTYPES, null, ScreensEnums.PROPERTY_TYPE_GET.getScrenIndex(), SignInActivity.class.getClass());

            passParameterbean.setNoNeedToDisplayLoadingDialog(true);

            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {

        }

    }


    // this method is used for login user
    public void getStatesListing() {
        try {


            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (jsonObject_main.has(JSONCommonKeywords.StateList)) {
                                JSONArray jsonArray_statelist = jsonObject_main.getJSONArray(JSONCommonKeywords.StateList);
                                if (jsonArray_statelist == null || jsonArray_statelist.equals("")) {

                                } else {
                                    if (jsonArray_statelist.length() != 0) {
                                        for (int i = 0; i < jsonArray_statelist.length(); i++) {

                                            JSONObject jsonObject_single = jsonArray_statelist.getJSONObject(i);

                                            String statecode = jsonObject_single.getString(JSONCommonKeywords.stateCode);
                                            String statename = jsonObject_single.getString(JSONCommonKeywords.StateName);
                                            Statesbean statesbean = new Statesbean();
                                            statesbean.setStateCode(statecode);
                                            statesbean.setStateName(statename);
                                            stringStatesbeanHashMap_globle.put(statecode, statesbean);


                                        }
                                    }
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GET_STATES_LISTING, null, ScreensEnums.LOGIN.getScrenIndex(), SignInActivity.class.getClass());


            new RequestToServer(passParameterbean, null).execute();


        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }
    }

    // this method is used for getinterest
    public void getEventTypeFromServer() {
        try {


            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {

                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {

                                if (CommonMethods.checkJSONArrayHasData(jsonObject_main, "EventTypes")) {
                                    JSONArray jsonArray_interests = jsonObject_main.getJSONArray("EventTypes");
                                    for (int i = 0; i < jsonArray_interests.length(); i++) {

                                        JSONObject jsonObject_single = jsonArray_interests.getJSONObject(i);

                                        EventTypebean eventTypebean = new EventTypebean();

                                        if (jsonObject_single.has(JSONCommonKeywords.id)) {
                                            eventTypebean.setTypeID(jsonObject_single.getString(JSONCommonKeywords.id));

                                        }

                                        if (jsonObject_single.has(JSONCommonKeywords.EventTypeName)) {
                                            eventTypebean.setTypeName(jsonObject_single.getString(JSONCommonKeywords.EventTypeName));

                                        }

                                        stringEventTypebeanHashMap_globle.put(eventTypebean.getTypeID(), eventTypebean);


                                    }


                                }


                            } else {

                            }




                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonMethods.customToastMessage(e.getMessage(), SplashActvitiy.this);

                        }

                    }


                }
            }, null, getApplicationContext(), URLs.GETEVENTTYPES, null, ScreensEnums.EVENT_TYPES.getScrenIndex(), SignInActivity.class.getClass());


            new RequestToServer(passParameterbean, null).execute();


        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }
    }
}
