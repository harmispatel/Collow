package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 1/2/17.
 */

public class Eventbean extends Basebean implements Serializable {
    String event_date=null;
    String event_day=null;
    String event_daytitle=null;
    String event_time=null;
    String event_title=null;

    public Eventbean() {
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getEvent_day() {
        return event_day;
    }

    public void setEvent_day(String event_day) {
        this.event_day = event_day;
    }

    public String getEvent_daytitle() {
        return event_daytitle;
    }

    public void setEvent_daytitle(String event_daytitle) {
        this.event_daytitle = event_daytitle;
    }

    public String getEvent_time() {
        return event_time;
    }

    public void setEvent_time(String event_time) {
        this.event_time = event_time;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }
}

