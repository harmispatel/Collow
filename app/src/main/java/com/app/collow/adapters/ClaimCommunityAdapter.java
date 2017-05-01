package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.activities.ClaimeCommunityActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ClaimCommunitybean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;

/**
 * Created by Harmis on 16/02/17.
 */

public class ClaimCommunityAdapter extends RecyclerView.Adapter {


    Activity activity = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public ClaimCommunityAdapter(RecyclerView recyclerView, Activity activity) {
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
                R.layout.claim_community_single_item, parent, false);

        vh = new ClaimeViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        try {
            ClaimCommunitybean claimCommunitybean = ClaimeCommunityActivity.claimCommunitybeanArrayList.get(position);

            if (CommonMethods.isTextAvailable(claimCommunitybean.getMgmtName())) {
                ((ClaimeViewHolder) holder).baseTextview_mgmt_name.setText(claimCommunitybean.getMgmtName());
            }

            if (claimCommunitybean.isNeedToTrueIconDisplay()) {
                ((ClaimeViewHolder) holder).imageview_true_icon.setVisibility(View.VISIBLE);
            } else {
                ((ClaimeViewHolder) holder).imageview_true_icon.setVisibility(View.GONE);

            }


            ((ClaimeViewHolder) holder).view.setTag(claimCommunitybean);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return ClaimeCommunityActivity.claimCommunitybeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ClaimeViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_mgmt_name = null;
        ImageView imageview_true_icon = null;

        View view = null;


        public ClaimeViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_mgmt_name = (BaseTextview) v.findViewById(R.id.textview_mgm_name);

            imageview_true_icon = (ImageView) v.findViewById(R.id.imageview_true_icon);

            view = v;


        }
    }


}
