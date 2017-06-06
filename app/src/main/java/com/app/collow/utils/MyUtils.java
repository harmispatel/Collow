package com.app.collow.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.activities.ACAdminListingActivity;
import com.app.collow.activities.ACFollowerAwaitingManageActivity;
import com.app.collow.activities.ACListEventsMainActivity;
import com.app.collow.activities.BaseActivity;
import com.app.collow.activities.ChatMainActivity;
import com.app.collow.activities.ClassifiedMainActivity;
import com.app.collow.activities.CommentListingActivity;
import com.app.collow.activities.CommunityActivitiesFeedActivitiy;
import com.app.collow.activities.CommunityInformationActivity;
import com.app.collow.activities.CommunityMenuActivity;
import com.app.collow.activities.CommunitySearchByNameActivity;
import com.app.collow.activities.EditProfileActivity;
import com.app.collow.activities.EventDetailActivity;
import com.app.collow.activities.FeatureMainActivity;
import com.app.collow.activities.FollowingActivity;
import com.app.collow.activities.FormsAndDocsMainActivity;
import com.app.collow.activities.GalleryMainActivity;
import com.app.collow.activities.NewsDetailActivity;
import com.app.collow.activities.NewsMainActivity;
import com.app.collow.activities.PollsMainActivity;
import com.app.collow.activities.UserEventMainActivity;
import com.app.collow.adapters.FollowingAdapter;
import com.app.collow.allenums.CommunityInformationScreenEnum;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.CommunityActivitiesFeedbean;
import com.app.collow.beans.Eventbean;
import com.app.collow.beans.Followingbean;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.Searchbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Harmis on 03/02/17.
 */

public class MyUtils {

