package com.app.collow.collowinterfaces;

import android.content.Context;
import android.view.View;

import com.app.collow.R;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.ConnectionDetector;

/**
 * Created by chinkal on 1/20/17.
 */

public abstract class MyOnClickListener implements  View.OnClickListener {
    Context context=null;
    public MyOnClickListener(Context context)
    {
        this.context=context;
    }

    public boolean isAvailableInternet(){
        ConnectionDetector connectionDetector=new ConnectionDetector(context);
        if(connectionDetector.isConnectingToInternet())
        {
            return  true;
        }

        return false;
    }


    public void showInternetNotfoundMessage()
    {
        CommonMethods.customToastMessage(this.context.getResources().getString(R.string.internet_connection_slow),this.context);
    }


}
