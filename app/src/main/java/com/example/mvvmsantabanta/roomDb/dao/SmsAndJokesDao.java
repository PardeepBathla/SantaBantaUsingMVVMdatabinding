package com.example.mvvmsantabanta.roomDb.dao;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mvvmsantabanta.roomDb.SmsAndJoke;
import com.example.mvvmsantabanta.utility.Constants;

import java.util.List;

/**
 * Created by Pavneet_Singh on 12/31/17.
 */

@Dao
public interface SmsAndJokesDao {

    @Query("SELECT * FROM " + Constants.DATABASE.TABLE_NAME_NOTE )
    List<SmsAndJoke> getNotes();

    /*
     * Insert the object in database
     * @param note, object to be inserted
     */
    @Insert
    long insertNote(SmsAndJoke note);

    /*
     * update the object in database
     * @param note, object to be updated
     */
    @Update
    void updateNote(SmsAndJoke repos);

    /*
     * delete the object from database
     * @param note, object to be deleted
     */
    @Delete
    void deleteNote(SmsAndJoke note);

    // Note... is varargs, here note is an array
    /*
     * delete list of objects from database
     * @param note, array of oject to be deleted
     */
    @Delete
    void deleteNotes(SmsAndJoke... note);

}
