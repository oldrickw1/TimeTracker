package com.example.timetracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class TimeSpendDAO extends SQLiteOpenHelper {



    public TimeSpendDAO(Context context) {
        super(context, "timeSpend.db", null, 1);

    }


    final private ArrayList<ActivityEntry> dummyEntries = new ArrayList<>(
            Arrays.asList(
                    new ActivityEntry(4, 0,"Programming"),
                    new ActivityEntry(5, 1, "Programming"),
                    new ActivityEntry(3, 2, "Programming"),
                    new ActivityEntry(5, 3, "Programming"),
                    new ActivityEntry(7, 4, "Programming")
            )
    );

    final private ArrayList<ActivityEntry> dummyEntries1 = new ArrayList<>(
            Arrays.asList(
                    new ActivityEntry(6, 0,"Programming"),
                    new ActivityEntry(2, 1, "Programming"),
                    new ActivityEntry(4, 2, "Programming"),
                    new ActivityEntry(9, 3, "Programming"),
                    new ActivityEntry(7, 4, "Programming"),
                    new ActivityEntry(7, 5, "Programming"),
                    new ActivityEntry(7, 6, "Programming")
            )
    );

    final private ArrayList<ActivityEntry> dummyEntries2 = new ArrayList<>(
            Arrays.asList(
                    new ActivityEntry(3, 0,"Programming"),
                    new ActivityEntry(4, 1, "Programming"),
                    new ActivityEntry(5, 2, "Programming"),
                    new ActivityEntry(2, 3, "Programming"),
                    new ActivityEntry(4, 4, "Programming"),
                    new ActivityEntry(5, 5, "Programming"),
                    new ActivityEntry(2, 6, "Programming")
            )
    );




    public ArrayList<ActivityEntry> getDummyEntries() {
        return dummyEntries;
    }

    public ArrayList<ActivityEntry> getPrevWeek() {
        return dummyEntries1;
    }

    public ArrayList<ActivityEntry> getNextWeek() {
        return dummyEntries2;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE timeSpend (id INTEGER PRIMARY KEY AUTOINCREMENT , timeStart INTEGER, timeEnd INTEGER)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addOne(Date start) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("INSERT INTO timeSpend (timeStart, timeEnd) VALUES ('Wed Jun 14 18:29:58 GMT+02:00 2023', strftime('%Y-%m-%d %H:%M:%S', 'now'));");


    }
}
