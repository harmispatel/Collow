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
import com.app.collow.beans.ACFollowersbean;
import com.app.collow.beans.Eventbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonMethods;

import java.util.List;

/**
 * Created by harmis on 8/2/17.
 */

public class ACFollowersAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<ACFollowersbean> followersList;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public ACFollowersAdapter(List<ACFollowersbean> followers, RecyclerView recyclerView) {
        followersList = followers;

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
        return followersList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.acfollowers_single_item, parent, false);

            vh = new ACFollowersViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ACFollowersProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof EventAdapter.EventViewHolder) {

            ACFollowersbean singleStudent = (ACFollowersbean) followersList.get(position);
            ((ACFollowersViewHolder) holder).baseTextview_followers_name.setText(String.valueOf("Position" + position
            ));

            if (CommonMethods.isTextAvailable(singleStudent.getMessage())) {
                ((ACFollowersViewHolder) holder).baseTextview_followers_name.setText(singleStudent.getMessage());
            }


        } else {
            ((ACFollowersProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ACFollowersViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_followers_name = null;
        BaseTextview baseTextview_followers_address = null;
        BaseTextview baseTextview_followers_type = null;

        ImageView imageview_followers_profilepic=null;


        public ACFollowersViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_followers_name = (BaseTextview) v.findViewById(R.id.textview_acfollowers_name);
            baseTextview_followers_address = (BaseTextview) v.findViewById(R.id.textview_acfollowers_address);
            baseTextview_followers_type = (BaseTextview) v.findViewById(R.id.textview_acfollowers_type);
            imageview_followers_profilepic=(ImageView)v.findViewById(R.id.imageview_acfollowers_profilepic);
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


    public static class ACFollowersProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ACFollowersProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}