package com.app.collow.commondialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.app.collow.allenums.ScreensEnums;
import com.app.collow.httprequests.MyDatePickerInterface;
import com.app.collow.utils.BundleCommonKeywords;
import com.app.collow.utils.CommonKeywords;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import  java.lang.String;
import java.lang.System;

/**
 * Created by chinkal on 1/23/17.
 */

public class MyDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    MyDatePickerInterface myDatePickerInterface=null;
    int index=0;
    public MyDatePicker() {


    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, yy, mm, dd);

        Bundle bundle=getArguments();
        if(bundle!=null)
        {
             index=bundle.getInt(BundleCommonKeywords.KEY_SCREEN_FROM_WHERE);
            if(index== ScreensEnums.ACNEWEVENT.getScrenIndex())
            {

            }
            else  if(index== ScreensEnums.START_DATE.getScrenIndex())
            {

            }
            else  if(index== ScreensEnums.END_DATE.getScrenIndex())
            {

            }
            else
            {
                dialog.getDatePicker().setMaxDate(new Date().getTime());

            }
        }
        else
        {
            dialog.getDatePicker().setMaxDate(new Date().getTime());

        }

        return dialog;
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm + 1, dd);


    }

    public void populateSetDate(int year, int month, int day) {


        try {
            dismiss();

            Date date;

            String dateString = year+ "-" +month+ "-" +day;
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
            String dates  = null;
            if(index== ScreensEnums.ACNEWEVENT.getScrenIndex())
            {
                dates  = new SimpleDateFormat(CommonKeywords.APP_NEED_DATE_FORMAT).format(date);

            }

            else
            {
                 dates  = new SimpleDateFormat(CommonKeywords.APP_NEED_DATE_FORMAT).format(date);


            }


            if(index== ScreensEnums.ACNEWEVENT.getScrenIndex())
            {
                myDatePickerInterface.selectedDate(dates,date);

            }
            else  if(index== ScreensEnums.START_DATE.getScrenIndex())
            {
                myDatePickerInterface.selectedDate(dates,date,index);

            }
            else  if(index== ScreensEnums.END_DATE.getScrenIndex())
            {
                myDatePickerInterface.selectedDate(dates,date,index);

            }
            else
            {
                myDatePickerInterface.selectedDate(dates,date);

            }

        } catch (ParseException e) {


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
}

