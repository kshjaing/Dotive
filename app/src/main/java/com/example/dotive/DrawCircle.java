package com.example.dotive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.sql.Struct;
import java.util.Arrays;

import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.isDarkmode;
import static com.example.dotive.MainActivity.totalHabit;

public class DrawCircle extends View {
    private Paint paint;
    Cursor cursor;
    String[] objectDays = new String[totalHabit];
    float btn_x, btn_y;

    public DrawCircle(Context context) {
        super(context);
        init(context);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dbHelper = new DBHelper(context_main, 4);
        db = dbHelper.getWritableDatabase();


        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);

        for (int i = 0; i < totalHabit; i++) {

            //DB에서 각 습관별 목표일수 뽑아옴
            cursor = db.rawQuery("SELECT objDays FROM Habits", null);
            cursor.moveToPosition(i);
            objectDays[i] = cursor.getString(0);

            btn_x = MainActivity.boxBtnArr[i].getX();
            btn_y = MainActivity.boxBtnArr[i].getY();
            if (isDarkmode == 0) {
                paint.setColor(Color.parseColor("#d9d9d9"));
            }
            else {
                paint.setColor(Color.parseColor("#7b879b"));
            }
            canvas.drawCircle(btn_x + 170, btn_y + 100, 100, paint);
        }
    }
}
