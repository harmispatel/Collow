package com.app.collow.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.LeftMenuItembean;

import java.util.List;

/**
 * Created by Harmis on 25/01/17.
 */

public class LeftMenuAdapter extends RecyclerView.Adapter<LeftMenuAdapter.LeftMenuViewHolder> {


    private List<LeftMenuItembean> leftMenuItembeanList;


    public LeftMenuAdapter(List<LeftMenuItembean> leftMenuItembeanList) {
        this.leftMenuItembeanList = leftMenuItembeanList;


    }


    @Override
    public LeftMenuAdapter.LeftMenuViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.left_menu_single_item, parent, false);


        return new LeftMenuViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LeftMenuViewHolder holder, int position) {
        LeftMenuItembean leftMenuItembean = leftMenuItembeanList.get(position);
        holder.baseTextview_left_menu_item.setText(leftMenuItembean.getLeftMenuItemName());
        holder.view.setTag(leftMenuItembean.getLeftMenuItemName());
    }




    @Override
    public int getItemCount() {
        return leftMenuItembeanList.size();
    }


    //
    public static class LeftMenuViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_left_menu_item = null;
        View view=null;

        public LeftMenuViewHolder(View v) {
            super(v);
            baseTextview_left_menu_item = (BaseTextview) v.findViewById(R.id.textview_left_menu_item);
            view=v;

        }
    }


}