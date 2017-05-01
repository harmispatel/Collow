package com.app.collow.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Gallerybean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.MyUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Harmis on 24/02/17.
 */

public class GalleryGridSubAdapter extends RecyclerView.Adapter {
    Activity activity = null;
   public static  Gallerybean gallerybean_globle = null;
    ArrayList<String> stringArrayList_urls = new ArrayList<>();


    public GalleryGridSubAdapter(Activity activity, Gallerybean gallerybean) {
        this.activity = activity;
        gallerybean_globle = gallerybean;
        stringArrayList_urls = gallerybean_globle.getStringArrayList_images_urls();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh = null;
        View v = null;


        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_size_one, parent, false);

        vh = new GalleryOneViewHolder(v);
        return vh;
    }


    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        if (stringArrayList_urls != null) {
            if (stringArrayList_urls.size() != 0) {

                handleOneImage(stringArrayList_urls.get(position), (GalleryOneViewHolder) holder);


            } else {
                handleOneImage(stringArrayList_urls.get(position), (GalleryOneViewHolder) holder);

            }
        } else {
            handleOneImage(stringArrayList_urls.get(position), (GalleryOneViewHolder) holder);

        }






    }


    @Override
    public int getItemCount() {
        return stringArrayList_urls.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }



    public void handleOneImage(String url, final GalleryOneViewHolder galleryOneViewHolder) {
        if (CommonMethods.isImageUrlValid(url)) {
            galleryOneViewHolder.progressBar_one.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(url)
                    .into(galleryOneViewHolder.imageView_one, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryOneViewHolder.progressBar_one.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryOneViewHolder.imageView_one.setImageResource(R.drawable.defualt_square);
                            galleryOneViewHolder.progressBar_one.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryOneViewHolder.imageView_one.setImageResource(R.drawable.defualt_square);
            galleryOneViewHolder.progressBar_one.setVisibility(View.GONE);

        }
        galleryOneViewHolder.view.setTag(String.valueOf(url));


        SocialOptionbean socialOptionbean = gallerybean_globle.getSocialOptionbean();
        MyUtils.handleSocialOption(activity, socialOptionbean, galleryOneViewHolder.baseTextview_likes, galleryOneViewHolder.baseTextview_comments, null);

    }

    //
    public static class GalleryOneViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_likes = null;
        BaseTextview baseTextview_comments = null;
        ImageView imageview_gallerylike = null;
        ImageView imageview_gallerycomments = null;
        ImageView imageView_one = null;
        ProgressBar progressBar_one = null;
        View view = null;

        public GalleryOneViewHolder(View v) {
            super(v);

            baseTextview_likes = (BaseTextview) v.findViewById(R.id.textview_gallery_likes);
            baseTextview_comments = (BaseTextview) v.findViewById(R.id.textview_gallery_comments);
            imageview_gallerylike = (ImageView) v.findViewById(R.id.imageview_gallery_likeicon);
            imageview_gallerycomments = (ImageView) v.findViewById(R.id.imageview_gallery_commenticon);
            imageView_one = (ImageView) v.findViewById(R.id.imageview_one);
            progressBar_one = (ProgressBar) v.findViewById(R.id.progress_bar);
            view = v;


        }
    }
}