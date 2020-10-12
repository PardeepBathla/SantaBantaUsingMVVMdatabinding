
package com.example.mvvmsantabanta.fragments.jokes.jokesModel;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class JokesFeaturedCategory implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("hindi_label")
    @Expose
    private String hindiLabel;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("children")
    @Expose
    private ArrayList<JokesFeaturedSubCategory> children = new ArrayList<>();

    protected JokesFeaturedCategory(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        hindiLabel = in.readString();
        icon = in.readString();
        updatedAt = in.readString();
        slug = in.readString();
        in.readList(children, JokesFeaturedSubCategory.class.getClassLoader());
    }
    public static final Creator<JokesFeaturedCategory> CREATOR = new Creator<JokesFeaturedCategory>() {
        @Override
        public JokesFeaturedCategory createFromParcel(Parcel in) {
            return new JokesFeaturedCategory(in);
        }

        @Override
        public JokesFeaturedCategory[] newArray(int size) {
            return new JokesFeaturedCategory[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHindiLabel() {
        return hindiLabel;
    }

    public void setHindiLabel(String hindiLabel) {
        this.hindiLabel = hindiLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public ArrayList<JokesFeaturedSubCategory> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<JokesFeaturedSubCategory> children) {
        this.children = children;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(hindiLabel);
        dest.writeString(icon);
        dest.writeString(updatedAt);
        dest.writeString(slug);
        dest.writeList(children);
    }
}
