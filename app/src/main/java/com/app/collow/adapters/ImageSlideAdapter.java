package com.app.collow.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.allenums.FileSupportEnum;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.utils.CommonMethods;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import static com.app.collow.utils.CommonMethods.isImageUrlValid;

/**
 * Created by Harmis on 02/02/17.
 */

public class ImageSlideAdapter extends PagerAdapter {

    Activity activity = null;
    ArrayList<String> strings_urls=null;
    int indexFromWhichScreen=-1;

    public ImageSlideAdapter(Activity mactivity,ArrayList<String> strings_urls,int indexFromWhichScreen) {
        activity = mactivity;
        this.strings_urls=strings_urls;
        this.indexFromWhichScreen=indexFromWhichScreen;
    }


    @Override
    public int getCount() {
        return strings_urls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        try {
            Context context = activity;

            view = LayoutInflater.from(context).inflate(R.layout.only_imageview_layout, null);

            final ImageView imageView_large = (ImageView) view.findViewById(R.id.imageview);
            ImageView imageView_play= (ImageView) view.findViewById(R.id.imageview_play_icon);
            final ProgressBar progressBar_image= (ProgressBar) view.findViewById(R.id.progress_image_chat);
            imageView_play.setVisibility(View.GONE);
            String url = strings_urls.get(position);


            if (isImageUrlValid(url)) {

                if(indexFromWhichScreen==ScreensEnums.NEWS_DETAILS.getScrenIndex())

                {
                    String mimeType=CommonMethods.getMimeTypeFromURL(url);

                    CommonMethods.displayLog("File Type",mimeType);

                    if(mimeType.equals("image/jpeg"))
                    {
                        Picasso.with(activity)
                                .load(url)
                                .into(imageView_large, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        imageView_large.setVisibility(View.VISIBLE);
                                    }

                                    @Override
                                    public void onError() {
                                    }
                                });
                    }
                    else if(mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                    {
                        imageView_large.setImageResource(R.drawable.docx);
                    }
                    else if(mimeType.equals(FileSupportEnum.VIDEO_MP4.getMimeType())||mimeType.equals(FileSupportEnum.VIDEO_AVI.getMimeType()))
                    {
                        imageView_play.setVisibility(View.VISIBLE);
                        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(url, MediaStore.Video.Thumbnails.MINI_KIND);

                        if(thumbnail!=null)
                        {
                            imageView_large.setImageBitmap(thumbnail);

                        }
                        else
                        {
                            imageView_large.setImageResource(R.drawable.defualt_square);
                        }
                    }
                    imageView_large.setTag(String.valueOf(url));
                    imageView_large.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(indexFromWhichScreen== ScreensEnums.NEWS_DETAILS.getScrenIndex())

                            {
                                String  url= (String) v.getTag();

                                CommonMethods.makeActionBasedOnURLMIMEType(activity,url);
                            }
                        }
                    });





                }
                else if(indexFromWhichScreen==ScreensEnums.COMMUNTIES_FEED_ACTIVITIES.getScrenIndex())
                {

                        String mimeType=CommonMethods.getMimeTypeFromURL(url);

                        CommonMethods.displayLog("File Type",mimeType);

                        if(mimeType.equals("image/jpeg"))
                        {
                            progressBar_image.setVisibility(View.VISIBLE);
                            Picasso.with(activity)
                                    .load(url)
                                    .into(imageView_large, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            imageView_large.setVisibility(View.VISIBLE);
                                            progressBar_image.setVisibility(View.GONE);

                                        }

                                        @Override
                                        public void onError() {
                                            progressBar_image.setVisibility(View.GONE);

                                        }
                                    });
                        }
                        else if(mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document"))
                        {
                            imageView_large.setImageResource(R.drawable.docx);

                        }
                        else if(mimeType.equals(FileSupportEnum.VIDEO_MP4.getMimeType())||mimeType.equals(FileSupportEnum.VIDEO_AVI.getMimeType()))
                        {
                            imageView_play.setVisibility(View.VISIBLE);
                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(url, MediaStore.Video.Thumbnails.MINI_KIND);

                            if(thumbnail!=null)
                            {
                                imageView_large.setImageBitmap(thumbnail);

                            }
                            else
                            {
                                imageView_large.setImageResource(R.drawable.defualt_square);
                            }
                        }
                        imageView_large.setTag(String.valueOf(url));
                        imageView_large.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if(indexFromWhichScreen== ScreensEnums.NEWS_DETAILS.getScrenIndex())

                                {
                                    String  url= (String) v.getTag();

                                    CommonMethods.makeActionBasedOnURLMIMEType(activity,url);
                                }
                            }
                        });






                }
                else
                {
                    Picasso.with(activity)
                            .load(url)
                            .into(imageView_large, new Callback() {
                                @Override
                                public void onSuccess() {
                                    imageView_large.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onError() {
                                }
                            });
                }



            } else {

            }



            (container).addView(view, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
