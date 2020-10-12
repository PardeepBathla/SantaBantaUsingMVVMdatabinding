
package com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel;



import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class SmsCategoriesResponseModel implements Serializable {


    @SerializedName("name")
    private String name;
    @SerializedName("icon")
    private String icon;
    @SerializedName("id")
    private Integer id;
    @SerializedName("children")
    private List<SubCategoryResponse> children = null;


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


    public List<SubCategoryResponse> getChildren() {
        return children;
    }

    public void setChildren(List<SubCategoryResponse> children) {
        this.children = children;
    }


}




