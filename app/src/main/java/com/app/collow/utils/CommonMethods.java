package com.app.collow.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.activities.SingleImageFullScreenActivity;
import com.app.collow.activities.SplashActvitiy;
import com.app.collow.activities.VideoPlayActivity;
import com.app.collow.allenums.FileSupportEnum;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.collowinterfaces.Login;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by harmis on 8/1/17.
 */

public class CommonMethods {
    public static void customToastMessage(String message, Context context) {

        if (message != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        }
    }

    public static boolean isMorethenThreeCharacters(String content) {
        if (content.length() >= 3) {

            return true;
        } else {
            return false;
        }

    }

    public static boolean emailValidator(String email) {
        Matcher matcher = null;
        try {
            Pattern pattern;
            final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            pattern = Pattern.compile(EMAIL_PATTERN);
            matcher = pattern.matcher(email);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return matcher.matches();
    }

    public static void startNewActivityWithCloseCurrentActivity(Activity activity, Class className, Bundle bundle) {
        try {

            Intent intent = new Intent(activity, activity.getClass());
            if (bundle != null) {
                intent.putExtras(bundle);
            }


            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static boolean isImageUrlValid(String stringurl) {
        boolean isValidUrl = false;
        if (stringurl == null || stringurl.equals("") || stringurl.isEmpty() || stringurl.equals("null")) {
            isValidUrl = false;


        } else {
            isValidUrl = true;
        }
        return isValidUrl;
    }

    public static void setUpImageFromOnline(final String imageURL, final ImageView imageView, final ProgressBar progressBar_thumb) {

        if (CommonMethods.isImageUrlValid(imageURL)) {

            /*com.nostra13.universalimageloader.core.ImageLoader.getInstance()
                    .displayImage(imageURL, imageView, BaseActivity.displayImageOptions, new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            progressBar_thumb.setProgress(0);
                            progressBar_thumb.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            progressBar_thumb.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            progressBar_thumb.setVisibility(View.GONE);
                            imageView.setVisibility(View.VISIBLE);

                            Log.e("Images Size", imageURL);
                            Log.e("Bitmap size", "Width" + loadedImage.getWidth() + "Height" + loadedImage.getHeight());


                            imageView.setImageBitmap(loadedImage);
                        }
                    }, new ImageLoadingProgressListener() {
                        @Override
                        public void onProgressUpdate(String imageUri, View view, int current, int total) {
                            progressBar_thumb.setProgress(Math.round(100.0f * current / total));
                        }
                    });
*/
        } else {
            progressBar_thumb.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);

        }
    }


    public static void displayLog(String tag, String message) {
        if (SplashActvitiy.isNeedToDisplayLog) {

            try {
                if (tag != null) {
                    if (message != null) {
                        Log.d("MYTag-" + tag, message);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public static void convertLoginbeanToJSON(Context context, Loginbean loginbean) {
        if (context != null) {
            CommonSession commonSession = new CommonSession(context);

            if (loginbean != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<Loginbean>() {
                }.getType();
                String logincontent = gson.toJson(loginbean, type);
                commonSession.resetLoginJsonCOntent();
                displayLog("NewSave", logincontent);

                commonSession.storeLoginJsonCOntent(logincontent);
            }
        }

    }


    public static Loginbean convertJSONToLoginbean(String logincontent) {

        Loginbean loginbean = null;
        Gson gson = new Gson();
        Type type = new TypeToken<Loginbean>() {
        }.getType();
        loginbean = gson.fromJson(logincontent, type);
        displayLog("GetLogin", logincontent);


        return loginbean;
    }

    public static String getStringWithCommaFromList(List<String> stringList) {
        String myProductList = null;

        if (stringList != null) {
            if (stringList.size() != 0) {
                myProductList = stringList.toString().replaceAll("[\\s\\[\\]]", "");
            }
        }
        return myProductList;
    }

    public static boolean checkJSONArrayHasData(JSONObject jsonObject, String tag) {

        try {
            if (jsonObject == null) {
                return false;
            } else if (jsonObject.equals("")) {
                return false;

            } else if (jsonObject.has(tag)) {
                JSONArray jsonArray = jsonObject.getJSONArray(tag);
                if (jsonArray == null) {
                    return false;
                } else if (jsonArray.equals("")) {
                    return false;
                } else if (jsonArray.length() == 0) {
                    return false;

                } else {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


        return false;
    }

    public static boolean checkSuccessResponceFromServer(JSONObject jsonObject_main) {

        try {
            if (jsonObject_main == null) {
                return false;

            } else if (jsonObject_main.equals("")) {
                return false;

            } else {
                if (jsonObject_main.has(JSONCommonKeywords.success)) {
                    int success = jsonObject_main.getInt(JSONCommonKeywords.success);
                    if (success == 1) {
                        return true;


                    } else {
                        return false;

                    }
                } else {
                    return false;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean handleKeyInJSON(JSONObject jsonObject_main, String key) {

        try {
            if (jsonObject_main == null) {
                return false;

            } else if (jsonObject_main.equals("")) {
                return false;

            } else {
                if (jsonObject_main.has(key)) {
                    return true;
                } else {
                    return false;

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean isTextAvailable(String isTextAvailable) {
        if (isTextAvailable == null) {
            return false;
        } else if (isTextAvailable.equals("")) {
            return false;
        } else if (isTextAvailable.equals("null")) {
            return false;

        } else {
            return true;
        }
    }

    public static String handleFlagValues(String value) {
        if (value == null) {
            value = "0";
        } else if (value.equals("")) {
            value = "0";

        } else if (value.equals("null")) {
            value = "0";


        } else {

        }
        return value;
    }


    public static CommunityAccessbean getCommunityAccessFromJSON(JSONObject jsonObject_singleItem)

    {
        CommunityAccessbean communityAccessbean = new CommunityAccessbean();
        try {
            if (handleKeyInJSON(jsonObject_singleItem, JSONCommonKeywords.isFollowedCommunity)) {
                String isFollowedCommunity = jsonObject_singleItem.getString(JSONCommonKeywords.isFollowedCommunity);
                if (CommonMethods.isTextAvailable(isFollowedCommunity)) {
                    if (isFollowedCommunity.equals("1")) {
                        communityAccessbean.setCommunityFollowed(true);
                    } else {
                        communityAccessbean.setCommunityFollowed(false);
                    }
                    //{"userId":"8","StartLimit":0,"isFollowing":"1","CommunityId":"ChIJn0AAnpX7wIkRjW0_-Ad70iw","isHome":"1"}
                } else {
                    communityAccessbean.setCommunityFollowed(false);

                }
            }


            if (handleKeyInJSON(jsonObject_singleItem, JSONCommonKeywords.isMyDefualtCommunity)) {
                String isMyDefualtCommunity = jsonObject_singleItem.getString(JSONCommonKeywords.isMyDefualtCommunity);
                if (CommonMethods.isTextAvailable(isMyDefualtCommunity)) {

                } else {

                }

            }


            if (handleKeyInJSON(jsonObject_singleItem, JSONCommonKeywords.isClaimedCommunity)) {
                String isClaimedCommunity = jsonObject_singleItem.getString(JSONCommonKeywords.isClaimedCommunity);
                if (CommonMethods.isTextAvailable(isClaimedCommunity)) {
                    if (isClaimedCommunity.equals("1")) {
                        communityAccessbean.setCommunityClaimedAndApproved(true);
                    } else {
                        communityAccessbean.setCommunityClaimedAndApproved(false);

                    }
                } else {
                    communityAccessbean.setCommunityClaimedAndApproved(false);

                }


            }

            if (handleKeyInJSON(jsonObject_singleItem, JSONCommonKeywords.IsHomeCommunity)) {
                String isMyDefualtCommunity = jsonObject_singleItem.getString(JSONCommonKeywords.IsHomeCommunity);
                if (CommonMethods.isTextAvailable(isMyDefualtCommunity)) {
                    if (isMyDefualtCommunity.equals("1")) {
                        communityAccessbean.setHomeDefualtCommunity(true);
                    } else {
                        communityAccessbean.setHomeDefualtCommunity(false);

                    }
                } else {
                    communityAccessbean.setHomeDefualtCommunity(false);

                }

            }





            if (handleKeyInJSON(jsonObject_singleItem, JSONCommonKeywords.isClaimedCommunityRequest)) {
                String isClaimeCommunityRequest= jsonObject_singleItem.getString(JSONCommonKeywords.isClaimedCommunityRequest);
                if (CommonMethods.isTextAvailable(isClaimeCommunityRequest)) {
                    if (isClaimeCommunityRequest.equals("1")) {
                        communityAccessbean.setClaimedCommunityRequestSent(true);
                    } else {
                        communityAccessbean.setClaimedCommunityRequestSent(false);

                    }
                } else {
                    communityAccessbean.setClaimedCommunityRequestSent(false);

                }

            }






        } catch (JSONException e) {
            e.printStackTrace();

        }
        return communityAccessbean;
    }

    public static boolean isPriceValid(String number) {
        /*if(number!=null)
        {
            if(!number.equals(""))
            {
                if(number.equals("0"))
                {
                    return  false;

                }
                else {
                    if (number.startsWith("00"))
                    {
                        return  false;

                    }
                    else
                    {
                        return  true;

                    }
                }
            }
            else
            {
                return  false;

            }
        }
        else
        {
            return  false;
        }*/
        return true;

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }


    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), BufferType.SPANNABLE);
                }
            }
        });

    }


    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }


    public static int randInt(int min, int max) {

        // Usually this can be a field rather than a method variable
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public static void setDefualtImageBasedONMIMEType(String mimeType, ImageView imageview_singe_only) {

        if (mimeType != null) {
            //.docx
            if (mimeType.equals(FileSupportEnum.DOCX.getMimeType())) {
                imageview_singe_only.setImageResource(R.drawable.docx);

            } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                imageview_singe_only.setImageResource(R.drawable.docx);

            }

            //pdf
            else if (mimeType.equals(FileSupportEnum.PDF.getMimeType())) {
                imageview_singe_only.setImageResource(R.drawable.pdf);

            }
            //excel
            else if (mimeType.equals(FileSupportEnum.ZIP.getMimeType())) {
                imageview_singe_only.setImageResource(R.drawable.zip);

            }
            //apk
            else if (mimeType.equals(FileSupportEnum.TXT.getMimeType()))

            {
                imageview_singe_only.setImageResource(R.drawable.txt);

            }
            //txt
            else if (mimeType.equals(FileSupportEnum.XLS.getMimeType())) {
                imageview_singe_only.setImageResource(R.drawable.xls);

            }
            //ppt
            else if (mimeType.equals("application/vnd.ms-powerpoint")) {
                imageview_singe_only.setImageResource(R.drawable.defualt_square);

            }
            //ppt
            else if (mimeType.equals("application/vnd.ms-powerpoint")) {
                imageview_singe_only.setImageResource(R.drawable.defualt_square);

            }
            //Audio Video Interleave (AVI)
            else if (mimeType.equals("video/x-msvideo")) {
                imageview_singe_only.setImageResource(R.drawable.defualt_square);

            }
            //html
            else if (mimeType.equals("text/html")) {
                imageview_singe_only.setImageResource(R.drawable.defualt_square);


            }//mp4
            else if (mimeType.equals("text/html")) {
                imageview_singe_only.setImageResource(R.drawable.defualt_square);

            }


            //video/3gpp
            //video/vnd.uvvu.mp4
        }

    }

    public static void makeActionBasedOnURLMIMEType(Activity activity, String url) {
        String mimeType = CommonMethods.getMimeTypeFromURL(url);
        if (mimeType != null)

        {
            if (mimeType.equals("image/jpeg")) {
                Intent intent = new Intent(activity, SingleImageFullScreenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(BundleCommonKeywords.KEY_URL, url);
                intent.putExtras(bundle);
                activity.startActivity(intent);

            } else if (mimeType.equals(FileSupportEnum.VIDEO_MP4.getMimeType()) || mimeType.equals(FileSupportEnum.VIDEO_AVI.getMimeType())) {
                Intent intent = new Intent(activity, VideoPlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(BundleCommonKeywords.KEY_URL, url);
                intent.putExtras(bundle);
                activity.startActivity(intent);


            }

        }


    }


    public static SocialOptionbean getSocialOptionbean(JSONObject jsonObject) {
        SocialOptionbean socialOptionbean = null;
        try {
            if (jsonObject != null) {
                if (!jsonObject.equals("")) {
                    socialOptionbean = new SocialOptionbean();
                    if (handleKeyInJSON(jsonObject, JSONCommonKeywords.TotalLike)) {
                        String total_likes = jsonObject.getString(JSONCommonKeywords.TotalLike);
                        if (total_likes == null || total_likes.equals("")) {
                            total_likes = "0";
                        }

                        socialOptionbean.setLike(Integer.parseInt(total_likes));


                        String total_comments = jsonObject.getString(JSONCommonKeywords.TotalComment);
                        if (total_comments == null || total_comments.equals("")) {
                            total_comments = "0";
                        }

                        socialOptionbean.setComments(Integer.parseInt(total_comments));


                        String total_views = jsonObject.getString(JSONCommonKeywords.TotalViews);
                        if (total_views == null || total_views.equals("")) {
                            total_views = "0";
                        }

                        socialOptionbean.setViews(Integer.parseInt(total_views));


                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return socialOptionbean;
    }

    public static String getMimeTypeFromURL(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String dateConversionInAgo(Activity activity, String datestring) {
        String returnResult = null;
        try {



            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
            Date past = format.parse(datestring);
            Date now = new Date();
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if (seconds < 60) {
                returnResult = activity.getResources().getString(R.string.today) + " " + seconds + " " + activity.getResources().getString(R.string.second_ago);
            } else if (minutes < 60) {
                returnResult = activity.getResources().getString(R.string.today) + " " + minutes + " " + activity.getResources().getString(R.string.mint_ago);
            } else if (hours < 24) {
                System.out.println(hours + " hours ago");
                returnResult = activity.getResources().getString(R.string.today) + " " + hours + " " + activity.getResources().getString(R.string.hours_ago);

            } else {
                System.out.println(days + " days ago");

                returnResult = days + " " + activity.getResources().getString(R.string.days_ago);


            }
        } catch (Exception j) {
            j.printStackTrace();
        }
        return returnResult;
    }

  public static String changeDateFormat(String dateNeedToChange, int index)
  {
      Date date;

      String dateChanged= null;
      try {

          if(dateNeedToChange==null||dateNeedToChange.equals("")|| TextUtils.isEmpty(dateNeedToChange) )
          {

          }
          else
          {
              if(index==1)
              {
                  date = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(dateNeedToChange);

                  dateChanged  = new SimpleDateFormat(CommonKeywords.APP_NEED_DATE_FORMAT).format(date);
              }

          }




      } catch (ParseException e) {
          e.printStackTrace();
      }


      return dateChanged;
  }


    }

