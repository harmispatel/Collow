package com.app.collow.allenums;

/**
 * Created by harmis on 28/10/16.
 */
public enum HTTPRequestMethodEnums {

    POST(1), GET(2) ,MIME(3);
    int methodIndex = 1;

    HTTPRequestMethodEnums(int methodIndex) {
        this.methodIndex = methodIndex;
    }

   public int getHTTPRequestMethodInex() {

        return this.methodIndex;
    }
}
