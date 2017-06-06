package com.app.collow.beans;

/**
 * Created by harmis on 12/1/17.
 */

public abstract class Basebean {
    String message=null;

    public boolean isServiceSuccess() {
        return isServiceSuccess;
    }

    public void setServiceSuccess(boolean serviceSuccess) {
        isServiceSuccess = serviceSuccess;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    boolean isServiceSuccess=false;


    public String getLikeOrDislikeFlag() {
        return likeOrDislikeFlag;
    }

    public void setLikeOrDislikeFlag(String likeOrDislikeFlag) {
        this.likeOrDislikeFlag = likeOrDislikeFlag;
    }

    String likeOrDislikeFlag="0";



    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    int position=0;

    public SocialOptionbean getSocialOptionbean() {
        return socialOptionbean;
    }

    public void setSocialOptionbean(SocialOptionbean socialOptionbean) {
        this.socialOptionbean = socialOptionbean;
    }

   private SocialOptionbean socialOptionbean=null;

}
