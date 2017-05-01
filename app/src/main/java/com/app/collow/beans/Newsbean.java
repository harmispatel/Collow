package com.app.collow.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by harmis on 1/2/17.
 */

public class Newsbean  implements Serializable {
    String news_userprofilepic=null;
    String news_username=null;
    String news_time=null;
    String news_title=null;
    String news_description=null;
    String message=null;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    int position=0;



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
    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    boolean isLiked=false;

    public String getActivityID() {
        return activityID;
    }

    public void setActivityID(String activityID) {
        this.activityID = activityID;
    }

    String activityID=null;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    String communityName=null;

    public ArrayList<String> getStringArrayList_fileURLs() {
        return stringArrayList_fileURLs;
    }

    public void setStringArrayList_fileURLs(ArrayList<String> stringArrayList_fileURLs) {
        this.stringArrayList_fileURLs = stringArrayList_fileURLs;
    }

    ArrayList<String> stringArrayList_fileURLs=null;

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    String news_id=null;

    public String getNews_pic() {
        return news_pic;
    }

    public void setNews_pic(String news_pic) {
        this.news_pic = news_pic;
    }

    String news_pic=null;

    public Newsbean() {
    }

    public String getNews_userprofilepic() {
        return news_userprofilepic;
    }

    public void setNews_userprofilepic(String news_userprofilepic) {
        this.news_userprofilepic = news_userprofilepic;
    }

    public String getNews_username() {
        return news_username;
    }

    public void setNews_username(String news_username) {
        this.news_username = news_username;
    }

    public String getNews_time() {
        return news_time;
    }

    public void setNews_time(String news_time) {
        this.news_time = news_time;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }
    public SocialOptionbean getSocialOptionbean() {
        return socialOptionbean;
    }

    public void setSocialOptionbean(SocialOptionbean socialOptionbean) {
        this.socialOptionbean = socialOptionbean;
    }

    SocialOptionbean socialOptionbean=null;
}
