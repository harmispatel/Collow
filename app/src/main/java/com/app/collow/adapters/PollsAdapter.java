package com.app.collow.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.collow.R;
import com.app.collow.activities.PollsMainActivity;
import com.app.collow.activities.PollDetailsActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Pollsbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;

/**
 * Created by harmis on 1/2/17.
 */

public class PollsAdapter extends RecyclerView.Adapter {


    Activity activity = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    String communityID = null,communityText=null;
    CommunityAccessbean communityAccessbean=null;



    public PollsAdapter(Activity activity, RecyclerView recyclerView, String communityID, String communityText, CommunityAccessbean communityAccessbean) {
        this.activity = activity;
        this.communityID=communityID;
        this.communityText=communityText;
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
                R.layout.polls_single_item, parent, false);

        vh = new PollsViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Pollsbean pollsbean = PollsMainActivity.pollsbeanArrayList.get(position);
        if(CommonMethods.isTextAvailable(pollsbean.getTitle()))
        {
            ((PollsViewHolder) holder).baseTextview_polls_title.setText(String.valueOf(pollsbean.getTitle()));

        }


        if(CommonMethods.isTextAvailable(pollsbean.getCreatedDate()))
        {
            ((PollsViewHolder) holder).baseTextview_polls_time.setText(String.valueOf(pollsbean.getCreatedDate()));

        }

        if(CommonMethods.isTextAvailable(pollsbean.getPollVotes()))
        {
            ((PollsViewHolder) holder).baseTextview_polls_votes.setText(String.valueOf(pollsbean.getPollVotes())+" "+activity.getResources().getString(R.string.votes));

        }


        ((PollsViewHolder) holder).view_click.setTag(pollsbean);


        ((PollsViewHolder) holder).view_click.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {

                if (isAvailableInternet()) {
                    Pollsbean pollsbean1_local = (Pollsbean) v.getTag();
                    Bundle bundle = new Bundle();

                    Intent intent = new Intent(activity, PollDetailsActivity.class);
                    bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN, pollsbean1_local);
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID,communityID);
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_NAME_TEXT, communityText);

                    bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }



            }
        });
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return PollsMainActivity.pollsbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class PollsViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_polls_title = null;
        BaseTextview baseTextview_polls_votes = null;
        BaseTextview baseTextview_polls_time = null;
        View view_click = null;


        public PollsViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_polls_title = (BaseTextview) v.findViewById(R.id.textview_polls_title);
            baseTextview_polls_votes = (BaseTextview) v.findViewById(R.id.textview_polls_votes);
            baseTextview_polls_time = (BaseTextview) v.findViewById(R.id.textview_polls_duration);

            view_click = v;




        }
    }


}