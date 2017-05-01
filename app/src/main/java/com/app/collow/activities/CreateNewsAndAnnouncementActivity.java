package com.app.collow.activities;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.Filebean;
import com.app.collow.beans.Imagebean;
import com.app.collow.beans.Newsbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.SocialOptionbean;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.recyledecor.GridSpacingItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by Harmis on 07/02/17.
 */

public class CreateNewsAndAnnouncementActivity extends BaseActivity {
    BaseEdittext edittext_createnews_title = null;
    BaseEdittext edittext_createnews_description = null;
    BaseTextview baseTextview_left_side = null;
    View view_home = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null, imageView_plus_icon_only;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    CommonSession commonSession = null;
    BaseTextview baseTextview_header_title = null;
    ArrayList<Filebean> filebeanArrayList_uploading = new ArrayList<>();
    RecyclerView recyclerView_grid_news_images = null;
    UploadImageAdapter uploadImageAdapter = null;
    RelativeLayout relativeLayout_upload_image_plus_view = null;
    MyButtonView myButtonView_create_news = null;
    String communityID = null;

    public static Bitmap getVidioThumbnail(String path) {
        Bitmap bitmap = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.MICRO_KIND);
            if (bitmap != null) {
                return bitmap;
            }
        }
        // MediaMetadataRetriever is available on API Level 8 but is hidden until API Level 10
        Class<?> clazz = null;
        Object instance = null;
        try {
            clazz = Class.forName("android.media.MediaMetadataRetriever");
            instance = clazz.newInstance();
            final Method method = clazz.getMethod("setDataSource", String.class);
            method.invoke(instance, path);
            // The method name changes between API Level 9 and 10.
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD) {
                bitmap = (Bitmap) clazz.getMethod("captureFrame").invoke(instance);
            } else {
                final byte[] data = (byte[]) clazz.getMethod("getEmbeddedPicture").invoke(instance);
                if (data != null) {
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                }
                if (bitmap == null) {
                    bitmap = (Bitmap) clazz.getMethod("getFrameAtTime").invoke(instance);
                }
            }
        } catch (Exception e) {
            bitmap = null;
        } finally {
            try {
                if (instance != null) {
                    clazz.getMethod("release").invoke(instance);
                }
            } catch (final Exception ignored) {
            }
        }
        return bitmap;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(CreateNewsAndAnnouncementActivity.this, getApplicationContext(), getIntent().getExtras(), CreateNewsAndAnnouncementActivity.class.getClass());
            commonSession = new CommonSession(CreateNewsAndAnnouncementActivity.this);
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                communityID = bundle.getString(BundleCommonKeywords.KEY_COMMUNITY_ID);
            }
            setupHeaderView();
            findViewByIDs();
            setupEvents();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.create_news_annnoucement));
            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);
            baseTextview_left_side = (BaseTextview) toolbar_header.findViewById(R.id.textview_left_side_title);

            imageView_right_menu.setImageResource(R.drawable.community_main_menu);
            baseTextview_left_side.setText(getResources().getString(R.string.back));
            baseTextview_left_side.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            baseTextview_left_side.setText(getResources().getString(R.string.cancel));
            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            });

            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


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


    // method2 to get the file path from uri

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.news_create_new, null);

            edittext_createnews_title = (BaseEdittext) view_home.findViewById(R.id.edittext_createnews_title);
            edittext_createnews_description = (BaseEdittext) view_home.findViewById(R.id.edittext_createnews_description);


            imageView_plus_icon_only = (ImageView) view_home.findViewById(R.id.imageview_upload_image_plus_icon);
            relativeLayout_upload_image_plus_view = (RelativeLayout) view_home.findViewById(R.id.relativelayout_upload_create_news_images);
            frameLayout_container.addView(view_home);


            myButtonView_create_news = (MyButtonView) view_home.findViewById(R.id.mybuttonview_create_news);
            //  getNewsListing(0);


            recyclerView_grid_news_images = (RecyclerView) view_home.findViewById(R.id.recycler_view_grid_create_news);
            recyclerView_grid_news_images.setHasFixedSize(true);
            // Disabled nested scrolling since Parent scrollview will scroll the content.
            recyclerView_grid_news_images.setNestedScrollingEnabled(false);
            StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
            recyclerView_grid_news_images.setLayoutManager(gaggeredGridLayoutManager);


            int spanCount = 3; // 3 columns
            int spacing = 15; // 50px
            boolean includeEdge = false;
            recyclerView_grid_news_images.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


            uploadImageAdapter = new UploadImageAdapter(this);
            recyclerView_grid_news_images.setAdapter(uploadImageAdapter);
            relativeLayout_upload_image_plus_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDialog();
                }
            });
            imageView_plus_icon_only.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startDialog();
                }
            });


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }

    private void setupEvents() {
        try {

            myButtonView_create_news.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try {
                        if (TextUtils.isEmpty(edittext_createnews_title.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_title), CreateNewsAndAnnouncementActivity.this);
                        } else if (!CommonMethods.isMorethenThreeCharacters(edittext_createnews_title.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), CreateNewsAndAnnouncementActivity.this);

                        } else if (TextUtils.isEmpty(edittext_createnews_description.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.description), CreateNewsAndAnnouncementActivity.this);
                        } else if (!CommonMethods.isMorethenThreeCharacters(edittext_createnews_description.getText().toString())) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), CreateNewsAndAnnouncementActivity.this);

                        }  else {

                            requestParametersbean.setTitle(edittext_createnews_title.getText().toString());
                            requestParametersbean.setDescripton(edittext_createnews_description.getText().toString());
                            requestParametersbean.setCommunityID(communityID);
                            requestParametersbean.setUserId(commonSession.getLoggedUserID());
                            requestParametersbean.setImgCount(String.valueOf(filebeanArrayList_uploading.size() - 1));
                            requestParametersbean.setFileArrayList_uploading(filebeanArrayList_uploading);


                            ///make request here
                            createNews();
                        }
                    } catch (Exception e) {
                        new BaseException(e, false, retryParameterbean);
                    }

                }
            });
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }
    }

    public void createNews() {

        try {
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.CREATE_NEWS.getScrenIndex(), requestParametersbean);

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
                                    CommonMethods.customToastMessage(getResources().getString(R.string.create_news_success), CreateNewsAndAnnouncementActivity.this);
                                } else {
                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateNewsAndAnnouncementActivity.this);

                                }


                                Newsbean newsbean = new Newsbean();


                                JSONObject json_single_news = jsonObject_main.getJSONObject(JSONCommonKeywords.News);


                                if (CommonMethods.handleKeyInJSON(json_single_news, JSONCommonKeywords.Newsid)) {
                                    newsbean.setNews_id(json_single_news.getString(JSONCommonKeywords.Newsid));

                                }
                                if (CommonMethods.handleKeyInJSON(json_single_news, JSONCommonKeywords.News)) {
                                    newsbean.setNews_title(json_single_news.getString(JSONCommonKeywords.News));

                                }
                                if (CommonMethods.handleKeyInJSON(json_single_news, JSONCommonKeywords.Description)) {
                                    newsbean.setNews_description(json_single_news.getString(JSONCommonKeywords.Description));

                                }

                                if (CommonMethods.handleKeyInJSON(json_single_news, JSONCommonKeywords.NewsDate)) {
                                    newsbean.setNews_time(json_single_news.getString(JSONCommonKeywords.NewsDate));

                                }


                                if (CommonMethods.handleKeyInJSON(json_single_news, JSONCommonKeywords.UserName)) {
                                    newsbean.setNews_username(json_single_news.getString(JSONCommonKeywords.UserName));

                                }
                                if (CommonMethods.handleKeyInJSON(json_single_news, JSONCommonKeywords.CommunityName)) {
                                    newsbean.setCommunityName(json_single_news.getString(JSONCommonKeywords.CommunityName));

                                }


                                if (CommonMethods.handleKeyInJSON(json_single_news, JSONCommonKeywords.ProfilePic)) {
                                    newsbean.setNews_userprofilepic(json_single_news.getString(JSONCommonKeywords.ProfilePic));

                                }

                                if (CommonMethods.handleKeyInJSON(json_single_news, JSONCommonKeywords.ActivityId)) {
                                    newsbean.setActivityID(json_single_news.getString(JSONCommonKeywords.ActivityId));

                                }

                                SocialOptionbean socialOptionbean = CommonMethods.getSocialOptionbean(json_single_news);

                                if (socialOptionbean != null) {
                                    newsbean.setSocialOptionbean(socialOptionbean);
                                }


                                ArrayList<String> files_of_news = new ArrayList<>();


                                if (CommonMethods.handleKeyInJSON(json_single_news, JSONCommonKeywords.Image))

                                {
                                    if (CommonMethods.checkJSONArrayHasData(json_single_news, JSONCommonKeywords.Image)) {
                                        JSONArray jsonArray_images = json_single_news.getJSONArray(JSONCommonKeywords.Image);
                                        for (int j = 0; j < jsonArray_images.length(); j++) {

                                            if (CommonMethods.isImageUrlValid(jsonArray_images.getString(j))) {
                                                files_of_news.add(jsonArray_images.getString(j));
                                            }
                                        }


                                    }


                                } else {

                                }


                                newsbean.setStringArrayList_fileURLs(files_of_news);

                                NewsMainActivity.newsbeanArrayList.add(0, newsbean);
                                NewsMainActivity.newsAdapter.notifyItemInserted(0);
                                NewsMainActivity.mRecyclerView.smoothScrollToPosition(0);
                                NewsMainActivity.mRecyclerView.setVisibility(View.VISIBLE);
                                NewsMainActivity.baseTextview_error.setVisibility(View.GONE);
                                finish();


                            } else {
                                if (jsonObject_main.has(JSONCommonKeywords.message)) {
                                    responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                                }


                                if (responcebean.getErrorMessage() == null) {
                                    CommonMethods.customToastMessage(getResources().getString(R.string.create_news_failed), CreateNewsAndAnnouncementActivity.this);
                                } else {
                                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateNewsAndAnnouncementActivity.this);

                                }

                            }


                        } catch (Exception e) {
                            CommonMethods.customToastMessage(e.getMessage(), CreateNewsAndAnnouncementActivity.this);

                        }

                    } else {
                        if (responcebean.getErrorMessage() == null) {
                            CommonMethods.customToastMessage(getResources().getString(R.string.create_news_failed), CreateNewsAndAnnouncementActivity.this);
                        } else {
                            CommonMethods.customToastMessage(responcebean.getErrorMessage(), CreateNewsAndAnnouncementActivity.this);

                        }
                    }


                }
            }, CreateNewsAndAnnouncementActivity.this, getApplicationContext(), URLs.CREATENEWS, jsonObjectGetPostParameterEachScreen, ScreensEnums.CREATE_CLASSIFIED.getScrenIndex(), SignInActivity.class.getClass());

            passParameterbean.setRequestMethod(HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex());
            passParameterbean.setFileArrayList_uploading(filebeanArrayList_uploading);
            passParameterbean.setNoNeedToDisplayLoadingDialog(false);
            passParameterbean.setForImageUploadingCustomKeyword(true);
            passParameterbean.setForImageUploadingCustomKeywordName(JSONCommonKeywords.NewsImage);
            new RequestToServer(passParameterbean, null).execute();
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }

    }

    private void startDialog() {
        try {
          /*  AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
            myAlertDialog.setTitle(getResources().getString(R.string.upload_pictures_title));
            myAlertDialog.setMessage(getResources().getString(R.string.how_do_you_want_set));

            myAlertDialog.setPositiveButton(getResources().getString(R.string.gallery),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            pictureActionIntent = new Intent(
                                    Intent.ACTION_GET_CONTENT, null);
                            pictureActionIntent.setType("image*//*");
                            pictureActionIntent.putExtra("return-data", true);
                            startActivityForResult(pictureActionIntent,
                                    GALLERY_PICTURE);
                        }
                    });

            myAlertDialog.setNegativeButton(getResources().getString(R.string.camera),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                            }

                        }
                    });
            myAlertDialog.show();*/
            showFileChooser();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // intent.setType("application/*");
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(CreateNewsAndAnnouncementActivity.this, "Please install a File Manager.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        try {
            if (requestCode == 1) {
                if (resultCode == Activity.RESULT_OK) {

                    Uri selectedFileURI = data.getData();
                    String getAudioPath = getPath(selectedFileURI);


                    String mimeType = CommonMethods.getMimeType(CreateNewsAndAnnouncementActivity.this, selectedFileURI);

                    CommonMethods.displayLog("MIME", mimeType);


                    recyclerView_grid_news_images.setVisibility(View.VISIBLE);
                    relativeLayout_upload_image_plus_view.setVisibility(View.GONE);


                    if (filebeanArrayList_uploading.size() > 1) {
                        //remove top layout
                        Filebean filebean = filebeanArrayList_uploading.get(filebeanArrayList_uploading.size() - 1);
                        filebeanArrayList_uploading.remove(filebean);
                    } else {

                    }

                    Filebean filebean = new Filebean();
                    filebean.setFilePath(getAudioPath);
                    filebean.setMimeType(mimeType);
                    filebeanArrayList_uploading.add(filebean);
                    filebeanArrayList_uploading.add(filebeanArrayList_uploading.size(), getDefualPlusbean());

                    if (uploadImageAdapter != null) {
                        uploadImageAdapter.notifyDataSetChanged();
                    }
                }
            }


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }

    public Bitmap decodeAndResizeFile(File f) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 100;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    Filebean getDefualPlusbean() {
        Filebean filebean = new Filebean();
        filebean.setPlusIconNeedToEnable(true);
        return filebean;
    }

    public Bitmap retriveVideoFrameFromVideo(String p_videoPath)
            throws Throwable {
        Bitmap m_bitmap = null;
        MediaMetadataRetriever m_mediaMetadataRetriever = null;
        try {
            m_mediaMetadataRetriever = new MediaMetadataRetriever();
            m_mediaMetadataRetriever.setDataSource(p_videoPath);
            m_bitmap = m_mediaMetadataRetriever.getFrameAtTime();
        } catch (Exception m_e) {
            throw new Throwable(
                    "Exception in retriveVideoFrameFromVideo(String p_videoPath)"
                            + m_e.getMessage());
        } finally {
            if (m_mediaMetadataRetriever != null) {
                m_mediaMetadataRetriever.release();
            }
        }
        return m_bitmap;
    }

    public String getPath(final Uri uri) {

        //check here to KITKAT or new version
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(CreateNewsAndAnnouncementActivity.this, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(CreateNewsAndAnnouncementActivity.this, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(CreateNewsAndAnnouncementActivity.this, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(CreateNewsAndAnnouncementActivity.this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageViewHolder> {

        private Context context;

        public UploadImageAdapter(Context context) {
            this.context = context;
        }

        @Override
        public UploadImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.only_imageview_with_close_icon, null);
            UploadImageViewHolder rcv = new UploadImageViewHolder(layoutView);
            return rcv;
        }

        @Override
        public void onBindViewHolder(UploadImageViewHolder holder, int position) {


            try {
                final Filebean filebean = filebeanArrayList_uploading.get(position);

                holder.imageview_delete_icon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Imagebean imagebean1 = (Imagebean) v.getTag();
                        filebeanArrayList_uploading.remove(imagebean1.getBitmap());
                        if (filebeanArrayList_uploading.size() == 0) {
                            recyclerView_grid_news_images.setVisibility(View.GONE);
                            relativeLayout_upload_image_plus_view.setVisibility(View.VISIBLE);
                        }
                        uploadImageAdapter = new UploadImageAdapter(CreateNewsAndAnnouncementActivity.this);
                        recyclerView_grid_news_images.setAdapter(uploadImageAdapter);

                    }
                });

                if (filebean.isPlusIconNeedToEnable()) {

                    holder.view_main.setTag(filebean);
                    holder.imageview_delete_icon.setVisibility(View.GONE);
                    holder.imageview_singe_only.setImageResource(R.drawable.plus_icon);
                    holder.imageview_play_icon_if_video.setVisibility(View.GONE);

                } else {

                    if (filebean.getMimeType() != null) {
                        if (filebean.getMimeType().equals("jpg")) {

                            File file = new File(filebean.getFilePath());
                            Bitmap bitmap = decodeAndResizeFile(file);
                            if (bitmap != null) {
                                holder.imageview_singe_only.setImageBitmap(bitmap);

                            }
                            holder.imageview_play_icon_if_video.setVisibility(View.GONE);


                        } else if (filebean.getMimeType().equals("mp4"))


                        {

                            Bitmap bitmap = getVidioThumbnail(filebean.getFilePath());
                            if (bitmap != null) {
                                holder.imageview_singe_only.setImageBitmap(bitmap);

                            }
                            holder.imageview_play_icon_if_video.setVisibility(View.VISIBLE);

                        } else if (filebean.getMimeType().equals("video/x-msvideo"))


                        {

                            Bitmap bitmap = getVidioThumbnail(filebean.getFilePath());
                            if (bitmap != null) {
                                holder.imageview_singe_only.setImageBitmap(bitmap);

                            }
                            holder.imageview_play_icon_if_video.setVisibility(View.VISIBLE);


                        } else if (filebean.getMimeType().equals("video/3gpp")) {
                            Bitmap bitmap = getVidioThumbnail(filebean.getFilePath());
                            if (bitmap != null) {
                                holder.imageview_singe_only.setImageBitmap(bitmap);

                            }
                            holder.imageview_play_icon_if_video.setVisibility(View.VISIBLE);

                        } else if (filebean.getMimeType().equals("video/vnd.uvvu.mp4")) {
                            Bitmap bitmap = getVidioThumbnail(filebean.getFilePath());
                            if (bitmap != null) {
                                holder.imageview_singe_only.setImageBitmap(bitmap);

                            } else {
                                bitmap = retriveVideoFrameFromVideo(filebean.getFilePath());
                                if (bitmap != null) {
                                    holder.imageview_singe_only.setImageBitmap(bitmap);

                                }
                            }
                            holder.imageview_play_icon_if_video.setVisibility(View.VISIBLE);

                        } else {

                            CommonMethods.setDefualtImageBasedONMIMEType(filebean.getMimeType(), holder.imageview_singe_only);
                            holder.imageview_play_icon_if_video.setVisibility(View.GONE);

                        }
                    } else {
                        holder.imageview_singe_only.setImageResource(R.drawable.defualt_square);
                        holder.imageview_play_icon_if_video.setVisibility(View.GONE);
                    }


                }
                holder.imageview_delete_icon.setTag(filebean);


            } catch (Resources.NotFoundException e) {
                CommonMethods.displayLog("error", e.getMessage());

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return filebeanArrayList_uploading.size();
        }
    }

    public class UploadImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageview_singe_only = null, imageview_delete_icon = null, imageview_play_icon_if_video = null;
        View view_main = null;

        public UploadImageViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            view_main = itemView;
            imageview_singe_only = (ImageView) itemView.findViewById(R.id.imageview_single_only);
            imageview_delete_icon = (ImageView) itemView.findViewById(R.id.close_dialog);
            imageview_play_icon_if_video = (ImageView) itemView.findViewById(R.id.imageview_play_icon);

        }

        @Override
        public void onClick(View view) {
            if (filebeanArrayList_uploading.size() >= 12) {
                CommonMethods.customToastMessage(getResources().getString(R.string.image_upload_limit_corss_message_all_files), CreateNewsAndAnnouncementActivity.this);
            } else {
                Filebean filebean = (Filebean) view.getTag();
                if (filebean.isPlusIconNeedToEnable()) {
                    startDialog();
                }
            }

        }
    }
}
