package com.app.collow.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.adapters.SearchHistoryAdapter;
import com.app.collow.adapters.SearchResultsAdapter;
import com.app.collow.asyntasks.GetCityAndState;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.Locationbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.Searchbean;
import com.app.collow.collowinterfaces.GetLocationListener;
import com.app.collow.database.CollowDatabaseHandler;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.GPSTracker;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

/**
 * Created by Harmis on 26/01/17. This activity display options of search option (1)By community name (2) Near by (3) Recent searches
 */

public class CommunitySearchFilterOptionsActivity extends BaseActivity implements SetupViewInterface, GetLocationListener {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    public static RecyclerView recyclerView_recent_searches = null;
    public static BaseTextview baseTextview_empty_recent_searches = null;
    public static CommunitySearchFilterOptionsActivity communitySearchFilterOptionsActivity = null;
    View view_home = null;
    SearchResultsAdapter searchResultsAdapter = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RetryParameterbean retryParameterbean = null;
    BaseTextview baseTextview_search_by_communitiy_name = null;
    LinearLayout linearLayout_nearby_button = null;
    BaseTextview baseTextview_city_country_name_current_location = null;
    GPSTracker gpsTracker = null;
    PassParameterbean passParameterbean = null;
    CollowDatabaseHandler collowDatabaseHandler = null;
    List<Searchbean> searchbeanList_search_history = new ArrayList<>();
    CommonSession commonSession = null;
    GetCityAndState getCityAndState = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            communitySearchFilterOptionsActivity = this;
            retryParameterbean = new RetryParameterbean(CommunitySearchFilterOptionsActivity.this, getApplicationContext(), getIntent().getExtras(), CommunitySearchFilterOptionsActivity.class.getClass());
            collowDatabaseHandler = new CollowDatabaseHandler(CommunitySearchFilterOptionsActivity.this);
            commonSession = new CommonSession(this);
            setupHeaderView();
            findViewByIDs();
            setupEvents();

            getCityAndState = new GetCityAndState(CommunitySearchFilterOptionsActivity.this, getApplicationContext(), this, retryParameterbean, this);
            runOnUiThread(new TimerTask() {
                @Override
                public void run() {
                    searchbeanList_search_history = collowDatabaseHandler.getSearchedCommunities(commonSession.getLoggedUserID());
                    SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(CommunitySearchFilterOptionsActivity.this, searchbeanList_search_history);


                    recyclerView_recent_searches.setAdapter(searchHistoryAdapter);
                    if (searchbeanList_search_history.size() != 0) {
                        baseTextview_empty_recent_searches.setVisibility(View.GONE);
                    }
                    recyclerView_recent_searches.setVisibility(View.VISIBLE);


                }
            });

            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                if (checkAndRequestPermissions()) {
                    Locationbean locationbean = getCityAndState.callAndGetIfAllPermissionAvailabe();
                    baseTextview_city_country_name_current_location.setText(handleLocationDetails(locationbean));


                }


            } else {
                Locationbean locationbean = getCityAndState.callAndGetIfAllPermissionAvailabe();
                baseTextview_city_country_name_current_location.setText(handleLocationDetails(locationbean));


            }


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    public void setupHeaderView() {
        try {

            baseTextview_header_title = (BaseTextview) toolbar_header.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.search));

            imageView_left_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.GONE);
            imageView_right_menu = (ImageView) toolbar_header.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.VISIBLE);
            imageView_right_menu.setImageResource(R.drawable.cross);

            DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {

                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }

                @Override
                public void onDrawerOpened(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }

                @Override
                public void onDrawerClosed(View drawerView) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }

                @Override
                public void onDrawerStateChanged(int newState) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    drawerLayout.closeDrawer(Gravity.LEFT);

                }
            };
            drawerLayout.setDrawerListener(drawerListener);


            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
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

    private void findViewByIDs() {
        try {
            view_home = getLayoutInflater().inflate(R.layout.search_types_main, null);

            baseTextview_search_by_communitiy_name = (BaseTextview) view_home.findViewById(R.id.basetextview_button_search_by_communitiy_name);
            baseTextview_city_country_name_current_location = (BaseTextview) view_home.findViewById(R.id.baseTextview_city_country_name_current_location);
            baseTextview_empty_recent_searches = (BaseTextview) view_home.findViewById(R.id.empty_view);
            linearLayout_nearby_button = (LinearLayout) view_home.findViewById(R.id.linerlayout_search_by_near);
            recyclerView_recent_searches = (RecyclerView) view_home.findViewById(R.id.my_recycler_view_recent_search_listing);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(1);
            // use a linear layout manager
            recyclerView_recent_searches.setLayoutManager(mLayoutManager);
            recyclerView_recent_searches.addItemDecoration(dividerItemDecoration);

            frameLayout_container.addView(view_home);

        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    private void setupEvents() {
        baseTextview_search_by_communitiy_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 if (CommunitySearchByNameActivity.communitySearchByNameActivity != null) {
                       CommunitySearchByNameActivity.communitySearchByNameActivity.finish();
                  }
                  Intent intent = new Intent(CommunitySearchFilterOptionsActivity.this, CommunitySearchByNameActivity.class);
                  startActivity(intent);


            }
        });

        linearLayout_nearby_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    if (CommunitySearchResultsActivity.communitySearchResultsActivity != null) {
                        CommunitySearchResultsActivity.communitySearchResultsActivity.finish();
                    }
                    Intent intent = new Intent(CommunitySearchFilterOptionsActivity.this, CommunitySearchResultsActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO: Handle the error.
                }


            }
        });

    }

    private boolean checkAndRequestPermissions() {

        int access_fine_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);


        List<String> listPermissionsNeeded = new ArrayList<>();


        if (access_fine_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (
                            perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                            ) {
                        // process the normal flow
                        //else any one or both the permissions are not granted


                        try {
                            Locationbean locationbean = getCityAndState.callAndGetIfAllPermissionAvailabe();
                            baseTextview_city_country_name_current_location.setText(handleLocationDetails(locationbean));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)


                                ) {
                            showDialogOK("Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }


    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", okListener)
                .create()
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {



                getCityAndState.callAndGetIfAllPermissionAvailabe();



        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CommunitySearchFilterOptionsActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(intent, CommonKeywords.INTENT_INDEX_FOR_SETTING_INTENT);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    @Override
    public void setupUI(Responcebean responcebean) {
        if (responcebean.isServiceSuccess()) {
            final Locationbean locationbean = getCityAndState.getAddress(responcebean.getResponceContent());


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    baseTextview_city_country_name_current_location.setText(handleLocationDetails(locationbean));

                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    baseTextview_city_country_name_current_location.setText(getResources().getString(R.string.no_found_location));

                }
            });

        }
    }


    public String handleLocationDetails(Locationbean locationbean) {

        String location = "";
        if (locationbean != null) {
            final StringBuffer stringBuffer = new StringBuffer();
            if (locationbean.getCity() != null && !locationbean.getCity().equals("")) {
                stringBuffer.append(locationbean.getCity());
                stringBuffer.append(" ");


            }

            if (locationbean.getState() != null && !locationbean.getState().equals("")) {
                stringBuffer.append(locationbean.getState());
            }
            location = stringBuffer.toString();

        } else {

        }


        return location;
    }


    @Override
    public void updateLocation(final Locationbean locationbean) {
        if (locationbean != null)

        {
            if (locationbean.isServiceSuccess()) {
                runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        baseTextview_city_country_name_current_location.setText(handleLocationDetails(locationbean));

                    }
                });

            }
        }
    }
}