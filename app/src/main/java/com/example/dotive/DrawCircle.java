package com.example.dotive;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.totalHabit;

public class DrawCircle extends View {
    public DrawCircle(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
    }
}
