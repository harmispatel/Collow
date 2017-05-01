package com.app.collow.asyntasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;

import com.app.collow.R;
import com.app.collow.activities.CommunitySearchFilterOptionsActivity;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.asyntasks.RequestToServer;
import com.app.collow.beans.Locationbean;
import com.app.collow.beans.PassParameterbean;
import com.app.collow.beans.RetryParameterbean;
import com.app.collow.collowinterfaces.GetLocationListener;
import com.app.collow.setupUI.SetupViewInterface;
import com.app.collow.utils.CommonKeywords;
import com.app.collow.utils.GPSTracker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Harmis on 01/02/17.
 */

public class GetCityAndState {


    private String Address1 = "", Address2 = "", City = "", State = "", Country = "", County = "", PIN = "";

    Activity activity=null;
    Context context=null;
    GPSTracker gpsTracker=null;
    PassParameterbean passParameterbean=null;
    SetupViewInterface setupViewInterface=null;
    RetryParameterbean retryParameterbean=null;
    GetLocationListener getLocationListener=null;

    public GetCityAndState(Activity activity, Context context, SetupViewInterface setupViewInterface, RetryParameterbean retryParameterbean,GetLocationListener getLocationListener)
    {
        this.activity=activity;
        this.context=context;
        this.setupViewInterface=setupViewInterface;
        this.retryParameterbean=retryParameterbean;
        this.getLocationListener=getLocationListener;
    }


    public Locationbean callAndGetIfAllPermissionAvailabe() {
        gpsTracker = new GPSTracker(activity);
        Locationbean locationbean=new Locationbean();

        if (gpsTracker.canGetLocation()) {
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();

            if (latitude == 0.0) {
                activity.runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                Looper.prepare();
                                // this code will be executed after 2 seconds
                                callAndGetIfAllPermissionAvailabe();
                                Looper.loop();
                            }
                        }, 2000);
                    }
                });

            } else {
                locationbean=getLocationName(latitude, longitude);
                locationbean.setServiceSuccess(true);
                getLocationListener.updateLocation(locationbean);
            }


            //   Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            showSettingsAlert();

        }
        return locationbean;
    }
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                activity.startActivityForResult(intent, CommonKeywords.INTENT_INDEX_FOR_SETTING_INTENT);
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


    public Locationbean getLocationName(double lattitude, double longitude) {

        Geocoder gcd = new Geocoder(activity, Locale.getDefault());
        Locationbean locationbean=new Locationbean();
        try {


            if (gcd.isPresent()) {
                List<Address> addresses = gcd.getFromLocation(lattitude, longitude,1);

                for (Address adrs : addresses) {
                    if (adrs != null) {




                        String city = adrs.getLocality();
                        if (city != null && !city.equals("")) {
                            locationbean.setCity(city);


                        } else {

                        }

                        String state = adrs.getAdminArea();
                        if (state != null && !state.equals("")) {
                            locationbean.setState(state);


                        } else {

                        }
                    }

                }
            } else {
                String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lattitude + ","
                        + longitude + "&sensor=true";

                passParameterbean = new PassParameterbean(setupViewInterface, activity, context, url, null, -1, activity.getClass());
                passParameterbean.setNoNeedToDisplayLoadingDialog(true);
                passParameterbean.setScreenIndex(ScreensEnums.GET_LATLONG_BY_GOOGLE.getScrenIndex());
              //  baseTextview_city_country_name_current_location.setText(getResources().getString(R.string.fetching_location));
                new RequestToServer(passParameterbean, retryParameterbean).execute();
            }


        } catch (Exception e) {
            e.printStackTrace();
            if(e instanceof IOException)
            {

               String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + lattitude + ","
                        + longitude + "&sensor=true";

                passParameterbean = new PassParameterbean(setupViewInterface, activity, context, url, null, -1, activity.getClass());
                passParameterbean.setNoNeedToDisplayLoadingDialog(true);
                passParameterbean.setScreenIndex(ScreensEnums.GET_LATLONG_BY_GOOGLE.getScrenIndex());
                //  baseTextview_city_country_name_current_location.setText(getResources().getString(R.string.fetching_location));
                new RequestToServer(passParameterbean, retryParameterbean).execute();
            }
        }
        return locationbean;

    }



    public Locationbean getAddress(String jsoncontent) {
        Address1 = "";
        Address2 = "";
        City = "";
        State = "";
        Country = "";
        County = "";
        PIN = "";
        Locationbean locationbean=new Locationbean();

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
                            locationbean.setCity(City);
                        } else if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                            County = long_name;
                        } else if (Type.equalsIgnoreCase("administrative_area_level_1")) {
                            State = long_name;
                            locationbean.setState(State);

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

        return locationbean;
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
