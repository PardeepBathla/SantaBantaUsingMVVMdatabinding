
package com.example.mvvmsantabanta.fragments.memes.memesModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Breadcrumb {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("link")
    @Expose
    private String link;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
