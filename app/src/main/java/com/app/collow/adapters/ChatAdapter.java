package com.app.collow.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.collow.R;
import com.app.collow.activities.ChatDetailActivity;
import com.app.collow.activities.ChatMainActivity;
import com.app.collow.activities.NewsDetailActivity;
import com.app.collow.activities.NewsMainActivity;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Chatbean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by harmis on 27/2/17.
 */

public class ChatAdapter extends RecyclerView.Adapter {


    Activity activity = null;
    CommonSession commonSession = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public ChatAdapter(RecyclerView recyclerView, Activity activity) {
        this.activity = activity;
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

        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.chat_single_item, parent, false);

        vh = new ChatViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        Chatbean chatbean = ChatMainActivity.chatbeanArrayList.get(position);


        if (CommonMethods.isTextAvailable(chatbean.getChat_title())) {
            ((ChatViewHolder) holder).baseTextview_chat_title.setText(chatbean.getChat_title());
        }
        if (CommonMethods.isTextAvailable(chatbean.getChat_description())) {
            MyUtils.handleAndRedirectToReadMore(activity, ((ChatViewHolder) holder).baseTextview_description, 2, activity.getResources().getString(R.string.more_text), chatbean.getChat_description());


        }
        if (CommonMethods.isTextAvailable(chatbean.getChat_time())) {
            ((ChatViewHolder) holder).baseTextview_chat_time.setText(chatbean.getChat_time());
        }
        if (CommonMethods.isTextAvailable(chatbean.getChat_username())) {

            ((ChatViewHolder) holder).baseTextview_chat_username.setText(chatbean.getChat_username());

        }
        if (CommonMethods.isTextAvailable(chatbean.getCommunityName())) {

            ((ChatViewHolder) holder).baseTextview_chat_communityname.setText(chatbean.getCommunityName());

        }
        if (chatbean.isLiked()) {
            ((ChatViewHolder) holder).imageView_likes.setImageResource(R.drawable.like_blue);
        } else {
            ((ChatViewHolder) holder).imageView_likes.setImageResource(R.drawable.unlike_blue);


        }


        SocialOptionbean socialOptionbean = chatbean.getSocialOptionbean();
        MyUtils.handleSocialOption(activity, socialOptionbean, ((ChatViewHolder) holder).baseTextview_chat_likes, ((ChatViewHolder) holder).baseTextview_chat_comments, ((ChatViewHolder) holder).baseTextview_chat_views);


