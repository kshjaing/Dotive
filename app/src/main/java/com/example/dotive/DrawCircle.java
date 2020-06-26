package com.example.dotive;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.totalHabit;

public class DrawCircle extends View {
    private Paint paint;

    public DrawCircle(Context context) {
        super(context);
        init(context);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void init(Context context) {
        paint = new Paint();
    }
}
