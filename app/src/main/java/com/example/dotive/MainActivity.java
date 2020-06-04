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
        for (int i = 0; i < 30; i++) {
            if (i < 10) {
                canvas.drawCircle(120 + i * 130, 400, 40, paint);
            }

            else if (10 <= i && i < 20) {
                canvas.drawCircle(120 + i * 130 - 1300, 530, 40, paint);
            }

            else if (20 <= i && i < 30) {
                canvas.drawCircle(120 + i * 130 - 2600, 660, 40, paint);
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
