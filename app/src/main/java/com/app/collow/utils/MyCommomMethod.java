package com.app.collow.utils;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.app.collow.beans.Followingbean;
import com.app.collow.beans.Responcebean;
import org.json.JSONObject;


public class MyCommomMethod {

    AppCompatActivity mActivity = null;
    MyJSONParser mJsonParser = null;
    LayoutInflater gLayoutInflater = null;
    CommonSession mCommonSession = null;

    public MyCommomMethod(AppCompatActivity myActivity) {
        // TODO Auto-generated constructor stub
        try {
            mActivity = myActivity;
            gLayoutInflater = myActivity.getLayoutInflater();
            mJsonParser = new MyJSONParser();
            mCommonSession = new CommonSession(myActivity);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyCommomMethod(Context myActivity) {
        // TODO Auto-generated constructor stub
        try {
            mActivity = (AppCompatActivity) myActivity;
            gLayoutInflater = mActivity.getLayoutInflater();
            mJsonParser = new MyJSONParser();
            mCommonSession = new CommonSession(mActivity);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public String unfollowCommunity(Followingbean bean) {

        String success_value = null;
         try {
            JSONObject jsonObject_parent = null;

            try {
                JSONObject json = new JSONObject();


                json.put("userId", mCommonSession.getLoggedUserID());
                json.put("CommunityId", bean.getCommunityID());



                    jsonObject_parent = mJsonParser.postData(URLs.UNFOLLOW_COMMUNITY,
                            json);

                if (jsonObject_parent == null) {
                    success_value="0";

                } else {
                    success_value = jsonObject_parent
                            .getString("success");

                    if (success_value.equals("1")) {
                        success_value="1";



                    } else {
                        success_value="0";

                    }
                }

            } catch (Exception exception) {
                success_value="0";
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
             success_value="0";
            e.printStackTrace();
        }
        return success_value;

    }


}