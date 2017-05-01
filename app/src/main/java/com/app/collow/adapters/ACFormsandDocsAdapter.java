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

import com.app.collow.R;
import com.app.collow.activities.ACFormsandDocsMainActivity;
import com.app.collow.activities.ACViewFormsandDocs;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.ACFormsandDocsbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;

/**
 * Created by harmis on 23/2/17.
 */

public class ACFormsandDocsAdapter extends RecyclerView.Adapter {

    Activity activity = null;
    String communityID = null;
    View view;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;

    public ACFormsandDocsAdapter(Activity activity, RecyclerView recyclerView) {
        this.activity = activity;
        //  this.communityID=communityID;
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
                R.layout.aclist_forms_single_item, parent, false);

        vh = new ACDocumentViewHolder(v);

        return vh;
    }

    @Override

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ACFormsandDocsbean documentbean = ACFormsandDocsMainActivity.acdocumentbeanArrayList.get(position);


        if (CommonMethods.isTextAvailable(documentbean.getAclist_documenttiitle())) {
            ((ACDocumentViewHolder) holder).baseTextview_aclistdocument_title.setText(documentbean.getAclist_documenttiitle());
        }
        if (CommonMethods.isTextAvailable(documentbean.getAclist_documentdate())) {
            ((ACDocumentViewHolder) holder).baseTextview_aclistdocument_time.setText(documentbean.getAclist_documentdate());


        }
        if (CommonMethods.isTextAvailable(documentbean.getAclist_documentdate())) {
            ((ACDocumentViewHolder) holder).baseTextview_aclistdocument_date.setText(documentbean.getAclist_documentdate());
        }


       /* if (CommonMethods.isImageUrlValid(documentbean.getAclist_image())) {

            Picasso.with(activity)
                    .load(documentbean.getAclist_image())
                    .into(((ACDocumentViewHolder) holder).imageview_aclistdocument, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            ((ACDocumentViewHolder) holder).imageview_aclistdocument.setImageResource(R.drawable.defualt_square);

                        }
                    });

        } else {
            ((ACDocumentViewHolder) holder).imageview_aclistdocument.setImageResource(R.drawable.defualt_square);
        }*/

        ((ACDocumentViewHolder) holder).view_click.setTag(documentbean);


        ((ACDocumentViewHolder) holder).view_click.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {

                if (isAvailableInternet()) {
                    ACFormsandDocsbean documentbean1_local = (ACFormsandDocsbean) v.getTag();

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BundleCommonKeywords.KEY_CUSTOM_CLASS_BEAN, documentbean1_local);
                    Intent intent = new Intent(activity, ACViewFormsandDocs.class);
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
        return ACFormsandDocsMainActivity.acdocumentbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ACDocumentViewHolder extends RecyclerView.ViewHolder {

        BaseTextview baseTextview_aclistdocument_title = null;
        BaseTextview baseTextview_aclistdocument_date = null;
        BaseTextview baseTextview_aclistdocument_time = null;
        ImageView imageView_aclistdocument_rightarrow = null;

        ImageView imageview_aclistdocument = null;

        View view_click = null;

        public ACDocumentViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_aclistdocument_title = (BaseTextview) v.findViewById(R.id.textview_aclistforms_doctitle);
            baseTextview_aclistdocument_date = (BaseTextview) v.findViewById(R.id.textview_aclistforms_docdate);
            baseTextview_aclistdocument_time = (BaseTextview) v.findViewById(R.id.textview_aclistforms_doctime);

            imageview_aclistdocument = (ImageView) v.findViewById(R.id.imageview_aclistforms_image);
            imageView_aclistdocument_rightarrow = (ImageView) v.findViewById(R.id.imageview_aclistforms_rightarrow);
            view_click = v;


        }
    }


}
