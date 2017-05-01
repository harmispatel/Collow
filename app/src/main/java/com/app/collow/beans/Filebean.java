package com.app.collow.beans;

import java.io.File;
import java.io.Serializable;

/**
 * Created by Harmis on 08/02/17.
 */

public class Filebean implements Serializable {

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    String filePath=null;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    String mimeType=null;
    File file=null;
    boolean isPlusIconNeedToEnable=false;

    public boolean isPlusIconNeedToEnable() {
        return isPlusIconNeedToEnable;
    }

    public void setPlusIconNeedToEnable(boolean plusIconNeedToEnable) {
        isPlusIconNeedToEnable = plusIconNeedToEnable;
    }
}
