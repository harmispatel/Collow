package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 04/02/17.
 */

public class PropertyTypesbean extends  Basebean implements Serializable {

    String typesCode=null;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypesCode() {
        return typesCode;
    }

    public void setTypesCode(String typesCode) {
        this.typesCode = typesCode;
    }

    String typeName=null;

}
