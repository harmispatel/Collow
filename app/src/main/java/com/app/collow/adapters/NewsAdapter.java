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
import com.app.collow.activities.NewsDetailActivity;
import com.app.collow.activities.NewsMainActivity;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
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
 * Created by harmis on 1/2/17.
 */

public class NewsAdapter extends RecyclerView.Adapter {


    Activity activity = null;
    CommonSession commonSession = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public NewsAdapter(RecyclerView recyclerView, Activity activity) {
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
                R.layout.news_single_item, parent, false);

        vh = new NewsViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        Newsbean newsbean = NewsMainActivity.newsbeanArrayList.get(position);


        if (CommonMethods.isTextAvailable(newsbean.getNews_title())) {
            ((NewsViewHolder) holder).baseTextview_news_title.setText(newsbean.getNews_title());
        }
        if (CommonMethods.isTextAvailable(newsbean.getNews_description())) {
            MyUtils.handleAndRedirectToReadMore(activity, ((NewsViewHolder) holder).baseTextview_description, 2, activity.getResources().getString(R.string.more_text), newsbean.getNews_description());


        }
        if (CommonMethods.isTextAvailable(newsbean.getNews_time())) {
            ((NewsViewHolder) holder).baseTextview_news_time.setText(newsbean.getNews_time());
        }
        if (CommonMethods.isTextAvailable(newsbean.getNews_username())) {

            ((NewsViewHolder) holder).baseTextview_news_username.setText(newsbean.getNews_username());

        }
        if (CommonMethods.isTextAvailable(newsbean.getCommunityName())) {

            ((NewsViewHolder) holder).baseTextview_news_communityname.setText(newsbean.getCommunityName());

        }
        if (newsbean.isLiked()) {
            ((NewsViewHolder) holder).imageView_likes.setImageResource(R.drawable.like_blue);
        } else {
            ((NewsViewHolder) holder).imageView_likes.setImageResource(R.drawable.unlike_blue);


        }


        SocialOptionbean socialOptionbean = newsbean.getSocialOptionbean();
        MyUtils.handleSocialOption(activity, socialOptionbean, ((NewsViewHolder) holder).baseTextview_news_likes, ((NewsViewHolder) holder).baseTextview_news_comments, ((NewsViewHolder) holder).baseTextview_news_views);


        if (CommonMethods.isImageUrlValid(newsbean.getNews_userprofilepic())) {

            Picasso.with(activity)
                    .load(newsbean.getNews_userprofilepic())
                    .into(((NewsViewHolder) holder).imageview_news_profilepic, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            ((NewsViewHolder) holder).imageview_news_profilepic.setImageResource(R.drawable.defualt_square);

                        }
                    });

        } else {
            ((NewsViewHolder) holder).imageview_news_profilepic.setImageResource(R.drawable.defualt_square);
        }


        ((NewsViewHolder) holder).linearLayout_likes.setTag(String.valueOf(newsbean.getPosition()));
        ((NewsViewHolder) holder).linearLayout_likes.setOnClickListener(new MyOnClickListener(activity) {
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
        ((NewsViewHolder) holder).linearLayout_comments.setTag(String.valueOf(newsbean.getPosition()));
        ((NewsViewHolder) holder).linearLayout_comments.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                try {
                    if (isAvailableInternet()) {
                        int position = Integer.parseInt(v.getTag().toString());
                        Newsbean newsbean1 = NewsMainActivity.newsbeanArrayList.get(position);
                        MyUtils.openCommentswActivity(activity, ScreensEnums.NEWS.getScrenIndex(), newsbean1.getActivityID(), newsbean1.getPosition(), "news", newsbean1.getNews_title());

                    } else {
                        showInternetNotfoundMessage();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        ((NewsViewHolder) holder).linearLayout_news_details.setTag(String.valueOf(newsbean.getPosition()));
        ((NewsViewHolder) holder).linearLayout_news_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int positon=Integer.parseInt(v.getTag().toString());
                    Newsbean newsbean1 =NewsMainActivity.newsbeanArrayList.get(positon);
                    Intent intent = new Intent(activity, NewsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BundleCommonKeywords.KEY_NEWS_BEAN, newsbean1);
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
            final Newsbean newsbean1 = NewsMainActivity.newsbeanArrayList.get(position);
            RequestParametersbean requestParametersbean = new RequestParametersbean();
            requestParametersbean.setActivityId(newsbean1.getActivityID());
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setLiketype("news");


            if(newsbean1.isLiked())
            {
                requestParametersbean.setLike("0");

            }
            else
            {
                requestParametersbean.setLike("1");

            }





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

                                    if (newsbean1.isLiked()) {
                                        newsbean1.setLiked(false);
                                    } else {
                                        newsbean1.setLiked(true);
                                    }
                                    SocialOptionbean socialOptionbean = newsbean1.getSocialOptionbean();
                                    socialOptionbean.setLike(Integer.parseInt(totallikes));
                                    NewsMainActivity.newsbeanArrayList.set(newsbean1.getPosition(), newsbean1);
                                    notifyItemChanged(newsbean1.getPosition());
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, activity, activity, URLs.LIKEACTIVITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.LIKES.getScrenIndex(), NewsMainActivity.class.getClass());
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
        return NewsMainActivity.newsbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_news_title = null;
        BaseTextview baseTextview_news_username = null;
        BaseTextview baseTextview_news_time = null;

        BaseTextview baseTextview_news_likes = null;
        BaseTextview baseTextview_news_comments = null;
        BaseTextview baseTextview_news_views = null;
        BaseTextview baseTextview_news_communityname = null;
        BaseTextview baseTextview_description = null;

        CircularImageView imageview_news_profilepic = null;
        LinearLayout linearLayout_likes = null, linearLayout_comments = null, linearLayout_views = null;
        LinearLayout linearLayout_news_details = null;
        View view = null;
        ImageView imageView_likes = null;


        public NewsViewHolder(View v) {
            super(v);

            baseTextview_news_title = (BaseTextview) v.findViewById(R.id.textview_news_title);
            baseTextview_news_username = (BaseTextview) v.findViewById(R.id.textview_news_username);
            baseTextview_news_time = (BaseTextview) v.findViewById(R.id.textview_news_time);
            baseTextview_news_communityname = (BaseTextview) v.findViewById(R.id.textview_news_community_name);
            baseTextview_description = (BaseTextview) v.findViewById(R.id.textview_communityactivity_text_post);


            imageview_news_profilepic = (CircularImageView) v.findViewById(R.id.imageview_news_userprofilepic);

            baseTextview_news_likes = (BaseTextview) v.findViewById(R.id.textview_likes_counts);
            baseTextview_news_comments = (BaseTextview) v.findViewById(R.id.textview_comments_counts);
            baseTextview_news_views = (BaseTextview) v.findViewById(R.id.textview_view_counts);

            linearLayout_likes = (LinearLayout) v.findViewById(R.id.layout_likes);
            linearLayout_comments = (LinearLayout) v.findViewById(R.id.layout_comments);
            linearLayout_views = (LinearLayout) v.findViewById(R.id.layout_views);
            linearLayout_news_details = (LinearLayout) v.findViewById(R.id.layout_news_details);
            imageView_likes = (ImageView) v.findViewById(R.id.imageview_likes);

            view = v;


        }
    }


}
