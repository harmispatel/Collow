package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 09/02/17.
 */

public class SocialOptionbean implements Serializable {


    int like=0;
    int comments=0;

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    int views=0;

}
