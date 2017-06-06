package com.app.collow.beans;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import java.io.Serializable;

/**
 * Created by chinkal on 1/20/17.
 */

public class RetryParameterbean implements Serializable {

    Activity activity=null;
    Context context=null;
    Bundle bundle=null;

    public RetryParameterbean(Activity activity,Context context,Bundle bundle,Class currentActivityClass)
    {
        this.activity=activity;
        this.bundle=bundle;
        this.context=context;
        this.currentActivityClass=currentActivityClass;
    }




    public Class getCurrentActivityClass() {
        return currentActivityClass;
    }

    public void setCurrentActivityClass(Class currentActivityClass) {
        this.currentActivityClass = currentActivityClass;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    Class currentActivityClass=null;

}
