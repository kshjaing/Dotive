package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.app.Activity;

class MyView extends View {
    public MyView(Context context) {
        super(context);
        setBackgroundColor(Color.WHITE);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(R.color.YellowBackground);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        for (int i = 0; i < 28; i++) {
            if (i < 7) {
                canvas.drawCircle(100 + i * 120, 400, 50, paint);
            }

            else if (7 <= i && i < 14) {
                canvas.drawCircle(100 + i * 120 - 840, 520, 50, paint);
            }

            else if (14 <= i && i < 21) {
                canvas.drawCircle(100 + i * 120 - 1680, 640, 50, paint);
            }

            else if (21 <= i && i < 28) {
                canvas.drawCircle(100 + i * 120 - 2520, 760, 50, paint);
            }
        }

    }
}



public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyView w = new MyView(this);
        setContentView(w);
    }
}
