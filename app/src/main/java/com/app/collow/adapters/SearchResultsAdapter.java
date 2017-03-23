package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.activities.BaseActivity;
import com.app.collow.activities.CommunitySearchFilterOptionsActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Searchbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.database.CollowDatabaseHandler;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by harmis on 12/1/17.
 */
public class SearchResultsAdapter extends RecyclerView.Adapter  {



    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    Activity activity=null;
    boolean isFromSearchScreen=false;
    CollowDatabaseHandler collowDatabaseHandler=null;
    CommonSession commonSession=null;


    public SearchResultsAdapter(Activity activity, RecyclerView recyclerView, boolean isFromSearchScreen) {
        this.activity=activity;
        this.isFromSearchScreen=isFromSearchScreen;
        collowDatabaseHandler=new CollowDatabaseHandler(activity);
        commonSession=new CommonSession(activity);
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
                R.layout.search_single_raw_item, parent, false);

        vh = new SearchViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SearchViewHolder) {
            //setup here data

            try {
                Searchbean searchbean = BaseActivity.searchbeanArrayList_new.get(position);

                if(searchbean.getCommunityName()==null||searchbean.getCommunityName().equals(""))
                {

                }
                else
                {
                    ((SearchViewHolder) holder).baseTextview_community_name.setText(searchbean.getCommunityName());
                }


                StringBuffer stringBuffer=new StringBuffer();

                if(CommonMethods.isTextAvailable(searchbean.getAddress()))
                {
                    stringBuffer.append(searchbean.getAddress());
                    stringBuffer.append(" ");

                }

                if(CommonMethods.isTextAvailable(searchbean.getCity()))
                {
                    stringBuffer.append(searchbean.getCity());
                    stringBuffer.append(" ");

                }

                if(CommonMethods.isTextAvailable(searchbean.getState()))
                {
                    stringBuffer.append(searchbean.getState());
                    stringBuffer.append(" ");

                }

                if(CommonMethods.isTextAvailable(searchbean.getCountry()))
                {
                    stringBuffer.append(searchbean.getCountry());
                }


                ((SearchViewHolder) holder).baseTextview_address.setText(stringBuffer.toString());

                final SearchViewHolder searchViewHolder= (SearchViewHolder) holder;

                if(CommonMethods.isImageUrlValid(searchbean.getCommuntiyImageURL()))
                {
                    Picasso.with(activity)
                            .load(searchbean.getCommuntiyImageURL())
                            .into(((SearchViewHolder) holder).imageView_community, new Callback() {
                                @Override
                                public void onSuccess() {
                                    searchViewHolder.imageView_community.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError() {
                                }
                            });
                }

                ((SearchViewHolder) holder).view.setTag(searchbean);

                ((SearchViewHolder) holder).view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            Searchbean searchbean_local= (Searchbean) v.getTag();
                            if(isFromSearchScreen)
                            {
                                collowDatabaseHandler.saveSearchedHistory(searchbean_local.getCommunityName(),commonSession.getLoggedUserID());

                                if(CommunitySearchFilterOptionsActivity.recyclerView_recent_searches!=null)
                                {
                                    List<Searchbean>searchbeanList_search_history = collowDatabaseHandler.getSearchedCommunities(commonSession.getLoggedUserID());
                                    CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.removeAllViews();;
                                    SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(activity, searchbeanList_search_history);
                                    CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.setAdapter(searchHistoryAdapter);
                                    if(CommunitySearchFilterOptionsActivity.recyclerView_recent_searches!=null)
                                    {
                                        CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.setVisibility(View.VISIBLE);
                                    }
                                    if(CommunitySearchFilterOptionsActivity.baseTextview_empty_recent_searches!=null)
                                    {
                                        CommunitySearchFilterOptionsActivity.baseTextview_empty_recent_searches.setVisibility(View.GONE);
                                    }

                                }


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return BaseActivity.searchbeanArrayList_new.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }



    //
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_community_name=null,baseTextview_address=null;
        ImageView  imageView_community=null;
        View view=null;

        public SearchViewHolder(View v) {
            super(v);
            baseTextview_community_name= (BaseTextview) v.findViewById(R.id.textview_search_community_name);
            baseTextview_address= (BaseTextview) v.findViewById(R.id.textview_search_community_address);
            imageView_community= (ImageView) v.findViewById(R.id.imageview_search_item);
            view=v;

            //define here findview by ids

        }
    }


}