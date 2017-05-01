package com.app.collow.allenums;

/**
 * Created by Harmis on 07/02/17.
 */

public enum LikeDisLike {
    LIKE(1), DISLIKE(0) ;
    int likeDislikeValue = 1;

    LikeDisLike(int methodIndex) {
        this.likeDislikeValue = methodIndex;
    }

    public int getLikeDislikeValue() {

        return this.likeDislikeValue;
    }
}
