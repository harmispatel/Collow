package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by harmis on 1/2/17.
 */

public class Newsbean extends Basebean implements Serializable {
    String news_userprofilepic=null;
    String news_username=null;
    String news_time=null;
    String news_title=null;
    String news_description=null;

    public Newsbean() {
    }

    public String getNews_userprofilepic() {
        return news_userprofilepic;
    }

    public void setNews_userprofilepic(String news_userprofilepic) {
        this.news_userprofilepic = news_userprofilepic;
    }

    public String getNews_username() {
        return news_username;
    }

    public void setNews_username(String news_username) {
        this.news_username = news_username;
    }

    public String getNews_time() {
        return news_time;
    }

    public void setNews_time(String news_time) {
        this.news_time = news_time;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }
}
