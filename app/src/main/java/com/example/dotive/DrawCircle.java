package com.example.dotive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.sql.Struct;
import java.util.Arrays;

import static com.example.dotive.MainActivity.calDate;
import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.isDarkmode;
import static com.example.dotive.MainActivity.totalHabit;
import static com.example.dotive.MainActivity.dateDiff;

public class DrawCircle extends View {
    Paint paint, strokePaint;
    Cursor cursor;
    Integer[] objectDays = new Integer[totalHabit];
    float btn_x, btn_y;


    int btn_Width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,360,
            getResources().getDisplayMetrics());
    int btn_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,220,
            getResources().getDisplayMetrics());
    float radius = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,36,
            getResources().getDisplayMetrics());

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
        strokePaint = new Paint();
        dbHelper = new DBHelper(context_main, 4);
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        db = dbHelper.getWritableDatabase();

        for (int i = 0; i < totalHabit; i++) {

            //paint[i].setAntiAlias(true);
            //DB에서 각 습관별 목표일수 뽑아옴
            cursor = db.rawQuery("SELECT objDays FROM Habits", null);
            cursor.moveToPosition(i);
            objectDays[i] = Integer.parseInt(cursor.getString(0));
            btn_x = MainActivity.boxBtnArr[i].getX();
            btn_y = MainActivity.boxBtnArr[i].getY();
            if (isDarkmode == 0) {
                paint.setColor(Color.parseColor("#d9d9d9"));
            }
            else {
                paint.setColor(Color.parseColor("#7b879b"));
            }

            //-------------------------목표일수에 따른 원 크기와 위치 로직---------------------------

            //목표일수 1~7일
            switch (objectDays[i]) {
                case 1:
                    canvas.drawCircle(btn_x + btn_Width / 2, btn_y + btn_Height / 2, radius, paint);
                    break;
                case 2:
                    canvas.drawCircle(btn_x + (btn_Width / 2) - radius * 1.5f, btn_y + btn_Height / 2, radius, paint);
                    canvas.drawCircle(btn_x + (btn_Width / 2) + radius * 1.5f, btn_y + btn_Height / 2, radius, paint);
                    break;
                case 3:
                    canvas.drawCircle(btn_x + (btn_Width / 2) - radius * 2.5f, btn_y + btn_Height / 2, radius, paint);
                    canvas.drawCircle(btn_x + btn_Width / 2, btn_y + btn_Height / 2, radius, paint);
                    canvas.drawCircle(btn_x + (btn_Width / 2) + radius * 2.5f, btn_y + btn_Height / 2, radius, paint);
                    break;
                case 4:
                    for (int j = 0; j < 4; j++) {
                        //현재날짜 테두리 테스트
                        if (j == (Integer.parseInt(dateDiff[i]) - 1)){
                            strokePaint.setStrokeWidth(30);
                            strokePaint.setStyle(Paint.Style.STROKE);
                            strokePaint.setColor(Color.parseColor("#ffb313"));
                            strokePaint.setAntiAlias(true);
                            canvas.drawCircle(btn_x + radius * (j + 1) * 2, btn_y + btn_Height / 2, radius * 0.75f, strokePaint);
                        }
                        canvas.drawCircle(btn_x + radius * (j + 1) * 2, btn_y + btn_Height / 2, radius * 0.75f, paint);
                    }
                    break;
                case 5:
                    for (int j = 0; j < 5; j++) {
                        canvas.drawCircle(btn_x + radius * (j + 1) * 1.68f, btn_y + btn_Height / 2, radius * 0.65f, paint);
                    }
                    break;
                case 6:
                    for (int j = 0; j < 6; j++) {
                        canvas.drawCircle(btn_x + radius * (j + 1) * 1.43f, btn_y + btn_Height / 2, radius * 0.55f, paint);

                    }
                    break;
                case 7:
                    for (int j = 0; j < 7; j++) {
                        canvas.drawCircle(btn_x + radius * (j + 1) * 1.26f, btn_y + btn_Height / 2, radius * 0.45f, paint);

                    }
                    break;
            }

            //목표일수 8~14일
            if (8 <= objectDays[i] && objectDays[i] <= 14) {
                for (int j = 0; j < objectDays[i]; j++) {
                    if (j < 7){
                        canvas.drawCircle(btn_x + radius * (j + 1) * 1.25f, btn_y + btn_Height / 2 - radius, radius * 0.45f, paint);
                    }
                    else {
                        canvas.drawCircle(btn_x + radius * (j - 6) * 1.25f, btn_y + btn_Height / 2 + radius * 0.7f, radius * 0.45f, paint);
                    }
                }
            }

            //목표일수 15~20일
            else if (15 <= objectDays[i] && objectDays[i] <= 20) {
                for (int j = 0; j < objectDays[i]; j++) {
                    if (j < 10){
                        canvas.drawCircle(btn_x + radius * (j + 1) * 0.905f, btn_y + btn_Height / 2 - radius, radius * 0.3f, paint);
                    }
                    else {
                        canvas.drawCircle(btn_x + radius * (j - 9) * 0.905f, btn_y + btn_Height / 2 + radius * 0.35f, radius * 0.3f, paint);
                    }
                }
            }

            //목표일수 21~30일
            else if (21 <= objectDays[i] && objectDays[i] <= 30) {
                for (int j = 0; j < objectDays[i]; j++) {
                    if (j < 10) {
                        canvas.drawCircle(btn_x + radius * (j + 1) * 0.905f, btn_y + radius * 1.7f, radius * 0.3f, paint);
                    }
                    else if (10 <= j && j < 20) {
                        canvas.drawCircle(btn_x + radius * (j - 9) * 0.905f, btn_y + radius * 2.7f, radius * 0.3f, paint);
                    }
                    else if (20 <= j) {
                        canvas.drawCircle(btn_x + radius * (j - 19) * 0.905f, btn_y + radius * 3.7f, radius * 0.3f, paint);
                    }
                }
            }
        }
    }
}
