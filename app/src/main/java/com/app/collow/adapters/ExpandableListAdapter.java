package com.app.collow.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.widget.BaseExpandableListAdapter;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.collow.R;
import com.app.collow.activities.GalleryMainActivity;
import com.app.collow.allenums.CommunityInformationScreenEnum;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Menubean;
import com.app.collow.beans.SubMenubean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.utils.BundleCommonKeywords;

/**
 * Created by Harmis on 01/03/17.
 */

public class ExpandableListAdapter   extends BaseExpandableListAdapter {

    private Context _context;
    private List<Menubean> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Menubean, List<SubMenubean>> _listDataChild;
    Activity activity=null;
    String communityID=null;

    public ExpandableListAdapter(Activity activity,Context context, List<Menubean> listDataHeader,
                                 HashMap<Menubean, List<SubMenubean>> listChildData,String communityID) {
        this._context = context;
        this.activity=activity;
        this.communityID=communityID;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final SubMenubean subMenubean = (SubMenubean) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.community_menu_single_item, null);
        }

        BaseTextview  baseTextview_menu_name = (BaseTextview) convertView.findViewById(R.id.textview_community_menu_name);
        ImageView  imageView_menu_circle = (ImageView) convertView.findViewById(R.id.imageview_menu_dot);
        View   view = convertView;

        baseTextview_menu_name.setText(String.valueOf(subMenubean.getMenuName()));


        Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.menu_dot_bg);
        LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
        GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
        solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
        solidColor_main_bg.setStroke(3, subMenubean.getIconResId());
        imageView_menu_circle.setBackground(tempDrawable_main_bg);
        imageView_menu_circle.setVisibility(View.GONE);
        view.setTag(subMenubean);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int size=0;
        List<SubMenubean> subMenubeen=_listDataChild.get(_listDataHeader.get(groupPosition));
        if(subMenubeen==null)
        {
            size=0;

        }
        else if(subMenubeen.size()==0)
        {
            size=0;

        }
        else
        {
            size=subMenubeen.size();
        }


        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        Menubean menubean = (Menubean) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.community_menu_single_item, null);
            BaseTextview  baseTextview_menu_name = (BaseTextview) convertView.findViewById(R.id.textview_community_menu_name);
            ImageView  imageView_menu_circle = (ImageView) convertView.findViewById(R.id.imageview_menu_dot);

            baseTextview_menu_name.setText(menubean.getTitle());
            baseTextview_menu_name.setTextColor(activity.getResources().getColor(R.color.black));
            Drawable tempDrawable_main_bg = activity.getResources().getDrawable(R.drawable.menu_dot_bg);
            LayerDrawable bubble_main_bg = (LayerDrawable) tempDrawable_main_bg;
            GradientDrawable solidColor_main_bg = (GradientDrawable) bubble_main_bg.findDrawableByLayerId(R.id.outerRectangle);
            solidColor_main_bg.setColor(activity.getResources().getColor(android.R.color.transparent));
            solidColor_main_bg.setStroke(3, menubean.getIconResId());
            imageView_menu_circle.setBackground(tempDrawable_main_bg);
        }



        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
