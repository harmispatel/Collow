package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.collow.R;
import com.app.collow.activities.MyActivitiesActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Activitiesbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


/**
 * Created by harmis on 12/1/17.
 */
/*
public class MyActivitiesAdapter extends RecyclerView.Adapter {


    Activity activity = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public MyActivitiesAdapter(Activity activity, RecyclerView recyclerView) {
        this.activity = activity;

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
                R.layout.my_activities_single_raw, parent, false);


        vh = new MyActivtiesViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        Activitiesbean activitiesbean = MyActivitiesActivity.activitiesbeanArrayList.get(position);

        if (CommonMethods.isTextAvailable(activitiesbean.getCommentText())) {
            ((MyActivtiesViewHolder) holder).baseTextview_title.setText(activitiesbean.getTitle());

        }

        ///likes count

        SpannableStringBuilder builder = new SpannableStringBuilder();

        if (CommonMethods.isTextAvailable(activitiesbean.getUserName())) {
            SpannableString title = new SpannableString(activitiesbean.getUserName());
            title.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.black)), 0, title.length(), 0);
            builder.append(title);
        }

        if (CommonMethods.isTextAvailable(activitiesbean.getTitle())) {

            SpannableString str2 = new SpannableString(activitiesbean.getTitle());
            str2.setSpan(new ForegroundColorSpan(activity.getResources().getColor(R.color.left_Menu_text_color_gray)), 0, str2.length(), 0);
            str2.setSpan(new RelativeSizeSpan(0.7f), 0, str2.length(), 0); // set size
            builder.append(" ");

            builder.append(str2);
        }

        ((MyActivtiesViewHolder) holder).baseTextview_title.setText(builder, BaseTextview.BufferType.SPANNABLE);

        if (CommonMethods.isTextAvailable(activitiesbean.getTime())) {
            ((MyActivtiesViewHolder) holder).baseTextview_time.setText(activitiesbean.getTime());

        }

        if (CommonMethods.isImageUrlValid(activitiesbean.getProfilePic())) {
            Picasso.with(activity)
                    .load(activitiesbean.getProfilePic())
                    .into((((MyActivtiesViewHolder) holder).circularImageView_image), new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            ((MyActivtiesViewHolder) holder).circularImageView_image.setImageResource(R.drawable.defualt_circle);
                        }
                    });
        } else {
            ((MyActivtiesViewHolder) holder).circularImageView_image.setImageResource(R.drawable.defualt_circle);

        }


    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return MyActivitiesActivity.activitiesbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class MyActivtiesViewHolder extends RecyclerView.ViewHolder {

        BaseTextview baseTextview_title = null, baseTextview_time = null;
        CircularImageView circularImageView_image = null;


        public MyActivtiesViewHolder(View v) {
            super(v);
            baseTextview_title = (BaseTextview) v.findViewById(R.id.textview_my_activity_title);
            baseTextview_time = (BaseTextview) v.findViewById(R.id.textview_my_activity_time);
            circularImageView_image = (CircularImageView) v.findViewById(R.id.circular_my_activity_image);

        }
    }


}*/