        if (CommonMethods.isImageUrlValid(chatbean.getChat_userprofilepic())) {

            Picasso.with(activity)
                    .load(chatbean.getChat_userprofilepic())
                    .into(((ChatViewHolder) holder).imageview_chat_profilepic, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            ((ChatViewHolder) holder).imageview_chat_profilepic.setImageResource(R.drawable.defualt_square);

                        }
                    });

        } else {
            ((ChatViewHolder) holder).imageview_chat_profilepic.setImageResource(R.drawable.defualt_square);
        }


        ((ChatViewHolder) holder).linearLayout_likes.setTag(String.valueOf(chatbean.getPosition()));
        ((ChatViewHolder) holder).linearLayout_likes.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                try {
                    if (isAvailableInternet()) {
                        int position = Integer.parseInt(v.getTag().toString());

                        likeItemhandler(position);


                    } else {
                        showInternetNotfoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        ((ChatViewHolder) holder).linearLayout_comments.setTag(String.valueOf(chatbean.getPosition()));
        ((ChatViewHolder) holder).linearLayout_comments.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                try {
                    if (isAvailableInternet()) {
                        int position = Integer.parseInt(v.getTag().toString());
                        Chatbean chatbean1 = ChatMainActivity.chatbeanArrayList.get(position);
                        MyUtils.openCommentswActivity(activity, ScreensEnums.CHAT.getScrenIndex(), chatbean1.getActivityID(), chatbean1.getPosition(), "chat", chatbean1.getChat_title());

                    } else {
                        showInternetNotfoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        ((ChatViewHolder) holder).linearLayout_chat_details.setTag(String.valueOf(chatbean.getPosition()));
        ((ChatViewHolder) holder).linearLayout_chat_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int positon=Integer.parseInt(v.getTag().toString());
                    Chatbean chatbean1 =ChatMainActivity.chatbeanArrayList.get(positon);
                    Intent intent = new Intent(activity, ChatDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BundleCommonKeywords.KEY_NEWS_BEAN, chatbean1);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void likeItemhandler(final int position) {


        try {
            final Chatbean chatbean1 = ChatMainActivity.chatbeanArrayList.get(position);
            RequestParametersbean requestParametersbean = new RequestParametersbean();
            requestParametersbean.setActivityId(chatbean1.getActivityID());
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setLiketype("news");
            if (chatbean1.getLikeOrDislikeFlag().equals("0")) {
                chatbean1.setLikeOrDislikeFlag("1");
            } else if (chatbean1.getLikeOrDislikeFlag().equals("1")) {
                chatbean1.setLikeOrDislikeFlag("0");
            } else {
                chatbean1.setLikeOrDislikeFlag("0");

            }
            requestParametersbean.setLike(chatbean1.getLikeOrDislikeFlag());


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.LIKES.getScrenIndex(), requestParametersbean);
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

                                    if (chatbean1.isLiked()) {
                                        chatbean1.setLiked(false);
                                    } else {
                                        chatbean1.setLiked(true);
                                    }
                                    SocialOptionbean socialOptionbean = chatbean1.getSocialOptionbean();
                                    socialOptionbean.setLike(Integer.parseInt(totallikes));
                                    ChatMainActivity.chatbeanArrayList.set(chatbean1.getPosition(), chatbean1);
                                    notifyItemChanged(chatbean1.getPosition());
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, activity, activity, URLs.LIKEACTIVITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.LIKES.getScrenIndex(), ChatMainActivity.class.getClass());
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
        return ChatMainActivity.chatbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_chat_title = null;
        BaseTextview baseTextview_chat_username = null;
        BaseTextview baseTextview_chat_time = null;

        BaseTextview baseTextview_chat_likes = null;
        BaseTextview baseTextview_chat_comments = null;
        BaseTextview baseTextview_chat_views = null;
        BaseTextview baseTextview_chat_communityname = null;
        BaseTextview baseTextview_description = null;

        CircularImageView imageview_chat_profilepic = null;
        LinearLayout linearLayout_likes = null, linearLayout_comments = null, linearLayout_views = null;
        LinearLayout linearLayout_chat_details = null;
        View view = null;
        ImageView imageView_likes = null;


        public ChatViewHolder(View v) {
            super(v);

            baseTextview_chat_title = (BaseTextview) v.findViewById(R.id.textview_chat_title);
            baseTextview_chat_username = (BaseTextview) v.findViewById(R.id.textview_chat_username);
            baseTextview_chat_time = (BaseTextview) v.findViewById(R.id.textview_chat_time);
            baseTextview_chat_communityname = (BaseTextview) v.findViewById(R.id.textview_chat_community_name);
            baseTextview_description = (BaseTextview) v.findViewById(R.id.textview_communityactivity_text_post);


            imageview_chat_profilepic = (CircularImageView) v.findViewById(R.id.imageview_chat_userprofilepic);

            baseTextview_chat_likes = (BaseTextview) v.findViewById(R.id.textview_likes_counts);
            baseTextview_chat_comments = (BaseTextview) v.findViewById(R.id.textview_comments_counts);
            baseTextview_chat_views = (BaseTextview) v.findViewById(R.id.textview_view_counts);

            linearLayout_likes = (LinearLayout) v.findViewById(R.id.layout_likes);
            linearLayout_comments = (LinearLayout) v.findViewById(R.id.layout_comments);
            linearLayout_views = (LinearLayout) v.findViewById(R.id.layout_views);
            linearLayout_chat_details = (LinearLayout) v.findViewById(R.id.layout_chat_details);
            imageView_likes = (ImageView) v.findViewById(R.id.imageview_likes);

            view = v;


        }
    }


}
