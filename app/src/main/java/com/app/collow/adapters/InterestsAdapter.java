package com.app.collow.adapters;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.collow.R;
import com.app.collow.activities.EditProfileActivity;
import com.app.collow.activities.SplashActvitiy;
import com.app.collow.beans.Interestbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;

/**
 * Created by Harmis on 27/02/17.
 */

public class InterestsAdapter extends RecyclerView.Adapter {

    Activity activity = null;
    CommonSession commonSession = null;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public InterestsAdapter(RecyclerView recyclerView, Activity activity) {
        this.activity = activity;
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
                R.layout.edit_profile_grid_item, parent, false);

        vh = new InterestsViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        try {
            Drawable tempDrawable = activity.getResources().getDrawable(R.drawable.dot_bg);
            LayerDrawable bubble = (LayerDrawable) tempDrawable;
            GradientDrawable solidColor = (GradientDrawable) bubble.findDrawableByLayerId(R.id.outerRectangle);
            solidColor.setColor(SplashActvitiy.integerArrayList_colors.get(CommonMethods.randInt(0, SplashActvitiy.integerArrayList_colors.size() - 1)));
            ((InterestsViewHolder) holder).imageview_dot.setImageDrawable(tempDrawable);

            ((InterestsViewHolder) holder).countryName.setText(EditProfileActivity.interestbeanArrayList.get(position).getInterestName());
            Interestbean interestbean =  EditProfileActivity.interestbeanArrayList.get(position);

            if (interestbean.isInterestSelected()) {

                Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.button_transparent_background);
                LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
                GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
                int colorcode = 0;
                if (EditProfileActivity.interestbeanArrayList.get(position).getColorcode() != 0) {
                    colorcode = EditProfileActivity.interestbeanArrayList.get(position).getColorcode();

                } else {
                    colorcode = SplashActvitiy.integerArrayList_colors.get(CommonMethods.randInt(0, SplashActvitiy.integerArrayList_colors.size() - 1));

                }
                solidColor_main_bg.setColor(colorcode);
                solidColor_main_bg.setStroke(1, activity.getResources().getColor(R.color.white));
                ((InterestsViewHolder) holder).view_main.setBackground(tempDrawable_main_bg);
                ((InterestsViewHolder) holder).imageview_dot.setVisibility(View.GONE);
                interestbean.setColorcode(colorcode);
                EditProfileActivity.interestAutoCompleteTextview_select_your_interest.removeObject(interestbean);

                EditProfileActivity.interestAutoCompleteTextview_select_your_interest.addObject(interestbean);

            } else {

                Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.button_transparent_background);
                LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
                GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
                solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
                solidColor_main_bg.setStroke(1, activity.getResources().getColor(R.color.white));
                ((InterestsViewHolder) holder).view_main.setBackground(tempDrawable_main_bg);
                ((InterestsViewHolder) holder).imageview_dot.setVisibility(View.VISIBLE);


            }
            ((InterestsViewHolder) holder).view_main.setTag(String.valueOf(position));
            ((InterestsViewHolder) holder).view_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    int pos = Integer.parseInt(v.getTag().toString());
                    Interestbean interestbean1 = EditProfileActivity.interestbeanArrayList.get(pos);
                    if (interestbean1.isInterestSelected()) {
                        interestbean1.setInterestSelected(false);
                        EditProfileActivity.interestAutoCompleteTextview_select_your_interest.removeObject(interestbean1);


                    } else {
                        interestbean1.setInterestSelected(true);

                    }
                    EditProfileActivity.interestbeanArrayList.set(pos, interestbean1);
                    notifyItemChanged(pos);
                }
            });
        } catch (Resources.NotFoundException e) {
            CommonMethods.displayLog("error", e.getMessage());
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return EditProfileActivity.interestbeanArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    //
    public static class InterestsViewHolder extends RecyclerView.ViewHolder {
        public TextView countryName;
        ImageView imageview_dot = null;
        View view_main = null;

        public InterestsViewHolder(View itemView) {
            super(itemView);
            view_main = itemView;
            countryName = (TextView) itemView.findViewById(R.id.country_name);
            imageview_dot = (ImageView) itemView.findViewById(R.id.imageview_only_dot);
        }
    }


}
