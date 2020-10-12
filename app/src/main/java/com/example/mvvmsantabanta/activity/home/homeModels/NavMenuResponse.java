package com.example.mvvmsantabanta.activity.home.homeModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


import java.util.HashMap;
import java.util.Map;


public class NavMenuResponse {

    @SerializedName("data")
    private ArrayList<NavMenuDetails> data = null;

    public ArrayList<NavMenuDetails> getData() {
        return data;
    }

    public void setData(ArrayList<NavMenuDetails> data) {
        this.data = data;
    }
}











