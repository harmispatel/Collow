package com.app.collow.commondialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.app.collow.allenums.ModificationOptions;
import com.app.collow.allenums.ScreensEnums;
import com.app.collow.httprequests.MyDatePickerInterface;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Harmis on 22/02/17.
 */

public class MyTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    MyDatePickerInterface myDatePickerInterface=null;
    int index=0;
    boolean is24HoursFormat=false;

    public MyTimePicker() {



    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hourofday = calendar.get(Calendar.HOUR_OF_DAY);
        int mint = calendar.get(Calendar.MINUTE);
        Bundle bundle=getArguments();
        if(bundle!=null)
        {
             index=bundle.getInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE);
            is24HoursFormat=bundle.getBoolean(BundleCommonKeywords.KEY_NEED_24_HOURS_TIME_FORMAT);
        }
        else
        {

        }

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this, hourofday, mint,is24HoursFormat);

        return timePickerDialog;
    }



    public void populateSetDate(int hourOfDay, int minute) {


        try {
            dismiss();




            myDatePickerInterface.selectedTime(hourOfDay,minute,index,is24HoursFormat);

        } catch (Exception e) {


        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {

            myDatePickerInterface = (MyDatePickerInterface ) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MyInterface ");
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        populateSetDate(hourOfDay,minute);
    }
}

