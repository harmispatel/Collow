package com.app.collow.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Classifiedbean;
import com.app.collow.beans.Eventbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonMethods;

import java.util.List;

/**
 * Created by harmis on 1/2/17.
 */

public class EventAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Eventbean> eventList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public EventAdapter(List<Eventbean> events, RecyclerView recyclerView) {
        eventList = events;

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
    public int getItemViewType(int position) {
        return eventList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.event_single_item, parent, false);

            vh = new EventViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new EventProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventViewHolder) {

            Eventbean singleStudent = (Eventbean) eventList.get(position);
            ((EventViewHolder) holder).baseTextview_event_title.setText(String.valueOf("Position" + position
            ));

            if (CommonMethods.isTextAvailable(singleStudent.getMessage())) {
                ((EventViewHolder) holder).baseTextview_event_title.setText(singleStudent.getMessage());
            }


        } else {
            ((EventProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class EventViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_event_title = null;
        BaseTextview baseTextview_event_daytitle = null;
        BaseTextview baseTextview_event_date = null;
        BaseTextview baseTextview_event_day = null;
        BaseTextview baseTextview_event_time = null;
        ImageView imageview_event_bullet=null;


        public EventViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_event_title = (BaseTextview) v.findViewById(R.id.textview_event_eventtitle);
            baseTextview_event_daytitle = (BaseTextview) v.findViewById(R.id.textview_event_daytitle);
            baseTextview_event_date = (BaseTextview) v.findViewById(R.id.textview_event_date);
            baseTextview_event_day = (BaseTextview) v.findViewById(R.id.textview_event_day);
            baseTextview_event_time=(BaseTextview) v.findViewById(R.id.textview_event_time);
            imageview_event_bullet=(ImageView)v.findViewById(R.id.imageview_event_bullet);
         /*   v.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(),
                            "OnClick :" + student.getName() + " \n " + student.getEmailId(),
                            Toast.LENGTH_SHORT).show();

                }
            });*/

        }
    }


    public static class EventProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public EventProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
