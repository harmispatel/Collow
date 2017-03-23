package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 1/2/17.
 */

public class Pollsbean extends Basebean implements Serializable{
    String polls_description=null;
    String polls_votes=null;
    String polls_duration=null;

    public Pollsbean() {
    }

    public String getPolls_description() {
        return polls_description;
    }

    public void setPolls_description(String polls_description) {
        this.polls_description = polls_description;
    }

    public String getPolls_votes() {
        return polls_votes;
    }

    public void setPolls_votes(String polls_votes) {
        this.polls_votes = polls_votes;
    }

    public String getPolls_duration() {
        return polls_duration;
    }

    public void setPolls_duration(String polls_duration) {
        this.polls_duration = polls_duration;
    }
}
