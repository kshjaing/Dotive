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

import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.isDarkmode;
import static com.example.dotive.MainActivity.totalHabit;

public class DrawCircle extends View {
    Paint paint;
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
            objectDays[i] = Integer.parseInt(cursor.getString(0));

            btn_x = MainActivity.boxBtnArr[i].getX();
            btn_y = MainActivity.boxBtnArr[i].getY();
            if (isDarkmode == 0) {
                paint.setColor(Color.parseColor("#d9d9d9"));
            }
            else {
                paint.setColor(Color.parseColor("#7b879b"));
            }

            //-------------------------습관 개수에 따른 원 크기와 위치 로직---------------------------

            //습관개수 1~3개
            if (1 <= objectDays[i] && objectDays[i] <= 3) {
                //버튼 중간값
                if (objectDays[i] == 1) {
                    canvas.drawCircle(btn_x + btn_Width / 2, btn_y + btn_Height / 2, radius, paint);
                }

                //버튼 중간값에서 반지름의 1.5배만큼 빼고 더함
                if (objectDays[i] == 2) {
                    canvas.drawCircle(btn_x + (btn_Width / 2) - radius * 1.5f, btn_y + btn_Height / 2, radius, paint);
                    canvas.drawCircle(btn_x + (btn_Width / 2) + radius * 1.5f, btn_y + btn_Height / 2, radius, paint);
                }

                if (objectDays[i] == 3) {
                    canvas.drawCircle(btn_x + (btn_Width / 2) - radius * 2.5f, btn_y + btn_Height / 2, radius, paint);
                    canvas.drawCircle(btn_x + btn_Width / 2, btn_y + btn_Height / 2, radius, paint);
                    canvas.drawCircle(btn_x + (btn_Width / 2) + radius * 2.5f, btn_y + btn_Height / 2, radius, paint);
                }
            }

            if (4 <= objectDays[i] && objectDays[i] <= 14) {
                canvas.drawCircle(btn_x + btn_Width / 2, btn_y + btn_Height / 2, radius / 2, paint);
            }

            if (15 <= objectDays[i]) {
                canvas.drawCircle(btn_x + btn_Width / 2, btn_y + btn_Height / 2, radius * 0.3f, paint);
            }
        }
    }
}
