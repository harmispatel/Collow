package com.app.collow.httprequests;


import com.app.collow.allenums.ScreensEnums;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.utils.CommonKeywords;

import org.json.JSONObject;

/**
 * Created by harmis on 8/11/16.
 */
public class GetPostParameterEachScreen {

    public static JSONObject getPostParametersAccordingIndex(int index, RequestParametersbean requestParametersbean) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (index == ScreensEnums.LOGIN.getScrenIndex()) {
                //recommend tab

                if (requestParametersbean.getIsSocial() != null) {
                    if (requestParametersbean.getIsSocial().equals(CommonKeywords.SOCIAL_YES)) {
                        jsonObject = getPostParametersAccordingIndex(ScreensEnums.REGISTER.getScrenIndex(), requestParametersbean);
                    } else {
                        jsonObject.put("isSocial", requestParametersbean.getIsSocial());
                        jsonObject.put("UserName", requestParametersbean.getUser_email());
                        jsonObject.put("PassWord", requestParametersbean.getUser_password());

                    }
                } else {
                    jsonObject.put("UserName", requestParametersbean.getUser_email());
                    jsonObject.put("PassWord", requestParametersbean.getUser_password());
                    jsonObject.put("isSocial", requestParametersbean.getIsSocial());

                }


            } else if (index == ScreensEnums.GETPRODILEPICBYEMIAL.getScrenIndex()) {
                //recommend tab

                jsonObject.put("UserName", requestParametersbean.getUser_email());


            } else if (index == ScreensEnums.REGISTER.getScrenIndex()) {
                //recommend tab

                jsonObject.put("user_email", requestParametersbean.getUser_email());
                jsonObject.put("user_password", requestParametersbean.getUser_password());
                jsonObject.put("first_name", requestParametersbean.getFirst_name());
                jsonObject.put("last_name", requestParametersbean.getLast_name());
                jsonObject.put("isSocial", requestParametersbean.getIsSocial());
                // jsonObject.put("device_token", requestParametersbean.getDevice_token());
                jsonObject.put("socialType", requestParametersbean.getSocialType());
                jsonObject.put("profile_pic", requestParametersbean.getProfile_pic());


            } else if (index == ScreensEnums.FORGOT_PASSWORD.getScrenIndex()) {
                //recommend tab

                jsonObject.put("Email", requestParametersbean.getUser_email());


            } else if (index == ScreensEnums.MESSAGES_INBOX.getScrenIndex()) {
                //recommend tab

                jsonObject.put("userId", requestParametersbean.getUserId());
                jsonObject.put("StartLimit", requestParametersbean.getStart_limit());


            } else if (index == ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex()) {
                //recommend tab

                jsonObject.put("userId", requestParametersbean.getUserId());
                jsonObject.put("StartLimit", requestParametersbean.getStart_limit());
                jsonObject.put("City", requestParametersbean.getCity());
                jsonObject.put("State", requestParametersbean.getState());
                jsonObject.put("type", requestParametersbean.getType());


            } else if (index == ScreensEnums.MY_ACTIVITIES.getScrenIndex()) {
                // my activities

                jsonObject.put("userId", requestParametersbean.getUserId());

                jsonObject.put("StartLimit", requestParametersbean.getStart_limit());
                jsonObject.put("isFollowing", requestParametersbean.getIsFromFollowing());
                jsonObject.put("CommunityId", requestParametersbean.getCommunityID());
                jsonObject.put("isHome", "0");


            } else if (index == ScreensEnums.MY_POST.getScrenIndex()) {
                // my posts

                jsonObject.put("userId", requestParametersbean.getUserId());
                jsonObject.put("StartLimit", requestParametersbean.getStart_limit());


            } else if (index == ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", requestParametersbean.getUserId());

                jsonObject.put("StartLimit", requestParametersbean.getStart_limit());
                jsonObject.put("isFollowing", requestParametersbean.getIsFromFollowing());
                jsonObject.put("CommunityId", requestParametersbean.getCommunityID());
                jsonObject.put("isHome", "1");


            } else if (index == ScreensEnums.GET_COMMUNTIES.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getStart_limit()));


            } else if (index == ScreensEnums.SAVE_MORE_INFORMATION.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("FirstName", String.valueOf(requestParametersbean.getFirst_name()));
                jsonObject.put("LastName", String.valueOf(requestParametersbean.getLast_name()));
                jsonObject.put("UserEmail", String.valueOf(requestParametersbean.getUser_email()));
                jsonObject.put("State", String.valueOf(requestParametersbean.getState()));
                jsonObject.put("StateText", String.valueOf(requestParametersbean.getStateText()));

                jsonObject.put("Dob", String.valueOf(requestParametersbean.getDob()));
                jsonObject.put("Interest", String.valueOf(requestParametersbean.getInterest()));
                jsonObject.put("InterestText", String.valueOf(requestParametersbean.getInterestText()));


                jsonObject.put("City", String.valueOf(requestParametersbean.getCity()));
                jsonObject.put("Community", String.valueOf(requestParametersbean.getCommunity()));
                jsonObject.put("CommunityText", String.valueOf(requestParametersbean.getCommunityText()));


            } else if (index == ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("IsCommuntiySearchByName", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getIsCommuntiySearchByName()));
                jsonObject.put("Currentlat", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getCurrentlat()));
                jsonObject.put("Currentlong", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getCurrentlong()));
                jsonObject.put("SearchText", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getSearchText()));
                jsonObject.put("CityID", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getCityID()));
                jsonObject.put("CityText", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getCityText()));
                jsonObject.put("StateID", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getStateID()));
                jsonObject.put("StateText", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getStateText()));
                jsonObject.put("CommunityTypeID", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getCommunityTypeID()));
                jsonObject.put("CommunityType", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getCommunityTypeText()));
                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getStart_limit()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getUser_id()));
                jsonObject.put("PageToken", String.valueOf(requestParametersbean.getSearchbeanPassPostData().getPageToken()));


            } else if (index == ScreensEnums.GET_COMMUNITY_DETAILS.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));


            } else if (index == ScreensEnums.FOLLOW_COMMUNITY.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userName", String.valueOf(requestParametersbean.getUser_email()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityName", String.valueOf(requestParametersbean.getCommunityText()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));


            }else if (index == ScreensEnums.UNFOLLOW_COMMUNITY.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));


            } else if (index == ScreensEnums.CLAIM_COMMUNITY.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUser_email()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityName", String.valueOf(requestParametersbean.getCommunityText()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("TeamsId", String.valueOf(requestParametersbean.getTeamsMgmtCommaSeperatedIDs()));


            } else if (index == ScreensEnums.CREATE_CLASSIFIED.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("Title", String.valueOf(requestParametersbean.getTitle()));
                jsonObject.put("Description", String.valueOf(requestParametersbean.getDescripton()));
                jsonObject.put("Price", String.valueOf(requestParametersbean.getPrice()));
                jsonObject.put("Category", String.valueOf(requestParametersbean.getCategory()));
                jsonObject.put("Type", String.valueOf(requestParametersbean.getType()));
                jsonObject.put("ImgCount", String.valueOf(requestParametersbean.getImgCount()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));


            } else if (index == ScreensEnums.CREATE_GALLERY.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("title", String.valueOf(requestParametersbean.getTitle()));
                jsonObject.put("ImgCount", String.valueOf(requestParametersbean.getImgCount()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("albumName", String.valueOf(requestParametersbean.getAlbumName()));


            } else if (index == ScreensEnums.GALLERY.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getStart_limit()));


            } else if (index == ScreensEnums.CLASSIFIED.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getStart_limit()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));


            } else if (index == ScreensEnums.CONTACT_CLASSIFIED.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("Message", String.valueOf(requestParametersbean.getMessage()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("OwnerId", String.valueOf(requestParametersbean.getOwnerId()));
                jsonObject.put("MenuType", String.valueOf(requestParametersbean.getMenuType()));
                jsonObject.put("MenuId", String.valueOf(requestParametersbean.getMenuId()));


            } else if (index == ScreensEnums.IMINTERESTED_CLASSIFIED.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("userName", String.valueOf(requestParametersbean.getUser_email()));
                jsonObject.put("OwnerId", String.valueOf(requestParametersbean.getOwnerId()));
                jsonObject.put("MenuType", String.valueOf(requestParametersbean.getMenuType()));
                jsonObject.put("MenuId", String.valueOf(requestParametersbean.getMenuId()));
                jsonObject.put("PropertyTitle", String.valueOf(requestParametersbean.getPropertyTitle()));


            } else if (index == ScreensEnums.POLLS.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getStart_limit()));


            } else if (index == ScreensEnums.NEWS.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));


            } else if (index == ScreensEnums.CREATE_NEWS.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("Title", String.valueOf(requestParametersbean.getTitle()));
                jsonObject.put("Description", String.valueOf(requestParametersbean.getDescripton()));
                jsonObject.put("ImgCount", String.valueOf(requestParametersbean.getImgCount()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("mimeType", String.valueOf("MIME"));


            }
            //{ "ActivityId":"20","userId":"2   33","liketype":"news","like":"1"}

            else if (index == ScreensEnums.LIKES.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("ActivityId", String.valueOf(requestParametersbean.getActivityId()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("liketype", String.valueOf(requestParametersbean.getLiketype()));
                jsonObject.put("like", String.valueOf(requestParametersbean.getLike()));


            } else if (index == ScreensEnums.SET_AS_HOME.getScrenIndex() || index == ScreensEnums.ACFOLLOWERS.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getStart_limit()));


            } else if (index == ScreensEnums.FILTER_COMMUNITY.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("City", String.valueOf(requestParametersbean.getCity()));
                jsonObject.put("State", String.valueOf(requestParametersbean.getState()));
                jsonObject.put("type", String.valueOf(requestParametersbean.getType()));
                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getStart_limit()));
                jsonObject.put("SearchText", String.valueOf(requestParametersbean.getSearchText()));
                jsonObject.put("ZipCode", String.valueOf(requestParametersbean.getZipCode()));


                // {"IsCommuntiySearchByName":"1","Currentlat":"23.72","Currentlong":"72.25","SearchText":"communtiysearch","CityID":"1",
                // "CityText":"","StateID":"id","StateText":"text","CommunityTypeID":"1","CommunityTypeText":"text","StartLimit":"0"}

            } else if (index == ScreensEnums.SEND_COMMENT.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("ActivityId", String.valueOf(requestParametersbean.getActivityId()));
                jsonObject.put("commentType", String.valueOf(requestParametersbean.getType()));
                jsonObject.put("commentText", String.valueOf(requestParametersbean.getPostText()));


                //CommentAttach

            } else if (index == ScreensEnums.COMMENTS_LISTING_MAIN.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("ActivityId", String.valueOf(requestParametersbean.getActivityId()));
                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getStart_limit()));


                //CommentAttach

            } else if (index == ScreensEnums.COMMENT_LIKE_DISLIKE.getScrenIndex()) {
                // communities feeds activities get data for communties feeds

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("ActivityId", String.valueOf(requestParametersbean.getActivityId()));
                jsonObject.put("CommentId", String.valueOf(requestParametersbean.getPostedCommentID()));


                //CommentAttach

            } else if (index == ScreensEnums.APPROVE_OR_REJECT_FOLLOWERS_REQUEST.getScrenIndex()) {

                jsonObject.put("userId", String.valueOf(requestParametersbean.getFollowersuserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("ApproveType", String.valueOf(requestParametersbean.getApproveOrReject()));


            } else if (index == ScreensEnums.MGMT_TEAM_LISTING.getScrenIndex()) {

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getStart_limit()));


            } else if (index == ScreensEnums.VIEW.getScrenIndex()) {

                //details id either news
                jsonObject.put("Typeid", String.valueOf(requestParametersbean.getActivityId()));
                jsonObject.put("Type", String.valueOf(requestParametersbean.getType()));
                jsonObject.put("isFromFeed", String.valueOf(requestParametersbean.getIsFromFeed()));


            }

            //Event

            else if (index == ScreensEnums.ACNEWEVENT.getScrenIndex()) {

                //details id either news
                jsonObject.put("Title", String.valueOf(requestParametersbean.getTitle()));
                jsonObject.put("Description", String.valueOf(requestParametersbean.getDescripton()));
                jsonObject.put("Date", String.valueOf(requestParametersbean.getDate()));
                jsonObject.put("StartTime", String.valueOf(requestParametersbean.getStartTime()));
                jsonObject.put("EndTime", String.valueOf(requestParametersbean.getEndTime()));
                jsonObject.put("EventLocation", String.valueOf(requestParametersbean.getLocation()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("ImgCount", String.valueOf(requestParametersbean.getImgCount()));
                jsonObject.put("EventType", String.valueOf(requestParametersbean.getTypeID()));
                jsonObject.put("eventTimeMode", String.valueOf(requestParametersbean.getEvent_time_mode()));
                jsonObject.put("EventId", String.valueOf(requestParametersbean.getEventID()));
                jsonObject.put("ActivityId", String.valueOf(requestParametersbean.getActivityId()));




                if (requestParametersbean.getJsonArray_deleted_files_url() == null) {

                } else {
                    jsonObject.put("deletedFilesUrls", requestParametersbean.getJsonArray_deleted_files_url());

                }


            } else if (index == ScreensEnums.ACGETEVENTS.getScrenIndex()) {

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("StartLimit", String.valueOf(requestParametersbean.getStart_limit()));

                jsonObject.put("Typeid", String.valueOf(requestParametersbean.getTypeID()));
                jsonObject.put("StartDate", String.valueOf(requestParametersbean.getStartDate()));
                jsonObject.put("EndDate", String.valueOf(requestParametersbean.getEndDate()));
                jsonObject.put("SearchText", String.valueOf(requestParametersbean.getSearchText()));

            } else if (index == ScreensEnums.DELETEEVENT.getScrenIndex()) {

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("EventId", String.valueOf(requestParametersbean.getEventID()));


            } else if (index == ScreensEnums.CREATE_POLLS.getScrenIndex()) {

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("PollTitle", String.valueOf(requestParametersbean.getPollTitle()));
                jsonObject.put("PollOption", requestParametersbean.getPollsOptions());


            } else if (index == ScreensEnums.DELETE_GALLERY.getScrenIndex()) {

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("GalleryId", String.valueOf(requestParametersbean.getGalleryID()));


            } else if (index == ScreensEnums.FALG_AS_GALLERY_INAPPROPRIATE.getScrenIndex()) {

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("EventId", String.valueOf(requestParametersbean.getEventID()));


            } else if (index == ScreensEnums.SAVE_POLL.getScrenIndex()) {

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("CommunityId", String.valueOf(requestParametersbean.getCommunityID()));
                jsonObject.put("PollID", String.valueOf(requestParametersbean.getPollID()));


            } else if (index == ScreensEnums.GIVE_VOTE.getScrenIndex()) {

                jsonObject.put("userId", String.valueOf(requestParametersbean.getUserId()));
                jsonObject.put("pollId", String.valueOf(requestParametersbean.getPollID()));
                jsonObject.put("PollOptionId", String.valueOf(requestParametersbean.getPollOptionID()));


            }


        } catch (Exception e) {


        }
        return jsonObject;
    }
}
