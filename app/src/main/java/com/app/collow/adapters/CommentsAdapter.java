package com.app.collow.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.activities.CommentListingActivity;
import com.app.collow.activities.SingleImageFullScreenActivity;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Commentsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Harmis on 13/02/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter {

    public static final int ITEM_TYPE_LEFT = 0;
    public static final int ITEM_TYPE_RIGHT = 1;
    Activity activity = null;
    CommonSession commonSession = null;
    String activityID = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public CommentsAdapter(RecyclerView recyclerView, Activity activity, String activityID) {
        this.activity = activity;
        this.activityID = activityID;
        commonSession = new CommonSession(activity);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                    .getLayoutManager();


            recyclerView
                    .addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrolled(RecyclerView recyclerView,
                                               int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            totalItemCount = linearLayoutManager.getItemCount();
                            lastVisibleItem = linearLayoutManager
                                    .findLastVisibleItemPosition();
                            if (!loading
                                    && totalItemCount <= (lastVisibleItem + CommonKeywords.VISIBLE_THRESHOLD)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {



                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    });
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = null;
        if (viewType == ITEM_TYPE_LEFT) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.chat_left_view, parent, false);
        } else if (viewType == ITEM_TYPE_RIGHT) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.chat_right_view, parent, false);
        }


        vh = new CommentsViewHolder(v);

        return vh;
    }

    @Override
    public int getItemViewType(int position) {


        Commentsbean commentsbean = CommentListingActivity.commentsbeanArrayList.get(position);

        if (commentsbean.isLoggedUserCommentShouldBeRightSide()) {
            return ITEM_TYPE_RIGHT;


        } else {

            return ITEM_TYPE_LEFT;

        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


        Commentsbean commentsbean = CommentListingActivity.commentsbeanArrayList.get(position);
        setupLeftOrRight(holder, commentsbean);


    }


    public void setupLeftOrRight(final RecyclerView.ViewHolder holder, Commentsbean commentsbean) {
        CommonMethods.displayLog("liked", "Count" + commentsbean.getLikeDisLikeCount());
        CommonMethods.displayLog("liked", "Flag Boolea" + commentsbean.isAlreadyLikedbyMe());
        CommonMethods.displayLog("liked", "Imagefile " + commentsbean.getUploadedFileURL());

        if (CommonMethods.isTextAvailable(commentsbean.getName())) {
            ((CommentsViewHolder) holder).baseTextview_chat_username.setText(commentsbean.getName());
        }

        if (CommonMethods.isTextAvailable(commentsbean.getCommentedDate())) {

            ((CommentsViewHolder) holder).baseTextview_datetime.setText(CommonMethods.dateConversionInAgo(activity, commentsbean.getCommentedDate()));

        }

        if (CommonMethods.isTextAvailable(commentsbean.getPostContent())) {
            ((CommentsViewHolder) holder).baseTextview_chat_content.setText(commentsbean.getPostContent());
        }


        if (CommonMethods.isImageUrlValid(commentsbean.getUserProfilePic())) {

            Picasso.with(activity)
                    .load(commentsbean.getUserProfilePic())
                    .into(((CommentsViewHolder) holder).circularImageView_profile_pic, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            ((CommentsViewHolder) holder).circularImageView_profile_pic.setImageResource(R.drawable.defualt_square);

                        }
                    });

        } else {
            ((CommentsViewHolder) holder).circularImageView_profile_pic.setImageResource(R.drawable.defualt_square);
        }


        ((CommentsViewHolder) holder).baseTextview_likes.setText(commentsbean.getLikeDisLikeCount() + " " + activity.getResources().getString(R.string.likes));

        if (commentsbean.isAlreadyLikedbyMe())

        {
            ((CommentsViewHolder) holder).imageView_likes.setImageResource(R.drawable.chat_likeselected);
        } else {
            ((CommentsViewHolder) holder).imageView_likes.setImageResource(R.drawable.chat_likeunselected);

        }


        //handle either image or text

        //if only text =0
        //if only image =1
        //if  text+image =2


        if (commentsbean.getPostedType() == 0) {

            ((CommentsViewHolder) holder).layout_if_image_or_video_uploaded.setVisibility(View.GONE);
            ((CommentsViewHolder) holder).baseTextview_chat_content.setVisibility(View.VISIBLE);


        } else if (commentsbean.getPostedType() == 1) {
            ((CommentsViewHolder) holder).layout_if_image_or_video_uploaded.setVisibility(View.VISIBLE);
            ((CommentsViewHolder) holder).baseTextview_chat_content.setVisibility(View.GONE);
            if (commentsbean.isFromLocal()) {
                Bitmap bitmap = commentsbean.getBitmap();
                if (bitmap != null) {
                    ((CommentsViewHolder) holder).imageView_if_imageuploaded.setImageBitmap(bitmap);

                } else {
                    ((CommentsViewHolder) holder).imageView_if_imageuploaded.setImageResource(R.drawable.defualt_square);

                }
            } else {
                if (CommonMethods.isImageUrlValid(commentsbean.getUploadedFileURL())) {
                    String mimiType = CommonMethods.getMimeTypeFromURL(commentsbean.getUploadedFileURL());

                    ((CommentsViewHolder) holder).progressBar_for_image.setVisibility(View.VISIBLE);


                    Picasso.with(activity)
                            .load(commentsbean.getUploadedFileURL())
                            .into(((CommentsViewHolder) holder).imageView_if_imageuploaded, new Callback() {
                                @Override
                                public void onSuccess() {
                                    ((CommentsViewHolder) holder).progressBar_for_image.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError() {
                                    ((CommentsViewHolder) holder).imageView_if_imageuploaded.setImageResource(R.drawable.defualt_square);
                                    ((CommentsViewHolder) holder).progressBar_for_image.setVisibility(View.GONE);

                                }
                            });
                    ((CommentsViewHolder) holder).view_clicked.setTag(commentsbean.getUploadedFileURL());
                    ((CommentsViewHolder) holder).view_clicked.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = (String) v.getTag();
                            Intent intent = new Intent(activity, SingleImageFullScreenActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString(BundleCommonKeywords.KEY_URL, url);
                            intent.putExtras(bundle);
                            activity.startActivity(intent);

                        }
                    });
                } else {
                    ((CommentsViewHolder) holder).progressBar_for_image.setVisibility(View.GONE);

                }
            }


        } else if (commentsbean.getPostedType() == 2) {
            ((CommentsViewHolder) holder).layout_if_image_or_video_uploaded.setVisibility(View.VISIBLE);
            ((CommentsViewHolder) holder).baseTextview_chat_content.setVisibility(View.VISIBLE);
            if (commentsbean.isFromLocal()) {
                Bitmap bitmap = commentsbean.getBitmap();
                if (bitmap != null) {
                    ((CommentsViewHolder) holder).imageView_if_imageuploaded.setImageBitmap(bitmap);

                } else {
                    ((CommentsViewHolder) holder).imageView_if_imageuploaded.setImageResource(R.drawable.defualt_square);

                }
            } else {
                if (CommonMethods.isImageUrlValid(commentsbean.getUploadedFileURL())) {
                    String mimiType = CommonMethods.getMimeTypeFromURL(commentsbean.getUploadedFileURL());
                    ((CommentsViewHolder) holder).progressBar_for_image.setVisibility(View.VISIBLE);

                    Picasso.with(activity)
                            .load(commentsbean.getUploadedFileURL())
                            .into(((CommentsViewHolder) holder).imageView_if_imageuploaded, new Callback() {
                                @Override
                                public void onSuccess() {
                                    ((CommentsViewHolder) holder).progressBar_for_image.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError() {
                                    ((CommentsViewHolder) holder).imageView_if_imageuploaded.setImageResource(R.drawable.defualt_square);
                                    ((CommentsViewHolder) holder).progressBar_for_image.setVisibility(View.GONE);


                                }
                            });
                } else {

                }
            }


            ((CommentsViewHolder) holder).imageView_if_imageuploaded.setVisibility(View.VISIBLE);

        }


        ((CommentsViewHolder) holder).imageView_likes.setTag(String.valueOf(commentsbean.getPosition()));
        ((CommentsViewHolder) holder).imageView_likes.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                try {
                    int pos = Integer.parseInt(v.getTag().toString());

                    if (isAvailableInternet()) {
                        likeItemhandler(pos);

                    } else {
                        showInternetNotfoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        ((CommentsViewHolder) holder).linearLayout_main_like_dislike.setTag(String.valueOf(commentsbean.getPosition()));

        ((CommentsViewHolder) holder).linearLayout_main_like_dislike.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                try {
                    int pos = Integer.parseInt(v.getTag().toString());

                    if (isAvailableInternet()) {
                        likeItemhandler(pos);
                    } else {
                        showInternetNotfoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void likeItemhandler(final int position) {


        try {
            final Commentsbean commentsbean = CommentListingActivity.commentsbeanArrayList.get(position);
            RequestParametersbean requestParametersbean = new RequestParametersbean();
            requestParametersbean.setActivityId(activityID);
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setPostedCommentID(commentsbean.getCommentID());


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.COMMENT_LIKE_DISLIKE.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {
                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                                String totallikes = jsonObject_main.getString(JSONCommonKeywords.TotalLike);
                                if (totallikes == null || totallikes.equals("")) {

                                } else {

                                    if (commentsbean.isAlreadyLikedbyMe()) {
                                        commentsbean.setAlreadyLikedbyMe(false);

                                    } else
                                        commentsbean.setAlreadyLikedbyMe(true);


                                    commentsbean.setLikeDisLikeCount(Integer.parseInt(totallikes));
                                    CommentListingActivity.commentsbeanArrayList.set(commentsbean.getPosition(), commentsbean);
                                    notifyItemChanged(commentsbean.getPosition());
                                }

                            }
                            else
                            {
                                if(CommonMethods.checkJSONArrayHasData(jsonObject_main,JSONCommonKeywords.message))
                                {
                                    CommonMethods.customToastMessage(jsonObject_main.getString(JSONCommonKeywords.message),activity);
                                }
                                else
                                {
                                    CommonMethods.customToastMessage(activity.getResources().getString(R.string.something_wrong),activity);

                                }
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonMethods.customToastMessage(e.getMessage(),activity);

                        }
                    }
                    else
                    {

                    }

                }
            }, activity, activity, URLs.AddCommentLikeDislike, jsonObjectGetPostParameterEachScreen, ScreensEnums.COMMENT_LIKE_DISLIKE.getScrenIndex(), CommentListingActivity.class.getClass());
            passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return CommentListingActivity.commentsbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class CommentsViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_chat_username = null;
        BaseTextview baseTextview_chat_content = null;
        BaseTextview baseTextview_likes = null;
        BaseTextview baseTextview_datetime = null;


        View view_clicked = null;
        ImageView imageView_likes = null;
        ImageView imageView_if_imageuploaded = null, imageview_play_icon = null;

        CircularImageView circularImageView_profile_pic = null;
        FrameLayout layout_if_image_or_video_uploaded = null;
        LinearLayout linearLayout_main_like_dislike = null;
        ProgressBar progressBar_for_image = null;


        public CommentsViewHolder(View v) {
            super(v);

            baseTextview_chat_username = (BaseTextview) v.findViewById(R.id.textview_chat_username);
            circularImageView_profile_pic = (CircularImageView) v.findViewById(R.id.imageview_chat_userprofilepic);
            imageView_likes = (ImageView) v.findViewById(R.id.imageview_chat_likescount);
            baseTextview_chat_content = (BaseTextview) v.findViewById(R.id.textview_chat_chatcontent);
            baseTextview_likes = (BaseTextview) v.findViewById(R.id.textview_chat_chatlikes);
            baseTextview_datetime = (BaseTextview) v.findViewById(R.id.textview_chat_daytime);
            imageView_if_imageuploaded = (ImageView) v.findViewById(R.id.imageview_if_image_uploaded);
            imageview_play_icon = (ImageView) v.findViewById(R.id.imageview_play_icon_if_video_uploaded);
            layout_if_image_or_video_uploaded = (FrameLayout) v.findViewById(R.id.layout_if_image_or_video_uploaded);
            linearLayout_main_like_dislike = (LinearLayout) v.findViewById(R.id.layout_main_like_dislike);

            progressBar_for_image = (ProgressBar) v.findViewById(R.id.progress_image_chat);


            view_clicked = v;


        }
    }


}