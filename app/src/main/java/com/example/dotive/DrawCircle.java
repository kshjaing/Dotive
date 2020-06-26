package com.example.dotive;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.totalHabit;

public class DrawCircle extends View {
    private Paint paint;
    Cursor cursor;
    Integer[] objectDays = new Integer[totalHabit];;
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

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        dbHelper.getWritableDatabase();

        //DB에서 각 습관별 목표일수 뽑아옴
        cursor = db.rawQuery("SELECT objDays FROM Habits", null);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);
        canvas.drawCircle(100, 100, 100, paint);
        /*
        for (int i = 0; i < totalHabit; i++) {
            while(cursor.moveToPosition(i)) {
                objectDays[i] = Integer.parseInt(cursor.getString(0));
            }
            btn_x = findViewById(R.id.ll).findViewWithTag("box_" + i).getX();
            btn_y = findViewById(R.id.ll).findViewWithTag("box_" + i).getY();
            paint.setColor(Color.GREEN);
            canvas.drawCircle(btn_x+170, btn_y+100, 100, paint);
        }*/
    }
}
