package com.app.collow.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.activities.GalleryMainActivity;
import com.app.collow.allenums.CommunityInformationScreenEnum;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Menubean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.SubMenubean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.database.CollowDatabaseHandler;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.MyUtils;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * Created by Harmis on 01/02/17.
 */

public class MenuAdapter extends ExpandableRecyclerViewAdapter<MenuAdapter.MainCategoryViewHolder, MenuAdapter.SubCategoryViewHolder> {


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    Activity activity = null;
    CollowDatabaseHandler collowDatabaseHandler = null;
    CommonSession commonSession = null;
    String communityID = null;
    CommunityAccessbean communityAccessbean = null;
    private LayoutInflater mInflater;

    public MenuAdapter(List<? extends ExpandableGroup> groups, Activity activity, String communityID, CommunityAccessbean communityAccessbean) {
        super(groups);
        this.activity = activity;
        this.communityID = communityID;
        this.communityAccessbean = communityAccessbean;
        mInflater = LayoutInflater.from(activity);
        collowDatabaseHandler = new CollowDatabaseHandler(activity);
        commonSession = new CommonSession(activity);
    }


    @NonNull
    @Override
    public SubCategoryViewHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View recipeView = mInflater.inflate(R.layout.community_menu_single_item, childViewGroup, false);
        return new SubCategoryViewHolder(recipeView);
    }

    @Override
    public void onBindChildViewHolder(SubCategoryViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final SubMenubean subMenubean = (SubMenubean) group.getItems().get(childIndex);
        holder.bind(subMenubean);

    }

    @Override
    public void onBindGroupViewHolder(MainCategoryViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.bind(group);

    }


    @Override
    public MainCategoryViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View recipeView = mInflater.inflate(R.layout.community_menu_single_item, parent, false);
        return new MainCategoryViewHolder(recipeView);
    }


    public class MainCategoryViewHolder extends GroupViewHolder {

        BaseTextview baseTextview_menu_name = null;
        ImageView imageView_menu_circle = null;
        View view = null;

        public MainCategoryViewHolder(View v) {
            super(v);
            baseTextview_menu_name = (BaseTextview) v.findViewById(R.id.textview_community_menu_name);
            imageView_menu_circle = (ImageView) v.findViewById(R.id.imageview_menu_dot);
            view = v;
        }

        public void bind(ExpandableGroup genre) {

            if (genre instanceof Menubean) {
                baseTextview_menu_name.setText(genre.getTitle());
                Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.menu_dot_bg);
                LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
                GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
                solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
                solidColor_main_bg.setStroke(3, ((Menubean) genre).getIconResId());
                imageView_menu_circle.setBackground(tempDrawable_main_bg);
                Menubean menubean = (Menubean) genre;


                view.setTag(menubean);

                if (communityAccessbean.isCommunityClaimedAndApproved()) {

                    if (menubean.getTitle().equals(activity.getResources().getString(R.string.info))) {

                    } else {
                        view.setOnClickListener(new MyOnClickListener(activity) {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Menubean menubean1 = (Menubean) v.getTag();
                                    RequestParametersbean requestParametersbean = new RequestParametersbean();
                                    requestParametersbean.setCommunityID(communityID);
                                    MyUtils.menuItemOpenByItsName(activity, menubean1.getTitle(), 0, requestParametersbean, communityAccessbean);

                                } catch (Resources.NotFoundException e) {
                                    e.printStackTrace();
                                }


                            }
                        });
                    }


                } else {
                    view.setOnClickListener(new MyOnClickListener(activity) {
                        @Override
                        public void onClick(View v) {
                            try {
                                Menubean menubean1 = (Menubean) v.getTag();
                                RequestParametersbean requestParametersbean = new RequestParametersbean();
                                requestParametersbean.setCommunityID(communityID);
                                MyUtils.menuItemOpenByItsName(activity, menubean1.getTitle(), 0, requestParametersbean, communityAccessbean);


                            } catch (Resources.NotFoundException e) {
                                e.printStackTrace();
                            }


                        }
                    });
                }
            }


        }
    }

    public class SubCategoryViewHolder extends ChildViewHolder {

        BaseTextview baseTextview_menu_name = null;
        ImageView imageView_menu_circle = null;
        View view = null;

        public SubCategoryViewHolder(View v) {
            super(v);
            baseTextview_menu_name = (BaseTextview) v.findViewById(R.id.textview_community_menu_name);
            imageView_menu_circle = (ImageView) v.findViewById(R.id.imageview_menu_dot);
            view = v;
        }

        public void bind(SubMenubean subMenubean) {

            baseTextview_menu_name.setText(String.valueOf(subMenubean.getMenuName()));


            Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.menu_dot_bg);
            LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
            GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
            solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
            solidColor_main_bg.setStroke(3, subMenubean.getIconResId());
            imageView_menu_circle.setBackground(tempDrawable_main_bg);
            imageView_menu_circle.setVisibility(View.GONE);
            view.setTag(subMenubean);

            view.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {
                    try {
                        SubMenubean subMenubean1 = (SubMenubean) v.getTag();
                        if (subMenubean1.getMenuName().equals(activity.getResources().getString(R.string.gallery))) {
                            if (GalleryMainActivity.galleryMainActivity != null) {
                                GalleryMainActivity.galleryMainActivity.finish();
                            }

                            Intent intent = new Intent(activity, GalleryMainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt(BundleCommonKeywords.KEY_COMMUNITY_INNFORMATION_INDEX, CommunityInformationScreenEnum.NORMAL_SEARCH_LISTING.getIndexFromWhereCalledCommunityInformation());
                            bundle.putString(BundleCommonKeywords.KEY_COMMUNITY_ID, communityID);
                            Log.e("ad","menu adaptr=="+communityID);
                            bundle.putSerializable(BundleCommonKeywords.KEY_COMMUNITY_ACCESSBEAN, communityAccessbean);

                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                    } catch (Resources.NotFoundException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
    }
}

