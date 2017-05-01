package com.app.collow.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.collow.R;
import com.app.collow.activities.CollowMessageMainActivity;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CollowMessagebean;
import com.app.collow.utils.CommonMethods;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.vincentbrison.openlibraries.android.swipelistview.SwipeListView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Harmis on 20/02/17.
 */

public class CollowMessageAdapter extends BaseAdapter {

    public interface MyAdapterCallbacks {
        public void onClickDelete(int i);
    }

    private WeakReference<Activity> mContext;
    private MyAdapterCallbacks mCallbacks;
    private SwipeListView mListView;
    Activity activity=null;

    public CollowMessageAdapter(Activity context, MyAdapterCallbacks callbacks, SwipeListView listview) {
        mContext = new WeakReference<Activity>(context);
        this.activity=context;
        mCallbacks = callbacks;
        mListView = listview;

    }

    @Override
    public int getCount() {
        return CollowMessageMainActivity.collowMessagebeanArrayList_all.size();
    }

    @Override
    public Object getItem(int i) {
        return CollowMessageMainActivity.collowMessagebeanArrayList_all.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            view = mContext.get().getLayoutInflater().inflate(R.layout.collow_message_single_tem, null);
            holder.baseTextview_sender_name = (BaseTextview) view.findViewById(R.id.textview_message_sender_name);
            holder.baseTextview_content = (BaseTextview) view.findViewById(R.id.textview_message_title);
            holder.baseTextview_date = (BaseTextview) view.findViewById(R.id.textview_message_received_time);
            holder.circularImageView_profile = (CircularImageView) view.findViewById(R.id.imageview_message_profileimage);


            holder.viewDelete = (View) view.findViewById(R.id.entry_listview_view_delete);
            view.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) view.getTag();

        }

        mListView.recycle(view, i);



        holder.viewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollowMessageAdapter.this.mCallbacks.onClickDelete(i);
            }
        });
        CollowMessagebean collowMessagebean= CollowMessageMainActivity.collowMessagebeanArrayList_all.get(i);

        if(CommonMethods.isTextAvailable(collowMessagebean.getUsername()))
        {
            holder.baseTextview_sender_name.setText(String.valueOf(i));
        }

        if(CommonMethods.isTextAvailable(collowMessagebean.getContent()))
        {
            holder.baseTextview_content.setText(collowMessagebean.getContent());

        }

        if(CommonMethods.isImageUrlValid(collowMessagebean.getProfilepic()))
        {
            Picasso.with(activity)
                    .load(collowMessagebean.getProfilepic())
                    .into((holder.circularImageView_profile), new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            holder.circularImageView_profile.setImageResource(R.drawable.defualt_square);
                        }
                    });
        }
        else
        {
            holder.circularImageView_profile.setImageResource(R.drawable.defualt_square);
        }

        return view;
    }

    private static class ViewHolder {
        protected BaseTextview baseTextview_sender_name=null,baseTextview_content=null,baseTextview_date=null;
        CircularImageView circularImageView_profile=null;
        protected View viewDelete;
    }
}
