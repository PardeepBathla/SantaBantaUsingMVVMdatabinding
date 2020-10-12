package com.example.mvvmsantabanta.activity.home.homeModels;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NavMenuInfo implements Serializable {

    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;
    @SerializedName("icon")
    private String icon;
    @SerializedName("id")
    private Integer id;
    @SerializedName("children")
    private List<MenuCategoryChild> menuCategoryChildren = null;



    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }





    public List<MenuCategoryChild> getMenuCategoryChildren() {
        return menuCategoryChildren;
    }


    public void setMenuCategoryChildren(List<MenuCategoryChild> menuCategoryChildren) {
        this.menuCategoryChildren = menuCategoryChildren;
    }

}