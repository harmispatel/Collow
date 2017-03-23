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
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.activities.ACListEventsMainActivity;
import com.app.collow.activities.AClistEvent_ViewEvents;
import com.app.collow.activities.CommunityInformationActivity;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACListEventBean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.MyUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harmis on 6/2/17.
 */

public class ACListEventAdapter extends RecyclerView.Adapter {

    Activity activity = null;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    String communityID=null;
    View view;
    private OnLoadMoreListener onLoadMoreListener;

    public ACListEventAdapter(Activity activity, RecyclerView recyclerView) {
        this.activity=activity;
        this.communityID=communityID;
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
                    R.layout.aclist_event_single_item, parent, false);

            vh = new ACListEventViewHolder(v);

        return vh;
    }

    @Override

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            ACListEventBean eventbean = ACListEventsMainActivity.aceventbeanArrayList.get(position);


            if (CommonMethods.isTextAvailable(eventbean.getAclist_eventtiitle())) {
                ((ACListEventViewHolder) holder).baseTextview_aclistevent_title.setText(eventbean.getAclist_eventtiitle());
            }
            if (CommonMethods.isTextAvailable(eventbean.getAclist_eventdate())) {
                ((ACListEventViewHolder) holder).baseTextview_aclistevent_time.setText(eventbean.getAclist_eventtime());


            }
            if (CommonMethods.isTextAvailable(eventbean.getAclist_eventdate())) {
                ((ACListEventViewHolder) holder).baseTextview_aclistevent_date.setText(eventbean.getAclist_eventdate());
            }


            if (CommonMethods.isImageUrlValid(eventbean.getAclist_image())) {

                Picasso.with(activity)
                        .load(eventbean.getAclist_image())
                        .into(((ACListEventViewHolder) holder).imageview_aclistevent, new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                ((ACListEventViewHolder) holder).imageview_aclistevent.setImageResource(R.drawable.defualt_square);

                            }
                        });

            } else {
                ((ACListEventViewHolder) holder).imageview_aclistevent.setImageResource(R.drawable.defualt_square);
            }

            ((ACListEventViewHolder) holder).view_click.setTag(eventbean);


             ((ACListEventViewHolder) holder).view_click.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {

                    if (isAvailableInternet()) {
                         ACListEventBean eventbean1_local = (ACListEventBean) v.getTag();

                        Bundle bundle=new Bundle();
                        bundle.putSerializable(BundleCommonKeywords.KEY_ACEVENT_BEAN,eventbean1_local);
                        Intent intent = new Intent(activity, AClistEvent_ViewEvents.class);
                        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                       /* bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, eventbean1_local.getAclist_eventdate());
                        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, eventbean1_local.getAclist_eventtime());*/
                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                        CommonMethods.displayLog("Details","Details");
                    }
                }
            });

        }


    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return ACListEventsMainActivity.aceventbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ACListEventViewHolder extends RecyclerView.ViewHolder {

        BaseTextview baseTextview_aclistevent_title = null;
        BaseTextview baseTextview_aclistevent_date= null;
        BaseTextview baseTextview_aclistevent_time = null;
        ImageView imageView_aclistevent_rightarrow=null;

        ImageView imageview_aclistevent = null;

        View view_click = null;
        public ACListEventViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_aclistevent_title = (BaseTextview) v.findViewById(R.id.textview_aclistevent_eventtitle);
            baseTextview_aclistevent_date = (BaseTextview) v.findViewById(R.id.textview_aclistevent_eventdate);
            baseTextview_aclistevent_time = (BaseTextview) v.findViewById(R.id.textview_aclistevent_eventtime);

            imageview_aclistevent = (ImageView) v.findViewById(R.id.imageview_aclistevent_image);
            imageView_aclistevent_rightarrow=(ImageView)v.findViewById(R.id.imageview_aclistevent_rightarrow) ;
            view_click = v;



        }
    }



}
