package com.app.collow.activities;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.app.collow.R;
import com.app.collow.adapters.LeftMenuAdapter;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.LeftMenuItembean;
import com.app.collow.beans.Loginbean;
import com.app.collow.beans.Searchbean;
import com.app.collow.collowinterfaces.MyOnClickListener;
import com.app.collow.utils.CommonMethods;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.RecyclerItemClickListener;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.linkedin.platform.LISessionManager;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harmis on 8/1/17.
 */

public class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    //this is search result globally defined user for different activities
    public GoogleApiClient mGoogleApiClient;
    public DrawerLayout drawerLayout = null;
    public FrameLayout frameLayout_container = null;
    public Toolbar toolbar_header = null;
    BaseTextview baseTextview_setting = null;
    CommonSession commonSession_baseclass = null;
    CircularImageView circularImageView_profile_pic = null;
    RecyclerView recyclerView_left_menu_items = null;
    ArrayList<LeftMenuItembean> leftMenuItembeanArrayList = new ArrayList<>();
    //Right navigation iterms
    FrameLayout frameLayout_profile_user_icon = null;
    View view_right_navigation = null;
    //Left navigation item

    BaseTextview baseTextview_left_menu_unread_message = null;
    FrameLayout frameLayout_inbox_message = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_screen);
        commonSession_baseclass = new CommonSession(BaseActivity.this);


        loadLeftMenuData();
        FacebookSdk.sdkInitialize(getApplicationContext());

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
        // [END build_client]
        findViewByIDs();
        setupEvents();

    }


    private void findViewByIDs() {
        //Sign in findviewby id
        toolbar_header = (Toolbar) findViewById(R.id.toolbar);
        frameLayout_container = (FrameLayout) findViewById(R.id.frame_container);
        view_right_navigation = findViewById(R.id.rightview);
        //set up left menu
        setupLeftMenu();

drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        baseTextview_setting = (BaseTextview) findViewById(R.id.textview_setting);


        //Left navigatin items
        circularImageView_profile_pic = (CircularImageView) findViewById(R.id.profile_image);
        frameLayout_profile_user_icon = (FrameLayout) findViewById(R.id.layout_left_navigation_user_icon);

        final Loginbean loginbean = CommonMethods.convertJSONToLoginbean(commonSession_baseclass.getLoginJsonContent());
        if (loginbean != null) {
            if (CommonMethods.isImageUrlValid(loginbean.getProfile_pic())) {
                Picasso.with(this)
                        .load(loginbean.getProfile_pic())
                        .into(circularImageView_profile_pic, new Callback() {
                            @Override
                            public void onSuccess() {
                                CommonMethods.displayLog("Success", "Image done");
                            }

                            @Override
                            public void onError() {
                                circularImageView_profile_pic.setImageResource(R.drawable.user_defualt_icon);
                            }
                        });
            }
        }

        frameLayout_profile_user_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);
                if (EditProfileActivity.editProfileActivity != null) {
                    EditProfileActivity.editProfileActivity.finish();
                }
                Intent intent = new Intent(BaseActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        circularImageView_profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(Gravity.LEFT);

                if (EditProfileActivity.editProfileActivity != null) {
                    EditProfileActivity.editProfileActivity.finish();
                }
                Intent intent = new Intent(BaseActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setupLeftMenu() {

        frameLayout_inbox_message = (FrameLayout) findViewById(R.id.framlayout_left_menu_message);
        baseTextview_left_menu_unread_message = (BaseTextview) findViewById(R.id.textview_left_menu_unread_message_counter);


        recyclerView_left_menu_items = (RecyclerView) findViewById(R.id.my_recycler_view_left_menu_iterms_new);
        LeftMenuAdapter leftMenuAdapter = new LeftMenuAdapter(leftMenuItembeanArrayList);
        // Define layout manager
        recyclerView_left_menu_items.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_left_menu_items.setAdapter(leftMenuAdapter);
        recyclerView_left_menu_items.addOnItemTouchListener(
                new RecyclerItemClickListener(BaseActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        String name = view.getTag().toString();
                        drawerLayout.closeDrawer(Gravity.LEFT);

                        if (name != null) {
                            if (name.equals(getResources().getString(R.string.home))) {
                                //home
                                if (CommunityActivitiesFeedActivitiy.communityActivitiesFeedActivitiy != null) {
                                    CommunityActivitiesFeedActivitiy.communityActivitiesFeedActivitiy.finish();
                                }

                                Intent intent = new Intent(BaseActivity.this, CommunityActivitiesFeedActivitiy.class);
                                startActivity(intent);
                            } else if (name.equals(getResources().getString(R.string.activity))) {
                                //activities
                                if (MyActivitiesActivity.myActivitiesActivity != null) {
                                    MyActivitiesActivity.myActivitiesActivity.finish();
                                }

                                Intent intent = new Intent(BaseActivity.this, MyActivitiesActivity.class);
                                startActivity(intent);
                            } else if (name.equals(getResources().getString(R.string.my_posts))) {
                                //post
                                   if (MyPostActivity.myPostActivity != null) {
                                    MyPostActivity.myPostActivity.finish();
                                }
                                Intent intent = new Intent(BaseActivity.this, MyPostActivity.class);
                                startActivity(intent);
                            } else if (name.equals(getResources().getString(R.string.following))) {
                                //following

                                if (FollowingActivity.followingActivity != null) {
                                    FollowingActivity.followingActivity.finish();
                                }
                                Intent intent = new Intent(BaseActivity.this, FollowingActivity.class);
                                startActivity(intent);
                            } else if (name.equals(getResources().getString(R.string.search))) {
                                //search

                                if (CommunitySearchFilterOptionsActivity.communitySearchFilterOptionsActivity != null) {
                                    CommunitySearchFilterOptionsActivity.communitySearchFilterOptionsActivity.finish();
                                }
                                //search
                                Intent intent = new Intent(BaseActivity.this, CommunitySearchFilterOptionsActivity.class);
                                startActivity(intent);
                            } else if (name.equals(getResources().getString(R.string.settings))) {
                                //setting

                            } else if (name.equals(getResources().getString(R.string.team_title))) {
                                //activities
                                if (MgmtTeamListingActivity.mgmtTeamListingActivity != null) {
                                    MgmtTeamListingActivity.mgmtTeamListingActivity.finish();
                                }
                                Intent intent = new Intent(BaseActivity.this, MgmtTeamListingActivity.class);
                                startActivity(intent);

                            }

                        }

                    }


                })
        );


        frameLayout_inbox_message.setOnClickListener(new MyOnClickListener(BaseActivity.this) {
            @Override
            public void onClick(View v) {
                if (isAvailableInternet()) {
                    drawerLayout.closeDrawer(Gravity.LEFT);
                    if (CollowMessageMainActivity.collowMessageMainActivity != null) {
                        CollowMessageMainActivity.collowMessageMainActivity.finish();
                    }
                    Intent intent = new Intent(BaseActivity.this, CollowMessageMainActivity.class);
                    startActivity(intent);
                } else {
                    showInternetNotfoundMessage();
                }

            }
        });
    }

    private void setupEvents() {
        baseTextview_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SettingActivity.settingActivity != null) {
                    SettingActivity.settingActivity.finish();
                }
                Intent intent = new Intent(BaseActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }


    protected void logoutDialog() {
        try {
            AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                    BaseActivity.this);
            // myAlertDialog.setTitle(getResources().getString(R.string.contact_title));
            myAlertDialog.setMessage(getResources().getString(R.string.logout_message));

            myAlertDialog.setPositiveButton(getResources().getString(R.string.logout_ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {


                            try {
                                if (SettingActivity.settingActivity != null) {
                                    SettingActivity.settingActivity.finish();
                                }

                                LISessionManager.getInstance(getApplicationContext()).clearSession();

                                LoginManager.getInstance().logOut();

                                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                                        new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(Status status) {

                                            }
                                        });

                                commonSession_baseclass.resetLoggedUserID();
                                Intent intent = new Intent(BaseActivity.this, SignInActivity.class);
                                ComponentName cn = intent.getComponent();
                                Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
                                startActivity(mainIntent);


                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                    });

            myAlertDialog.setNegativeButton(getResources().getString(R.string.logout_cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {


                        }
                    });
            myAlertDialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void loadLeftMenuData() {


        leftMenuItembeanArrayList.add(new LeftMenuItembean(getResources().getString(R.string.home)));
        leftMenuItembeanArrayList.add(new LeftMenuItembean(getResources().getString(R.string.activity)));
        leftMenuItembeanArrayList.add(new LeftMenuItembean(getResources().getString(R.string.my_posts)));
        leftMenuItembeanArrayList.add(new LeftMenuItembean(getResources().getString(R.string.following)));

        if (commonSession_baseclass.isUserAssociateWithTeam()) {
            leftMenuItembeanArrayList.add(new LeftMenuItembean(getResources().getString(R.string.team_title)));

        } else {

        }
        leftMenuItembeanArrayList.add(new LeftMenuItembean(getResources().getString(R.string.search)));

    }


}