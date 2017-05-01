package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 07/02/17.
 */

public class CommunityTypebean extends  Basebean implements Serializable {


    String typeID=null;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID)     {
        this.typeID = typeID;
    }

    String typeName=null;
}
