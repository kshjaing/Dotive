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
    public int totalHabit = 0;  //총 습관 개수
    public int activityMoveCount = 0; //액티비티 이동 횟수
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
        Button[] mainboxBtn = new Button[totalHabit];

        //스크롤뷰 생성
        sv = new ScrollView(this);
        sv.setBackgroundColor(Color.parseColor("#FFF7CD"));
        setContentView(sv);
        //선형레이아웃 생성
        ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        sv.addView(ll);

        //총 습관 개수만큼 메인박스 생성
        if (totalHabit > 0) {
            for (int i = 0; i < totalHabit; i++) {
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(1170, 700);
                mainboxBtn[i].setLayoutParams(lp);
                mainboxBtn[i] = new Button(this);
                ll.addView(mainboxBtn[i]);
            }
        }

        //습관 추가 버튼
        plusimgbtn = new ImageButton(this);
        plusimgbtn.getBackground().setAlpha(0);          //이미지 뒷배경 투명하게
        plusimgbtn.setImageDrawable(getResources().getDrawable(R.drawable.plusbutton));
        plusimgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++activityMoveCount;
                Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });
        ll.addView(plusimgbtn);

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




