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
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.activities.ACEventDetailsActivity;
import com.app.collow.activities.ACListEventsMainActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Eventbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by harmis on 6/2/17.
 */

public class ACListEventAdapter extends RecyclerView.Adapter {

    Activity activity = null;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    View view;
    String communityID = null;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public ACListEventAdapter(Activity activity, RecyclerView recyclerView, String communityID) {
        this.activity = activity;
        this.communityID = communityID;
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
                R.layout.aclist_event_single_item, parent, false);

        vh = new ACListEventViewHolder(v);

        return vh;
    }

    @Override

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Eventbean eventbean = ACListEventsMainActivity.aceventbeanArrayList_filtered.get(position);


        if (CommonMethods.isTextAvailable(eventbean.getEvent_title())) {
            ((ACListEventViewHolder) holder).baseTextview_aclistevent_title.setText(eventbean.getEvent_title());
        }
        if (CommonMethods.isTextAvailable(eventbean.getEvent_start_time())) {

            StringBuffer stringBuffer = new StringBuffer();

            stringBuffer.append(eventbean.getEvent_start_time());
            if (CommonMethods.isTextAvailable(eventbean.getEvent_end_time())) {
                stringBuffer.append("-");

                stringBuffer.append(eventbean.getEvent_end_time());


            }

            ((ACListEventViewHolder) holder).baseTextview_aclistevent_time.setText(stringBuffer.toString());


        }
        if (CommonMethods.isTextAvailable(eventbean.getEvent_date())) {
            ((ACListEventViewHolder) holder).baseTextview_aclistevent_date.setText(eventbean.getEvent_date());
        }
        ArrayList<String> stringArrayList = eventbean.getStringArrayList_images_url();


        if (stringArrayList != null) {
            if (stringArrayList.size() == 0) {
                ((ACListEventViewHolder) holder).imageview_aclistevent.setImageResource(R.drawable.defualt_square);

            } else {
                if (CommonMethods.isImageUrlValid(stringArrayList.get(0))) {
                    ((ACListEventViewHolder) holder).progressBar.setVisibility(View.VISIBLE);


                    Picasso.with(activity)
                            .load(stringArrayList.get(0))
                            .into(((ACListEventViewHolder) holder).imageview_aclistevent, new Callback() {
                                @Override
                                public void onSuccess() {
                                    ((ACListEventViewHolder) holder).progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError() {
                                    ((ACListEventViewHolder) holder).imageview_aclistevent.setImageResource(R.drawable.defualt_square);
                                    ((ACListEventViewHolder) holder).progressBar.setVisibility(View.GONE);

                                }
                            });


                } else {
                    ((ACListEventViewHolder) holder).imageview_aclistevent.setImageResource(R.drawable.defualt_square);
                }

            }


        }


        ((ACListEventViewHolder) holder).view_click.setTag(eventbean);


        ((ACListEventViewHolder) holder).view_click.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {

                if (isAvailableInternet()) {
                    Eventbean eventbean1_local = (Eventbean) v.getTag();

                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(activity, ACEventDetailsActivity.class);
                    bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN, eventbean1_local);
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                    bundle.putInt(BundleCommonKeywords.POSITION, position);

                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                    CommonMethods.displayLog("Details", "Details");
                }
            }
        });

    }

    public void updateList(ArrayList<Eventbean> list){
        ACListEventsMainActivity.aceventbeanArrayList_filtered = list;
        notifyDataSetChanged();
    }
    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return ACListEventsMainActivity.aceventbeanArrayList_filtered.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ACListEventViewHolder extends RecyclerView.ViewHolder {

        BaseTextview baseTextview_aclistevent_title = null;
        BaseTextview baseTextview_aclistevent_date = null;
        BaseTextview baseTextview_aclistevent_time = null;
        ImageView imageView_aclistevent_rightarrow = null;

        ImageView imageview_aclistevent = null;
        ProgressBar progressBar = null;

        View view_click = null;

        public ACListEventViewHolder(View v) {
            super(v);

            baseTextview_aclistevent_title = (BaseTextview) v.findViewById(R.id.textview_aclistevent_eventtitle);
            baseTextview_aclistevent_date = (BaseTextview) v.findViewById(R.id.textview_aclistevent_eventdate);
            baseTextview_aclistevent_time = (BaseTextview) v.findViewById(R.id.textview_aclistevent_eventtime);

            imageview_aclistevent = (ImageView) v.findViewById(R.id.imageview_aclistevent_image);
            imageView_aclistevent_rightarrow = (ImageView) v.findViewById(R.id.imageview_aclistevent_rightarrow);
            view_click = v;
            progressBar = (ProgressBar) v.findViewById(R.id.progress_bar);


        }
    }


}
