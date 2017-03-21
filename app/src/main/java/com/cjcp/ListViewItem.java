package com.cjcp;

/**
 * Created by Pc on 2016-12-14.
 */

import android.graphics.drawable.Drawable;

public class ListViewItem {
    private Drawable iconDrawable; //, lineDrawable;
    private String titleStr ;
    private String descStr ;

    public void setIcon(Drawable icon) {
        iconDrawable = icon ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public Drawable getIcon() {
        return this.iconDrawable ;
    }

    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }
}