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
import com.app.collow.beans.FormsAndDocsbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonMethods;

import java.util.List;

/**
 * Created by harmis on 1/2/17.
 */

public class FormsAndDocsAdapter extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;

    private List<FormsAndDocsbean> formsanddocslist;

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public FormsAndDocsAdapter(List<FormsAndDocsbean> formsanddocs, RecyclerView recyclerView) {
        formsanddocslist = formsanddocs;

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
        return formsanddocslist.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.formsanddocs_single_item, parent, false);

            vh = new FormsAndDocsViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new FormsAndDocsProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof FormsAndDocsViewHolder) {

           FormsAndDocsbean singleStudent = (FormsAndDocsbean) formsanddocslist.get(position);
            ((FormsAndDocsViewHolder) holder).baseTextview_formsanddocs_category.setText(String.valueOf("Position" + position
            ));

            if (CommonMethods.isTextAvailable(singleStudent.getMessage())) {
                ((FormsAndDocsViewHolder) holder).baseTextview_formsanddocs_category.setText(singleStudent.getMessage());
            }


        } else {
            ((FormsAndDocsProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return formsanddocslist.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class FormsAndDocsViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_formsanddocs_category = null;
        BaseTextview baseTextview_formsanddocs_time = null;
        BaseTextview baseTextview_formsanddocs_download = null;




        public FormsAndDocsViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_formsanddocs_category = (BaseTextview) v.findViewById(R.id.textview_formsanddocs_category);
            baseTextview_formsanddocs_time = (BaseTextview) v.findViewById(R.id.textview_formsanddocs_time);
            baseTextview_formsanddocs_download = (BaseTextview) v.findViewById(R.id.textview_formsanddocs_downloads);


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


    public static class FormsAndDocsProgressViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public FormsAndDocsProgressViewHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}

