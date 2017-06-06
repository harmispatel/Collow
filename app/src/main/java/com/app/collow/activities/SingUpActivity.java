package com.app.collow.activities;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.allenums.HTTPRequestMethodEnums;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RequestParametersbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.httprequests.GetPostParameterEachScreen;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISession;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by harmis on 6/1/17.
 */

public class SingUpActivity extends AppCompatActivity implements SetupViewInterface,
        GoogleApiClient.OnConnectionFailedListener {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    protected static final int CAMERA_REQUEST = 0;
    protected static final int GALLERY_PICTURE = 1;
    private static final int RC_SIGN_IN = 9001;
    public static Bitmap bitmap;
    //Sign up
    ImageView imageView_signup_profile_pic = null;
    ImageView imageView_signup_facebook = null;
    ImageView imageView_signup_linkdin = null;
    CircularImageView circularImageView_profile_pic = null;
    ImageView imageView_signup_gmail = null;
    BaseEdittext edittext_signup_firstname = null;
    BaseEdittext editText_signup_lastname = null;
    BaseEdittext editText_signup_email = null;
    BaseEdittext editText_signup_password = null;
    MyButtonView baseTextview_signup = null;
    BaseTextview baseTextview_heade_title = null;
    BaseTextview baseTextview_terms_of_use = null;
    BaseTextview baseTextview_privacy_policy = null;
    CommonSession commonSession = null;
    BaseTextview textview_signup_iamaregistered = null;
    RetryParameterbean retryParameterbean = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    private Intent pictureActionIntent = null;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager;
    public static SingUpActivity singUpActivity=null;
    //this is used for checking need to call facebook request for getting url in do in background
    boolean isFirsneedToTakeFbProfilePicURL=false;
    // set the permission to retrieve basic information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        FacebookSdk.sdkInitialize(getApplicationContext());
        //setup initial information for facebook
        callbackManager = CallbackManager.Factory.create();
        singUpActivity=this;
        commonSession = new CommonSession(SingUpActivity.this);

        retryParameterbean = new RetryParameterbean(SingUpActivity.this, getApplicationContext(), getIntent().getExtras(), SingUpActivity.class.getClass());

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        findViewByIDs();
        setupEvents();


    }

    private void findViewByIDs() {
        //Sign up findviewby id
        imageView_signup_profile_pic = (ImageView) findViewById(R.id.imageview_signup_profilepic_normal);
        imageView_signup_facebook = (ImageView) findViewById(R.id.imageview_signup_facebook);
        imageView_signup_linkdin = (ImageView) findViewById(R.id.imageview_signup_linkdin);
        imageView_signup_gmail = (ImageView) findViewById(R.id.imageview_signup_gmail);
        edittext_signup_firstname = (BaseEdittext) findViewById(R.id.edittext_signup_firstname);
        editText_signup_lastname = (BaseEdittext) findViewById(R.id.edittext_signup_lastname);
        editText_signup_email = (BaseEdittext) findViewById(R.id.edittext_signup_emailaddress);
        editText_signup_password = (BaseEdittext) findViewById(R.id.edittext_signup_password);
        baseTextview_signup = (MyButtonView) findViewById(R.id.mybuttonview_signup);
        baseTextview_heade_title = (BaseTextview) findViewById(R.id.textview_header_title);
        textview_signup_iamaregistered = (BaseTextview) findViewById(R.id.textview_signup_iamaregistered);

        baseTextview_heade_title.setText(getResources().getString(R.string.signup));
        circularImageView_profile_pic = (CircularImageView) findViewById(R.id.imageview_signup_profilepic_circle);

        baseTextview_terms_of_use = (BaseTextview) findViewById(R.id.textview_signup_terms);
        baseTextview_privacy_policy = (BaseTextview) findViewById(R.id.textview_signup_privacy);


        SpannableString content_privacy = new SpannableString(getResources().getString(R.string.privacy_policy));
        content_privacy.setSpan(new UnderlineSpan(), 0, content_privacy.length(), 0);
        baseTextview_privacy_policy.setText(content_privacy);


        SpannableString content_terms = new SpannableString(getResources().getString(R.string.terms_of_use));
        content_terms.setSpan(new UnderlineSpan(), 0, content_terms.length(), 0);
        baseTextview_terms_of_use.setText(content_terms);


    }

    private void setupEvents() {
        textview_signup_iamaregistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SignInActivity.signInActivity!=null)
                {
                    SignInActivity.signInActivity.finish();
                }
                finish();
                Intent intent = new Intent(SingUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
        baseTextview_signup.setOnClickListener(new MyOnClickListener(SingUpActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {


                    if (TextUtils.isEmpty(edittext_signup_firstname.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_frist_name), SingUpActivity.this);
                    } else if (!CommonMethods.isMorethenThreeCharacters(edittext_signup_firstname.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), SingUpActivity.this);

                    } else if (TextUtils.isEmpty(editText_signup_lastname.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_last_name), SingUpActivity.this);

                    } else if (!CommonMethods.isMorethenThreeCharacters(editText_signup_lastname.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), SingUpActivity.this);

                    } else if (TextUtils.isEmpty(editText_signup_email.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_email), SingUpActivity.this);

                    } else if (!CommonMethods.isMorethenThreeCharacters(editText_signup_email.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), SingUpActivity.this);

                    } else if (!CommonMethods.emailValidator(editText_signup_email.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_valid_email), SingUpActivity.this);

                    } else if (TextUtils.isEmpty(editText_signup_password.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_password), SingUpActivity.this);

                    } else if (!CommonMethods.isMorethenThreeCharacters(editText_signup_password.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_minimum_three_characters), SingUpActivity.this);

                    } else {

                        requestParametersbean.setFirst_name(edittext_signup_firstname.getText().toString());
                        requestParametersbean.setLast_name(editText_signup_lastname.getText().toString());
                        requestParametersbean.setUser_email(editText_signup_email.getText().toString());
                        requestParametersbean.setUser_password(editText_signup_password.getText().toString());
                        requestParametersbean.setIsSocial(CommonKeywords.SOCIAL_NO);


                        doRegister();


                    }
                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());


                }


            }
        });

        imageView_signup_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });
        circularImageView_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDialog();
            }
        });


        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                try {


                    // Facebook Email address
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(
                                        JSONObject object,
                                        GraphResponse response) {
                                    Log.v("LoginActivity Response ", response.toString());

                                    try {


                                        //{Response:  responseCode: 200, graphObject: {"id":"2161939574031878","name":"Vasim Mansuri","email":"vasim@harmistechnology.com","gender":"male"}, error: null}


                                        String id = object.getString("id");
                                        String fullName = object.getString("name");
                                        String email = object.getString("email");


                                        StringTokenizer stok = new StringTokenizer(fullName);
                                        String firstName = stok.nextToken();

                                        StringBuilder middleName = new StringBuilder();
                                        String lastName = stok.nextToken();
                                        while (stok.hasMoreTokens()) {
                                            middleName.append(lastName + " ");
                                            lastName = stok.nextToken();
                                        }

                                        if (object.has("picture")) {
                                            String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                            requestParametersbean.setProfile_pic(profilePicUrl);

                                        }



                                        requestParametersbean.setUser_email(email);
                                        requestParametersbean.setFirst_name(firstName);
                                        requestParametersbean.setLast_name(lastName);
                                        requestParametersbean.setId(id);

                                        requestParametersbean.setSocialType(CommonKeywords.SOCIAL_TYPE_FACEBOOK);
                                        requestParametersbean.setIsSocial(CommonKeywords.SOCIAL_YES);

                                        doRegister();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields","id,name,email,gender,birthday,picture.type(large)");
                    request.setParameters(parameters);
                    request.executeAsync();


                } catch (Exception e) {
                    e.printStackTrace();

                }


            }

            @Override
            public void onCancel() {
                // [START_EXCLUDE]
                //  updateUI(null);
                // [END_EXCLUDE]
                Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                // [START_EXCLUDE]
                //  updateUI(null);
                // [END_EXCLUDE]
                Toast.makeText(getApplicationContext(), "onError", Toast.LENGTH_LONG).show();

            }
        });
        imageView_signup_facebook.setOnClickListener(new MyOnClickListener(SingUpActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    LoginManager.getInstance().logInWithReadPermissions(SingUpActivity.this, Arrays.asList("email", "public_profile", "user_birthday"));

                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                }


            }
        });


        imageView_signup_linkdin.setOnClickListener(new MyOnClickListener(SingUpActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    LISessionManager sessionManager = LISessionManager
                            .getInstance(SingUpActivity.this);
                    LISession session = sessionManager.getSession();
                    boolean accessTokenValid = session.isValid();

                    if (accessTokenValid) {
                        getLinkedInProfile();
                    } else {

                        LISessionManager.getInstance(getApplicationContext())
                                .init(SingUpActivity.this, buildScope(), new AuthListener() {
                                    @Override
                                    public void onAuthSuccess() {


                                        getLinkedInProfile();
                                    }

                                    @Override
                                    public void onAuthError(LIAuthError error) {

                                        Toast.makeText(getApplicationContext(), "failed "
                                                        + error.toString(),
                                                Toast.LENGTH_LONG).show();
                                    }
                                }, true);
                    }
                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                }


            }
        });

        imageView_signup_gmail.setOnClickListener(new MyOnClickListener(SingUpActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    signIn();

                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                }


            }
        });
    }

    @Override
    public void setupUI(Responcebean responcebean) {

        boolean isLoginSuccess = false;
        try {
            if (responcebean.isServiceSuccess()) {


                JSONObject jsonObject_main = null;
                jsonObject_main = new JSONObject(responcebean.getResponceContent());

                if (jsonObject_main.has(JSONCommonKeywords.success)) {
                    int success = jsonObject_main.getInt(JSONCommonKeywords.success);
                    if (success == 1) {
                        JSONObject jsonObject_profile = jsonObject_main.getJSONObject(JSONCommonKeywords.Profile);


                        Loginbean loginbean=new Loginbean();




                        if (jsonObject_profile.has(JSONCommonKeywords.userid)) {
                            isLoginSuccess = true;

                            loginbean.setUserid(jsonObject_profile.getString(JSONCommonKeywords.userid));
                            commonSession.storeLoggedUserID(loginbean.getUserid());

                        }

                        if (jsonObject_profile.has(JSONCommonKeywords.IsHomeCommunity)) {

                            String ishomecommunity=jsonObject_profile.getString(JSONCommonKeywords.IsHomeCommunity);
                            if(ishomecommunity==null||ishomecommunity.equals(""))
                            {
                                commonSession.storeUserSetHomeCommunity(false);
                                loginbean.setHomeDefualtCommunity(false);

                            }
                            else if(ishomecommunity.equals("1"))
                            {
                                loginbean.setHomeDefualtCommunity(true);
                                commonSession.storeUserSetHomeCommunity(true);
                            }
                            else
                            {
                                commonSession.storeUserSetHomeCommunity(false);
                                loginbean.setHomeDefualtCommunity(false);

                            }


                        }

                        if (jsonObject_profile.has(JSONCommonKeywords.name)) {

                            loginbean.setName(jsonObject_profile.getString(JSONCommonKeywords.name));
                        }
                        if (jsonObject_profile.has(JSONCommonKeywords.FirstName)) {

                            loginbean.setFirst_name(jsonObject_profile.getString(JSONCommonKeywords.FirstName));
                        }

                        if (jsonObject_profile.has(JSONCommonKeywords.LastName)) {

                            loginbean.setLast_name(jsonObject_profile.getString(JSONCommonKeywords.LastName));
                        }
                        if (jsonObject_profile.has(JSONCommonKeywords.IscompletedProfile)) {

                            loginbean.setIscompletedprofile(jsonObject_profile.getString(JSONCommonKeywords.IscompletedProfile));
                        }if (jsonObject_profile.has(JSONCommonKeywords.Email)) {

                            loginbean.setEmail(jsonObject_profile.getString(JSONCommonKeywords.Email));
                        }if (jsonObject_profile.has(JSONCommonKeywords.ProfilePic)) {

                            loginbean.setProfile_pic(jsonObject_profile.getString(JSONCommonKeywords.ProfilePic));
                        }

                        if(loginbean.getIscompletedprofile()==null||loginbean.getIscompletedprofile().equals(""))
                        {
                            commonSession.storeUserProfileCompleted(false);
                        }
                        else if(loginbean.getIscompletedprofile().equals("1"))
                        {
                            commonSession.storeUserProfileCompleted(true);

                        }
                        else
                        {
                            commonSession.storeUserProfileCompleted(false);

                        }


                        //this is convert loginbean object to json and storing in session
                        CommonMethods.convertLoginbeanToJSON(getApplicationContext(),loginbean);



                        if (jsonObject_main.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                        }
                    } else {
                        if (jsonObject_main.has(JSONCommonKeywords.message)) {
                            responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.message));
                        }
                    }
                } else {
                    if (jsonObject_main.has(JSONCommonKeywords.Comments)) {
                        responcebean.setErrorMessage(jsonObject_main.getString(JSONCommonKeywords.Comments));
                    }
                }


                if (isLoginSuccess) {


                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), SingUpActivity.this);
                    Intent intent = null;

                    if(commonSession.isUserSetHomeCommunity())
                    {
                        intent = new Intent(SingUpActivity.this, CommunityActivitiesFeedActivitiy.class);

                    }
                    else
                    {
                        intent = new Intent(SingUpActivity.this, CommunitySearchFilterOptionsActivity.class);

                    }
                    ComponentName cn = intent.getComponent();
                    Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                    startActivity(mainIntent);
                } else {
                    if (responcebean.getErrorMessage() != null) {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), SingUpActivity.this);

                    }
                }
            } else {

                if (responcebean.getErrorMessage() != null) {
                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), SingUpActivity.this);

                }
            }
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);

        }


    }


    private void startDialog() {
        try {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(this);
            myAlertDialog.setTitle(getResources().getString(R.string.upload_pictures_title));
            myAlertDialog.setMessage(getResources().getString(R.string.how_do_you_want_set));

            myAlertDialog.setPositiveButton(getResources().getString(R.string.gallery),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            pictureActionIntent = new Intent(
                                    Intent.ACTION_GET_CONTENT, null);
                            pictureActionIntent.setType("image/*");
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
            myAlertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GALLERY_PICTURE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    // Get the Image from data

                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        // Get the cursor
                        Cursor cursor = getContentResolver()
                                .query(selectedImage, filePathColumn, null,
                                        null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor
                                .getColumnIndex(filePathColumn[0]);



                            bitmap = decodeAndResizeFile(new File(
                                    cursor.getString(columnIndex)));
                        cursor.close();


                        imageView_signup_profile_pic.setVisibility(View.GONE);
                        circularImageView_profile_pic.setVisibility(View.VISIBLE);

                        if(bitmap!=null)
                        {
                            circularImageView_profile_pic.setImageBitmap(bitmap);

                        }


                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), SingUpActivity.this);
                }
            } else if (resultCode == RESULT_CANCELED) {
                CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), SingUpActivity.this);

            }
        } else if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {

                try {

                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");


                    imageView_signup_profile_pic.setVisibility(View.GONE);
                    circularImageView_profile_pic.setVisibility(View.VISIBLE);
                    if(bitmap!=null)
                    {
                        circularImageView_profile_pic.setImageBitmap(bitmap);

                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else if (resultCode == RESULT_CANCELED) {
                CommonMethods.customToastMessage(getResources().getString(R.string.cancelled), SingUpActivity.this);

            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);

            LISessionManager.getInstance(getApplicationContext())
                    .onActivityResult(this,
                            requestCode, resultCode, data);

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(result);
            }
        }

    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        try {
            if (result.isSuccess()) {
                // Signed in successfully, show authenticated UI.

                GoogleSignInAccount acct = result.getSignInAccount();

                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                CommonMethods.displayLog("Gmail", personEmail + " " + personName + "" + personGivenName);


                StringTokenizer stok = new StringTokenizer(personName);
                String firstName = stok.nextToken();

                StringBuilder middleName = new StringBuilder();
                String lastName = stok.nextToken();
                while (stok.hasMoreTokens()) {
                    middleName.append(lastName + " ");
                    lastName = stok.nextToken();
                }


                requestParametersbean.setUser_email(personEmail);
                requestParametersbean.setFirst_name(firstName);
                requestParametersbean.setLast_name(lastName);



                if(personPhoto == null)
                {//set default image
                }else{
                    requestParametersbean.setProfile_pic(personPhoto.toString());

                }

                requestParametersbean.setSocialType(CommonKeywords.SOCIAL_TYPE_GMAIL);

                requestParametersbean.setId(personId);


                requestParametersbean.setIsSocial(CommonKeywords.SOCIAL_YES);

                doRegister();


            } else {
                // Signed out, show unauthenticated UI.
            }
        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        bitmap = null;
        // bitmap.recycle();
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


    // this method is used for login user
    public void doRegister() {
        try {

                JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.REGISTER.getScrenIndex(), requestParametersbean);

            //if social comes new to works as login service
            String url=null;
            int request_method;

                if(requestParametersbean.getIsSocial().equals(CommonKeywords.SOCIAL_YES))
                {
                    url=URLs.LOGIN;
                    request_method=HTTPRequestMethodEnums.POST.getHTTPRequestMethodInex();
                }
            else
                {
                    url=URLs.REGISTER;

                    request_method=HTTPRequestMethodEnums.MIME.getHTTPRequestMethodInex();


                }
            PassParameterbean passParameterbean = new PassParameterbean(this, SingUpActivity.this, getApplicationContext(), url, jsonObjectGetPostParameterEachScreen, ScreensEnums.REGISTER.getScrenIndex(), SingUpActivity.class.getClass());
            passParameterbean.setRequestMethod(request_method);
            passParameterbean.setForImageUploadingCustomKeywordName("profile_pic");

            passParameterbean.setBitmap(bitmap);

                new RequestToServer(passParameterbean, retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void getLinkedInProfile() {


        String host = "api.linkedin.com";
        String topCardUrl = "https://" + host
                + "/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address,picture-urls::(original))";
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(SingUpActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                    JSONObject jsonObject = result.getResponseDataAsJson();

                    if (jsonObject.has("emailAddress")) {
                        requestParametersbean.setUser_email(jsonObject.getString("emailAddress"));
                    }
                    if (jsonObject.has("firstName")) {
                        requestParametersbean.setFirst_name(jsonObject.getString("firstName"));
                    }
                    if (jsonObject.has("lastName")) {
                        requestParametersbean.setLast_name(jsonObject.getString("lastName"));
                    }

                    if (jsonObject.has("pictureUrl")) {
                        requestParametersbean.setProfile_pic(jsonObject.getString("pictureUrl"));
                    }
                    if (jsonObject.has("id")) {
                        requestParametersbean.setId(jsonObject.getString("id"));
                    }
                    requestParametersbean.setSocialType(CommonKeywords.SOCIAL_TYPE_LINKEDIN);
                    requestParametersbean.setIsSocial(CommonKeywords.SOCIAL_YES);
                    doRegister();


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "error" +
                                    e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });
    }
}