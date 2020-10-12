
package com.example.mvvmsantabanta.fragments.jokes.jokesModel;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JokesPivotModel {

    @SerializedName("category_id")
    private Long mCategoryId;
    @SerializedName("joke_id")
    private Long mJokeId;

    public Long getCategoryId() {
        return mCategoryId;
    }

    public Long getJokeId() {
        return mJokeId;
    }

    public static class Builder {

        private Long mCategoryId;
        private Long mJokeId;

        public Builder withCategoryId(Long categoryId) {
            mCategoryId = categoryId;
            return this;
        }

        public Builder withJokeId(Long jokeId) {
            mJokeId = jokeId;
            return this;
        }

        public JokesPivotModel build() {
            JokesPivotModel jokesPivotModel = new JokesPivotModel();
            jokesPivotModel.mCategoryId = mCategoryId;
            jokesPivotModel.mJokeId = mJokeId;
            return jokesPivotModel;
        }

    }

}
