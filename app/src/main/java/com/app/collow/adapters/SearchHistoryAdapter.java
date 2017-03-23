package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Searchbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.database.CollowDatabaseHandler;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Harmis on 31/01/17.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter  {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Activity activity=null;
    boolean isFromSearchScreen=false;
    CollowDatabaseHandler collowDatabaseHandler=null;
    CommonSession commonSession=null;
    ArrayList<Searchbean> searchbeanArrayList=null;


    public SearchHistoryAdapter(Activity activity, RecyclerView recyclerView, boolean isFromSearchScreen, ArrayList<Searchbean> searchbeanArrayList) {
        this.activity=activity;
        this.isFromSearchScreen=isFromSearchScreen;
        collowDatabaseHandler=new CollowDatabaseHandler(activity);
        commonSession=new CommonSession(activity);
        this.searchbeanArrayList=searchbeanArrayList;
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
        return searchbeanArrayList.get(position) != null ? VIEW_ITEM : VIEW_PROG;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == VIEW_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.search_history_raw, parent, false);

            vh = new SearchHistoryViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar_item, parent, false);

            vh = new SearchProgressHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchHistoryViewHolder) {
            //setup here data

            try {
                Searchbean searchbean = searchbeanArrayList.get(position);

                if(searchbean.getCommunityName()==null||searchbean.getCommunityName().equals(""))
                {

                }
                else
                {
                   ((SearchHistoryViewHolder) holder).baseTextview_community_name.setText(searchbean.getCommunityName());
                }
                
                ((SearchHistoryViewHolder) holder).imageView_delete_search_history.setTag(searchbean);

                ((SearchHistoryViewHolder) holder).imageView_delete_search_history.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Searchbean searchbean1= (Searchbean) v.getTag();
                            collowDatabaseHandler.deleteSearchedHistory(searchbean1.getCommunityID());
                            searchbeanArrayList.remove(searchbean1);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                


            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            ((SearchProgressHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return searchbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }



    //
    public static class SearchHistoryViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_community_name=null;
        ImageView imageView_delete_search_history=null;

        public SearchHistoryViewHolder(View v) {
            super(v);
            baseTextview_community_name= (BaseTextview) v.findViewById(R.id.textview_search_community_name_search_history);
            imageView_delete_search_history= (ImageView) v.findViewById(R.id.imageview_search_history_delete);
            //define here findview by ids

        }
    }

    public static class SearchProgressHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public SearchProgressHolder(View v) {
            super(v);
            progressBar = (ProgressBar) v.findViewById(R.id.progressBar1);
        }
    }
}
