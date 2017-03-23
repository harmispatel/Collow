package com.app.collow.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.adapters.SearchAdapter;
import com.app.collow.adapters.SearchHistoryAdapter;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.Responcebean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.beans.Searchbean;
import com.app.collow.database.CollowDatabaseHandler;
import com.app.collow.recyledecor.DividerItemDecoration;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.BaseException;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.CommonSession;
import com.app.collow.utils.GPSTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Harmis on 26/01/17.
 */

public class CommunitySearchFilterOptionsActivity extends BaseActivity implements SetupViewInterface {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    View view_home = null;
    SearchAdapter searchAdapter = null;
    BaseTextview baseTextview_header_title = null;
    //header iterms
    ImageView imageView_left_menu = null, imageView_right_menu = null;
    RetryParameterbean retryParameterbean = null;

    BaseTextview baseTextview_search_by_communitiy_name = null;
    LinearLayout linearLayout_nearby_button = null;
    BaseTextview baseTextview_city_country_name_current_location = null;
    public static RecyclerView recyclerView_recent_searches = null;
   public static BaseTextview baseTextview_empty_recent_searches = null;

    GPSTracker gpsTracker = null;
    PassParameterbean passParameterbean = null;
    CollowDatabaseHandler collowDatabaseHandler = null;
    List<Searchbean> searchbeanList_search_history = new ArrayList<>();
    CommonSession commonSession = null;
    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            retryParameterbean = new RetryParameterbean(CommunitySearchFilterOptionsActivity.this, getApplicationContext(), getIntent().getExtras(), CommunitySearchFilterOptionsActivity.class.getClass());
            collowDatabaseHandler = new CollowDatabaseHandler(CommunitySearchFilterOptionsActivity.this);
            commonSession = new CommonSession(this);
            setupHeaderView();
            findViewByIDs();
            setupEvents();

            runOnUiThread(new TimerTask() {
                @Override
                public void run() {
                    searchbeanList_search_history = collowDatabaseHandler.getSearchedCommunities(commonSession.getLoggedUserID());
                    SearchHistoryAdapter searchHistoryAdapter = new SearchHistoryAdapter(CommunitySearchFilterOptionsActivity.this, searchbeanList_search_history);


                    recyclerView_recent_searches.setAdapter(searchHistoryAdapter);
                    if(searchbeanList_search_history.size()!=0)
                    {
                        baseTextview_empty_recent_searches.setVisibility(View.GONE);
                    }
                    recyclerView_recent_searches.setVisibility(View.VISIBLE);


                }
            });

