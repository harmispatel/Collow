package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 25/01/17.
 */

public class Activitiesbean implements Serializable {

    String userName=null;
    String time=null;

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String communityID=null;
    String communityName=null;
    String commentText=null;
    String type=null;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title=null;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


    String profilePic=null;
}
