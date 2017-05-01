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
import com.app.collow.activities.ACListFAQsMainActivity;
import com.app.collow.activities.ACViewFaq;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACFAQbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;

/**
 * Created by harmis on 8/2/17.
 */

public class ACFAQAdapter extends RecyclerView.Adapter {


    Activity activity = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    String communityID = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public ACFAQAdapter(Activity mactivity, RecyclerView recyclerView) {
        activity = mactivity;
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
                R.layout.acfaq_single_item, parent, false);

        vh = new ACFaqViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


        ACFAQbean faqbean = ACListFAQsMainActivity.acfaqbeanArrayList.get(position);

        if (CommonMethods.isTextAvailable(faqbean.getAcfaq_faqtiitle())) {
            ((ACFaqViewHolder) holder).baseTextview_acfaq_title.setText(faqbean.getAcfaq_faqtiitle());

        }


        if (CommonMethods.isTextAvailable(faqbean.getAcfaq_faqdate())) {
            ((ACFaqViewHolder) holder).baseTextview_acfaq_date.setText(faqbean.getAcfaq_faqdate());

        }
        if (CommonMethods.isTextAvailable(faqbean.getAcfaq_faqtime())) {
            ((ACFaqViewHolder) holder).baseTextview_acfaq_time.setText(faqbean.getAcfaq_faqtime());

        }


        ((ACFaqViewHolder) holder).view.setTag(faqbean);
        ((ACFaqViewHolder) holder).view.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {

                if (isAvailableInternet()) {
                    ACFAQbean faqbean1_local = (ACFAQbean) v.getTag();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN, faqbean1_local);
                    Intent intent = new Intent(activity, ACViewFaq.class);
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                       /* bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, eventbean1_local.getAclist_eventdate());
                        bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, eventbean1_local.getAclist_eventtime());*/
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                    CommonMethods.displayLog("Details", "Details");
                }
            }
        });
    }


    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return ACListFAQsMainActivity.acfaqbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ACFaqViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_acfaq_title = null;
        BaseTextview baseTextview_acfaq_date = null;
        BaseTextview baseTextview_acfaq_time = null;

        ImageView imageview_acfaq = null;
        View view = null;

        public ACFaqViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_acfaq_title = (BaseTextview) v.findViewById(R.id.textview_acfaq_faqtitle);
            baseTextview_acfaq_date = (BaseTextview) v.findViewById(R.id.textview_acfaq_faqdate);
            baseTextview_acfaq_time = (BaseTextview) v.findViewById(R.id.textview_acfaqt_faqtime);

            imageview_acfaq = (ImageView) v.findViewById(R.id.imageview_acfaq_image);
            view = v;

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


    public static class ACFaqProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ACFaqProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}