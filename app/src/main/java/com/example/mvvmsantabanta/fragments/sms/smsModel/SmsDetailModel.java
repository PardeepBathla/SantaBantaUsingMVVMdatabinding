
package com.example.mvvmsantabanta.fragments.sms.smsModel;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class SmsDetailModel extends BaseObservable { //BaseObservables is for observing change in data

    @SerializedName("author_name")
    private Object mAuthorName;
    @SerializedName("categories")
    private List<SmsCategoryModel> mCategories;
    @SerializedName("favourites")
    private List<SmsFavouriteModel> mFavourite;
    @SerializedName("content")
    private String mContent;
    @SerializedName("id")
    private Long mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("language")
    private String mLanguage;
    @SerializedName("fav_count")
    private int fav_count;
    @SerializedName("viewing_prefrence")
    private Object mViewingPrefrence;

    private int busy = 8;

    @Bindable
    public int getBusy() {
        return this.busy;
    }

    public void setBusy(int busy) {
        this.busy = busy;
//        notifyPropertyChanged(BR.busy);
    }


    public int getFav_count() {
        return fav_count;
    }

    public void setFav_count(int fav_count) {
        this.fav_count = fav_count;
    }


    public List<SmsFavouriteModel> getmFavourite() {
        return mFavourite;
    }

    public void setmFavourite(List<SmsFavouriteModel> mFavourite) {
        this.mFavourite = mFavourite;
    }

    public Object getAuthorName() {
        return mAuthorName;
    }

    public List<SmsCategoryModel> getCategories() {
        return mCategories;
    }

    public String getContent() {
        return mContent;
    }

    public Long getId() {
        return mId;
    }

    public String getImage() {
        return mImage;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public Object getViewingPrefrence() {
        return mViewingPrefrence;
    }

    public static class Builder {

        private Object mAuthorName;
        private List<SmsCategoryModel> mCategories;
        private String mContent;
        private Long mId;
        private String mImage;
        private String mLanguage;
        private Object mViewingPrefrence;

        public Builder withAuthorName(Object authorName) {
            mAuthorName = authorName;
            return this;
        }

        public Builder withCategories(List<SmsCategoryModel> categories) {
            mCategories = categories;
            return this;
        }

        public Builder withContent(String content) {
            mContent = content;
            return this;
        }

        public Builder withId(Long id) {
            mId = id;
            return this;
        }

        public Builder withImage(String image) {
            mImage = image;
            return this;
        }

        public Builder withLanguage(String language) {
            mLanguage = language;
            return this;
        }

        public Builder withViewingPrefrence(Object viewingPrefrence) {
            mViewingPrefrence = viewingPrefrence;
            return this;
        }

        public SmsDetailModel build() {
            SmsDetailModel smsDetailModel = new SmsDetailModel();
            smsDetailModel.mAuthorName = mAuthorName;
            smsDetailModel.mCategories = mCategories;
            smsDetailModel.mContent = mContent;
            smsDetailModel.mId = mId;
            smsDetailModel.mImage = mImage;
            smsDetailModel.mLanguage = mLanguage;
            smsDetailModel.mViewingPrefrence = mViewingPrefrence;
            return smsDetailModel;
        }

    }

}
