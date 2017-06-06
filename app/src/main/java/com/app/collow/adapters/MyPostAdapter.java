package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.activities.MyPostActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.MyPostbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

/**
 * Created by harmis on 12/1/17.
 */
public class MyPostAdapter extends RecyclerView.Adapter {


    Activity activity = null;
    boolean isNeedToDisplayCheckbox = false;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public MyPostAdapter(Activity activity, RecyclerView recyclerView) {
        this.activity = activity;
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
                R.layout.my_post_single_raw, parent, false);

        vh = new MyPostViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {


        final MyPostbean myPostbean = MyPostActivity.myPostbeanArrayList.get(position);


        if (CommonMethods.isTextAvailable(myPostbean.getTitle())) {
            ((MyPostViewHolder) holder).baseTextview_title.setText(myPostbean.getTitle());

        }

        if (CommonMethods.isTextAvailable(myPostbean.getNewsDate())) {
            ((MyPostViewHolder) holder).baseTextview_date.setText(myPostbean.getNewsDate());

        }

        if (CommonMethods.isImageUrlValid(myPostbean.getImage())) {
            ((MyPostViewHolder) holder).progressBar.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(myPostbean.getImage())
                    .into(((MyPostViewHolder) holder).imageView_my_post, new Callback() {
                        @Override
                        public void onSuccess() {
                            ((MyPostViewHolder) holder).progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            ((MyPostViewHolder) holder).imageView_my_post.setImageResource(R.drawable.defualt_square);
                            ((MyPostViewHolder) holder).progressBar.setVisibility(View.GONE);

                        }
                    });
        } else {
            ((MyPostViewHolder) holder).imageView_my_post.setImageResource(R.drawable.defualt_square);
            ((MyPostViewHolder) holder).progressBar.setVisibility(View.GONE);

        }



        if (isNeedToDisplayCheckbox) {
            ((MyPostViewHolder) holder).chkSelected.setVisibility(View.VISIBLE);

        } else {
            ((MyPostViewHolder) holder).chkSelected.setVisibility(View.GONE);

        }

       // ((MyPostViewHolder) holder).chkSelected.setChecked(myPostbean.isSelected());


        ((MyPostViewHolder) holder).chkSelected.setTag(String.valueOf(myPostbean.getPosition()));

      /*  ((MyPostViewHolder) holder).chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                int position = Integer.parseInt(v.getTag().toString());


                MyPostbean myPostbean1 = MyPostActivity.myPostbeanArrayList.get(position);
                if (cb.isChecked()) {
                    myPostbean1.setSelected(false);
                } else {
                    myPostbean1.setSelected(true);
                }
                MyPostActivity.myPostbeanArrayList.set(position, myPostbean1);
                notifyItemChanged(position);


            }
        });*/
        ((MyPostViewHolder) holder).view.setTag(myPostbean);

        ((MyPostViewHolder) holder).view.setOnClickListener(new MyOnClickListener(activity) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    if (isAvailableInternet()) {
                        // MyPostbean myPostbean= (MyPostbean) v.getTag();
                        //MyUtils.openCommunityFeedActivity(activity, ScreensEnums.MY_FOLLOWING_LISTING.getScrenIndex(),myPostbean.getCommunityID(),myPostbean.getCommunityName(),followingbean1_local.getCommunityAccessbean());

                    } else {
                        showInternetNotfoundMessage();
                    }


                } else {
                    showInternetNotfoundMessage();
                }
            }
        });
        ((MyPostViewHolder) holder).view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                isNeedToDisplayCheckbox = true;
                notifyDataSetChanged();


                return false;
            }
        });

    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return MyPostActivity.myPostbeanArrayList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class MyPostViewHolder extends RecyclerView.ViewHolder {
        public CheckBox chkSelected;
        public View view = null;
        public BaseTextview baseTextview_title = null;
        public BaseTextview baseTextview_date = null;
        ProgressBar progressBar = null;
        ImageView imageView_my_post = null;


        public MyPostViewHolder(View v) {
            super(v);
            chkSelected = (CheckBox) v
                    .findViewById(R.id.chkSelected);
            view = v;
            baseTextview_title = (BaseTextview) view.findViewById(R.id.textview_post_title);
            baseTextview_date = (BaseTextview) view.findViewById(R.id.textview_post_date);
            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            imageView_my_post = (ImageView) view.findViewById(R.id.imageview_mypost);
            //define here findview by ids

        }
    }


}