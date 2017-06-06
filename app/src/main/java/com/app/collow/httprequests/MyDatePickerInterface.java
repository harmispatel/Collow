package com.app.collow.httprequests;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by chinkal on 1/23/17.
 */

public interface MyDatePickerInterface extends Serializable {
    public void selectedDate(String selectedDate, Date date);
    public void selectedDate(String selectedDate,Date date,int index);

    public void selectedTime(int hourOfDay,int minute,int forwhichFieldNeedToSet,boolean is24HOursFormat);
}
