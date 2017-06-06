package com.app.collow.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.beans.GettingStartedbean;
import com.app.collow.utils.CommonSession;
import com.eftimoff.viewpagertransformers.CubeInTransformer;
import com.eftimoff.viewpagertransformers.CubeOutTransformer;
import com.eftimoff.viewpagertransformers.RotateUpTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GettingStartedActivity extends AppCompatActivity {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    ViewPager viewPager_slider = null;
    BaseTextview baseTextview_getting_started = null;
    List<GettingStartedbean> gettingStartedbeanList = new ArrayList<>();
    GettingStartedAdapter gettingStartedAdapter = null;
    private LinearLayout pager_indicator;
    private ImageView[] dots;
    private int dotsCount;
    CommonSession commonSession=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started);

        commonSession=new CommonSession(GettingStartedActivity.this);


        GettingStartedbean gettingStartedbean1 = new GettingStartedbean();
        gettingStartedbean1.setWelcomeTitle("Welcome");
        gettingStartedbean1.setDetails("Welcome to new world");
        gettingStartedbean1.setImageResourceID(R.drawable.social_image);


        GettingStartedbean gettingStartedbean2 = new GettingStartedbean();
        gettingStartedbean2.setWelcomeTitle("Welcome");
        gettingStartedbean2.setDetails("Welcome to new world");
        gettingStartedbean2.setImageResourceID(R.drawable.social_image);
        GettingStartedbean gettingStartedbean3 = new GettingStartedbean();
        gettingStartedbean3.setWelcomeTitle("Welcome");
        gettingStartedbean3.setDetails("Welcome to new world");
        gettingStartedbean3.setImageResourceID(R.drawable.social_image);

        GettingStartedbean gettingStartedbean4 = new GettingStartedbean();
        gettingStartedbean4.setWelcomeTitle("Welcome");
        gettingStartedbean4.setDetails("Welcome to new world");
        gettingStartedbean4.setImageResourceID(R.drawable.social_image);


        gettingStartedbeanList.add(gettingStartedbean1);
        gettingStartedbeanList.add(gettingStartedbean2);
        gettingStartedbeanList.add(gettingStartedbean3);
        gettingStartedbeanList.add(gettingStartedbean4);


        gettingStartedAdapter = new GettingStartedAdapter(GettingStartedActivity.this, gettingStartedbeanList);


        viewPager_slider = (ViewPager) findViewById(R.id.pager_feature_product);
        baseTextview_getting_started = (BaseTextview) findViewById(R.id.button_slider_getstarted);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);

        viewPager_slider.setOffscreenPageLimit(4);


        viewPager_slider.setClipToPadding(false);
        viewPager_slider.setPageMargin(24);
        viewPager_slider.setPadding(48, 8, 48, 8);
        viewPager_slider.setAdapter(gettingStartedAdapter);

        baseTextview_getting_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                finish();

                if(commonSession.getLoggedUserID()==null)
                {
                    Intent intent = new Intent(GettingStartedActivity.this, SignInActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if(commonSession.getUserProfileCompleted())
                    {
                        Intent intent = new Intent(GettingStartedActivity.this, EditProfileActivity.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(GettingStartedActivity.this, FollowingActivity.class);
                        startActivity(intent);
                    }
                }



            }
        });





        setUiPageViewController();

        viewPager_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dotsCount; i++) {
                    dots[i].setImageDrawable(getResources().getDrawable(R.drawable.dottedunselected));
                }

                dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selected));

                if(position==0)
                {

                }
                else if(position==3)
                {

                }
                else
                {

                }


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        if (Build.VERSION.SDK_INT >= 23) {
            // Marshmallow+
            if (checkAndRequestPermissions()) {
            }


        } else {


        }
    }

    private void setUiPageViewController() {

        dotsCount = gettingStartedAdapter.getCount();
        dots = new ImageView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.dottedunselected));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(7, 0, 7, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selected));
    }

    private boolean checkAndRequestPermissions() {

        // int access_coarse_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        // int access_fine_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int write_external_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        int read_external_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int camera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);


        //  int read_phone_state = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        // int call_phone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        //  int read_contacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);


        //  int get_accounts = ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS);

        List<String> listPermissionsNeeded = new ArrayList<>();


       /* if (access_coarse_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (access_fine_location != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }*/
        /*if (read_phone_state != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (call_phone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (read_contacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }*/
        if (write_external_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (read_external_storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        /*if (get_accounts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.GET_ACCOUNTS);
        }*/


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
                //   perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                //   perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                //  perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                //   perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                // perms.put(Manifest.permission.READ_CONTACTS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                //  perms.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (
                            perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                    && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                                    && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            ) {
                        // process the normal flow
                        //else any one or both the permissions are not granted


                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)


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

    public class GettingStartedAdapter extends PagerAdapter {

        Activity activity = null;
        private List<GettingStartedbean> list_gettingStartedbeen;

        public GettingStartedAdapter(Activity mactivity, List<GettingStartedbean> mlist_gettingStartedbeen) {
            this.list_gettingStartedbeen = mlist_gettingStartedbeen;
            activity = mactivity;
        }

        @Override
        public float getPageWidth(int position) {
            if(position==0)
            {
                return  1.0f;
            }
            else
            {
                return 1.0f;

            }
        }

        @Override
        public int getCount() {
            return list_gettingStartedbeen.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = LayoutInflater.from(activity).inflate(R.layout.activity_gettings_started_dynamical_page, container, false);

            BaseTextview baseTextview_welcome = null, baseTextview_details = null;
            ImageView imageView_geting_started = null;
            LinearLayout linearLayout_welcometext=null;
            linearLayout_welcometext= (LinearLayout) view.findViewById(R.id.layout_for_welcome);
            linearLayout_welcometext.setVisibility(View.VISIBLE);
            baseTextview_welcome = (BaseTextview) view
                    .findViewById(R.id.textview_slider_welcome1);

            baseTextview_details = (BaseTextview) view
                    .findViewById(R.id.textview_slider_welcome2);

            imageView_geting_started = (ImageView) view
                    .findViewById(R.id.imageview_slider_image);

            GettingStartedbean gettingStartedbean = list_gettingStartedbeen.get(position);


            baseTextview_welcome.setText(gettingStartedbean.getWelcomeTitle());
            baseTextview_details.setText(gettingStartedbean.getDetails());


            imageView_geting_started.setImageResource(gettingStartedbean.getImageResourceID());
            container.addView(view);
            imageView_geting_started.setTag(String.valueOf(position));
            imageView_geting_started.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = Integer.parseInt(v.getTag().toString());

                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
