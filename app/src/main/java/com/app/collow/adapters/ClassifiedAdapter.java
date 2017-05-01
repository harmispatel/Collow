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
import com.app.collow.activities.ClassifiedDetailActivity;
import com.app.collow.activities.ClassifiedMainActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Classifiedbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by harmis on 1/2/17.
 */

public class ClassifiedAdapter extends RecyclerView.Adapter {


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Activity activity=null;
    String communityID=null;


    public ClassifiedAdapter( RecyclerView recyclerView, Activity activity,String communityID) {
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
                    R.layout.classfied_single_item, parent, false);

            vh = new ClassifiedViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        try {
            Classifiedbean classifiedbean =  ClassifiedMainActivity.classifiedbeanArrayList.get(position);

            if(CommonMethods.isTextAvailable(classifiedbean.getClassified_name()))
            {
                ((ClassifiedViewHolder) holder).baseTextview_classifed_name.setText(classifiedbean.getClassified_name());
            }

            if(CommonMethods.isTextAvailable(classifiedbean.getClssified_price()))
            {
                ((ClassifiedViewHolder) holder).baseTextview_classified_price.setText(classifiedbean.getClssified_price());
            }

            if(CommonMethods.isTextAvailable(classifiedbean.getStatus()))
            {
                ((ClassifiedViewHolder) holder).baseTextview_classified_status.setText(classifiedbean.getStatus());
            }
            else
            {
                ((ClassifiedViewHolder) holder).baseTextview_classified_status.setVisibility(View.GONE);

            }
            if(CommonMethods.isTextAvailable(classifiedbean.getClassified_date()))
            {
                ((ClassifiedViewHolder) holder).baseTextview_classified_date.setText(classifiedbean.getClassified_date());
            }

            ArrayList<String> stringArrayList=classifiedbean.getStringArrayList_images();
            if(stringArrayList.size()!=0)
            {
                String firstURL=stringArrayList.get(0);
                Picasso.with(activity)
                        .load(firstURL)
                        .into((((ClassifiedViewHolder) holder).imageview_classifiedimage), new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError() {
                                ((ClassifiedViewHolder) holder).imageview_classifiedimage.setImageResource(R.drawable.defualt_square);
                            }
                        });
            }
            else
            {
                ((ClassifiedViewHolder) holder).imageview_classifiedimage.setImageResource(R.drawable.defualt_square);

            }


            ((ClassifiedViewHolder) holder).view.setTag(classifiedbean);
            ((ClassifiedViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Classifiedbean classifiedbean1= (Classifiedbean) v.getTag();
                    Intent intent=new Intent(activity, ClassifiedDetailActivity.class);
                    Bundle  bundle=new Bundle();
                    bundle.putSerializable(BundleCommonKeywords.KEY_CLASSIFIED_BEAN,classifiedbean1);
                    bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID,communityID);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return ClassifiedMainActivity.classifiedbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ClassifiedViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_classifed_name = null;
        BaseTextview baseTextview_classified_price = null;
        BaseTextview baseTextview_classified_date = null;
        BaseTextview baseTextview_classified_status = null;
        ImageView imageview_classifiedimage=null;

        View view=null;



        public ClassifiedViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_classifed_name = (BaseTextview) v.findViewById(R.id.textview_classified_name);
            baseTextview_classified_price = (BaseTextview) v.findViewById(R.id.textview_classified_price);
            baseTextview_classified_date = (BaseTextview) v.findViewById(R.id.textview_classified_date);
            baseTextview_classified_status = (BaseTextview) v.findViewById(R.id.textview_classified_status);
            imageview_classifiedimage=(ImageView)v.findViewById(R.id.imageview_classfied_image);

            view=v;


        }
    }



}
