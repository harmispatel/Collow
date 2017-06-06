package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 29/10/16.
 */
public class Responcebean implements Serializable {

    int responseCode = 0;
    String responseMessage = null;
    String responceContent=null;

    public String getSearchedText() {
        return searchedText;
    }

    public void setSearchedText(String searchedText) {
        this.searchedText = searchedText;
    }

    String searchedText=null;

    public int getScreenIndex() {
        return screenIndex;
    }

    public void setScreenIndex(int screenIndex) {
        this.screenIndex = screenIndex;
    }

    int screenIndex=0;



    public void setException(Exception exception) {
        this.exception = exception;
    }

    Exception exception=null;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    String errorMessage=null;

    public boolean isServiceSuccess() {
        return isServiceSuccess;
    }

    public void setServiceSuccess(boolean serviceSuccess) {
        isServiceSuccess = serviceSuccess;
    }

    boolean isServiceSuccess=false;

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }


    public String getResponceContent() {
        return responceContent;
    }

    public void setResponceContent(String responceContent) {
        this.responceContent = responceContent;
    }





}
