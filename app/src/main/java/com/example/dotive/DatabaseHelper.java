package com.example.dotive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    //DB 생성 후 테이블을 만듦.

    private static final String DATABASE_NAME = "Dotive_DB.db"; //DB명
    private static final int DATABASE_VERSION = 4; //1이라했는데 에러가 4라고해서 4했더니 오류 안남.

    public static final String TABLE_NAME = "Habits"; //Habits 테이블
    public static final String Habits_ID = "id"; //id (기본키, AUTO 증가)
    public static final String Habits_NAME = "habitName"; //습관 명
    public static final String Habits_COLOR = "habitColor"; //습관 색깔
    public static final String Habits_OBJDAYS = "objDays"; //목표 일수
    public static final String Habits_PROGRESS = "habitProgress"; //습관 진행도
    public static final String Habits_CREATEDAYS = "habitCreateDay"; //습관 생성 날짜

    public static final String[] ALL_COLUMNS = {Habits_ID, Habits_NAME, Habits_COLOR, Habits_OBJDAYS,Habits_PROGRESS, Habits_CREATEDAYS};

    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    Habits_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Habits_NAME + " TEXT, " +
                    Habits_COLOR + " TEXT, " +
                    Habits_OBJDAYS + " INTEGER, " +
                    Habits_PROGRESS + " TEXT, " +
                    Habits_CREATEDAYS + " TEXT " + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
