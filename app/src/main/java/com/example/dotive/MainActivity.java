package com.example.dotive;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
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
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static Context context_main;
    public static int totalHabit = 0;  //총 습관 개수
    public static int activityMoveCount = 0; //액티비티 이동 횟수
    public static Boolean isCreatePressed = false;  //습관생성 버튼클릭여부
    public static Boolean isDarkmode = false;   //다크모드 여부

    Space space;
    TextView txtSettings, txtEdit;
    ScrollView sv;
    LinearLayout ll;
    FrameLayout fl;
    Button[] boxBtnArr;
    Button boxBtn;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_main = this;
        setContentView(R.layout.activity_main);

        //설정버튼 클릭이벤트 부여
        txtSettings = new TextView(this);
        txtSettings = findViewById(R.id.txtSettings);
        txtSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


        //DB 객체 가져옴
        HabitDBManager DBManager = HabitDBManager.getInstance(this);



        //스크롤뷰 생성
        sv = new ScrollView(this);
        sv = findViewById(R.id.sv);

        //다크모드
        if (!isDarkmode) {
            sv.setBackgroundColor(Color.parseColor("#FFF7CD"));
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
        }


        //선형 레이아웃 생성
        ll = new LinearLayout(this);
        ll = findViewById(R.id.ll);


        //----------------------------앱 시작 시 총 습관 개수 계산해서 버튼 생성-----------------------------
        if (totalHabit > 0) {
            boxBtnArr = new Button[totalHabit];
            Space[] spaces = new Space[totalHabit];

            //px을 dp로 변환
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                    getResources().getDisplayMetrics());           //박스버튼 높이
            int spaceHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,
                    getResources().getDisplayMetrics());           //여백 높이

            //습관 수만큼 박스 생성
            for (int i = 0; i < totalHabit; i++) {
                boxBtnArr[i] = new Button(this);
                boxBtnArr[i].setHeight(height);

                //다크모드에 따른 버튼 색변경(임시)
                if (!isDarkmode) {
                    boxBtnArr[i].setBackgroundColor(Color.WHITE);
                }
                else {
                    boxBtnArr[i].setBackgroundColor(Color.parseColor("#2C323E"));
                }
                //태그설정
                boxBtnArr[i].setTag("box_" + i);

                spaces[i] = new Space(this);
                spaces[i].setMinimumHeight(spaceHeight);
                ll.addView(spaces[i]);
                ll.addView(boxBtnArr[i]);


                //클릭시 버튼 태그 토스트(테스트용)
                boxBtnArr[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    protected void onStart() {
        super.onStart();

        if (isCreatePressed) {
            boxBtn = new Button(this);
            space = new Space(this);

            //클릭시 버튼 태그 토스트(테스트용)
            boxBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
                }
            });

            //px을 dp로 변환
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,
                    getResources().getDisplayMetrics());           //박스버튼 너비
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                    getResources().getDisplayMetrics());           //박스버튼 높이
            int spaceHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,
                    getResources().getDisplayMetrics());           //여백 높이


            //박스 생성
            boxBtn.setWidth(width);
            boxBtn.setHeight(height);
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                    0, 0);
            linearParams.setMargins(0, height, 0, 0);
            boxBtn.setLayoutParams(linearParams);


            //태그설정
            boxBtn.setTag("box_" + (totalHabit + 1));
            space.setMinimumHeight(spaceHeight);
            //ll.addView(space);
            ll.addView(boxBtn);

            isCreatePressed = false;
        }

        //CreateActivity에서 총 습관수 계산값을 가져옴
        /*
        if (activityMoveCount > 0) {
            Intent intent = getIntent();
            totalHabit = intent.getExtras().getInt("totalHabit+");
        }*/


        //메인액티비티로 돌아왔을 때 다크모드 체크
        if (!isDarkmode) {
            sv.setBackgroundColor(Color.parseColor("#FFF7CD"));
            for (int i = 0; i < totalHabit; i++) {
                ll.findViewWithTag("box_" + i).setBackgroundColor(Color.WHITE);
            }
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            for (int i = 0; i < totalHabit; i++) {
                ll.findViewWithTag("box_" + i).setBackgroundColor(Color.parseColor("#2C323E"));
            }
        }




    }

    public void ibtnPlus_onClick(View view) {
        Intent intent = new Intent(MainActivity.this, CreateActivity.class);
        startActivity(intent);
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




