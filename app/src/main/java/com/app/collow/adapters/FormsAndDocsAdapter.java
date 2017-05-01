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
import com.app.collow.activities.FormsAndDocsMainActivity;
import com.app.collow.activities.FormsAndDocsDetailActivity;
import com.app.collow.activities.NewsDetailActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.FormsAndDocsbean;
import com.app.collow.beans.Newsbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;

/**
 * Created by harmis on 1/2/17.
 */

public class FormsAndDocsAdapter extends RecyclerView.Adapter {


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Activity activity=null;


    public FormsAndDocsAdapter(Activity activity,RecyclerView recyclerView) {
        this.activity=activity;
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
                    R.layout.formsanddocs_single_item, parent, false);

            vh = new FormsAndDocsViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

           FormsAndDocsbean formsAndDocsbean = FormsAndDocsMainActivity.formsanddocsbeanlist.get(position);

          if(CommonMethods.isTextAvailable(formsAndDocsbean.getFormsanddocs_title()))
          {
              ((FormsAndDocsViewHolder) holder).baseTextview_formsanddocs_title.setText(formsAndDocsbean.getFormsanddocs_title());
          }

        if(CommonMethods.isTextAvailable(formsAndDocsbean.getFormsanddocs_download()))
        {
            ((FormsAndDocsViewHolder) holder).baseTextview_formsanddocs_download.setText(formsAndDocsbean.getFormsanddocs_download());
        }if(CommonMethods.isTextAvailable(formsAndDocsbean.getFormsanddocs_date()))
        {
            ((FormsAndDocsViewHolder) holder).baseTextview_formsanddocs_time.setText(formsAndDocsbean.getFormsanddocs_date());
        }


        ((FormsAndDocsViewHolder) holder).view.setTag(formsAndDocsbean);
            ((FormsAndDocsViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(activity, FormsAndDocsDetailActivity.class);
                    FormsAndDocsbean formsAndDocsbean1= (FormsAndDocsbean) v.getTag();
                    Bundle bundle=new Bundle();
                    bundle.putSerializable(BundleCommonKeywords.KEY_FORMANDDOCUMENT_BEAN,formsAndDocsbean1);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);

                }
            });


    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return FormsAndDocsMainActivity.formsanddocsbeanlist.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class FormsAndDocsViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_formsanddocs_title = null;
        BaseTextview baseTextview_formsanddocs_time = null;
        BaseTextview baseTextview_formsanddocs_download = null;
        ImageView imageView_aclistevent_rightarrow=null;
        View view=null;




        public FormsAndDocsViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_formsanddocs_title = (BaseTextview) v.findViewById(R.id.textview_formsanddocs_title);
            baseTextview_formsanddocs_time = (BaseTextview) v.findViewById(R.id.textview_formsanddocs_time);
            baseTextview_formsanddocs_download = (BaseTextview) v.findViewById(R.id.textview_formsanddocs_downloads);
            imageView_aclistevent_rightarrow=(ImageView)v.findViewById(R.id.imageview_myactivities_rightarrow) ;
            view=v;


        }
    }


}

