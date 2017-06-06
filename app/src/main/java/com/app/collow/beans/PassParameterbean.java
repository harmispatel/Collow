package com.app.collow.beans;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.setupUI.SetupViewInterface;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 29/10/16.
 */
public class PassParameterbean implements Serializable {
    boolean isDataNeedsFromDatabase = false;

    String requestURL = null;
    int requestMethod = HTTPRequestMethodEnums.POST.getHTTPRequestMethodInex();
    boolean isConnectionTimeOutException = false;
    Activity activity = null;
    JSONObject jsonObjectRequestParams = null;
    int redirectScreenIndex = 0;
    int screenIndex = 0;
    boolean isFromAddToCart = false;
    Class className = null;
    boolean forImageUploadingCustomKeyword = false;
    String forImageUploadingCustomKeywordName = null;
    ArrayList<Imagebean> bitmapArrayList_mutliple_image = new ArrayList<>();
    ArrayList<Filebean> fileArrayList_uploading = new ArrayList<>();
    boolean isNeedToFirstTakeFacebookProfilePic = false;
    boolean noNeedToDisplayLoadingDialog = false;
    Bundle bundle = null;
    SetupViewInterface setupViewInterface = null;
    View currentView = null;
    Context context = null;
    Bitmap bitmap = null;

    public PassParameterbean(SetupViewInterface setupViewInterface, Activity activity, Context context, String requestURL, JSONObject jsonObjectRequestParams, int screenIndex, Class className) {
        this.setupViewInterface = setupViewInterface;
        this.activity = activity;
        this.context = context;
        this.requestURL = requestURL;
        this.jsonObjectRequestParams = jsonObjectRequestParams;
        this.screenIndex = screenIndex;
        this.className = className;
    }

    public boolean isForImageUploadingCustomKeyword() {
        return forImageUploadingCustomKeyword;
    }

    public void setForImageUploadingCustomKeyword(boolean forImageUploadingCustomKeyword) {
        this.forImageUploadingCustomKeyword = forImageUploadingCustomKeyword;
    }

    public String getForImageUploadingCustomKeywordName() {
        return forImageUploadingCustomKeywordName;
    }

    public void setForImageUploadingCustomKeywordName(String forImageUploadingCustomKeywordName) {
        this.forImageUploadingCustomKeywordName = forImageUploadingCustomKeywordName;
    }

    public ArrayList<Imagebean> getBitmapArrayList_mutliple_image() {
        return bitmapArrayList_mutliple_image;
    }

    public void setBitmapArrayList_mutliple_image(ArrayList<Imagebean> bitmapArrayList_mutliple_image) {
        this.bitmapArrayList_mutliple_image = bitmapArrayList_mutliple_image;
    }

    public ArrayList<Filebean> getFileArrayList_uploading() {
        return fileArrayList_uploading;
    }

    public void setFileArrayList_uploading(ArrayList<Filebean> fileArrayList_uploading) {
        this.fileArrayList_uploading = fileArrayList_uploading;
    }

    public boolean isNeedToFirstTakeFacebookProfilePic() {
        return isNeedToFirstTakeFacebookProfilePic;
    }

    public void setNeedToFirstTakeFacebookProfilePic(boolean needToFirstTakeFacebookProfilePic) {
        isNeedToFirstTakeFacebookProfilePic = needToFirstTakeFacebookProfilePic;
    }

    public boolean isNoNeedToDisplayLoadingDialog() {
        return noNeedToDisplayLoadingDialog;
    }

    public void setNoNeedToDisplayLoadingDialog(boolean noNeedToDisplayLoadingDialog) {
        this.noNeedToDisplayLoadingDialog = noNeedToDisplayLoadingDialog;
    }

    public boolean isDataNeedsFromDatabase() {
        return isDataNeedsFromDatabase;
    }

    public void setDataNeedsFromDatabase(boolean dataNeedsFromDatabase) {
        isDataNeedsFromDatabase = dataNeedsFromDatabase;
    }

    public boolean isFromAddToCart() {
        return isFromAddToCart;
    }

    public void setFromAddToCart(boolean fromAddToCart) {
        isFromAddToCart = fromAddToCart;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    public SetupViewInterface getSetupViewInterface() {
        return setupViewInterface;
    }

    public void setSetupViewInterface(SetupViewInterface setupViewInterface) {
        this.setupViewInterface = setupViewInterface;
    }

    public View getCurrentView() {
        return currentView;
    }

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Class getClassName() {
        return className;
    }

    public void setClassName(Class className) {
        this.className = className;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getScreenIndex() {
        return screenIndex;
    }

    public void setScreenIndex(int screenIndex) {
        this.screenIndex = screenIndex;
    }


    public int getRedirectScreenIndex() {
        return redirectScreenIndex;
    }

    public void setRedirectScreenIndex(int redirectScreenIndex) {
        this.redirectScreenIndex = redirectScreenIndex;
    }


    public JSONObject getJsonObjectRequestParams() {
        return jsonObjectRequestParams;
    }

    public void setJsonObjectRequestParams(JSONObject jsonObjectRequestParams) {
        this.jsonObjectRequestParams = jsonObjectRequestParams;
    }


    public boolean isConnectionTimeOutException() {
        return isConnectionTimeOutException;
    }

    public void setConnectionTimeOutException(boolean connectionTimeOutException) {
        isConnectionTimeOutException = connectionTimeOutException;
    }

    public int getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(int requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestURL() {
        return requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }


}
