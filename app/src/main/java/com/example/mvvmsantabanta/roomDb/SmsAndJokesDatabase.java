package com.example.mvvmsantabanta.roomDb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mvvmsantabanta.roomDb.dao.SmsAndJokesDao;
import com.example.mvvmsantabanta.utility.Constants;

@Database(entities = { SmsAndJoke.class }, version = 1)
//@TypeConverters({DateRoomConverter.class})
public abstract class SmsAndJokesDatabase extends RoomDatabase {

    public abstract SmsAndJokesDao getSmsAndJokesDao(); // method name should be same as Dao class name with get prefix


    private static SmsAndJokesDatabase noteDB;

    // synchronized is use to avoid concurrent access in multithred environment
    public static /*synchronized*/ SmsAndJokesDatabase getInstance(Context context) {
        if (null == noteDB) {
            noteDB = buildDatabaseInstance(context);
        }
        return noteDB;
    }

    private static SmsAndJokesDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context,
                SmsAndJokesDatabase.class,
                Constants.DATABASE.DB_NAME).allowMainThreadQueries().build();
    }

    public  void cleanUp(){
        noteDB = null;
    }
}