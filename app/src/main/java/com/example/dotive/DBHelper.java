package com.example.dotive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "Dotive.db";

    public DBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Habits (id INTEGER PRIMARY KEY AUTOINCREMENT, habitName TEXT, habitColor TEXT," +
                "objDays INTEGER, habitProgress TEXT, createDate TEXT)");
        db.execSQL("CREATE TABLE Settings (darkmode INTEGER, habitStrength TEXT, language TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Habits");
        db.execSQL("DROP TABLE IF EXISTS Settings");
        onCreate(db);
    }
}
