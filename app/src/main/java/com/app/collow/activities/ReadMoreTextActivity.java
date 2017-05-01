package com.app.collow.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.app.collow.R;
import com.app.collow.baseviews.BaseTextview;
import com.app.collow.utils.BundleCommonKeywords;

/**
 * Created by Harmis on 07/02/17.
 */

public class ReadMoreTextActivity extends AppCompatActivity {


    BaseTextview baseTextview_read_more = null;
    String text = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_more_text);

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            baseTextview_read_more = (BaseTextview) findViewById(R.id.textview_read_more);
            //==========================
            Bundle bundle = getIntent().getExtras();

            if (bundle != null) {
                text = bundle.getString(BundleCommonKeywords.KEY_READ_MORE_TEXT);
            }
            if (text != null) {
                baseTextview_read_more.setText(text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}


