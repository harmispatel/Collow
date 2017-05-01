package com.app.collow.beans;

import java.io.Serializable;

/**
 * Created by Harmis on 04/02/17.
 */

public class PropertyCategoriesbean extends  Basebean implements Serializable{

    String typeCode=null;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    String categoryName=null;
}
