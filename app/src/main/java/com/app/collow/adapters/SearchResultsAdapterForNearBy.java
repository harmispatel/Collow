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
import com.app.collow.activities.CommunitySearchResultsActivity;
import com.app.collow.activities.CommunityInformationActivity;
import com.app.collow.allenums.CommunityInformationScreenEnum;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Searchbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.MyUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by Harmis on 11/03/17.
 */

/**
 * Created by harmis on 12/1/17.
 */
public class SearchResultsAdapterForNearBy extends RecyclerView.Adapter {


    Activity activity = null;
    boolean isFromSearchScreen = false;
    CommonSession commonSession = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public SearchResultsAdapterForNearBy(Activity activity, RecyclerView recyclerView, boolean isFromSearchScreen) {
        this.activity = activity;
        this.isFromSearchScreen = isFromSearchScreen;
        commonSession = new CommonSession(activity);
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
                final Searchbean searchbean = CommunitySearchResultsActivity.searchbeanArrayList_new.get(position);


                if (searchbean.getCommunityAccessbean().isHomeDefualtCommunity()) {
                    ((SearchViewHolder) holder).imageView_home_as_searhc.setImageResource(R.drawable.homeselected);
                } else {
                    ((SearchViewHolder) holder).imageView_home_as_searhc.setImageResource(R.drawable.homeunselected);

                }


                if (searchbean.getCommunityName() == null || searchbean.getCommunityName().equals("")) {

                } else {
                    ((SearchViewHolder) holder).baseTextview_community_name.setText(searchbean.getCommunityName());
                }


                StringBuffer stringBuffer = new StringBuffer();

                if (CommonMethods.isTextAvailable(searchbean.getAddress())) {
                    stringBuffer.append(searchbean.getAddress());
                    stringBuffer.append(" ");

                }

                if (CommonMethods.isTextAvailable(searchbean.getCity())) {
                    stringBuffer.append(searchbean.getCity());
                    stringBuffer.append(" ");

                }

                if (CommonMethods.isTextAvailable(searchbean.getState())) {
                    stringBuffer.append(searchbean.getState());
                    stringBuffer.append(" ");

                }

                if (CommonMethods.isTextAvailable(searchbean.getCountry())) {
                    stringBuffer.append(searchbean.getCountry());
                }


                ((SearchViewHolder) holder).baseTextview_address.setText(stringBuffer.toString());

                final SearchViewHolder searchViewHolder = (SearchViewHolder) holder;

                if (CommonMethods.isImageUrlValid(searchbean.getCommuntiyImageURL())) {
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
                            Searchbean searchbean_local = (Searchbean) v.getTag();

                            //if this is true means adapter called from communtiy search screen so need to store in db for managing community search history
                            if (isFromSearchScreen) {

                                // this is saving in db


                                commonSession.storeCommunityTypeId(searchbean_local.getCommunityID());

                                Intent intent = new Intent(activity, CommunityInformationActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.FROM_COMMUNITY_SEARCH.getIndexFromWhereCalledCommunityInformation());
                                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, searchbean_local.getCommunityID());
                                bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, searchbean_local.getCommunityAccessbean());

                                intent.putExtras(bundle);
                                activity.startActivity(intent);


                            } else {
                                commonSession.storeCommunityTypeId(searchbean_local.getCommunityID());

                                Intent intent = new Intent(activity, CommunityInformationActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
                                bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, searchbean_local.getCommunityID());
                                bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, searchbean_local.getCommunityAccessbean());

                                intent.putExtras(bundle);
                                activity.startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            CommonMethods.displayLog(SearchResultsAdapterForNearBy.class.getName(), e.getMessage());
                        }


                    }
                });

                ((SearchViewHolder) holder).imageView_home_as_searhc.setTag(searchbean);
                ((SearchViewHolder) holder).imageView_home_as_searhc.setOnClickListener(new MyOnClickListener(activity) {
                    @Override
                    public void onClick(View v) {

                        if (isAvailableInternet()) {

                            Searchbean searchbean = (Searchbean) v.getTag();
                            MyUtils.makeAsHomeCommunity(activity, searchbean.getCommunityID(), ScreensEnums.SEARCH_BY_COMMUNITYNAME.getScrenIndex());


                        } else {
                            showInternetNotfoundMessage();
                        }

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                CommonMethods.displayLog(SearchResultsAdapterForNearBy.class.getName(), e.getMessage());

            }


        } else {
        }
    }


    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return CommunitySearchResultsActivity.searchbeanArrayList_new.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_community_name = null, baseTextview_address = null;
        ImageView imageView_community = null;
        ImageView imageView_home_as_searhc = null;
        View view = null;

        public SearchViewHolder(View v) {
            super(v);
            baseTextview_community_name = (BaseTextview) v.findViewById(R.id.textview_search_community_name);
            baseTextview_address = (BaseTextview) v.findViewById(R.id.textview_search_community_address);
            imageView_community = (ImageView) v.findViewById(R.id.imageview_search_item);
            imageView_home_as_searhc = (ImageView) v.findViewById(R.id.imageview_home_in_search_result);
            view = v;

            //define here findview by ids

        }
    }


}