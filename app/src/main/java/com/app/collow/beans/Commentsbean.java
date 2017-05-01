package com.app.collow.beans;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Harmis on 13/02/17.
 */

public class Commentsbean extends   Basebean implements Serializable {
String userProfilePic=null;
    String name=null;

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    String commentID=null;


    public int getLikeDisLikeCount() {
        return likeDisLikeCount;
    }

    public void setLikeDisLikeCount(int likeDisLikeCount) {
        this.likeDisLikeCount = likeDisLikeCount;
    }

    int likeDisLikeCount=0;


    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getUploadedFileURL() {
        return uploadedFileURL;
    }

    public void setUploadedFileURL(String uploadedFileURL) {
        this.uploadedFileURL = uploadedFileURL;
    }




    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserProfilePic() {
        return userProfilePic;
    }

    public void setUserProfilePic(String userProfilePic) {
        this.userProfilePic = userProfilePic;
    }

    String postContent=null;
    String uploadedFileURL=null;

    public String getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(String commentedDate) {
        this.commentedDate = commentedDate;
    }

    String commentedDate=null;

    public int getPostedType() {
        return postedType;
    }

    public void setPostedType(int postedType) {
        this.postedType = postedType;
    }

    int postedType=0;




    public boolean isAlreadyLikedbyMe() {
        return isAlreadyLikedbyMe;
    }

    public void setAlreadyLikedbyMe(boolean alreadyLikedbyMe) {
        isAlreadyLikedbyMe = alreadyLikedbyMe;
    }

    boolean isAlreadyLikedbyMe=false;

    public boolean isFromLocal() {
        return isFromLocal;
    }

    public void setFromLocal(boolean fromLocal) {
        isFromLocal = fromLocal;
    }

    boolean isFromLocal=false;

    public boolean isLoggedUserCommentShouldBeRightSide() {
        return isLoggedUserCommentShouldBeRightSide;
    }

    public void setLoggedUserCommentShouldBeRightSide(boolean loggedUserCommentShouldBeRightSide) {
        isLoggedUserCommentShouldBeRightSide = loggedUserCommentShouldBeRightSide;
    }

    boolean isLoggedUserCommentShouldBeRightSide=false;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    Bitmap bitmap=null;


    public ArrayList<Filebean> getFileArrayList_uploading() {
        return fileArrayList_uploading;
    }

    public void setFileArrayList_uploading(ArrayList<Filebean> fileArrayList_uploading) {
        this.fileArrayList_uploading = fileArrayList_uploading;
    }

    ArrayList<Filebean> fileArrayList_uploading=null;
}