    //Community comments activity
    public static void openCommentswActivity(Activity activity, int indexFromWhichScreen, String activityID, int position, String categoryType, String title) {
        if (CommentListingActivity.commentListingActivity != null) {
            CommentListingActivity.commentListingActivity.finish();
        }
        Intent intent = new Intent(activity, CommentListingActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString(BundleCommonKeywords.KEY_ACTIVITY_ID, activityID);
        bundle.putString(BundleCommonKeywords.KEY_FEED_CATEGORY, categoryType);
        bundle.putString(BundleCommonKeywords.KEY_ACTIVITY_TITLE, title);
        bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, indexFromWhichScreen);
        bundle.putInt(BundleCommonKeywords.KEY_SELECTED_ITEM_POSITION, position);

        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);


    }


    //Community menu will open by this
    public static void openCommunityMenu(Activity activity, String communityID, String communtiyText, CommunityAccessbean communityAccessbean) {
        if (CommunityMenuActivity.communityMenuActivity != null) {
            CommunityMenuActivity.communityMenuActivity.finish();
        }


        Intent intent = new Intent(activity, CommunityMenuActivity.class);
        Bundle bundle = new Bundle();
        if (communityAccessbean == null) {
            communityAccessbean = new CommunityAccessbean();
        }
        bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);
        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, communtiyText);

        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);


    }
    //////
    public static void openCommunityMenu(Activity activity, String communityID, String communtiyText, CommunityAccessbean communityAccessbean,boolean isFromActivity) {
        if (CommunityMenuActivity.communityMenuActivity != null) {
            CommunityMenuActivity.communityMenuActivity.finish();
        }
        Intent intent = new Intent(activity, CommunityMenuActivity.class);
        Bundle bundle = new Bundle();
        if (communityAccessbean == null) {
            communityAccessbean = new CommunityAccessbean();
        }
        bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);
        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, communtiyText);
        bundle.putBoolean(BundleCommonKeywords.KEY_IS_ACTIVITY,isFromActivity);

        intent.putExtras(bundle);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);


    }


    //Community feed activity will open by this
    public static void openCommunityFeedActivity(Activity activity, int index, String communityID, String communtiyText, CommunityAccessbean communityAccessbean) {
        if (CommunityActivitiesFeedActivitiy.communityActivitiesFeedActivitiy != null) {
            CommunityActivitiesFeedActivitiy.communityActivitiesFeedActivitiy.finish();
        }
        Intent intent = new Intent(activity, CommunityActivitiesFeedActivitiy.class);
        Bundle bundle = new Bundle();

        Log.e("ad","communityID11="+communityID);
        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, communtiyText);
        bundle.putInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE, index);


        if (communityAccessbean == null) {
            communityAccessbean = new CommunityAccessbean();
        }
        bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);


        intent.putExtras(bundle);
        activity.startActivity(intent);


    }

    //Community menu handler

    public static void menuItemOpenByItsName(Activity activity, String title, int index, RequestParametersbean requestParametersbean, CommunityAccessbean communityAccessbean) {


        if (title.equals(activity.getResources().getString(R.string.feed))) {
            MyUtils.openCommunityFeedActivity(activity, ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex(), requestParametersbean.getCommunityID(), requestParametersbean.getCommunityText(), communityAccessbean);


        } else if (title.equals(activity.getResources().getString(R.string.admins))) {
            if (ACAdminListingActivity.acAdminListingActivity != null) {
                ACAdminListingActivity.acAdminListingActivity.finish();
            }


            Intent intent = new Intent(activity, ACAdminListingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
            //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
            }
            else {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            }
            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

            intent.putExtras(bundle);
            activity.startActivity(intent);


        } else if (title.equals(activity.getResources().getString(R.string.info))) {
            if (CommunityInformationActivity.CommunityInformationActivity != null) {
                CommunityInformationActivity.CommunityInformationActivity.finish();
            }
            activity.finish();


            Intent intent = new Intent(activity, CommunityInformationActivity.class);
            Bundle bundle = new Bundle();
            Log.e("ad","KEY_COMMUNITY_INNFORMATION_INDEX "+CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
            Log.e("ad","KEY_COMMUNITY_ID "+requestParametersbean.getCommunityID());
            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());

            if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
            }
            else {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            }
            intent.putExtras(bundle);
            activity.startActivity(intent);



            /*//home
            if (CommunityActivitiesFeedActivitiy.communityActivitiesFeedActivitiy != null) {
                CommunityActivitiesFeedActivitiy.communityActivitiesFeedActivitiy.finish();
            }

            Intent intent = new Intent(activity, CommunityActivitiesFeedActivitiy.class);
            activity.startActivity(intent);*/


        } else if (title.equals(activity.getResources().getString(R.string.followers))) {
            if (ACFollowerAwaitingManageActivity.acFollowerAwaitingManageActivity != null) {
                ACFollowerAwaitingManageActivity.acFollowerAwaitingManageActivity.finish();
            }


            Intent intent = new Intent(activity, ACFollowerAwaitingManageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
            //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
            }
            else {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            }
            if (communityAccessbean == null) {
                communityAccessbean = new CommunityAccessbean();
            }
            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, requestParametersbean.getCommunityText());

            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

            intent.putExtras(bundle);
            activity.startActivity(intent);

        } else if (title.equals(activity.getResources().getString(R.string.classified))) {
            if (ClassifiedMainActivity.classifiedMainActivity != null) {
                ClassifiedMainActivity.classifiedMainActivity.finish();
            }
             Log.e("ad","KEY_COMMUNITY_ID g"+requestParametersbean.getCommunityID());

            Intent intent = new Intent(activity, ClassifiedMainActivity.class);
            Bundle bundle = new Bundle();
            //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
            }
            else {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            }
            if (communityAccessbean == null) {
                communityAccessbean = new CommunityAccessbean();
            }
            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, requestParametersbean.getCommunityText());

            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

            intent.putExtras(bundle);
            activity.startActivity(intent);

        } else if (title.equals(activity.getResources().getString(R.string.news))) {

            if (NewsMainActivity.newsMainActivity != null) {
                NewsMainActivity.newsMainActivity.finish();
            }

            Intent intent = new Intent(activity, NewsMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
            //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
            }
            else {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            }
            if (communityAccessbean == null) {
                communityAccessbean = new CommunityAccessbean();
            }
            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

            intent.putExtras(bundle);
            activity.startActivity(intent);
        } else if (title.equals(activity.getResources().getString(R.string.chat))) {

        } else if (title.equals(activity.getResources().getString(R.string.events))) {

            CommonSession commonSession = new CommonSession(activity);
            //if user had opened community if user admin then this screen will open
            if (commonSession.isUserAdminNow()) {
                if (ACListEventsMainActivity.acListEventsMainActivity != null) {
                    ACListEventsMainActivity.acListEventsMainActivity.finish();
                }

                Intent intent = new Intent(activity, ACListEventsMainActivity.class);
                Bundle bundle = new Bundle();
                //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
                if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
                }
                else {
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
                }
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, requestParametersbean.getCommunityText());
                if (communityAccessbean == null) {
                    communityAccessbean = new CommunityAccessbean();
                }
                bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

                intent.putExtras(bundle);
                activity.startActivity(intent);
            } else {
                if (UserEventMainActivity.eventMainActivity != null) {
                    UserEventMainActivity.eventMainActivity.finish();
                }

                Intent intent = new Intent(activity, UserEventMainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
                //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
                if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
                }
                else {
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
                }
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, requestParametersbean.getCommunityText());
                if (communityAccessbean == null) {
                    communityAccessbean = new CommunityAccessbean();
                }
                bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }



        } else if (title.equals(activity.getResources().getString(R.string.gallery))) {
            if (GalleryMainActivity.galleryMainActivity != null) {
                GalleryMainActivity.galleryMainActivity.finish();
            }
            Intent intent = new Intent(activity, GalleryMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
            //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
            }
            else {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            }
            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);
            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, requestParametersbean.getCommunityText());

            intent.putExtras(bundle);
            activity.startActivity(intent);
        } else if (title.equals(activity.getResources().getString(R.string.polls))) {
            if (PollsMainActivity.pollsMainActivity != null) {
                PollsMainActivity.pollsMainActivity.finish();
            }
            Intent intent = new Intent(activity, PollsMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
            //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
            }
            else {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            }
            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

            intent.putExtras(bundle);
            activity.startActivity(intent);
        } else if (title.equals(activity.getResources().getString(R.string.forms))) {
            if (FormsAndDocsMainActivity.formsAndDocsMainActivity != null) {
                FormsAndDocsMainActivity.formsAndDocsMainActivity.finish();
            }
            Intent intent = new Intent(activity, FormsAndDocsMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
            //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);

            intent.putExtras(bundle);
            activity.startActivity(intent);
        } else if (title.equals(activity.getResources().getString(R.string.chat))) {
            if (ChatMainActivity.chatMainActivity != null) {
                ChatMainActivity.chatMainActivity.finish();
            }


            Intent intent = new Intent(activity, ChatMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
            //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
            }
            else {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            }
            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

            intent.putExtras(bundle);
            activity.startActivity(intent);


        } else if (title.equals(activity.getResources().getString(R.string.feature))) {
            if (FeatureMainActivity.featureMainActivity != null) {
                FeatureMainActivity.featureMainActivity.finish();
            }


            Intent intent = new Intent(activity, FeatureMainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
            //bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            if (!TextUtils.isEmpty(CommunityActivitiesFeedActivitiy.comId)) {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, CommunityActivitiesFeedActivitiy.comId);
            }
            else {
                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());
            }
            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

            intent.putExtras(bundle);
            activity.startActivity(intent);


        }
    }

    //basetextview -> where text will display
    //max line -> how much line should allow to display
    //more title text ->> give title ex.read more,more details etc
    //main content -> main content which want to display


    public static void handleAndRedirectToReadMore(final Activity activity, final BaseTextview baseTextview, final int maxLines, final String more_title_text, final String mainContent) {
        baseTextview.post(new Runnable() {
            @Override
            public void run() {
                // Past the maximum number of lines we want to display.
                try {
                    if (baseTextview.getLineCount() > maxLines) {
                        int lastCharShown = baseTextview.getLayout().getLineVisibleEnd(maxLines - 1);

                        baseTextview.setMaxLines(maxLines);

                        String moreString = more_title_text;
                        String suffix = moreString;

                        // 3 is a "magic number" but it's just basically the length of the ellipsis we're going to insert
                        String actionDisplayText = mainContent.substring(0, lastCharShown - suffix.length() - 3) + "..." + suffix;

                        SpannableString truncatedSpannableString = new SpannableString(actionDisplayText);
                        int startIndex = actionDisplayText.indexOf(moreString);
                        truncatedSpannableString.setSpan(new ForegroundColorSpan(activity.getResources().getColor(android.R.color.holo_red_dark)), startIndex, startIndex + moreString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        baseTextview.setText(truncatedSpannableString);
                    } else {
                        baseTextview.setText(mainContent);
                    }
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void shareIntent(Activity activity, String content) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        activity.startActivity(shareIntent);
    }


    public static void handleSocialOption(Activity activity, SocialOptionbean socialOptionbean, BaseTextview baseTextview_likes, BaseTextview baseTextview_comments, BaseTextview baseTextview_views) {
        if (socialOptionbean != null) {

            if (baseTextview_likes != null) {
                baseTextview_likes.setText(String.valueOf(socialOptionbean.getLike() + " " + activity.getResources().getString(R.string.likes)));

            }
            if (baseTextview_comments != null) {
                baseTextview_comments.setText(String.valueOf(socialOptionbean.getComments() + " " + activity.getResources().getString(R.string.comments)));

            }

            if (baseTextview_views != null) {
                baseTextview_views.setText(String.valueOf(socialOptionbean.getViews() + " " + activity.getResources().getString(R.string.views)));

            }


        }

    }

    public static void makeAsHomeCommunity(final Activity activity, String communityID, final int index) {


        try {
            CommonSession commonSession = new CommonSession(activity);
            RequestParametersbean requestParametersbean = new RequestParametersbean();

            requestParametersbean.setCommunityID(communityID);


            requestParametersbean.setUserId(commonSession.getLoggedUserID());


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.SET_AS_HOME.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {
                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {


                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                                }


                                if (responcebean.getErrorMessage() == null) {

                                    CommonMethods.customToastMessage(activity.getResources().getString(R.string.community_set_as_home), activity);
                                } else {
                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), activity);

                                }
                                boolean userOldcommunityID = false, useNewcommunityID = false;

                                String oldCommunityIDString = null, newCommunityIDString = null;
                                if (jsonObject_main.has(JSONCommonKeywords.oldCommunityId)) {
                                    oldCommunityIDString = jsonObject_main.getString(JSONCommonKeywords.oldCommunityId);
                                    if (oldCommunityIDString == null || oldCommunityIDString.equals("") || oldCommunityIDString.equals("null")) {
                                        userOldcommunityID = false;

                                    } else {


                                        userOldcommunityID = true;


                                    }
                                }


                                if (jsonObject_main.has(JSONCommonKeywords.newCommunityId)) {
                                    newCommunityIDString = jsonObject_main.getString(JSONCommonKeywords.newCommunityId);
                                    CommunityActivitiesFeedActivitiy.comId=newCommunityIDString;
                                    if (newCommunityIDString == null || newCommunityIDString.equals("") || newCommunityIDString.equals("null")) {
                                        useNewcommunityID = false;

                                    } else {


                                        useNewcommunityID = true;


                                    }
                                }


                                //this is comes from following listing and make community as home
                                if (index == ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex()) {
                                    for (int i = 0; i < FollowingActivity.followingbeanArrayList.size(); i++) {


                                        Followingbean followingbean1_checkID = FollowingActivity.followingbeanArrayList.get(i);
                                        if (followingbean1_checkID.getCommunityID() == null || followingbean1_checkID.getCommunityID().equals("")) {

                                        } else {
                                            if (userOldcommunityID) {
                                                if (followingbean1_checkID.getCommunityID().equals(oldCommunityIDString)) {


                                                    followingbean1_checkID.getCommunityAccessbean().setHomeDefualtCommunity(false);
                                                    FollowingActivity.followingbeanArrayList.set(i, followingbean1_checkID);

                                                    if (FollowingActivity.followingAdapter != null) {
                                                        FollowingActivity.followingAdapter.notifyItemChanged(i);
                                                    }


                                                }
                                            }


                                            if (useNewcommunityID) {
                                                if (followingbean1_checkID.getCommunityID().equals(newCommunityIDString)) {
                                                    followingbean1_checkID.getCommunityAccessbean().setHomeDefualtCommunity(true);
                                                    FollowingActivity.followingbeanArrayList.set(i, followingbean1_checkID);

                                                    //this is updating home community in session also
                                                    updateHomeCommunity(activity, followingbean1_checkID.getCommunityID(), followingbean1_checkID.getCommunityName());

                                                    if (FollowingActivity.followingAdapter != null) {
                                                        FollowingActivity.followingAdapter.notifyItemChanged(i);
                                                    }


                                                }

                                            }

                                        }

                                    }
                                }
                                //this is comes from Search community  listing and make community as home

                                else if (index == ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex()) {
                                    for (int i = 0; i < CommunitySearchByNameActivity.searchbeanArrayList_new.size(); i++) {


                                        Searchbean searchbean_checkID = CommunitySearchByNameActivity.searchbeanArrayList_new.get(i);
                                        if (searchbean_checkID.getCommunityID() == null || searchbean_checkID.getCommunityID().equals("")) {

                                        } else {
                                            if (userOldcommunityID) {
                                                if (searchbean_checkID.getCommunityID().equals(oldCommunityIDString)) {


                                                    searchbean_checkID.getCommunityAccessbean().setHomeDefualtCommunity(false);
                                                    CommunitySearchByNameActivity.searchbeanArrayList_new.set(i, searchbean_checkID);


                                                    if (CommunitySearchByNameActivity.searchResultsAdapter != null) {
                                                        CommunitySearchByNameActivity.searchResultsAdapter.notifyItemChanged(i);
                                                    }


                                                }
                                            }


                                            if (useNewcommunityID) {
                                                if (searchbean_checkID.getCommunityID().equals(newCommunityIDString)) {
                                                    searchbean_checkID.getCommunityAccessbean().setHomeDefualtCommunity(true);
                                                    CommunitySearchByNameActivity.searchbeanArrayList_new.set(i, searchbean_checkID);

                                                    //this is updating home community in session also
                                                    updateHomeCommunity(activity, searchbean_checkID.getCommunityID(), searchbean_checkID.getCommunityName());


                                                    if (CommunitySearchByNameActivity.searchResultsAdapter != null) {
                                                        CommunitySearchByNameActivity.searchResultsAdapter.notifyItemChanged(i);
                                                    }


                                                }

                                            }

                                        }

                                    }
                                }


                            } else {
                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));

                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), activity);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, activity, activity, URLs.HOMECOMMUNITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.SET_AS_HOME.getScrenIndex(), NewsMainActivity.class.getClass());
            passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void updateHomeCommunity(Activity activity, String communityID, String communityName) {
        try {
            CommonSession commonSession = new CommonSession(activity);
            if (commonSession.isUserSetHomeCommunity()) {

            } else {
                Loginbean loginbean = CommonMethods.convertJSONToLoginbean(commonSession.getLoginJsonContent());

                loginbean.setHomeCommunityName(communityName);
                loginbean.setHomeCommunityId(communityID);
                CommunityActivitiesFeedActivitiy.comId=communityID;
                // here flag set true as user had selected commnity as home
                commonSession.storeUserSetHomeCommunity(true);

                CommonMethods.convertLoginbeanToJSON(activity, loginbean);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void leftMenuUpdateDataifOpenedDrawer(Activity activity, DrawerLayout drawerLayout, final CircularImageView circularImageView_profile_pic, BaseTextview baseTextview_undread_messageMarker, RetryParameterbean retryParameterbean) {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            CommonMethods.displayLog("Left opened", "Drawer");

            CommonSession commonSession = new CommonSession(activity);
//circle image view display
            if (circularImageView_profile_pic != null) {
                final Loginbean loginbean = CommonMethods.convertJSONToLoginbean(commonSession.getLoginJsonContent());
                if (loginbean != null) {
                    if (CommonMethods.isImageUrlValid(loginbean.getProfile_pic())) {
                        Picasso.with(activity)
                                .load(loginbean.getProfile_pic())
                                .into(circularImageView_profile_pic, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        CommonMethods.displayLog("Success", "Image done");
                                    }

                                    @Override
                                    public void onError() {
                                        circularImageView_profile_pic.setImageResource(R.drawable.user_defualt_icon);
                                    }
                                });
                    }
                }
            }

            //unread message counter
            if (baseTextview_undread_messageMarker != null) {

                RequestParametersbean requestParametersbean = new RequestParametersbean();
                requestParametersbean.setUserId(commonSession.getLoggedUserID());

                checkUnreadMesssage(baseTextview_undread_messageMarker, activity, requestParametersbean, retryParameterbean);

            }

        }

    }


    public static void checkUnreadMesssage(BaseTextview baseTextview, Activity activity, RequestParametersbean requestParametersbean, RetryParameterbean retryParameterbean) {


        JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.UNREAD_MESSAGE.getScrenIndex(), requestParametersbean);

        PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
            @Override
            public void setupUI(Responcebean responcebean) {

//                if (responcebean.isServiceSuccess()) {
//                    try {
//                        JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
//                        if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
//
//
//
//                        }
//                    } catch (Exception e) {
//                        Log.e("Tag", "Error on checkUnreadMesssage=" + e.getMessage());
//                    }
//
//
//                }


            }
        }, activity, activity, URLs.UNREAD_MESSAGE_COUNTER, jsonObjectGetPostParameterEachScreen, ScreensEnums.UNREAD_MESSAGE.getScrenIndex(), EditProfileActivity.class.getClass());
        passParameterbean.setRequestMethod(HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex());
        passParameterbean.setNoNeedToDisplayLoadingDialog(true);
        new RequestToServer(passParameterbean, retryParameterbean).execute();
    }


    public static void setBackgroundColorBasedOnCategoryName(Activity activity, ImageView imageview_communityactivity_communitycategory, String categoryName) {
        if (categoryName.equals(CommonKeywords.TYPE_FEED_NEWS)) {
            Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.menu_dot_bg);
            LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
            GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
            solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
            solidColor_main_bg.setStroke(6, activity.getResources().getColor(R.color.menu_news));
            imageview_communityactivity_communitycategory.setBackground(tempDrawable_main_bg);
        } else if (categoryName.equals(CommonKeywords.TYPE_FEED_CLASSFIED)) {
            Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.menu_dot_bg);
            LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
            GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
            solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
            solidColor_main_bg.setStroke(6, activity.getResources().getColor(R.color.menu_classfied));
            imageview_communityactivity_communitycategory.setBackground(tempDrawable_main_bg);
        } else if (categoryName.equals(CommonKeywords.TYPE_FEED_EVENT)) {
            Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.menu_dot_bg);
            LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
            GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
            solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
            solidColor_main_bg.setStroke(6, activity.getResources().getColor(R.color.menu_events));
            imageview_communityactivity_communitycategory.setBackground(tempDrawable_main_bg);
        } else if (categoryName.equals(CommonKeywords.TYPE_FEED_POLL)) {
            Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.menu_dot_bg);
            LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
            GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
            solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
            solidColor_main_bg.setStroke(6, activity.getResources().getColor(R.color.menu_polls));
            imageview_communityactivity_communitycategory.setBackground(tempDrawable_main_bg);
        } else if (categoryName.equals(CommonKeywords.TYPE_FEED_GALLERY)) {
            Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.menu_dot_bg);
            LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
            GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
            solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
            solidColor_main_bg.setStroke(6, activity.getResources().getColor(R.color.menu_gallery));
            imageview_communityactivity_communitycategory.setBackground(tempDrawable_main_bg);
        }
    }


    public static void markAsViewed(final Activity activity, String activityID, String type, final int index, final int position, final RequestParametersbean requestParametersbean, final String communityID) {


        try {
            //activity id- if comes from feed screen else use respective feed id.

            requestParametersbean.setActivityId(activityID);
            requestParametersbean.setType(type);

            if (index == ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex()) {
                requestParametersbean.setIsFromFeed("1");

            } else {
                requestParametersbean.setIsFromFeed("0");

            }


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.VIEW.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {
                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                                String totalviews = jsonObject_main.getString(JSONCommonKeywords.TotalViews);
                                if (totalviews == null || totalviews.equals("")) {

                                } else {

                                    if (index == ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex()) {
                                        final CommunityActivitiesFeedbean communityActivitiesFeedbean = CommunityActivitiesFeedActivitiy.communityActivitiesFeedbeanArrayList.get(position);

                                        SocialOptionbean socialOptionbean = communityActivitiesFeedbean.getSocialOptionbean();
                                        socialOptionbean.setViews(Integer.parseInt(totalviews));
                                        CommunityActivitiesFeedActivitiy.communityActivitiesFeedbeanArrayList.set(communityActivitiesFeedbean.getPosition(), communityActivitiesFeedbean);
                                        CommunityActivitiesFeedActivitiy.communityActivitiesFeedAdapter.notifyItemChanged(communityActivitiesFeedbean.getPosition());


                                        Intent intent = new Intent(activity, CommunityInformationActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
                                        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, requestParametersbean.getCommunityID());

                                        intent.putExtras(bundle);
                                        activity.startActivity(intent);


                                    } else if (index == ScreensEnums.NEWS_DETAILS.getScrenIndex()) {
                                        Newsbean newsbean1 = NewsMainActivity.newsbeanArrayList.get(position);
                                        SocialOptionbean socialOptionbean = newsbean1.getSocialOptionbean();
                                        socialOptionbean.setViews(Integer.parseInt(totalviews));
                                        newsbean1.setSocialOptionbean(socialOptionbean);
                                        NewsMainActivity.newsbeanArrayList.set(newsbean1.getPosition(), newsbean1);
                                        NewsMainActivity.newsAdapter.notifyItemChanged(newsbean1.getPosition());


                                        if (NewsDetailActivity.baseTextview_news_views != null) {
                                            NewsDetailActivity.baseTextview_news_views.setText(String.valueOf(socialOptionbean.getViews() + " " + activity.getResources().getString(R.string.views)));
                                        }
                                    } else if (index == ScreensEnums.EVENTDETAIl.getScrenIndex()) {
                                        Eventbean eventbean = UserEventMainActivity.eventbeanArrayList.get(position);
                                        SocialOptionbean socialOptionbean = eventbean.getSocialOptionbean();
                                        socialOptionbean.setViews(Integer.parseInt(totalviews));
                                        eventbean.setSocialOptionbean(socialOptionbean);
                                        UserEventMainActivity.eventbeanArrayList.set(eventbean.getPosition(), eventbean);
                                        UserEventMainActivity.eventAdapter.notifyItemChanged(eventbean.getPosition());


                                        if (EventDetailActivity.baseTextview_events_views != null) {
                                            EventDetailActivity.baseTextview_events_views.setText(String.valueOf(socialOptionbean.getViews() + " " + activity.getResources().getString(R.string.views)));
                                        }
                                    } else if (index == ScreensEnums.GALLERYDETAIL.getScrenIndex()) {

                                    } else if (index == ScreensEnums.POLLS_DETAILS_FOR_USER.getScrenIndex()) {

                                    } else if (index == ScreensEnums.CLASSIFIED.getScrenIndex()) {

                                    }
                                }

                            } else

                            {
                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    CommonMethods.customToastMessage(jsonObject_main.getString(JSONCommonKeywords.message), activity);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        CommonMethods.customToastMessage(activity.getResources().getString(R.string.something_wrong), activity);

                    }

                }
            }, activity, activity, URLs.VIEWACTIVITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.VIEW.getScrenIndex(), activity.getClass());
            passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
