package com.app.collow.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.ConnectionDetector;

/**
 * Created by Harmis on 01/02/17.
 */

public class InternetLostActivity extends Activity {
    int i=0;
    BaseTextview  baseTextview_retry=null;
    RetryParameterbean retryParameterbean=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.internet_lost_dialog);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().setAttributes(lp);
       getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        baseTextview_retry= (BaseTextview) findViewById(R.id.mybuttonview_retry);
        retryParameterbean=BaseException.retryParameterbean;
        if(retryParameterbean!=null)
        {

        }


        baseTextview_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectionDetector connectionDetector = new ConnectionDetector(retryParameterbean.getActivity());
                if (connectionDetector.isConnectingToInternet()) {
                    finish();
                    if (retryParameterbean.getActivity() != null) {
                        retryParameterbean.getActivity().finish();
                        Bundle bundle = null;
                        if (retryParameterbean.getBundle() != null) {
                            bundle = retryParameterbean.getBundle();
                        } else {
                            bundle = null;
                        }
                        CommonMethods.startNewActivityWithCloseCurrentActivity(retryParameterbean.getActivity(), retryParameterbean.getCurrentActivityClass(), bundle);

                    }
                } else {
                    i++;


                }

            }
        });

    }


}



