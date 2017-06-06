package com.app.collow.activities;

import android.os.Bundle;
import android.view.View;

import com.app.collow.R;

/**
 * Created by harmis on 8/1/17.
 */

public class HomeActivity extends BaseActivity {

    View view_home = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewByIDs();
        setupEvents();

    }


    private void findViewByIDs() {
        view_home = getLayoutInflater().inflate(R.layout.home_screen, null);
        frameLayout_container.addView(view_home);

    }

    private void setupEvents() {

    }

}