package com.app.collow.allenums;

/**
 * Created by harmis on 5/12/16.
 */

public enum CartAmountEnum {


    NORMAL(1),WITHCOUPON(2),WITHOUTCOUPON(3);
    int methodIndex = 1;

    CartAmountEnum(int methodIndex) {
        this.methodIndex = methodIndex;
    }

    public int getCartTotalFlag() {

        return this.methodIndex;
    }
}
