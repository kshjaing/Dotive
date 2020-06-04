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
        canvas.drawColor(Color.parseColor("#FFF7CD"));
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#578DC2"));
        paint.setAntiAlias(true);
        for (int i = 0; i < 30; i++) {
            if (i < 10) {
                canvas.drawCircle(200 + i * 110, 400, 35, paint);
            }

            else if (10 <= i && i < 20) {
                canvas.drawCircle(200 + i * 110 - 1100, 540, 35, paint);
            }

            else if (20 <= i && i < 30) {
                canvas.drawCircle(200 + i * 110 - 2200, 680, 35, paint);
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
