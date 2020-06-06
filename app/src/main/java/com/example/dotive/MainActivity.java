package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.Image;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.app.Activity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {
    public static Context context_main;
    public static int totalHabit = 0;  //총 습관 개수
    public static int activityMoveCount = 0; //액티비티 이동 횟수
    ImageButton plusimgbtn;

    ScrollView sv;
    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_main = this;


        if (activityMoveCount > 0) {
            Intent intent = getIntent();
            totalHabit = intent.getExtras().getInt("totalHabit+");
        }


        //스크롤뷰 생성
        sv = new ScrollView(this);
        sv.setBackgroundColor(Color.parseColor("#FFF7CD"));
        setContentView(sv);
        //선형레이아웃 생성
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        sv.addView(ll);


        //습관 추가 버튼
        if (totalHabit == 0) {
            plusimgbtn = new ImageButton(this);
            plusimgbtn.getBackground().setAlpha(0);          //이미지 뒷배경 투명하게
            plusimgbtn.setImageDrawable(getResources().getDrawable(R.drawable.plusbutton));
            plusimgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityMoveCount += 1;
                    Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                    startActivity(intent);
                }
            });
            ll.addView(plusimgbtn);
        }
    }

    protected void onStart() {
        super.onStart();
        if (totalHabit > 0) {
            Button[] mainboxBtn = new Button[totalHabit];
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,400,
                    getResources().getDisplayMetrics());
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                    getResources().getDisplayMetrics());
            for (int i = 0; i < totalHabit; i++) {
                mainboxBtn[i] = new Button(this);
                mainboxBtn[i].setWidth(width);
                mainboxBtn[i].setHeight(height);
                mainboxBtn[i].setText("13일");

                ll.addView(mainboxBtn[i]);

            }
        }
        if (totalHabit > 0) {
            plusimgbtn = new ImageButton(this);
            plusimgbtn.getBackground().setAlpha(0);          //이미지 뒷배경 투명하게
            plusimgbtn.setImageDrawable(getResources().getDrawable(R.drawable.plusbutton));
            plusimgbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activityMoveCount += 1;
                    Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                    startActivity(intent);
                }
            });
            ll.addView(plusimgbtn);
        }
        /*
        if (activityMoveCount > 0) {
            Button btn = new Button(this);
            ll.addView(btn);
        }
        */
    }
}




    /* 원 생성
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
    }*/




