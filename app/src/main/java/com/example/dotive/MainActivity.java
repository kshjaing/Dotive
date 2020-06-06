package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout fl = new FrameLayout(this); //프레임레이아웃 객체 생성

        FrameLayout.LayoutParams frameLayoutParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                );

        ImageButton plusimgbtn = new ImageButton(this);
        plusimgbtn.getBackground().setAlpha(0); //이미지 뒷배경 투명하게
        plusimgbtn.setLayoutParams(frameLayoutParams);
        plusimgbtn.setImageDrawable(getResources().getDrawable(R.drawable.plusbutton));

        MyView w = new MyView(this);
        fl.addView(w);
        fl.addView(plusimgbtn);
        setContentView(fl);
    }
}

class MyView extends View {

    public MyView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.drawColor(Color.parseColor("#FFF7CD"));
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#578DC2"));
        paint.setAntiAlias(true);
        for (int i = 0; i < 30; i++) {
            if (i < 10) {
                canvas.drawCircle(215 + i * 110, 400, 32, paint);
            } else if (10 <= i && i < 20) {
                canvas.drawCircle(215 + i * 110 - 1100, 520, 32, paint);
            } else if (20 <= i && i < 30) {
                canvas.drawCircle(215 + i * 110 - 2200, 640, 32, paint);
            }
        }
    }


}




