package com.app.collow.beans;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.app.collow.utils.CommonKeywords;

import org.json.JSONArray;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harmis on 8/1/17.
 */

public class RequestParametersbean implements Serializable {

    //Login

    private   String user_email = null;
    private   String user_password = "";
    private   String device_token = null;

    public int getEvent_time_mode() {
        return event_time_mode;
    }

    public void setEvent_time_mode(int event_time_mode) {
        this.event_time_mode = event_time_mode;
    }

    private   int event_time_mode= CommonKeywords.EVENT_TIME_MODE_ALL_DAYS;

    public JSONArray getJsonArray_deleted_files_url() {
        return jsonArray_deleted_files_url;
    }

    public void setJsonArray_deleted_files_url(JSONArray jsonArray_deleted_files_url) {
        this.jsonArray_deleted_files_url = jsonArray_deleted_files_url;
    }

    private   JSONArray jsonArray_deleted_files_url=null;

    public ArrayList<Imagebean> getBitmapArrayList_mutliple_image() {
        return bitmapArrayList_mutliple_image;
    }

    public void setBitmapArrayList_mutliple_image(ArrayList<Imagebean> bitmapArrayList_mutliple_image) {
        this.bitmapArrayList_mutliple_image = bitmapArrayList_mutliple_image;
    }

    ArrayList<Imagebean> bitmapArrayList_mutliple_image=new ArrayList<>();


    //Register
    private String first_name = "";
    private String last_name = "";
    private   String profile_pic = null;
    private  String isSocial = null;
    private   int start_limit = 0;
    private  String socialType = null;
    private   String id = null;



    public JSONArray getPollsOptions() {
        return pollsOptions;
    }

    public void setPollsOptions(JSONArray pollsOptions) {
        this.pollsOptions = pollsOptions;
    }

    private  JSONArray pollsOptions =null;

    public String getPollID() {
        return pollID;
    }

    public void setPollID(String pollID) {
        this.pollID = pollID;
    }

    private  String pollID = "";

    public String getPollOptionID() {
        return pollOptionID;
    }

    public void setPollOptionID(String pollOptionID) {
        this.pollOptionID = pollOptionID;
    }

    private  String pollOptionID = "";

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    private  String searchText = "";


    public ArrayList<Filebean> getFileArrayList_uploading() {
        return fileArrayList_uploading;
    }

    public void setFileArrayList_uploading(ArrayList<Filebean> fileArrayList_uploading) {
        this.fileArrayList_uploading = fileArrayList_uploading;
    }

    ArrayList<Filebean> fileArrayList_uploading=new ArrayList<>();


    //Create classified
    private   String title = null;
    private  String descripton = null;
    private   String mimeTypeWithComma = null;
    private   String date = "";
    private   String startTime = "";

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private  String endTime = "";
    private   String location = "";





    public String getImgCount() {
        return imgCount;
    }

