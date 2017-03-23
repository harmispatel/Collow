package com.app.collow.adapters;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.activities.ClassifiedMainActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Classifiedbean;
import com.app.collow.beans.Gallerybean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonMethods;

import java.util.List;

/**
 * Created by harmis on 1/2/17.
 */

public class ClassifiedAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<Classifiedbean> classifiedbeanlist;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public ClassifiedAdapter(List<Classifiedbean> classified, RecyclerView recyclerView) {
        classifiedbeanlist = classified;

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
        return classifiedbeanlist.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.classfied_single_item, parent, false);

            vh = new ClassifiedViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new ClassifiedProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ClassifiedViewHolder) {

            Classifiedbean singleStudent = (Classifiedbean) classifiedbeanlist.get(position);
            ((ClassifiedViewHolder) holder).baseTextview_classifed_name.setText(String.valueOf("Position" + position
            ));

            if (CommonMethods.isTextAvailable(singleStudent.getMessage())) {
                ((ClassifiedViewHolder) holder).baseTextview_classifed_name.setText(singleStudent.getMessage());
            }


        } else {
            ((ClassifiedProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return classifiedbeanlist.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class ClassifiedViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_classifed_name = null;
        BaseTextview baseTextview_classified_price = null;
        BaseTextview baseTextview_classified_date = null;
        BaseTextview baseTextview_classified_category = null;
        ImageView imageview_classifiedimage=null;



        public ClassifiedViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_classifed_name = (BaseTextview) v.findViewById(R.id.textview_classified_name);
            baseTextview_classified_price = (BaseTextview) v.findViewById(R.id.textview_classified_price);
            baseTextview_classified_date = (BaseTextview) v.findViewById(R.id.textview_classified_date);
            baseTextview_classified_category = (BaseTextview) v.findViewById(R.id.textview_classified_category);
            imageview_classifiedimage=(ImageView)v.findViewById(R.id.imageview_classfied_image);


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


    public static class ClassifiedProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public ClassifiedProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
