package com.app.collow.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.beans.Responcebean;
import com.app.collow.httprequests.PlaceJSONParser;
import com.app.collow.utils.CommonMethods;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import smoothprogressbar.SmoothProgressBar;

public class SearchPlaceActivity extends AppCompatActivity {
    PlacesTask placesTask;
    ParserTask parserTask;
    ListView listView;
    MenuItem searchMenuItem = null;
    private SmoothProgressBar smoothProgressBar;
    private Interpolator mCurrentInterpolator;
    private int mStrokeWidth = 4;
    private int mSeparatorLength = 1;
    private int mSectionsCount = 1;
    private float mFactor = 1f;
    private float mSpeed = 1f;
    SearchView searchView =null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);
        listView = (ListView) findViewById(R.id.listview);
        smoothProgressBar = (SmoothProgressBar) findViewById(R.id.progressbar);
        smoothProgressBar.setVisibility(View.GONE);
        setInterpolator();
        listView.setTextFilterEnabled(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

                CommunitySearchByNameActivity.shouldDisplaythisActivity=true;
                if (CommunitySearchByNameActivity.setupViewInterface_for_place != null) {
                    Responcebean responcebean = new Responcebean();
                    Toast.makeText(getApplicationContext(), ((TextView) arg1).getText(),
                            Toast.LENGTH_SHORT).show();
                    responcebean.setSearchedText(((TextView) arg1).getText().toString());
                    CommunitySearchByNameActivity.setupViewInterface_for_place.setupUI(responcebean);
                    Intent intent = new Intent(SearchPlaceActivity.this, CommunitySearchByNameActivity.class);
                    finish();
                    startActivity(intent);

                }


            }
        });
    }

    /**
     * A method to download json data from url
     */
    private void setInterpolator() {


        mCurrentInterpolator = new AccelerateDecelerateInterpolator();

        smoothProgressBar.setSmoothProgressDrawableInterpolator(mCurrentInterpolator);
        smoothProgressBar.setSmoothProgressDrawableColors(getResources().getIntArray(R.array.gplus_colors));


        smoothProgressBar.setSmoothProgressDrawableSpeed(mSpeed);
        smoothProgressBar.setSmoothProgressDrawableProgressiveStartSpeed(mSpeed);
        smoothProgressBar.setSmoothProgressDrawableProgressiveStopSpeed(mSpeed);
        smoothProgressBar.setSmoothProgressDrawableStrokeWidth(dpToPx(mStrokeWidth));
        smoothProgressBar.setSmoothProgressDrawableSeparatorLength(dpToPx(mSeparatorLength));
        smoothProgressBar.setSmoothProgressDrawableSectionsCount(mSectionsCount);


    }

    public int dpToPx(int dp) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dp, r.getDisplayMetrics());
        return px;
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception", "while downloading url=" + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_place, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.menu_search); // get my MenuItem with placeholder submenu
        searchMenuItem.expandActionView(); // Expand the search menu item in order to show by default the query

         searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                // This is where you can be notified when the `SearchView` is closed
                // and change your views you see fit.
                finish();
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                if(CommunitySearchByNameActivity.shouldDisplaythisActivity)
                {

                }
                else
                {
                    if(CommunitySearchByNameActivity.communitySearchByNameActivity!=null)
                    {
                        CommunitySearchByNameActivity.communitySearchByNameActivity.finish();
                    }
                }

                finish();


                return true;
            }
        });
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                placesTask = new PlacesTask();
                placesTask.execute(query.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                placesTask = new PlacesTask();
                placesTask.execute(newText.toString());
                smoothProgressBar.setVisibility(View.VISIBLE);
                return false;
            }
        });
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        searchView.onActionViewCollapsed();
        super.onBackPressed();
        if(CommunitySearchByNameActivity.shouldDisplaythisActivity)
        {

        }
        else
        {
            if(CommunitySearchByNameActivity.communitySearchByNameActivity!=null)
            {
                CommunitySearchByNameActivity.communitySearchByNameActivity.finish();
            }
        }


        finish();
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            smoothProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyAEtyYFOvwhr3kiMQlG6l0fkPpI1_gv-DI";

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }


            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            CommonMethods.displayLog("Result", result.toString());


            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            listView.setAdapter(adapter);


            smoothProgressBar.setVisibility(View.GONE);

        }
    }

}
