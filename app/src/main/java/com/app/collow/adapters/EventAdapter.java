package com.app.collow.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.activities.EventDetailActivity;
import com.app.collow.activities.SplashActvitiy;
import com.app.collow.activities.UserEventMainActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Eventbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;

import java.util.List;

/**
 * Created by harmis on 1/2/17.
 */

public class EventAdapter extends RecyclerView.Adapter {
    Activity activity = null;
    private List<Eventbean> eventList;
CommonSession commonSession=null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    String communityID=null;
    CommunityAccessbean communityAccessbean=null;
    View view;
    public EventAdapter(Activity activity, RecyclerView recyclerView, String communityID, CommunityAccessbean communityAccessbean) {
        this.activity=activity;
        this.communityID=communityID;
        this.communityAccessbean=communityAccessbean;

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
                                    && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
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
                    R.layout.event_single_item, parent, false);

            vh = new EventViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        Eventbean eventbean = UserEventMainActivity.eventbeanArrayList.get(position);


        Drawable tempDrawable = activity.getResources().getDrawable(R.drawable.dot_bg);
        LayerDrawable bubble = (LayerDrawable) tempDrawable;
        GradientDrawable solidColor = (GradientDrawable) bubble.findDrawableByLayerId(R.id.outerRectangle);
        solidColor.setColor(Color.TRANSPARENT);
        solidColor.setStroke(2, SplashActvitiy.integerArrayList_colors.get(CommonMethods.randInt(0, SplashActvitiy.integerArrayList_colors.size() - 1)));
        ((EventViewHolder) holder).imageview_event_bullet.setImageDrawable(tempDrawable);



        if (CommonMethods.isTextAvailable(eventbean.getEvent_title())) {
            ((EventViewHolder) holder).baseTextview_event_title.setText(eventbean.getEvent_title());
        }

        if(eventbean.getEvent_time_mode()== CommonKeywords.EVENT_TIME_MODE_CUSTOM_TIME)
        {
            if (CommonMethods.isTextAvailable(eventbean.getEvent_start_time())) {

                StringBuffer stringBuffer=new StringBuffer();

                stringBuffer.append(eventbean.getEvent_start_time());
                if (CommonMethods.isTextAvailable(eventbean.getEvent_end_time())) {
                    stringBuffer.append("-");

                    stringBuffer.append(eventbean.getEvent_end_time());


                }

                ((EventViewHolder) holder).baseTextview_event_daytitle.setText(stringBuffer.toString());


            }
        }
        else if(eventbean.getEvent_time_mode()== CommonKeywords.EVENT_TIME_MODE_ALL_DAYS)
        {
            ((EventViewHolder) holder).baseTextview_event_daytitle.setText(activity.getResources().getString(R.string.all_days));
        }


        if (CommonMethods.isTextAvailable(eventbean.getEvent_day_name())) {

            ((EventViewHolder) holder).baseTextview_event_dayname.setText(eventbean.getEvent_day_name());



        }

        if (CommonMethods.isTextAvailable(eventbean.getEvent_day_date())) {

            ((EventViewHolder) holder).baseTextview_event_day.setText(eventbean.getEvent_day_date());



        }


        ((EventViewHolder) holder).view.setTag(eventbean);

        ((EventViewHolder) holder).view.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                if(isAvailableInternet())
                {
                    Eventbean eventbean1bean1_local= (Eventbean) v.getTag();
                    Intent intent = new Intent(activity, EventDetailActivity.class);

                    Bundle bundle=new Bundle();
                    bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN,eventbean1bean1_local);
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                    bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
                else
                {
                    showInternetNotfoundMessage();
                }
            }
        });

    }


    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return  UserEventMainActivity.eventbeanArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }



    public static class EventViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_event_title = null;
        BaseTextview baseTextview_event_daytitle = null;
        BaseTextview baseTextview_event_day = null;
        BaseTextview baseTextview_event_dayname = null;
        BaseTextview baseTextview_event_time = null;

        ImageView imageview_event_bullet=null;
    View view=null;

        public EventViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_event_title = (BaseTextview) v.findViewById(R.id.textview_event_eventtitle);
            baseTextview_event_daytitle = (BaseTextview) v.findViewById(R.id.textview_event_daytitle);
            baseTextview_event_day= (BaseTextview) v.findViewById(R.id.textview_event_day);
            baseTextview_event_dayname = (BaseTextview) v.findViewById(R.id.textview_event_day_name);
            baseTextview_event_time=(BaseTextview) v.findViewById(R.id.textview_event_time);
            imageview_event_bullet=(ImageView)v.findViewById(R.id.imageview_event_bullet);

            view=v;


        }
    }




}
