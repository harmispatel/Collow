package com.app.collow.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.collow.R;
import com.app.collow.adapters.CommentsAdapter;
import com.app.collow.adapters.GalleryGridSubAdapter;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.Commentsbean;
import com.app.collow.beans.CommunityActivitiesFeedbean;
import com.app.collow.beans.Gallerybean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Harmis on 13/02/17.
 */

public class CommentListingActivity extends BaseActivity implements SetupViewInterface {

    public static CommentsAdapter commentsAdapter = null;
    public static ArrayList<Commentsbean> commentsbeanArrayList = new ArrayList<>();
    public static RecyclerView mRecyclerView;
    public static CommentListingActivity commentListingActivity = null;
    public static SetupViewInterface setupViewInterface = null;
    protected Handler handler;
    View view_home = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    int current_start = 0;
    EditText editText_commnet_message = null;
    MyButtonView myButtonView_send = null;
    BaseTextview baseTextview_left_side = null;
    ImageView imageView_camera_button = null, imageView_setting_button = null;
    String activityID, feedCategoryType = null, title = null;
    int keyScreenFrom = 0;
    int selectedItemPosition = 0;
    LinearLayout linearLayout_load_more_comment = null;
    private BaseTextview baseTextview_error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        super.onCreate(savedInstanceState);
        try {
            setupViewInterface = this;
            commentListingActivity = this;
            commentsbeanArrayList.clear();
            retryParameterbean = new RetryParameterbean(CommentListingActivity.this, getApplicationContext(), getIntent().getExtras(), CommentListingActivity.class.getClass());
            commonSession = new CommonSession(CommentListingActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                activityID = bundle.getString(BundleCommonKeywords.KEY_ACTIVITY_ID);
                feedCategoryType = bundle.getString(BundleCommonKeywords.KEY_FEED_CATEGORY);
                title = bundle.getString(BundleCommonKeywords.KEY_ACTIVITY_TITLE);
                keyScreenFrom = bundle.getInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE);
                selectedItemPosition = bundle.getInt(BundleCommonKeywords.KEY_SELECTED_ITEM_POSITION);
            }
            handler = new Handler();
            setupHeaderView();
            findViewByIDs();
            setupEvents();
            getCommentListing();

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }

    public void setupHeaderView() {
        try {
            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            if (title != null) {
                baseTextview_header_title.setText(title);

            }
            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }
            };
            drawerLayout.setDrawerListener(drawerListener);
            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);

            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);
            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });


        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.comment_listing_main, null);
            myButtonView_send = (MyButtonView) view_home.findViewById(R.id.button_chat_send);
            editText_commnet_message = (EditText) view_home.findViewById(R.id.edittext_message_text);
            imageView_camera_button = (ImageView) view_home.findViewById(R.id.imageview_camera);
            imageView_setting_button = (ImageView) view_home.findViewById(R.id.imageview_setting);
            linearLayout_load_more_comment = (LinearLayout) view_home.findViewById(R.id.layout_load_more_comment);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            baseTextview_error = (BaseTextview) view_home.findViewById(R.id.empty_view);
            mRecyclerView = (RecyclerView) view_home.findViewById(R.id.my_recycler_view);


            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(25);
            // use a linear layout manager
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.addItemDecoration(dividerItemDecoration);


            commentsAdapter = new CommentsAdapter(mRecyclerView, this, activityID);
            mRecyclerView.setAdapter(commentsAdapter);
            frameLayout_container.addView(view_home);


            commentsAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    //add null , so the adapter will check view_type and show progress bar at bottom

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(commentsbeanArrayList.size()>10)
                            {
                                linearLayout_load_more_comment.setVisibility(View.VISIBLE);

                            }

                        }
                    }, 2000);

                }
            });

            linearLayout_load_more_comment.setOnClickListener(new MyOnClickListener(CommentListingActivity.this) {
                @Override
                public void onClick(View v) {
                    current_start += 10;

                    //   searchbean_post_data.getStart_limit()+=10;

                    requestParametersbean.setStart_limit(current_start);
                    //  getNewsListing();

                    getCommentListing();
                    //or you can add all at once but do not forget to call mAdapter.notifyDataSetChanged();


                }
            });


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    private void setupEvents() {
        try {
            myButtonView_send.setOnClickListener(new MyOnClickListener(CommentListingActivity.this) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        if (TextUtils.isEmpty(editText_commnet_message.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_message), CommentListingActivity.this);
                        } else {


                            sendMessage();
                        }
                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            imageView_camera_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(CommentListingActivity.this, UploadPictureWithTextActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(BundleCommonKeywords.KEY_ACTIVITY_ID, activityID);
                    bundle.putString(BundleCommonKeywords.KEY_FEED_CATEGORY, feedCategoryType);

                    intent.putExtras(bundle);
                    startActivity(intent);

                }
            });
            imageView_setting_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    CommonMethods.customToastMessage("Under working",CommentListingActivity.this);

                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }
    }


    // this method is used for login user
    public void getCommentListing() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setActivityId(activityID);


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.COMMENTS_LISTING_MAIN.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CommentListingActivity.this, getApplicationContext(), URLs.COMMENTS_LISTING_MAIN, jsonObjectGetPostParameterEachScreen, ScreensEnums.COMMENTS_LISTING_MAIN.getScrenIndex(), CommentListingActivity.class.getClass());

            passParameterbean.setNeedToFirstTakeFacebookProfilePic(false);
            if (current_start == 0) {
                passParameterbean.setNoNeedToDisplayLoadingDialog(false);

            } else {
                passParameterbean.setNoNeedToDisplayLoadingDialog(true);

            }

            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    // this method is used for login user
    public void sendMessage() {
        try {

            requestParametersbean.setStart_limit(current_start);
            requestParametersbean.setPostText(editText_commnet_message.getText().toString());
            requestParametersbean.setType(feedCategoryType);
            requestParametersbean.setActivityId(activityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.SEND_COMMENT.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this, CommentListingActivity.this, getApplicationContext(), URLs.COMMENT_ACTIVITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.SEND_COMMENT.getScrenIndex(), CommentListingActivity.class.getClass());

            passParameterbean.setNeedToFirstTakeFacebookProfilePic(false);
            passParameterbean.setForImageUploadingCustomKeyword(true);
            passParameterbean.setForImageUploadingCustomKeywordName(JSONCommonKeywords.CommentAttach);
            passParameterbean.setRequestMethod(HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex());

            passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    @Override
    public void setupUI(Responcebean responcebean) {

        try {
            if (UploadPictureWithTextActivity.uploadPictureWithTextActivity != null) {
                UploadPictureWithTextActivity.uploadPictureWithTextActivity.finish();
            }


            if (responcebean.getScreenIndex() == ScreensEnums.SEND_COMMENT.getScrenIndex()) {

                JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                if (responcebean.isServiceSuccess()) {
                    if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                        JSONObject jsonObject_comment = jsonObject_main.getJSONObject(JSONCommonKeywords.Comment);
                        Commentsbean commentsbean = getCommnetbeanFromJSON(jsonObject_comment);

                        updateMainScreenIfAnyCommentSentByUser();

                        mRecyclerView.setVisibility(View.VISIBLE);
                        baseTextview_error.setVisibility(View.GONE);
                        editText_commnet_message.setText("");

                        if (commentsbeanArrayList.size() == 0) {
                            commentsbeanArrayList.add(0, commentsbean);
                            commentsAdapter.notifyItemInserted(0);
                            mRecyclerView.smoothScrollToPosition(0);

                        } else {
                            int add_postion = commentsbeanArrayList.size();
                            commentsbean.setPosition(add_postion);
                            commentsbeanArrayList.add(add_postion, commentsbean);
                            commentsAdapter.notifyItemInserted(add_postion);
                            mRecyclerView.smoothScrollToPosition(add_postion);
                        }


                    }
                }


            } else {
                if (responcebean.isServiceSuccess()) {

                    JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                    if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                        //parse here data of following


                        if (CommonMethods.checkJSONArrayHasData(jsonObject_main, JSONCommonKeywords.commentsdata)) {

                            ArrayList<Commentsbean> commentsbeanArrayList_local = new ArrayList<>();
                            JSONArray jsonArray_comments_list = jsonObject_main.getJSONArray(JSONCommonKeywords.commentsdata);
                            for (int i = 0; i < jsonArray_comments_list.length(); i++) {

                                JSONObject jsonObject_single = jsonArray_comments_list.getJSONObject(i);

                                Commentsbean commentsbean = getCommnetbeanFromJSON(jsonObject_single);


                                commentsbean.setPosition(i);
                                commentsbeanArrayList_local.add(commentsbean);
                            }


                            if (current_start == 0) {
                                commentsbeanArrayList = commentsbeanArrayList_local;
                                commentsAdapter.notifyDataSetChanged();
                                mRecyclerView.setVisibility(View.VISIBLE);
                                baseTextview_error.setVisibility(View.GONE);
                                mRecyclerView.smoothScrollToPosition(commentsbeanArrayList.size() - 1);
                            } else {

                                int start = commentsbeanArrayList.size();
                                for (int i = 0; i < commentsbeanArrayList_local.size(); i++) {

                                    Commentsbean commentsbean = commentsbeanArrayList_local.get(i);
                                    commentsbean.setPosition(start);
                                    commentsbeanArrayList.add(start, commentsbean);
                                    commentsAdapter.notifyItemInserted(commentsbeanArrayList.size());

                                    start++;

                                }

                                commentsAdapter.setLoaded();
                                //mRecyclerView.smoothScrollToPosition(commentsbeanArrayList.size()-1);


                            }
                        } else {
                            handlerError(responcebean);

                        }


                    } else {
                        handlerError(responcebean);

                    }
                } else {
                    handlerError(responcebean);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            handlerError(responcebean);
            new BaseException(e, false, retryParameterbean);

        }


    }

    public Commentsbean getCommnetbeanFromJSON(JSONObject jsonObject_single_comment) {
        Commentsbean commentsbean = new Commentsbean();

        try {
            if (jsonObject_single_comment.has(JSONCommonKeywords.userid)) {
                String userID = jsonObject_single_comment.getString(JSONCommonKeywords.userid);

                if (CommonMethods.isTextAvailable(userID)) {
                    if (commonSession.getLoggedUserID().equals(userID)) {
                        commentsbean.setLoggedUserCommentShouldBeRightSide(true);
                    } else {
                        commentsbean.setLoggedUserCommentShouldBeRightSide(false);

                    }
                } else {
                    commentsbean.setLoggedUserCommentShouldBeRightSide(false);

                }


                commentsbean.setPostContent(jsonObject_single_comment.getString(JSONCommonKeywords.userid));
            }


            if (jsonObject_single_comment.has(JSONCommonKeywords.CommentId)) {
                commentsbean.setCommentID(jsonObject_single_comment.getString(JSONCommonKeywords.CommentId));
            }

            if (jsonObject_single_comment.has(JSONCommonKeywords.CommentText)) {
                commentsbean.setPostContent(jsonObject_single_comment.getString(JSONCommonKeywords.CommentText));
            }

            if (jsonObject_single_comment.has(JSONCommonKeywords.CommentDate)) {
                commentsbean.setCommentedDate(jsonObject_single_comment.getString(JSONCommonKeywords.CommentDate));
            }


            if (jsonObject_single_comment.has(JSONCommonKeywords.UserName)) {
                commentsbean.setName(jsonObject_single_comment.getString(JSONCommonKeywords.UserName));
            }


            if (jsonObject_single_comment.has(JSONCommonKeywords.TotalLike)) {

                commentsbean.setLikeDisLikeCount(Integer.parseInt(CommonMethods.handleFlagValues(jsonObject_single_comment.getString(JSONCommonKeywords.TotalLike))));
            }

            if (jsonObject_single_comment.has(JSONCommonKeywords.ProfilePic)) {
                commentsbean.setUserProfilePic(jsonObject_single_comment.getString(JSONCommonKeywords.ProfilePic));
            }
            if (jsonObject_single_comment.has(JSONCommonKeywords.isOnlyTextPosted)) {


                int value = Integer.parseInt(CommonMethods.handleFlagValues(jsonObject_single_comment.getString(JSONCommonKeywords.isOnlyTextPosted)));

                commentsbean.setPostedType(value);

            }
            if (jsonObject_single_comment.has(JSONCommonKeywords.isLiked)) {


                int value = Integer.parseInt(CommonMethods.handleFlagValues(jsonObject_single_comment.getString(JSONCommonKeywords.isLiked)));
                if (value == 1) {
                    commentsbean.setAlreadyLikedbyMe(true);
                }


            }
            if (jsonObject_single_comment.has(JSONCommonKeywords.UploadedImage)) {
                commentsbean.setUploadedFileURL(jsonObject_single_comment.getString(JSONCommonKeywords.UploadedImage));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return commentsbean;
    }

    public void handlerError(Responcebean responcebean) {
        try {
            if (responcebean.getErrorMessage() == null || responcebean.getErrorMessage().equals("")) {
                if (current_start == 0) {
                    baseTextview_error.setText(getResources().getString(R.string.no_comments_found));
                    mRecyclerView.setVisibility(View.GONE);
                    baseTextview_error.setVisibility(View.VISIBLE);

                } else {

                }
            } else {
                if (current_start == 0) {
                    baseTextview_error.setText(responcebean.getErrorMessage());
                    mRecyclerView.setVisibility(View.GONE);
                    baseTextview_error.setVisibility(View.VISIBLE);

                } else {

                }
            }
        } catch (Resources.NotFoundException e) {
            new BaseException(e, false, retryParameterbean);

        }
    }


    public void updateMainScreenIfAnyCommentSentByUser() {
        try {
            if (keyScreenFrom == ScreensEnums.NEWS.getScrenIndex()) {
                Newsbean newsbean1 = NewsMainActivity.newsbeanArrayList.get(selectedItemPosition);
                SocialOptionbean socialOptionbean = newsbean1.getSocialOptionbean();
                int ccount = socialOptionbean.getComments();
                ccount++;
                socialOptionbean.setComments(ccount);
                newsbean1.setSocialOptionbean(socialOptionbean);
                NewsMainActivity.newsbeanArrayList.set(newsbean1.getPosition(), newsbean1);
                NewsMainActivity.newsAdapter.notifyItemChanged(newsbean1.getPosition());

            } else if (keyScreenFrom == ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex()) {


                CommunityActivitiesFeedbean communityActivitiesFeedbean = CommunityActivitiesFeedActivitiy.communityActivitiesFeedbeanArrayList.get(selectedItemPosition);
                SocialOptionbean socialOptionbean = communityActivitiesFeedbean.getSocialOptionbean();
                int ccount = socialOptionbean.getComments();
                ccount++;
                socialOptionbean.setComments(ccount);
                communityActivitiesFeedbean.setSocialOptionbean(socialOptionbean);
                CommunityActivitiesFeedActivitiy.communityActivitiesFeedbeanArrayList.set(communityActivitiesFeedbean.getPosition(), communityActivitiesFeedbean);
                CommunityActivitiesFeedActivitiy.communityActivitiesFeedAdapter.notifyItemChanged(communityActivitiesFeedbean.getPosition());

            } else if (keyScreenFrom == ScreensEnums.NEWS_DETAILS.getScrenIndex()) {


                Newsbean newsbean1 = NewsMainActivity.newsbeanArrayList.get(selectedItemPosition);
                SocialOptionbean socialOptionbean = newsbean1.getSocialOptionbean();
                int ccount = socialOptionbean.getComments();
                ccount++;
                socialOptionbean.setComments(ccount);
                newsbean1.setSocialOptionbean(socialOptionbean);
                NewsMainActivity.newsbeanArrayList.set(newsbean1.getPosition(), newsbean1);
                NewsMainActivity.newsAdapter.notifyItemChanged(newsbean1.getPosition());


                if (NewsDetailActivity.baseTextview_news_details_comments != null) {
                    NewsDetailActivity.baseTextview_news_details_comments.setText(String.valueOf(socialOptionbean.getComments() + " " + getResources().getString(R.string.comments)));
                }

            }

            else if (keyScreenFrom == ScreensEnums.GALLERYDETAIL.getScrenIndex()) {


                Gallerybean gallerybean = GalleryMainActivity.gallerybeanArrayList_main.get(selectedItemPosition);
                SocialOptionbean socialOptionbean = gallerybean.getSocialOptionbean();
                int ccount = socialOptionbean.getComments();
                ccount++;
                socialOptionbean.setComments(ccount);
                gallerybean.setSocialOptionbean(socialOptionbean);
                GalleryMainActivity.gallerybeanArrayList_main.set(gallerybean.getPosition(), gallerybean);
                GalleryMainActivity.galleryGridAdapter.notifyItemChanged(gallerybean.getPosition());
                //sub gallery updating
                if (GalleryGridSubAdapter.gallerybean_globle != null) {
                    GalleryGridSubAdapter.gallerybean_globle.setSocialOptionbean(socialOptionbean);
                    if (GallerySubActivity.galleryGridSubAdapter != null) {
                        GallerySubActivity.galleryGridSubAdapter.notifyDataSetChanged();
                    }

                }

                if (GalleryDetailActivity.textview_gallerydetail_comments != null) {
                    GalleryDetailActivity.textview_gallerydetail_comments.setText(String.valueOf(socialOptionbean.getComments() + " " + getResources().getString(R.string.comments)));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
