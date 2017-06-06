package com.app.collow.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseEdittext;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.baseviews.MyButtonView;
import com.app.collow.beans.CommunityAccessbean;
import com.app.collow.beans.CommunityActivitiesFeedbean;
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
import com.app.collow.utils.ConnectionDetector;
import com.app.collow.utils.JSONCommonKeywords;
import com.app.collow.utils.URLs;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.StringTokenizer;

/**
 * Created by harmis on 6/1/17.
 */

public class SignInActivity extends AppCompatActivity implements SetupViewInterface,
        GoogleApiClient.OnConnectionFailedListener {


    //Sign in
    ImageView imageView_signin_profile_pic_normal = null;
    CircularImageView circularImageView_profile_pic = null;
    ImageView imageView_signin_facebook = null;
    ImageView imageView_signin_linkdin = null;
    ImageView imageView_signin_gmail = null;
    BaseEdittext edittext_signin_email = null;
    BaseEdittext editText_signin_password = null;
    BaseTextview textview_signin_create = null;
    MyButtonView baseTextview_signin = null;

    BaseTextview baseTextview_heade_title = null, baseTextview_already_registered = null;
    BaseTextview baseTextview_forgot_password = null;
    CommonSession commonSession = null;
    CommonMethods commonMethods = null;
    RequestParametersbean requestParametersbean = new RequestParametersbean();
    RetryParameterbean retryParameterbean = null;
    ConnectionDetector connectionDetector = null;
    private CallbackManager callbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;
    public static SignInActivity signInActivity=null;

    //this is used for checking need to call facebook request for getting url in do in background
    boolean isFirsneedToTakeFbProfilePicURL=false;

    // set the permission to retrieve basic information of User's linkedIn account
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        commonSession = new CommonSession(SignInActivity.this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        retryParameterbean = new RetryParameterbean(SignInActivity.this, getApplicationContext(), getIntent().getExtras(), SignInActivity.class.getClass());
        connectionDetector = new ConnectionDetector(SignInActivity.this);
        signInActivity=this;
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
        //Sign in findviewby id
        imageView_signin_profile_pic_normal = (ImageView) findViewById(R.id.image_signin_pro_pic_normal);
        circularImageView_profile_pic = (CircularImageView) findViewById(R.id.image_signin_pro_pic_circle);
        imageView_signin_facebook = (ImageView) findViewById(R.id.imageview_signin_facebook);
        imageView_signin_linkdin = (ImageView) findViewById(R.id.imageview_signin_linkdin);
        imageView_signin_gmail = (ImageView) findViewById(R.id.imageview_signin_gmail);
        edittext_signin_email = (BaseEdittext) findViewById(R.id.edittext_signinemail);
        editText_signin_password = (BaseEdittext) findViewById(R.id.edittext_signinpassword);
        baseTextview_signin = (MyButtonView) findViewById(R.id.mybuttonview_signin);
        textview_signin_create = (BaseTextview) findViewById(R.id.textview_signin_create);



        textview_signin_create = (BaseTextview) findViewById(R.id.textview_signin_create);


        baseTextview_forgot_password = (BaseTextview) findViewById(R.id.textview_forgot_passowrd);
        /*SpannableString content_terms = new SpannableString(getResources().getString(R.string.forgot_password));
        content_terms.setSpan(new UnderlineSpan(), 0, content_terms.length(), 0);
        baseTextview_forgot_password.setText(content_terms);
*/

        baseTextview_heade_title = (BaseTextview) findViewById(R.id.textview_header_title);
        baseTextview_heade_title.setText(getResources().getString(R.string.sign_in));
    }

    private void setupEvents() {
        textview_signin_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SingUpActivity.singUpActivity!=null)
                {
                    SingUpActivity.singUpActivity.finish();
                }
                Intent intent = new Intent(SignInActivity.this, SingUpActivity.class);
                startActivity(intent);
            }
        });


        //setup initial information for facebook
        callbackManager = CallbackManager.Factory.create();


        edittext_signin_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString() == null || s.toString().equals("")) {

                } else {
                    if (CommonMethods.emailValidator(s.toString())) {
                        getProfilePicByEmail(s.toString());
                    }
                }
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


                                         String id=object.getString("id");
                                          String fullName=object.getString("name");
                                         String email=object.getString("email");

                                        StringTokenizer stok = new StringTokenizer(fullName);
                                        String firstName = stok.nextToken();

                                        StringBuilder middleName = new StringBuilder();
                                        String lastName = stok.nextToken();
                                        while (stok.hasMoreTokens())
                                        {
                                            middleName.append(lastName + " ");
                                            lastName = stok.nextToken();
                                        }

                                        requestParametersbean.setUser_email(email);
                                        requestParametersbean.setFirst_name(firstName);
                                        requestParametersbean.setLast_name(lastName);
                                        requestParametersbean.setId(id);

                                        if (object.has("picture")) {
                                            String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                            requestParametersbean.setProfile_pic(profilePicUrl);

                                        }


                                        requestParametersbean.setSocialType(CommonKeywords.SOCIAL_TYPE_FACEBOOK);
                                        isFirsneedToTakeFbProfilePicURL=true;

                                        requestParametersbean.setIsSocial(CommonKeywords.SOCIAL_YES);

                                        doLogin();

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
        imageView_signin_facebook.setOnClickListener(new MyOnClickListener(SignInActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    requestParametersbean.setUser_email("");
                    requestParametersbean.setUser_password("");
                    edittext_signin_email.setText("");
                    editText_signin_password.setText("");

                    LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile", "user_birthday"));

                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                }


            }
        });


        imageView_signin_linkdin.setOnClickListener(new MyOnClickListener(SignInActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    requestParametersbean.setUser_email("");
                    requestParametersbean.setUser_password("");
                    edittext_signin_email.setText("");
                    editText_signin_password.setText("");
                    LISessionManager sessionManager = LISessionManager
                            .getInstance(SignInActivity.this);
                    LISession session = sessionManager.getSession();
                    boolean accessTokenValid = session.isValid();

                    if (accessTokenValid) {
                        getLinkedInProfile();
                    } else {

                        LISessionManager.getInstance(getApplicationContext())
                                .init(SignInActivity.this, buildScope(), new AuthListener() {
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

        imageView_signin_gmail.setOnClickListener(new MyOnClickListener(SignInActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    requestParametersbean.setUser_email("");
                    requestParametersbean.setUser_password("");
                    edittext_signin_email.setText("");
                    editText_signin_password.setText("");
                    signIn();

                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                }


            }
        });
        baseTextview_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordMainActivity.class);
                startActivity(intent);
            }
        });


        baseTextview_signin.setOnClickListener(new MyOnClickListener(SignInActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    if (TextUtils.isEmpty(edittext_signin_email.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_email), SignInActivity.this);

                    } else if (!CommonMethods.emailValidator(edittext_signin_email.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_valid_email), SignInActivity.this);

                    } else if (TextUtils.isEmpty(editText_signin_password.getText().toString())) {
                        CommonMethods.customToastMessage(getResources().getString(R.string.enter_password), SignInActivity.this);

                    } else {
                        requestParametersbean.setUser_email(edittext_signin_email.getText().toString());
                        requestParametersbean.setUser_password(editText_signin_password.getText().toString());
                        isFirsneedToTakeFbProfilePicURL=false;

                        requestParametersbean.setIsSocial(CommonKeywords.SOCIAL_NO);

                        doLogin();
                    }

                } else {
                    CommonMethods.customToastMessage(getResources().getString(R.string.internet_connection_slow), getApplicationContext());

                }


            }
        });

    }

    @Override
    public void setupUI(Responcebean responcebean) {

        try {

            if (responcebean.getScreenIndex() == ScreensEnums.LOGIN.getScrenIndex()) {
                //handle login response
                handleLoginResponce(responcebean);
            } else {
                //handle get profile pic by email response
                handlegetProfilePicByEmailResponce(responcebean);

            }


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);


        }


    }

    // [START signIn]
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent i) {
        callbackManager.onActivityResult(reqCode, resCode, i);

        LISessionManager.getInstance(getApplicationContext())
                .onActivityResult(this,
                        reqCode, resCode, i);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (reqCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(i);
            handleSignInResult(result);
        }
    }

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d( "handleSignInResult:","" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount acct = result.getSignInAccount();

            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            CommonMethods.displayLog("Gmail",personEmail+" "+personName +""+personGivenName);


            StringTokenizer stok = new StringTokenizer(personName);
            String firstName = stok.nextToken();

            StringBuilder middleName = new StringBuilder();
            String lastName = stok.nextToken();
            while (stok.hasMoreTokens())
            {
                middleName.append(lastName + " ");
                lastName = stok.nextToken();
            }
            if(personPhoto == null)
            {//set default image
            }else{
                requestParametersbean.setProfile_pic(personPhoto.toString());

            }


            requestParametersbean.setUser_email(personEmail);
            requestParametersbean.setFirst_name(firstName);
            requestParametersbean.setLast_name(lastName);

            requestParametersbean.setId(personId);


            requestParametersbean.setSocialType(CommonKeywords.SOCIAL_TYPE_GMAIL);

            isFirsneedToTakeFbProfilePicURL=false;

            requestParametersbean.setIsSocial(CommonKeywords.SOCIAL_YES);

            doLogin();


        } else {
            // Signed out, show unauthenticated UI.
        }
    }
    // [END handleSignInResult]
    /*
    This way linkedin responce comes

    {
        "emailAddress": "vasim@harmistechnology.com",
            "firstName": "vasim",
            "id": "dUQJmh_umZ",
            "lastName": "mansuri",
            "pictureUrl": "https://media.licdn.com/mpr/mprx/0_z4FpD6CoPyStDPNwUj5iDkPHxYElf9zwB01_Dk-Z8UY8rtGIMOcr_XQnlro7uAqbqRLGGiuWlaB3",
            "pictureUrls": {
        "_total": 1,
                "values": ["https://media.licdn.com/mpr/mprx/0__yLnRp1IirbzIWT33ebZYjpWicccW43337URjgCbm_nqDJP7_uksxRHb3Xi"]
    },
        "publicProfileUrl": "https://www.linkedin.com/in/vasim-mansuri-4995737b"
    }*/

    public void getLinkedInProfile() {


        String host = "api.linkedin.com";
        String topCardUrl = "https://" + host
                + "/v1/people/~:(id,first-name,last-name,public-profile-url,picture-url,email-address,picture-urls::(original))";
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(SignInActivity.this, topCardUrl, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse result) {
                try {
                   JSONObject jsonObject=result.getResponseDataAsJson();

                    if(jsonObject.has("emailAddress"))
                    {
                        requestParametersbean.setUser_email(jsonObject.getString("emailAddress"));
                    }
                    if(jsonObject.has("firstName"))
                    {
                        requestParametersbean.setFirst_name(jsonObject.getString("firstName"));
                    }if(jsonObject.has("lastName"))
                    {
                        requestParametersbean.setLast_name(jsonObject.getString("lastName"));
                    }

                    if(jsonObject.has("pictureUrl"))
                    {
                        requestParametersbean.setProfile_pic(jsonObject.getString("pictureUrl"));
                    }
                    if(jsonObject.has("id"))
                    {
                        requestParametersbean.setId(jsonObject.getString("id"));
                    }
                    isFirsneedToTakeFbProfilePicURL=false;


                    requestParametersbean.setIsSocial(CommonKeywords.SOCIAL_YES);
                    requestParametersbean.setSocialType(CommonKeywords.SOCIAL_TYPE_LINKEDIN);



                    doLogin();


                } catch (Exception e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onApiError(LIApiError error) {

            }
        });
    }


    // this method is used for login user
    public void doLogin() {
        try {



                JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.LOGIN.getScrenIndex(), requestParametersbean);
                PassParameterbean passParameterbean = new PassParameterbean(this,SignInActivity.this, getApplicationContext(), URLs.LOGIN, jsonObjectGetPostParameterEachScreen, ScreensEnums.LOGIN.getScrenIndex(), SignInActivity.class.getClass());

              passParameterbean.setNeedToFirstTakeFacebookProfilePic(isFirsneedToTakeFbProfilePicURL);

             new RequestToServer(passParameterbean,retryParameterbean).execute();


        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    public void handleLoginResponce(Responcebean responcebean) {
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



                        if (jsonObject_profile.has(JSONCommonKeywords.HomeCommunityId)) {

                            loginbean.setHomeCommunityId(jsonObject_profile.getString(JSONCommonKeywords.HomeCommunityId));


                        }if (jsonObject_profile.has(JSONCommonKeywords.HomeCommunityName)) {

                            loginbean.setHomeCommunityName(jsonObject_profile.getString(JSONCommonKeywords.HomeCommunityName));
                        }

                        if (jsonObject_profile.has(JSONCommonKeywords.isUserAssociateWithTeam)) {

                            int isUserAssociateWithTeam=jsonObject_profile.getInt(JSONCommonKeywords.isUserAssociateWithTeam);
                            if(isUserAssociateWithTeam==1)
                            {
                                loginbean.setUserAssociateWithTeam(true);
                                commonSession.storeUserAssociateWithTeam(true);

                            }
                            else
                            {
                                loginbean.setUserAssociateWithTeam(false);
                                commonSession.storeUserAssociateWithTeam(false);

                            }


                        }


                        CommunityAccessbean communityAccessbean=CommonMethods.getCommunityAccessFromJSON(jsonObject_profile);


                        loginbean.setCommunityAccessbean(communityAccessbean);





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

                    finish();
                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), SignInActivity.this);
                    Intent intent =null;

                    if(commonSession.isUserSetHomeCommunity())
                    {
                        intent = new Intent(SignInActivity.this, CommunityActivitiesFeedActivitiy.class);

                    }
                    else
                    {
                         intent = new Intent(SignInActivity.this, CommunitySearchFilterOptionsActivity.class);
                    }
                    ComponentName cn = intent.getComponent();
                    Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                    startActivity(mainIntent);
                } else {
                    if (responcebean.getErrorMessage() != null) {
                        CommonMethods.customToastMessage(responcebean.getErrorMessage(), SignInActivity.this);

                    }
                }
            } else {

                if (responcebean.getErrorMessage() != null) {
                    CommonMethods.customToastMessage(responcebean.getErrorMessage(), SignInActivity.this);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    // this method is used for getting user profile pic by email id

    public void getProfilePicByEmail(String email) {
        try {
            requestParametersbean.setUser_email(email);
            JSONObject jsonObjectGetPostParameterEachScreen = GetPostParameterEachScreen.getPostParametersAccordingIndex(ScreensEnums.GETPRODILEPICBYEMIAL.getScrenIndex(), requestParametersbean);
            PassParameterbean passParameterbean = new PassParameterbean(this,SignInActivity.this, getApplicationContext(), URLs.GETPROFILEPICBYEMIALID, jsonObjectGetPostParameterEachScreen, ScreensEnums.GETPRODILEPICBYEMIAL.getScrenIndex(), SignInActivity.class.getClass());
             passParameterbean.setNoNeedToDisplayLoadingDialog(true);
            new RequestToServer(passParameterbean,retryParameterbean).execute();
        } catch (Exception e) {
            e.printStackTrace();
            new BaseException(e, false, retryParameterbean);
        }
    }

    //{"success":1,"ImageUrl":"http:\/\/cygnasoftware.net\/collow-test\/uploads\/profilePic\/images\/collow_user_profile_pic.jpg"}
    public void handlegetProfilePicByEmailResponce(Responcebean responcebean) {
        try {
            if(responcebean.isServiceSuccess())
            {
                JSONObject jsonObject=new JSONObject(responcebean.getResponceContent());
                if(jsonObject!=null)
                {

                    if (jsonObject.has(JSONCommonKeywords.success)) {
                        int success = jsonObject.getInt(JSONCommonKeywords.success);
                        if (success == 1) {
                            if(jsonObject.has(JSONCommonKeywords.ImageUrl))
                            {
                                String url=jsonObject.getString(JSONCommonKeywords.ImageUrl);


                                if(CommonMethods.isImageUrlValid(url))
                                {

                                    Picasso.with(this)
                                            .load(url)
                                            .into(circularImageView_profile_pic, new Callback() {
                                                @Override
                                                public void onSuccess() {
                                                    imageView_signin_profile_pic_normal.setVisibility(View.GONE);
                                                    circularImageView_profile_pic.setVisibility(View.VISIBLE);
                                                }

                                                @Override
                                                public void onError() {
                                                    imageView_signin_profile_pic_normal.setVisibility(View.VISIBLE);
                                                    circularImageView_profile_pic.setVisibility(View.GONE);
                                                }
                                            });
                                }
                            }
                        }
                    }







                }

            }
            else
            {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}