    public void setImgCount(String imgCount) {
        this.imgCount = imgCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescripton() {
        return descripton;
    }

    public void setDescripton(String descripton) {
        this.descripton = descripton;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private  String price = null;
    private   String category = null;
    private   String type = "";
    private   String imgCount = null;

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    private   String typeID = "";

    public String getTeamsMgmtCommaSeperatedIDs() {
        return teamsMgmtCommaSeperatedIDs;
    }

    public void setTeamsMgmtCommaSeperatedIDs(String teamsMgmtCommaSeperatedIDs) {
        this.teamsMgmtCommaSeperatedIDs = teamsMgmtCommaSeperatedIDs;
    }

    private   String teamsMgmtCommaSeperatedIDs = "";

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    private    String postText = null;

    private  String activityId = null;

    public String getLiketype() {
        return liketype;
    }

    public void setLiketype(String liketype) {
        this.liketype = liketype;
    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    private  String liketype = null;
    private  String like = null;




    public String getMenuType() {
        return menuType;
    }

    public void setMenuType(String menuType) {
        this.menuType = menuType;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    private    String menuType = null;
    private    String menuId = null;
    private    String ownerId = null;

    public CommunityActivitiesFeedbean getCommunityActivitiesFeedbean() {
        return communityActivitiesFeedbean;
    }

    public void setCommunityActivitiesFeedbean(CommunityActivitiesFeedbean communityActivitiesFeedbean) {
        this.communityActivitiesFeedbean = communityActivitiesFeedbean;
    }

    CommunityActivitiesFeedbean communityActivitiesFeedbean=null;

    public String getPropertyTitle() {
        return propertyTitle;
    }

    public void setPropertyTitle(String propertyTitle) {
        this.propertyTitle = propertyTitle;
    }

    private String propertyTitle = null;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private  String message = null;


    //Save More information
    private  String userId = null;

    public String getPollTitle() {
        return pollTitle;
    }

    public void setPollTitle(String pollTitle) {
        this.pollTitle = pollTitle;
    }

    private  String pollTitle = "";

    public String getIsFromFeed() {
        return isFromFeed;
    }

    public void setIsFromFeed(String isFromFeed) {
        this.isFromFeed = isFromFeed;
    }

    private   String isFromFeed = "0";

    public String getFollowersuserId() {
        return followersuserId;
    }

    public void setFollowersuserId(String followersuserId) {
        this.followersuserId = followersuserId;
    }

    private   String followersuserId = null;


    public String getPostedCommentID() {
        return postedCommentID;
    }

    public void setPostedCommentID(String postedCommentID) {
        this.postedCommentID = postedCommentID;
    }

    private String postedCommentID = null;

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    String eventID = "";

    public String getGalleryID() {
        return galleryID;
    }

    public void setGalleryID(String galleryID) {
        this.galleryID = galleryID;
    }

    String galleryID = "";

    public String getCommunityID() {
        return communityID;
    }

    public void setCommunityID(String communityID) {
        this.communityID = communityID;
    }

    public String getInterestText() {
        return interestText;
    }

    public void setInterestText(String interestText) {
        this.interestText = interestText;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getStateText() {
        return stateText;
    }

    public void setStateText(String stateText) {
        this.stateText = stateText;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

   private String state = "";
    private  String stateText = "";
    private  String dob = "";
    private String community = "";
    private  String interest = "";
    private String interestText = "";
    private String communityID="";
    private String startDate = "";

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    private String endDate = "";

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    private String albumName = "";

    public int getApproveOrReject() {
        return approveOrReject;
    }

    public void setApproveOrReject(int approveOrReject) {
        this.approveOrReject = approveOrReject;
    }

    private int approveOrReject =0;

    public String getIsFromFollowing() {
        return isFromFollowing;
    }

    public void setIsFromFollowing(String isFromFollowing) {
        this.isFromFollowing = isFromFollowing;
    }

    private  String isFromFollowing = "0";

    //Search Community

    public Searchbean getSearchbeanPassPostData() {
        return searchbeanPassPostData;
    }

    public void setSearchbeanPassPostData(Searchbean searchbeanPassPostData) {
        this.searchbeanPassPostData = searchbeanPassPostData;
    }

    private  Searchbean  searchbeanPassPostData=null;






    public String getCommunityText() {
        return CommunityText;
    }

    public void setCommunityText(String communityText) {
        CommunityText = communityText;
    }

    private  String CommunityText = null;

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    private  String City = "";

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    private  String zipCode = "";



    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public int getStart_limit() {
        return start_limit;
    }

    public void setStart_limit(int start_limit) {
        this.start_limit = start_limit;
    }


    public String getSocialType() {
        return socialType;
    }

    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getIsSocial() {
        return isSocial;
    }

    public void setIsSocial(String isSocial) {
        this.isSocial = isSocial;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_password() {
        return user_password;
    }

    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }


}