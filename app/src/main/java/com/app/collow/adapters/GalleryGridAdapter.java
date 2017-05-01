package com.app.collow.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.app.collow.R;
import com.app.collow.activities.GalleryMainActivity;
import com.app.collow.activities.SignInActivity;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.Gallerybean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.collowinterfaces.OnLoadMoreListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harmis on 23/02/17.
 */

public class GalleryGridAdapter extends RecyclerView.Adapter {
    Activity activity = null;
    String communityID = null;
    int ONE_IMAGW = 1;
    int TWO_IMAGW = 2;
    int THREE_IMAGW = 3;
    int FOUR_IMAGW = 4;
    int indexofScreenFromWhere = 0;
    CommonSession commonSession = null;
    private List<Gallerybean> gallerybeanList;
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public GalleryGridAdapter(Activity activity, RecyclerView recyclerView, int indexofScreenFromWhere) {
        this.activity = activity;
        this.indexofScreenFromWhere = indexofScreenFromWhere;
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
        RecyclerView.ViewHolder vh = null;
        View v = null;
        if (indexofScreenFromWhere == ScreensEnums.GALLERY.getScrenIndex()) {
            if (viewType == ONE_IMAGW) {
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_size_one, parent, false);

                vh = new GalleryOneViewHolder(v);

            } else if (viewType == TWO_IMAGW) {

                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_size_two, parent, false);
                vh = new GalleryTwoViewHolder(v);

            } else if (viewType == THREE_IMAGW) {
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_size_three, parent, false);
                vh = new GalleryThreeViewHolder(v);

            } else if (viewType == FOUR_IMAGW) {
                v = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.layout_size_four, parent, false);
                vh = new GalleryFourViewHolder(v);

            }

        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.layout_size_one, parent, false);

            vh = new GalleryOneViewHolder(v);
        }


        return vh;
    }


    @Override
    public int getItemViewType(int position) {

        if (indexofScreenFromWhere == ScreensEnums.GALLERY.getScrenIndex()) {
            Gallerybean gallerybean = GalleryMainActivity.gallerybeanArrayList_main.get(position);

            ArrayList<String> stringArrayList = gallerybean.getStringArrayList_images_urls();
            if (stringArrayList != null) {
                if (stringArrayList.size() != 0) {


                    if (stringArrayList.size() == 1) {
                        return ONE_IMAGW;

                    } else if (stringArrayList.size() == 2) {
                        return TWO_IMAGW;

                    } else if (stringArrayList.size() == 3) {
                        return THREE_IMAGW;

                    } else if (stringArrayList.size() == 4) {
                        return FOUR_IMAGW;

                    } else {
                        return FOUR_IMAGW;

                    }


                } else {
                    return ONE_IMAGW;

                }
            } else {
                return ONE_IMAGW;

            }

        } else {
            return ONE_IMAGW;

        }


    }

    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {


        Gallerybean gallerybean = GalleryMainActivity.gallerybeanArrayList_main.get(position);
        ArrayList<String> stringArrayList = gallerybean.getStringArrayList_images_urls();
        if (stringArrayList != null) {
            if (stringArrayList.size() != 0) {
                if (getItemViewType(position) == ONE_IMAGW) {

                    handleOneImage(gallerybean, stringArrayList, (GalleryOneViewHolder) holder);


                } else if (getItemViewType(position) == TWO_IMAGW) {
                    handlTwoImage(gallerybean, stringArrayList, (GalleryTwoViewHolder) holder);
                } else if (getItemViewType(position) == THREE_IMAGW) {
                    handlThreeImage(gallerybean, stringArrayList, (GalleryThreeViewHolder) holder);
                } else if (getItemViewType(position) == FOUR_IMAGW) {
                    handlFourImage(gallerybean, stringArrayList, (GalleryFourViewHolder) holder);
                }

            } else {
                handleOneImage(gallerybean, stringArrayList, (GalleryOneViewHolder) holder);

            }
        } else {
            handleOneImage(gallerybean, stringArrayList, (GalleryOneViewHolder) holder);

        }


    }


    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return GalleryMainActivity.gallerybeanArrayList_main.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void handleOneImage(Gallerybean gallerybean, ArrayList<String> stringArrayList, final GalleryOneViewHolder galleryOneViewHolder) {
        galleryOneViewHolder.view.setTag(gallerybean);
        galleryOneViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Gallerybean gallerybean1 = (Gallerybean) v.getTag();
                if (commonSession.isUserAdminNow()) {
                    showDialogGalleryOptionsForAdmin(gallerybean1);

                } else {
                    showDialogGalleryOptionsForUser(gallerybean1);

                }
                return false;
            }
        });
        if (CommonMethods.isImageUrlValid(stringArrayList.get(0))) {
            galleryOneViewHolder.progressBar_one.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(0))
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


        SocialOptionbean socialOptionbean = gallerybean.getSocialOptionbean();
        MyUtils.handleSocialOption(activity, socialOptionbean, galleryOneViewHolder.baseTextview_likes, galleryOneViewHolder.baseTextview_comments, null);


    }

    public void handlTwoImage(Gallerybean gallerybean, ArrayList<String> stringArrayList, final GalleryTwoViewHolder galleryTwoViewHolder) {
        galleryTwoViewHolder.view.setTag(gallerybean);
        galleryTwoViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Gallerybean gallerybean1 = (Gallerybean) v.getTag();
                if (commonSession.isUserAdminNow()) {
                    showDialogGalleryOptionsForAdmin(gallerybean1);

                } else {
                    showDialogGalleryOptionsForUser(gallerybean1);

                }
                return false;
            }
        });
        if (CommonMethods.isImageUrlValid(stringArrayList.get(0))) {
            galleryTwoViewHolder.progressBar_one.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(0))
                    .into(galleryTwoViewHolder.imageView_one, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryTwoViewHolder.progressBar_one.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryTwoViewHolder.imageView_one.setImageResource(R.drawable.defualt_square);
                            galleryTwoViewHolder.progressBar_one.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryTwoViewHolder.imageView_one.setImageResource(R.drawable.defualt_square);
            galleryTwoViewHolder.progressBar_one.setVisibility(View.GONE);

        }


        if (CommonMethods.isImageUrlValid(stringArrayList.get(1))) {
            galleryTwoViewHolder.progressBar_two.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(1))
                    .into(galleryTwoViewHolder.imageView_two, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryTwoViewHolder.progressBar_two.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryTwoViewHolder.imageView_two.setImageResource(R.drawable.defualt_square);
                            galleryTwoViewHolder.progressBar_two.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryTwoViewHolder.imageView_two.setImageResource(R.drawable.defualt_square);
            galleryTwoViewHolder.progressBar_two.setVisibility(View.GONE);

        }

        SocialOptionbean socialOptionbean = gallerybean.getSocialOptionbean();
        MyUtils.handleSocialOption(activity, socialOptionbean, galleryTwoViewHolder.baseTextview_likes, galleryTwoViewHolder.baseTextview_comments, null);

    }

    public void handlThreeImage(Gallerybean gallerybean, ArrayList<String> stringArrayList, final GalleryThreeViewHolder galleryThreeViewHolder) {
        galleryThreeViewHolder.view.setTag(gallerybean);
        galleryThreeViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Gallerybean gallerybean1 = (Gallerybean) v.getTag();
                if (commonSession.isUserAdminNow()) {
                    showDialogGalleryOptionsForAdmin(gallerybean1);

                } else {
                    showDialogGalleryOptionsForUser(gallerybean1);

                }
                return false;
            }
        });
        if (CommonMethods.isImageUrlValid(stringArrayList.get(0))) {
            galleryThreeViewHolder.progressBar_one.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(0))
                    .into(galleryThreeViewHolder.imageView_one, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryThreeViewHolder.progressBar_one.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryThreeViewHolder.imageView_one.setImageResource(R.drawable.defualt_square);
                            galleryThreeViewHolder.progressBar_one.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryThreeViewHolder.imageView_one.setImageResource(R.drawable.defualt_square);
            galleryThreeViewHolder.progressBar_one.setVisibility(View.GONE);

        }


        if (CommonMethods.isImageUrlValid(stringArrayList.get(1))) {
            galleryThreeViewHolder.progressBar_two.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(1))
                    .into(galleryThreeViewHolder.imageView_two, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryThreeViewHolder.progressBar_two.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryThreeViewHolder.imageView_two.setImageResource(R.drawable.defualt_square);
                            galleryThreeViewHolder.progressBar_two.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryThreeViewHolder.imageView_two.setImageResource(R.drawable.defualt_square);
            galleryThreeViewHolder.progressBar_two.setVisibility(View.GONE);

        }


        if (CommonMethods.isImageUrlValid(stringArrayList.get(2))) {
            galleryThreeViewHolder.progressBar_three.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(2))
                    .into(galleryThreeViewHolder.imageView_three, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryThreeViewHolder.progressBar_three.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryThreeViewHolder.imageView_three.setImageResource(R.drawable.defualt_square);
                            galleryThreeViewHolder.progressBar_three.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryThreeViewHolder.imageView_three.setImageResource(R.drawable.defualt_square);
            galleryThreeViewHolder.progressBar_three.setVisibility(View.GONE);

        }

        SocialOptionbean socialOptionbean = gallerybean.getSocialOptionbean();
        MyUtils.handleSocialOption(activity, socialOptionbean, galleryThreeViewHolder.baseTextview_likes, galleryThreeViewHolder.baseTextview_comments, null);

    }

    public void handlFourImage(Gallerybean gallerybean, ArrayList<String> stringArrayList, final GalleryFourViewHolder galleryFourViewHolder) {


        galleryFourViewHolder.view.setTag(gallerybean);
        galleryFourViewHolder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                Gallerybean gallerybean1 = (Gallerybean) v.getTag();
                if (commonSession.isUserAdminNow()) {
                    showDialogGalleryOptionsForAdmin(gallerybean1);

                } else {
                    showDialogGalleryOptionsForUser(gallerybean1);

                }
                return false;
            }
        });
        if (CommonMethods.isImageUrlValid(stringArrayList.get(0))) {
            galleryFourViewHolder.progressBar_one.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(0))
                    .into(galleryFourViewHolder.imageView_one, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryFourViewHolder.progressBar_one.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryFourViewHolder.imageView_one.setImageResource(R.drawable.defualt_square);
                            galleryFourViewHolder.progressBar_one.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryFourViewHolder.imageView_one.setImageResource(R.drawable.defualt_square);
            galleryFourViewHolder.progressBar_one.setVisibility(View.GONE);

        }


        if (CommonMethods.isImageUrlValid(stringArrayList.get(1))) {
            galleryFourViewHolder.progressBar_two.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(1))
                    .into(galleryFourViewHolder.imageView_two, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryFourViewHolder.progressBar_two.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryFourViewHolder.imageView_two.setImageResource(R.drawable.defualt_square);
                            galleryFourViewHolder.progressBar_two.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryFourViewHolder.imageView_two.setImageResource(R.drawable.defualt_square);
            galleryFourViewHolder.progressBar_two.setVisibility(View.GONE);

        }


        if (CommonMethods.isImageUrlValid(stringArrayList.get(2))) {
            galleryFourViewHolder.progressBar_three.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(2))
                    .into(galleryFourViewHolder.imageView_three, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryFourViewHolder.progressBar_three.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryFourViewHolder.imageView_three.setImageResource(R.drawable.defualt_square);
                            galleryFourViewHolder.progressBar_three.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryFourViewHolder.imageView_three.setImageResource(R.drawable.defualt_square);
            galleryFourViewHolder.progressBar_three.setVisibility(View.GONE);

        }


        if (CommonMethods.isImageUrlValid(stringArrayList.get(3))) {
            galleryFourViewHolder.progressBar_four.setVisibility(View.VISIBLE);
            Picasso.with(activity)
                    .load(stringArrayList.get(3))
                    .into(galleryFourViewHolder.imageView_four, new Callback() {
                        @Override
                        public void onSuccess() {
                            galleryFourViewHolder.progressBar_four.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError() {
                            galleryFourViewHolder.imageView_four.setImageResource(R.drawable.defualt_square);
                            galleryFourViewHolder.progressBar_four.setVisibility(View.GONE);

                        }
                    });
        } else {
            galleryFourViewHolder.imageView_four.setImageResource(R.drawable.defualt_square);
            galleryFourViewHolder.progressBar_four.setVisibility(View.GONE);

        }

        if (stringArrayList.size() >= 5) {
            galleryFourViewHolder.baseTextview_image_counters.setVisibility(View.VISIBLE);
            galleryFourViewHolder.baseTextview_image_counters.setText(String.valueOf(stringArrayList.size()));

        }


        SocialOptionbean socialOptionbean = gallerybean.getSocialOptionbean();
        MyUtils.handleSocialOption(activity, socialOptionbean, galleryFourViewHolder.baseTextview_likes, galleryFourViewHolder.baseTextview_comments, null);

    }
    // if user long press should be display view,download,and flag as inappropriate

    private void showDialogGalleryOptionsForUser(final Gallerybean gallerybean
    ) {

        // custom dialog
        try {
            final Dialog dialog = new Dialog(activity, R.style.MyDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow()
                    .setLayout(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );


            dialog.setContentView(R.layout.ac_followers_approve_reject_dialog);

            BaseTextview baseTextview_dialog_title = (BaseTextview) dialog.findViewById(R.id.textview_dialog_title);
            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);

            MyButtonView myButtonView_confirm = (MyButtonView) dialog.findViewById(R.id.mybuttonview_approve);
            MyButtonView myButtonView_cancel = (MyButtonView) dialog.findViewById(R.id.mybuttonview_reject);
            myButtonView_confirm.setVisibility(View.GONE);
            myButtonView_cancel.setVisibility(View.GONE);


            MyButtonView myButtonView_views = (MyButtonView) dialog.findViewById(R.id.mybuttonview_views);
            MyButtonView myButtonView_download = (MyButtonView) dialog.findViewById(R.id.mybuttonview_download);
            MyButtonView myButtonView_flag_as_inapproiate = (MyButtonView) dialog.findViewById(R.id.mybuttonview_flag_as_inappropriate);

            LinearLayout linearLayout_gallery_options = (LinearLayout) dialog.findViewById(R.id.gallery_options);
            linearLayout_gallery_options.setVisibility(View.VISIBLE);


            imageView_close_dialopg.setVisibility(View.VISIBLE);


            baseTextview_dialog_title.setVisibility(View.VISIBLE);
            baseTextview_dialog_title.setText(activity.getResources().getString(R.string.select_options));


            myButtonView_views.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {


                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            myButtonView_flag_as_inapproiate.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {
                    if (isAvailableInternet()) {
                        falgAsInAppropriate(dialog, gallerybean);

                    } else {
                        showInternetNotfoundMessage();
                    }
                }
            });
            myButtonView_download.setOnClickListener(new MyOnClickListener(activity) {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            imageView_close_dialopg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            dialog.show();
        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }


    }

    // if admin long press should be display delete or cancel options
    private void showDialogGalleryOptionsForAdmin(final Gallerybean gallerybean
    ) {

        // custom dialog
        try {
            final Dialog dialog = new Dialog(activity, R.style.MyDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.getWindow()
                    .setLayout(
                            ViewGroup.LayoutParams.FILL_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );


            dialog.setContentView(R.layout.ac_followers_approve_reject_dialog);
            BaseTextview baseTextview_dialog_title = (BaseTextview) dialog.findViewById(R.id.textview_dialog_title);
            ImageView imageView_close_dialopg = (ImageView) dialog.findViewById(R.id.close_dialog);
            MyButtonView myButtonView_confirm = (MyButtonView) dialog.findViewById(R.id.mybuttonview_approve);
            MyButtonView myButtonView_cancel = (MyButtonView) dialog.findViewById(R.id.mybuttonview_reject);

            myButtonView_confirm.setVisibility(View.VISIBLE);
            myButtonView_cancel.setVisibility(View.VISIBLE);


            LinearLayout linearLayout_gallery_options = (LinearLayout) dialog.findViewById(R.id.gallery_options);
            linearLayout_gallery_options.setVisibility(View.GONE);


            imageView_close_dialopg.setVisibility(View.GONE);

            myButtonView_confirm.setText(activity.getResources().getString(R.string.confirm));
            myButtonView_cancel.setText(activity.getResources().getString(R.string.logout_cancel));

            baseTextview_dialog_title.setVisibility(View.VISIBLE);
            baseTextview_dialog_title.setText(activity.getResources().getString(R.string.delete_gallery_confirm));
            myButtonView_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteGallery(dialog, gallerybean);
                }
            });

            myButtonView_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });
            imageView_close_dialopg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            CommonMethods.displayLog("error", e.getMessage());

        }


    }

    public void deleteGallery(final Dialog dialog, final Gallerybean gallerybean) {
        RequestParametersbean requestParametersbean = new RequestParametersbean();
        requestParametersbean.setCommunityID(communityID);
        requestParametersbean.setUserId(commonSession.getLoggedUserID());
        requestParametersbean.setGalleryID(gallerybean.getGalleryID());

        JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.DELETE_GALLERY.getScrenIndex(), requestParametersbean);


        PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
            @Override
            public void setupUI(Responcebean responcebean) {

                if (responcebean.isServiceSuccess()) {
                    try {
                        JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                        if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {


                            if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                            }


                            if (responcebean.getErrorMessage() == null) {
                                CommonMethods.customToastMessage(activity.getResources().getString(R.string.gallery_deleted_successfully), activity);

                            } else {
                                CommonMethods.customToastMessage(responcebean.getErrorMessage(), activity);

                            }
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }

                            //This event listing updating

                            if (GalleryMainActivity.galleryGridAdapter != null) {
                                if (GalleryMainActivity.gallerybeanArrayList_main.size() == 0) {
                                    GalleryMainActivity.mRecyclerView.setVisibility(View.GONE);
                                    GalleryMainActivity.baseTextview_error.setVisibility(View.VISIBLE);
                                } else {
                                    GalleryMainActivity.gallerybeanArrayList_main.remove(gallerybean.getPosition());
                                    GalleryMainActivity.galleryGridAdapter.notifyItemRemoved(gallerybean.getPosition());
                                    GalleryMainActivity.galleryGridAdapter.notifyItemRangeChanged(gallerybean.getPosition(), GalleryMainActivity.gallerybeanArrayList_main.size());
                                }

                                if (GalleryMainActivity.gallerybeanArrayList_main.size() == 0) {
                                    GalleryMainActivity.mRecyclerView.setVisibility(View.GONE);
                                    GalleryMainActivity.baseTextview_error.setVisibility(View.VISIBLE);
                                }


                            }


                        } else {
                            if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                            }


                            if (responcebean.getErrorMessage() == null) {
                                CommonMethods.customToastMessage(activity.getResources().getString(R.string.gallery_deleted_failed), activity);

                            } else {
                                CommonMethods.customToastMessage(responcebean.getErrorMessage(), activity);

                            }

                        }


                    } catch (Exception e) {
                        CommonMethods.customToastMessage(e.getMessage(), activity);

                    }

                }


            }
        }, activity, activity, URLs.DELETEGALLERY, jsonObjectGetPostParameterEachScreen, ScreensEnums.DELETEEVENT.getScrenIndex(), SignInActivity.class.getClass());
        new RequestToServer(passParameterbean, null).execute();
    }

    public void falgAsInAppropriate(final Dialog dialog, final Gallerybean gallerybean) {
        RequestParametersbean requestParametersbean = new RequestParametersbean();
        requestParametersbean.setCommunityID(communityID);
        requestParametersbean.setUserId(commonSession.getLoggedUserID());
        requestParametersbean.setGalleryID(gallerybean.getGalleryID());

        JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.FALG_AS_GALLERY_INAPPROPRIATE.getScrenIndex(), requestParametersbean);


        PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
            @Override
            public void setupUI(Responcebean responcebean) {

                if (responcebean.isServiceSuccess()) {
                    try {
                        JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());

                        if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {


                            if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                            }


                            if (responcebean.getErrorMessage() == null) {
                                CommonMethods.customToastMessage(activity.getResources().getString(R.string.request_sent), activity);

                            } else {
                                CommonMethods.customToastMessage(responcebean.getErrorMessage(), activity);

                            }
                            if (dialog != null && dialog.isShowing()) {
                                dialog.dismiss();
                            }


                        } else {
                            if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                            }


                            if (responcebean.getErrorMessage() == null) {
                                CommonMethods.customToastMessage(activity.getResources().getString(R.string.request_sent_failed), activity);

                            } else {
                                CommonMethods.customToastMessage(responcebean.getErrorMessage(), activity);

                            }

                        }


                    } catch (Exception e) {
                        CommonMethods.customToastMessage(e.getMessage(), activity);

                    }

                }


            }
        }, activity, activity, URLs.FLAG_AS_INAPPROPRITATE, jsonObjectGetPostParameterEachScreen, ScreensEnums.FALG_AS_GALLERY_INAPPROPRIATE.getScrenIndex(), SignInActivity.class.getClass());
        new RequestToServer(passParameterbean, null).execute();
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

    public static class GalleryTwoViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_likes = null;
        BaseTextview baseTextview_comments = null;
        ImageView imageview_gallerylike = null;
        ImageView imageview_gallerycomments = null;
        View view = null;
        ImageView imageView_one = null, imageView_two = null;
        ProgressBar progressBar_one = null, progressBar_two = null;

        public GalleryTwoViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_likes = (BaseTextview) v.findViewById(R.id.textview_gallery_likes);
            baseTextview_comments = (BaseTextview) v.findViewById(R.id.textview_gallery_comments);
            imageview_gallerylike = (ImageView) v.findViewById(R.id.imageview_gallery_likeicon);
            imageview_gallerycomments = (ImageView) v.findViewById(R.id.imageview_gallery_commenticon);

            imageView_one = (ImageView) v.findViewById(R.id.image_1);
            imageView_two = (ImageView) v.findViewById(R.id.image_2);

            progressBar_one = (ProgressBar) v.findViewById(R.id.progress_bar_1);
            progressBar_two = (ProgressBar) v.findViewById(R.id.progress_bar_2);

            view = v;


        }
    }

    public static class GalleryThreeViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_likes = null;
        BaseTextview baseTextview_comments = null;
        ImageView imageview_gallerylike = null;
        ImageView imageview_gallerycomments = null;
        View view = null;
        ImageView imageView_one = null, imageView_two = null, imageView_three = null;
        ProgressBar progressBar_one = null, progressBar_two = null, progressBar_three = null;

        public GalleryThreeViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_likes = (BaseTextview) v.findViewById(R.id.textview_gallery_likes);
            baseTextview_comments = (BaseTextview) v.findViewById(R.id.textview_gallery_comments);
            imageview_gallerylike = (ImageView) v.findViewById(R.id.imageview_gallery_likeicon);
            imageview_gallerycomments = (ImageView) v.findViewById(R.id.imageview_gallery_commenticon);


            imageView_one = (ImageView) v.findViewById(R.id.image_1);
            imageView_two = (ImageView) v.findViewById(R.id.image_2);
            imageView_three = (ImageView) v.findViewById(R.id.image_3);

            progressBar_one = (ProgressBar) v.findViewById(R.id.progress_bar_1);
            progressBar_two = (ProgressBar) v.findViewById(R.id.progress_bar_2);
            progressBar_three = (ProgressBar) v.findViewById(R.id.progress_bar_3);

            view = v;


        }
    }

    public static class GalleryFourViewHolder extends RecyclerView.ViewHolder {
        BaseTextview baseTextview_likes = null;
        BaseTextview baseTextview_comments = null;
        ImageView imageview_gallerylike = null;
        ImageView imageview_gallerycomments = null;
        BaseTextview baseTextview_image_counters = null;
        View view = null;

        ImageView imageView_one = null, imageView_two = null, imageView_three = null, imageView_four = null;
        ProgressBar progressBar_one = null, progressBar_two = null, progressBar_three = null, progressBar_four = null;

        public GalleryFourViewHolder(View v) {
            super(v);
            // tvName = (TextView) v.findViewById(R.id.tvName);

            baseTextview_likes = (BaseTextview) v.findViewById(R.id.textview_gallery_likes);
            baseTextview_comments = (BaseTextview) v.findViewById(R.id.textview_gallery_comments);
            imageview_gallerylike = (ImageView) v.findViewById(R.id.imageview_gallery_likeicon);
            imageview_gallerycomments = (ImageView) v.findViewById(R.id.imageview_gallery_commenticon);


            imageView_one = (ImageView) v.findViewById(R.id.image_1);
            imageView_two = (ImageView) v.findViewById(R.id.image_2);
            imageView_three = (ImageView) v.findViewById(R.id.image_3);
            imageView_four = (ImageView) v.findViewById(R.id.image_4);

            progressBar_one = (ProgressBar) v.findViewById(R.id.progress_bar_1);
            progressBar_two = (ProgressBar) v.findViewById(R.id.progress_bar_2);
            progressBar_three = (ProgressBar) v.findViewById(R.id.progress_bar_3);
            progressBar_four = (ProgressBar) v.findViewById(R.id.progress_bar_4);
            baseTextview_image_counters = (BaseTextview) v.findViewById(R.id.basetextview_image_counters);
            view = v;


        }
    }
}
