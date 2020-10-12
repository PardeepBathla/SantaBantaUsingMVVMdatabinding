package com.example.mvvmsantabanta.roomDb;



import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.mvvmsantabanta.utility.Constants;


import java.io.Serializable;

/**
 * Created by Pavneet_Singh on 12/30/17.
 */

@Entity(tableName = Constants.DATABASE.TABLE_NAME_NOTE)
public class SmsAndJoke implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long favourite_id;

    @ColumnInfo(name = "sms_content")
    // column name will be "note_content" instead of "content" in table
    private String content;

    private String urlToImage;

    private String category;

//    public Note(int note_id, String content, String title, Date date) {
//        this.note_id = note_id;
//        this.content = content;
//        this.title = title;
//        this.date = date;
//    }

    public SmsAndJoke(String content, String urlToImage,String category) {
        this.content = content;
        this.urlToImage = urlToImage;
        this.category = category;
    }


    public SmsAndJoke() {
    }

    public long getFavourite_id() {
        return favourite_id;
    }

    public void setFavourite_id(long favourite_id) {
        this.favourite_id = favourite_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String title) {
        this.content = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SmsAndJoke)) return false;

        SmsAndJoke smsAndJoke = (SmsAndJoke) o;

        if (favourite_id != smsAndJoke.favourite_id) return false;
        return content != null ? content.equals(smsAndJoke.content) : smsAndJoke.content == null;
    }


    @Override
    public int hashCode() {
        int result = (int) favourite_id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "note_id=" + favourite_id +
                ", category='" + category + '\'' +
                ", content='" + content + '\'' +
                ", urlToImage=" + urlToImage +
                '}';
    }
}
