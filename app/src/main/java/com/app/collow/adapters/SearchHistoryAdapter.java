package com.app.collow.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.activities.CommunitySearchByNameActivity;
import com.app.collow.activities.CommunitySearchFilterOptionsActivity;
import com.app.collow.allenums.CommunityInformationScreenEnum;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Searchbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.database.CollowDatabaseHandler;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;

import java.util.List;

/**
 * Created by Harmis on 31/01/17.
 */

public class SearchHistoryAdapter extends RecyclerView.Adapter  {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    Activity activity=null;
    CollowDatabaseHandler collowDatabaseHandler=null;
    CommonSession commonSession=null;
    List<Searchbean> searchbeanArrayList=null;


    public SearchHistoryAdapter(Activity activity,  List<Searchbean> searchbeanArrayList) {
        this.activity=activity;
        collowDatabaseHandler=new CollowDatabaseHandler(activity);
        commonSession=new CommonSession(activity);
        this.searchbeanArrayList=searchbeanArrayList;

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
                            collowDatabaseHandler.deleteSearchedHistory(searchbean1.getCommunityName());
                            searchbeanArrayList.remove(searchbean1);
                            notifyDataSetChanged();

                            if(searchbeanArrayList.size()==0)
                            {
                                if(CommunitySearchFilterOptionsActivity.recyclerView_recent_searches!=null)
                                {
                                    CommunitySearchFilterOptionsActivity.recyclerView_recent_searches.setVisibility(View.GONE);
                                }
                                if(CommunitySearchFilterOptionsActivity.baseTextview_empty_recent_searches!=null)
                                {
                                    CommunitySearchFilterOptionsActivity.baseTextview_empty_recent_searches.setVisibility(View.VISIBLE);
                                }
                            }


                            CommonMethods.displayLog("Clicked","Clicked");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                ((SearchHistoryViewHolder) holder).baseTextview_community_name.setTag(searchbean.getCommunityName());

                ((SearchHistoryViewHolder) holder).baseTextview_community_name.setOnClickListener(new MyOnClickListener(activity) {
                    @Override
                    public void onClick(View v) {
                        if(isAvailableInternet())
                        {
                            //here passed already serched community text to search screen where search result comes based on that.
                            if (CommunitySearchByNameActivity.communitySearchByNameActivity != null) {
                                CommunitySearchByNameActivity.communitySearchByNameActivity.finish();
                            }
                            String text_need_search= (String) v.getTag();
                            Intent intent=new Intent(activity, CommunitySearchByNameActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_SEARCH_HISTORY_ITEM_NAME,text_need_search);
                            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.FROM_COMMUNITY_SEARCH_HISTORY.getIndexFromWhereCalledCommunityInformation());
                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                        else
                        {
                            showInternetNotfoundMessage();
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



    @Override
    public int getItemCount() {
        return searchbeanArrayList.size();
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
