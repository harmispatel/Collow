package com.app.collow.beans;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Harmis on 01/02/17.
 */

public class Menubean extends ExpandableGroup<SubMenubean> {

    String menuName=null;
    private int iconResId;

    public Menubean(List<SubMenubean> items,String title, int iconResId) {
        super(title,items);
        this.iconResId = iconResId;

    }



    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }


    public void setMenuCirclebg(int menuCirclebg) {
        this.menuCirclebg = menuCirclebg;
    }

    int menuCirclebg=0;
    public int getIconResId() {
        return iconResId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menubean)) return false;

        Menubean genre = (Menubean) o;

        return getIconResId() == genre.getIconResId();

    }

    @Override
    public int hashCode() {
        return getIconResId();
    }



}
