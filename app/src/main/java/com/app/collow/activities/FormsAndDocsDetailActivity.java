package com.app.collow.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.collow.R;
import com.app.collow.adapters.ImageSlideAdapter;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.FormsAndDocsbean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.MyUtils;
import com.app.collow.utils.URLs;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by harmis on 1/2/17.
 */

public class FormsAndDocsDetailActivity extends BaseActivity {
    BaseTextview textview_formsanddocs_title;
    BaseTextview textview_formsanddocs_datetime;
    BaseTextview textview_formsanddocs_description;
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    FormsAndDocsbean docsbean = null;
    LinearLayout linearLayout_likes = null, linearLayout_comments = null, linearLayout_views = null;
    ImageView imageView_like = null;
    CommonSession commonSession = null;
    View view_home = null;
    BaseTextview baseTextview_left_side = null;
    BaseTextview baseTextview_header_title = null;
    RetryParameterbean retryParameterbean = null;
    FormsAndDocsbean formsAndDocsbean=null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Bundle bundle=getIntent().getExtras();
            if(bundle!=null)
            {
                formsAndDocsbean= (FormsAndDocsbean) bundle.getSerializable(BundleCommonKeywords.KEY_FORMANDDOCUMENT_BEAN);
            }



            setupHeaderView();
            findviewByIds();
            setupUI();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText("Form/Doc Detail");
            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(R.drawable.left_arrow, 0, 0, 0);


            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // MyUtils.openCommunityMenu(FormsAndDocsDetailActivity.this,false,false,"1","1");


                }
            });

            baseTextview_left_side.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);

        }


    }
    private void findviewByIds() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.formsanddocs_detail, null);


            textview_formsanddocs_title=(BaseTextview)view_home.findViewById(R.id.textview_formsanddocs_title);
            textview_formsanddocs_datetime=(BaseTextview)view_home.findViewById(R.id.textview_formsanddocs_date_time);
                textview_formsanddocs_description=(BaseTextview)view_home.findViewById(R.id.textview_formsanddocs_description);
            frameLayout_container.addView(view_home);
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }
    private void setupEvents() {
        try {
            if (docsbean != null) {
                linearLayout_likes.setTag(String.valueOf(docsbean.getPosition()));
                linearLayout_likes.setOnClickListener(new MyOnClickListener(FormsAndDocsDetailActivity.this) {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (isAvailableInternet()) {

                                likeItemhandler();


                            } else {
                                showInternetNotfoundMessage();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                linearLayout_comments.setTag(String.valueOf(docsbean.getPosition()));
                linearLayout_comments.setOnClickListener(new MyOnClickListener(FormsAndDocsDetailActivity.this) {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (isAvailableInternet()) {
                                int position = Integer.parseInt(v.getTag().toString());
                                FormsAndDocsbean docsbean1 = FormsAndDocsMainActivity.formsanddocsbeanlist.get(position);
                                MyUtils.openCommentswActivity(FormsAndDocsDetailActivity.this, ScreensEnums.NEWS_DETAILS.getScrenIndex(), docsbean1.getActivityID(), docsbean1.getPosition(), "forms and docs", docsbean1.getFormsanddocs_title());

                            } else {
                                showInternetNotfoundMessage();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


                if (docsbean.isLiked()) {
                    imageView_like.setImageResource(R.drawable.like_blue);
                } else {
                    imageView_like.setImageResource(R.drawable.unlike_blue);


                }


                SocialOptionbean socialOptionbean = docsbean.getSocialOptionbean();
               // MyUtils.handleSocialOption(FormsAndDocsDetailActivity.this, socialOptionbean, textview_formsanddocs_like, textview_formsanddocs_comment, textview_formsanddocs_view);


                ///likes count

                SpannableStringBuilder builder = new SpannableStringBuilder();

                if (CommonMethods.isTextAvailable(docsbean.getFormsanddocs_title())) {
                    textview_formsanddocs_title.setText(docsbean.getFormsanddocs_title());
                    baseTextview_header_title.setText(docsbean.getFormsanddocs_title());
                    SpannableString title = new SpannableString(docsbean.getFormsanddocs_title());
                    title.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, title.length(), 0);
                    builder.append(title);

                }


                if (CommonMethods.isTextAvailable(docsbean.getFormsanddocs_date())) {
                    textview_formsanddocs_datetime.setText(docsbean.getFormsanddocs_date());

                    SpannableString str2 = new SpannableString(docsbean.getFormsanddocs_date());
                    str2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.left_Menu_text_color_gray)), 0, str2.length(), 0);
                    str2.setSpan(new RelativeSizeSpan(0.7f), 0, str2.length(), 0); // set size

                    builder.append(" ");

                    builder.append(str2);
                }


                textview_formsanddocs_title.setText(builder, BaseTextview.BufferType.SPANNABLE);


                if (CommonMethods.isTextAvailable(docsbean.getFormsanddocs_description())) {
                    MyUtils.handleAndRedirectToReadMore(FormsAndDocsDetailActivity.this, textview_formsanddocs_title, 7, getResources().getString(R.string.more_text), docsbean.getFormsanddocs_description());

                }


            }







        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    private void setupUI() {
        try {
           if(formsAndDocsbean!=null)
           {
               if(CommonMethods.isTextAvailable(formsAndDocsbean.getFormsanddocs_title()))
               {
                   baseTextview_header_title.setText(formsAndDocsbean.getFormsanddocs_title());
               }

               if(CommonMethods.isTextAvailable(formsAndDocsbean.getFormsanddocs_date()))
               {
                   textview_formsanddocs_datetime.setText(formsAndDocsbean.getFormsanddocs_date());
               }

               if(CommonMethods.isTextAvailable(formsAndDocsbean.getFormsanddocs_description()))
               {
                   MyUtils.handleAndRedirectToReadMore(FormsAndDocsDetailActivity.this,textview_formsanddocs_description,7,getResources().getString(R.string.more_text),formsAndDocsbean.getFormsanddocs_description());

               }
           }
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }


    }
    private void likeItemhandler() {


        try {
            RequestParametersbean requestParametersbean = new RequestParametersbean();
            requestParametersbean.setActivityId(docsbean.getActivityID());
            requestParametersbean.setUserId(commonSession.getLoggedUserID());
            requestParametersbean.setLiketype("documents");
            if (docsbean.getLikeOrDislikeFlag().equals("0")) {
                docsbean.setLikeOrDislikeFlag("1");
            } else if (docsbean.getLikeOrDislikeFlag().equals("1")) {
                docsbean.setLikeOrDislikeFlag("0");
            } else {
                docsbean.setLikeOrDislikeFlag("0");

            }
            requestParametersbean.setLike(docsbean.getLikeOrDislikeFlag());


            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.LIKES.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(new SetupViewInterface() {
                @Override
                public void setupUI(Responcebean responcebean) {
                    if (responcebean.isServiceSuccess()) {
                        try {
                            JSONObject jsonObject_main = new JSONObject(responcebean.getResponceContent());
                            if (CommonMethods.checkSuccessResponceFromServer(jsonObject_main)) {
                                String totallikes = jsonObject_main.getString(JSONCommonKeywords.TotalLike);
                                if (totallikes == null || totallikes.equals("")) {
                                } else {


                                    if (docsbean.isLiked()) {
                                        docsbean.setLiked(false);
                                        imageView_like.setImageResource(R.drawable.unlike_blue);
                                    } else {
                                        imageView_like.setImageResource(R.drawable.like_blue);
                                        docsbean.setLiked(true);


                                    }

                                    SocialOptionbean socialOptionbean = docsbean.getSocialOptionbean();
                                    socialOptionbean.setLike(Integer.parseInt(totallikes));
                                 //   MyUtils.handleSocialOption(FormsAndDocsDetailActivity.this, socialOptionbean, textview_formsanddocs_like, textview_formsanddocs_comment, textview_formsanddocs_view);

                                    FormsAndDocsMainActivity.formsanddocsbeanlist.set(docsbean.getPosition(), docsbean);
                                    if (FormsAndDocsMainActivity.formsanddocsAdapter != null) {
                                        FormsAndDocsMainActivity.formsanddocsAdapter.notifyItemChanged(docsbean.getPosition());
                                    }
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }, FormsAndDocsDetailActivity.this, FormsAndDocsDetailActivity.this, URLs.LIKEACTIVITY, jsonObjectGetPostParameterEachScreen, ScreensEnums.LIKES.getScrenIndex(), NewsMainActivity.class.getClass());
            passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
