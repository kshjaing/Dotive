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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout rl = new RelativeLayout(this); //렐러티브레이아웃 객체 생성
        rl.setBackgroundColor(Color.parseColor("#FFF7CD"));
        RelativeLayout.LayoutParams relativeLayoutParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                );

        ImageButton plusimgbtn = new ImageButton(this);
        plusimgbtn.getBackground().setAlpha(0); //이미지 뒷배경 투명하게
        plusimgbtn.setLayoutParams(relativeLayoutParams);
        plusimgbtn.setImageDrawable(getResources().getDrawable(R.drawable.plusbutton));
        plusimgbtn.setLayoutParams(relativeLayoutParams);
        relativeLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        MyView w = new MyView(this);

        rl.addView(w);
        rl.addView(plusimgbtn);
        setContentView(rl);
    }
}

class MyView extends View {

    public MyView(Context context) {
        super(context);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();

        setBackgroundColor(Color.BLACK);
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




