package com.example.mvvmsantabanta.activity.home.homeModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class NavMenuDetails {

    @SerializedName("name")
    private String name;
    @SerializedName("info")
    private ArrayList<NavMenuInfo> navMenuInfo = null;

    boolean isExpanded;


    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<NavMenuInfo> getNavMenuInfo() {
        return navMenuInfo;
    }


    public void setNavMenuInfo(ArrayList<NavMenuInfo> navMenuInfo) {
        this.navMenuInfo = navMenuInfo;
    }


}