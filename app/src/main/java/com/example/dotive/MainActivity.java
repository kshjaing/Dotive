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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Toast;

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
        //DB 객체 가져옴
        HabitDBManager DBManager = HabitDBManager.getInstance(this);

        //액티비티 이동이 1번 이상일때 CreateActivity에서 총 습관수 계산값을 가져옴
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
        final LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT);

        sv.addView(ll);

        //습관이 하나도 없을 때 추가버튼을 제일 위로 생성
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
            Button[] boxBtn = new Button[totalHabit];
            Space[] spaces = new Space[totalHabit];

            //px을 dp로 변환
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,
                    getResources().getDisplayMetrics());           //박스버튼 너비
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                    getResources().getDisplayMetrics());           //박스버튼 높이
            int spaceHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,
                    getResources().getDisplayMetrics());           //여백 높이

            //습관 수만큼 박스 생성
            for (int i = 0; i < totalHabit; i++) {
                boxBtn[i] = new Button(this);
                boxBtn[i].setWidth(width);
                boxBtn[i].setHeight(height);
                boxBtn[i].setText("13일");
                //태그설정
                boxBtn[i].setTag("box_" + i);

                spaces[i] = new Space(this);
                spaces[i].setMinimumHeight(spaceHeight);
                ll.addView(spaces[i]);
                ll.addView(boxBtn[i]);

                //클릭시 버튼 태그 토스트(테스트용)
                boxBtn[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        //습관이 1개 이상 있을 때 습관추가버튼을 박스 밑으로 생성
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




