package com.app.collow.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.CommunityActivitiesFeedbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ImageGalleryDeatilActivity extends AppCompatActivity {

    public static ImageGalleryDeatilActivity imageGalleryDeatilActivity = null;
    public static BaseTextview textview_gallerydetail_comments = null;
    ImageView imageview_gallerydetail_comments = null;
    ImageView imageview_gallerydetail_close = null;
    CircularImageView imageview_gallerydetail_profilepic = null;
    ImageView imageview_gallerydetail_likes = null;
    BaseTextview textview_gallerydetail_likes = null;
    BaseTextview textview_gallerydetail_name = null;
    BaseTextview textview_gallerydetail_date = null;
    BaseTextview textview_gallerydetail_title = null;
    BaseTextview baseTextview_header_title = null;
    BaseTextview baseTextview_left_side = null;
    String KEY_COMMUNITY_ID = null;
    int current_start = 0;
    CommunityAccessbean communityAccessbean = null;
    CommonSession commonSession = null;
    String communityID = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageview_right_foursquare = null;
    ImageView imageView_delete = null, imageView_view = null, imageView_edit = null, imageView_search = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;

    ImageView imageView_main_details = null;
    String imageUrl = null;
    LinearLayout linearLayout_likes = null, linearLayout_comments = null, image_main_ll = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery_deatil);
        imageGalleryDeatilActivity = this;
        findViewByIDs();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            imageUrl = (bundle.getString("imageUrl"));

            setUpData();

        } else {
           finish();
        }
        setActions();
    }

    private void setUpData() {
        image_main_ll.setVisibility(View.VISIBLE);



        if (CommonMethods.isImageUrlValid(imageUrl)) {
            Picasso.with(ImageGalleryDeatilActivity.this)
                    .load(imageUrl)
                    .into(imageView_main_details, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            imageView_main_details.setImageResource(R.drawable.defualt_square);

                        }
                    });
        } else {
            imageView_main_details.setImageResource(R.drawable.defualt_square);

        }

//        boolean isComLike = communityActivitiesFeedbean.isLikedFeed();
//        if (!isComLike) {
//            imageview_gallerydetail_likes.setImageResource(R.drawable.gallery_unlike);
//            textview_gallerydetail_likes.setText("0 Likes");
//        } else {
//            imageview_gallerydetail_likes.setImageResource(R.drawable.gallery_like);
//            textview_gallerydetail_likes.setText("1 Likes");
//        }
//
//        //textview_gallerydetail_comments.setText(communityActivitiesFeedbean.);
//
//        if (CommonMethods.isImageUrlValid(communityActivitiesFeedbean.getUserprofilepic())) {
//            Picasso.with(imageGalleryDeatilActivity)
//                    .load(communityActivitiesFeedbean.getUserprofilepic())
//                    .into((imageview_gallerydetail_profilepic), new Callback() {
//                        @Override
//                        public void onSuccess() {
//                        }
//
//                        @Override
//                        public void onError() {
//                            imageview_gallerydetail_profilepic.setImageResource(R.drawable.user_defualt_icon);
//                        }
//                    });
//        } else {
//            imageview_gallerydetail_profilepic.setImageResource(R.drawable.user_defualt_icon);
//        }
//
//        textview_gallerydetail_name.setText(communityActivitiesFeedbean.getUsername() + " " + communityActivitiesFeedbean.getPosttime());
//        textview_gallerydetail_title.setText(communityActivitiesFeedbean.getTitle());

    }

    private void findViewByIDs() {
        try {


            imageView_main_details = (ImageView) findViewById(R.id.imageview_gallerydetail_image);
            imageview_gallerydetail_likes = (ImageView) findViewById(R.id.imageview_gallerydetail_like);
            imageview_gallerydetail_comments = (ImageView) findViewById(R.id.imageview_gallerydetail_comments);
            imageview_gallerydetail_close = (ImageView) findViewById(R.id.imageview_gallerydetail_close);
            imageview_gallerydetail_profilepic = (CircularImageView) findViewById(R.id.imageview_gallerydetail_profileimage);
            textview_gallerydetail_likes = (BaseTextview) findViewById(R.id.textview_gallerydetail_likes);
            textview_gallerydetail_comments = (BaseTextview) findViewById(R.id.textview_gallerydetail_comments);
            textview_gallerydetail_name = (BaseTextview) findViewById(R.id.textview_gallerydetail_name);
            textview_gallerydetail_date = (BaseTextview) findViewById(R.id.textview_gallerydetail_date);
            textview_gallerydetail_title = (BaseTextview) findViewById(R.id.textview_gallerydetail_title);


            linearLayout_likes = (LinearLayout) findViewById(R.id.layout_gallery_details_likes);
            linearLayout_comments = (LinearLayout) findViewById(R.id.layout_gallery_details_comments);
            image_main_ll = (LinearLayout) findViewById(R.id.image_main_ll);


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    private void setActions()
    {
        imageview_gallerydetail_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
