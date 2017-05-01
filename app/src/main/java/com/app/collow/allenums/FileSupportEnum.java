package com.app.collow.allenums;

/**
 * Created by Harmis on 09/02/17.
 */

public enum FileSupportEnum {

    PDF("pdf"), ZIP("zip") ,DOCX("docx"),TXT("txt"),XLS("xls"),APK("apk"),VIDEO_AVI("video/avi"),VIDEO_MSVIDEO("video/msvideo"),VIDEO_MP4("video/mp4");
    String mimeType =null;

    FileSupportEnum(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getMimeType() {

        return this.mimeType;
    }

}
