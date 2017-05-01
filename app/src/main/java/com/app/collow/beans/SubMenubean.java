package com.app.collow.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Harmis on 15/02/17.
 */

public class SubMenubean implements Parcelable {

    private String menuName;
    private boolean isFavorite;

    public int getIconResId() {
        return iconResId;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    private int iconResId;

    public SubMenubean(String name, boolean isFavorite,int iconResId) {
        this.menuName = name;
        this.isFavorite = isFavorite;
        this.iconResId = iconResId;

    }

    protected SubMenubean(Parcel in) {
        menuName = in.readString();
    }

    public String getMenuName() {
        return menuName;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubMenubean)) return false;

        SubMenubean artist = (SubMenubean) o;

        if (isFavorite() != artist.isFavorite()) return false;
        return getMenuName() != null ? getMenuName().equals(artist.getMenuName()) : artist.getMenuName() == null;

    }

    @Override
    public int hashCode() {
        int result = getMenuName() != null ? getMenuName().hashCode() : 0;
        result = 31 * result + (isFavorite() ? 1 : 0);
        return result;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(menuName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SubMenubean> CREATOR = new Creator<SubMenubean>() {
        @Override
        public SubMenubean createFromParcel(Parcel in) {
            return new SubMenubean(in);
        }

        @Override
        public SubMenubean[] newArray(int size) {
            return new SubMenubean[size];
        }
    };
}