            if (Build.VERSION.SDK_INT >= 23) {
                // Marshmallow+
                if (checkAndRequestPermissions()) {
                    callIfpermissionavailable();

                }


            } else {
                callIfpermissionavailable();

            }


        } catch (Exception e) {
            new BaseException(e, false, retryParameterbean);
        }

    }

    public void setupHeaderView() {
        try {
            View headerview = getLayoutInflater().inflate(R.layout.header, null);
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerview.setLayoutParams(layoutParams);
            baseTextview_header_title = (BaseTextview) headerview.findViewById(R.id.textview_header_title);
            baseTextview_header_title.setText(getResources().getString(R.string.search));

            imageView_left_menu = (ImageView) headerview.findViewById(R.id.imageview_left_menu);
            imageView_left_menu.setVisibility(View.VISIBLE);
            imageView_right_menu = (ImageView) headerview.findViewById(R.id.imageview_right_menu);
            imageView_right_menu.setVisibility(View.GONE);


            imageView_left_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);


                }
            });
            imageView_right_menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.RIGHT);


                }
            });
            setSupportActionBar(toolbar_header);
            toolbar_header.addView(headerview);
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


                Intent intent = new Intent(CommunitySearchFilterOptionsActivity.this, CommunitySearchResultsActivity.class);

                startActivity(intent);


            }
        });

    }


    private boolean checkAndRequestPermissions() {

        int access_coarse_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        int access_fine_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);


        List<String> listPermissionsNeeded = new ArrayList<>();


        if (access_coarse_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
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
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (
                            perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                    && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                            ) {
                        // process the normal flow
                        //else any one or both the permissions are not granted


                        callIfpermissionavailable();

                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)


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


    public void callIfpermissionavailable() {
        gpsTracker = new GPSTracker(getApplicationContext());

        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            if (latitude == 0.0) {
                runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                // this code will be executed after 2 seconds
                                callIfpermissionavailable();
                                Looper.loop();
                            }
                        }, 4000);
                    }
                });

            } else {
                getLocationName(latitude, longitude);
            }


            //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            showSettingsAlert();

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
        callIfpermissionavailable();

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


    public String getLocationName(double lattitude, double longitude) {

        String cityName = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {


            if (gcd.isPresent()) {
                List<Address> addresses = gcd.getFromLocation(lattitude, longitude,
                        10);

                for (Address adrs : addresses) {
                    if (adrs != null) {


                        final StringBuffer stringBuffer = new StringBuffer();


                        String city = adrs.getLocality();
                        if (city != null && !city.equals("")) {
                            cityName = city;
                            System.out.println("city ::  " + cityName);

                            stringBuffer.append(city);
                            stringBuffer.append(" ");


                        } else {

                        }

                        String state = adrs.getAdminArea();
                        if (state != null && !state.equals("")) {
                            System.out.println("state ::  " + state);
                            stringBuffer.append(state);


                        } else {

                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                baseTextview_city_country_name_current_location.setText(stringBuffer.toString());

                            }
                        });


                        // // you should also try with addresses.get(0).toSring();

                    }

                }
            } else {
                String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lattitude + ","
                        + longitude + "&sensor=true";

                passParameterbean = new PassParameterbean(this, CommunitySearchFilterOptionsActivity.this, getApplicationContext(), url, null, -1, CommunitySearchFilterOptionsActivity.class.getClass());
                passParameterbean.setNoNeedToDisplayLoadingDialog(true);
                baseTextview_city_country_name_current_location.setText(getResources().getString(R.string.fetching_location));
                new RequestToServer(passParameterbean, retryParameterbean).execute();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return cityName;

    }

    @Override
    public void setupUI(Responcebean responcebean) {
        if (responcebean.isServiceSuccess()) {
            getAddress(responcebean.getResponceContent());


            final StringBuffer stringBuffer = new StringBuffer();
            if (getCity() != null && !getCity().equals(" ")) {
                stringBuffer.append(getCity());
                stringBuffer.append(" ");


            }

            if (getState() != null && !getState().equals("")) {
                stringBuffer.append(getState());
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    baseTextview_city_country_name_current_location.setText(stringBuffer.toString());

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


    public void getAddress(String jsoncontent) {
        Address1 = "";
        Address2 = "";
        City = "";
        State = "";
        Country = "";
        County = "";
        PIN = "";

        try {

            JSONObject jsonObj = new JSONObject(jsoncontent);
            String Status = jsonObj.getString("status");
            if (Status.equalsIgnoreCase("OK")) {
                JSONArray Results = jsonObj.getJSONArray("results");
                JSONObject zero = Results.getJSONObject(0);
                JSONArray address_components = zero.getJSONArray("address_components");

                for (int i = 0; i < address_components.length(); i++) {
                    JSONObject zero2 = address_components.getJSONObject(i);
                    String long_name = zero2.getString("long_name");
                    JSONArray mtypes = zero2.getJSONArray("types");
                    String Type = mtypes.getString(0);

                    if (TextUtils.isEmpty(long_name) == false || !long_name.equals(null) || long_name.length() > 0 || long_name != "") {
                        if (Type.equalsIgnoreCase("street_number")) {
                            Address1 = long_name + " ";
                        } else if (Type.equalsIgnoreCase("route")) {
                            Address1 = Address1 + long_name;
                        } else if (Type.equalsIgnoreCase("sublocality")) {
                            Address2 = long_name;
                        } else if (Type.equalsIgnoreCase("locality")) {
                            // Address2 = Address2 + long_name + ", ";
                            City = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            County = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            State = long_name;
                        } else if (Type.equalsIgnoreCase("country")) {
                            Country = long_name;
                        } else if (Type.equalsIgnoreCase("postal_code")) {
                            PIN = long_name;
                        }
                    }

                    // JSONArray mtypes = zero2.getJSONArray("types");
                    // String Type = mtypes.getString(0);
                    // Log.e(Type,long_name);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getAddress1() {
        return Address1;

    }

    public String getAddress2() {
        return Address2;

    }

    public String getCity() {
        return City;

    }

    public String getState() {
        return State;

    }

    public String getCountry() {
        return Country;

    }

    public String getCounty() {
        return County;

    }

    public String getPIN() {
        return PIN;

    }


}