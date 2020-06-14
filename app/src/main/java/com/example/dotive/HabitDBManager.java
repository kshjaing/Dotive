package com.example.dotive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class HabitDBManager {
    static final String DB_Habit = "HabitDB";
    static final String Table_Habits = "Habits";
    static final int DB_VERSION = 1;

    Context myContext = null;

    public static HabitDBManager myDBManager = null;
    private SQLiteDatabase mydatabase = null;


    public static HabitDBManager getInstance(Context context)
    {
        if (myDBManager == null)
        {
            myDBManager = new HabitDBManager(context);
        }
        return myDBManager;
    }

    private HabitDBManager(Context context)
    {
        myContext = context;

        //DB Open
        mydatabase = context.openOrCreateDatabase(DB_Habit, context.MODE_PRIVATE, null);

        //Table 생성
        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS " + Table_Habits +
                "(" + "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "habit TEXT," +
                "habitColor TEXT," +
                "goalDays INTEGER," +
                "createDate TEXT," +
                "progress TEXT);");
    }
}
