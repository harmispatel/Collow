package com.app.collow.adapters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Menubean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.database.CollowDatabaseHandler;
import com.app.collow.utils.CommonSession;

import java.util.List;

/**
 * Created by Harmis on 01/02/17.
 */

public class AmenitiesAdapter extends RecyclerView.Adapter  {


    // The minimum amount of items to have below your current scroll position
    // before loading more.
    Activity activity=null;
    CollowDatabaseHandler collowDatabaseHandler=null;
    CommonSession commonSession=null;
    List<String> stringList_amenities=null;


    public AmenitiesAdapter(Activity activity,  List<String> stringList_amenities) {
        this.activity=activity;
        collowDatabaseHandler=new CollowDatabaseHandler(activity);
        commonSession=new CommonSession(activity);

        this.stringList_amenities=stringList_amenities;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.search_history_raw, parent, false);

        vh = new AmenitiesViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AmenitiesViewHolder) {
            //setup here data

            try {
                String  amentiName = stringList_amenities.get(position);

                ((AmenitiesViewHolder) holder).imageView_cross.setVisibility(View.GONE);
                 ((AmenitiesViewHolder) holder).baseTextview_amenity_name.setText(amentiName);








            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
        }
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return stringList_amenities.size();
    }




    //
    public static class AmenitiesViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_amenity_name=null;
        ImageView  imageView_cross=null;

        public AmenitiesViewHolder(View v) {
            super(v);
            baseTextview_amenity_name= (BaseTextview) v.findViewById(R.id.textview_search_community_name_search_history);
            imageView_cross= (ImageView) v.findViewById(R.id.imageview_search_history_delete);
            //define here findview by ids

        }
    }

}

