package com.app.collow.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.app.collow.R;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonSession;

public class FeedsUserProfileActivity extends AppCompatActivity {
    public static FeedsUserProfileActivity feedsUserProfileActivity=null;
    CommonSession commonSession = null;

    String userid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeds_user_profile_activity);
        commonSession = new CommonSession(FeedsUserProfileActivity.this);
        feedsUserProfileActivity=this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            userid = bundle.getString(BundleCommonKeywords.KEY_ID);
            Log.e("ad","userid=="+userid);

        }
        else
        {  Log.e("ad","FeedsUserProfileActivity---userid may null");
            finish();
        }
    }
}
