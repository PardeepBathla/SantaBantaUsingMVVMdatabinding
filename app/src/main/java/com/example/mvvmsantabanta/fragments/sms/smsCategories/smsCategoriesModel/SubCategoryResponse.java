package com.example.mvvmsantabanta.fragments.sms.smsCategories.smsCategoriesModel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubCategoryResponse implements Serializable {

    @SerializedName("id")
    private Integer id;
    @SerializedName("subcat_id")
    private Integer subcatId;
    @SerializedName("name")
    private String name;
    @SerializedName("slug")
    private String slug;



    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getSubcatId() {
        return subcatId;
    }


    public void setSubcatId(Integer subcatId) {
        this.subcatId = subcatId;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public String getSlug() {
        return slug;
    }


    public void setSlug(String slug) {
        this.slug = slug;
    }


}