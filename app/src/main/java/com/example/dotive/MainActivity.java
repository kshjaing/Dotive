package com.example.dotive;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

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
    public static Boolean darkmode = false;   //다크모드 인수
    public static Boolean isCreatePressed = false;

    Space space;
    ImageButton plusimgbtn;
    TextView txtview;
    ScrollView sv;
    LinearLayout llhor;
    LinearLayout llleft;
    LinearLayout llcenter;
    LinearLayout llright;
    FrameLayout fl;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_main = this;

        //설정버튼 생성
        txtview = new TextView(this);
        txtview.setText("설정");
        Typeface typeface = getResources().getFont(R.font.katuri);
        txtview.setTypeface(typeface);
        txtview.setTextSize(30);
        txtview.setOnClickListener(new View.OnClickListener() {
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
        if (!darkmode) {
            sv.setBackgroundColor(Color.parseColor("#FFF7CD"));
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#1C1C1F"));
        }
        setContentView(sv);



        //선형 레이아웃 생성(가장 밑에 수평, 그 위에 수직 레이아웃3개 배치)
        llhor = new LinearLayout(this);
        llleft = new LinearLayout(this);
        llcenter = new LinearLayout(this);
        llright = new LinearLayout(this);
        llhor.setOrientation(LinearLayout.HORIZONTAL);
        llcenter.setOrientation(LinearLayout.VERTICAL);


        /* 색 지정으로 선형 레이아웃 비율 확인(테스트용)
        llcenter.setBackgroundColor(Color.BLUE);
        llleft.setBackgroundColor(Color.RED);
        llright.setBackgroundColor(Color.GREEN);*/


        //양쪽 레이아웃과 가운데 레이아웃 비율을 1:10으로 설정
        final LinearLayout.LayoutParams linearParamsVerticalSide = new LinearLayout.LayoutParams(
                1, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        final LinearLayout.LayoutParams linearParamsVerticalCenter = new LinearLayout.LayoutParams(
                1, LinearLayout.LayoutParams.MATCH_PARENT, 10);
        final LinearLayout.LayoutParams linearParamsHorizontal = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);


        llhor.setLayoutParams(linearParamsHorizontal);
        llleft.setLayoutParams(linearParamsVerticalSide);
        llcenter.setLayoutParams(linearParamsVerticalCenter);
        llright.setLayoutParams(linearParamsVerticalSide);

        sv.addView(llhor);

        llhor.addView(llleft);
        llhor.addView(llcenter);
        llhor.addView(llright);
        llright.addView(txtview);

        //----------------------앱 시작 시 총 습관 개수 계산해서 버튼 생성-----------------------------
        if (totalHabit > 0) {
            Button[] boxBtn = new Button[totalHabit];
            Space[] spaces = new Space[totalHabit];

            //px을 dp로 변환
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,
                    getResources().getDisplayMetrics());           //박스버튼 너비
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                    getResources().getDisplayMetrics());           //박스버튼 높이
            int spaceHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,
                    getResources().getDisplayMetrics());           //여백 높이

            //습관 수만큼 박스 생성
            for (int i = 0; i < totalHabit; i++) {
                boxBtn[i] = new Button(this);
                boxBtn[i].setWidth(width);
                boxBtn[i].setHeight(height);

                //다크모드에 따른 버튼 색변경(임시)
                if (!darkmode) {
                    boxBtn[i].setBackgroundColor(Color.WHITE);
                }
                else {
                    boxBtn[i].setBackgroundColor(Color.parseColor("#31313C"));
                }
                //태그설정
                boxBtn[i].setTag("box_" + i);

                spaces[i] = new Space(this);
                spaces[i].setMinimumHeight(spaceHeight);
                llcenter.addView(spaces[i]);
                llcenter.addView(boxBtn[i]);


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
                    Intent intent = new Intent(MainActivity.this, CreateActivity.class);
                    startActivity(intent);
                }
            });
            llcenter.addView(plusimgbtn);
        }


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

            llcenter.addView(plusimgbtn);
        }



    }

    protected void onStart() {
        super.onStart();

        //CreateActivity에서 총 습관수 계산값을 가져옴
        if (activityMoveCount > 0) {
            Intent intent = getIntent();
            totalHabit = intent.getExtras().getInt("totalHabit+");
        }


        //메인액티비티로 돌아왔을 때 다크모드 체크
        if (!darkmode) {
            sv.setBackgroundColor(Color.parseColor("#FFF7CD"));
            llcenter.removeAllViewsInLayout();
            Button[] boxBtn = new Button[totalHabit];
            Space[] spaces = new Space[totalHabit];

            //px을 dp로 변환
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,
                    getResources().getDisplayMetrics());           //박스버튼 너비
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                    getResources().getDisplayMetrics());           //박스버튼 높이
            int spaceHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,
                    getResources().getDisplayMetrics());           //여백 높이

            //습관 수만큼 박스 생성
            for (int i = 0; i < totalHabit; i++) {
                boxBtn[i] = new Button(this);
                boxBtn[i].setWidth(width);
                boxBtn[i].setHeight(height);
                boxBtn[i].setBackgroundColor(Color.WHITE);
                //태그설정
                boxBtn[i].setTag("box_" + i);

                spaces[i] = new Space(this);
                spaces[i].setMinimumHeight(spaceHeight);
                llcenter.addView(spaces[i]);
                llcenter.addView(boxBtn[i]);


                //클릭시 버튼 태그 토스트(테스트용)
                boxBtn[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
                    }
                });
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
                llcenter.addView(plusimgbtn);
            }

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

                llcenter.addView(plusimgbtn);
            }

            isCreatePressed = false;
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#1C1C1F"));
            llcenter.removeAllViewsInLayout();
            Button[] boxBtn = new Button[totalHabit];
            Space[] spaces = new Space[totalHabit];

            //px을 dp로 변환
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,
                    getResources().getDisplayMetrics());           //박스버튼 너비
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                    getResources().getDisplayMetrics());           //박스버튼 높이
            int spaceHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,
                    getResources().getDisplayMetrics());           //여백 높이

            //습관 수만큼 박스 생성
            for (int i = 0; i < totalHabit; i++) {
                boxBtn[i] = new Button(this);
                boxBtn[i].setWidth(width);
                boxBtn[i].setHeight(height);
                boxBtn[i].setBackgroundColor(Color.parseColor("#31313C"));
                //태그설정
                boxBtn[i].setTag("box_" + i);

                spaces[i] = new Space(this);
                spaces[i].setMinimumHeight(spaceHeight);
                llcenter.addView(spaces[i]);
                llcenter.addView(boxBtn[i]);


                //클릭시 버튼 태그 토스트(테스트용)
                boxBtn[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
                    }
                });
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
                llcenter.addView(plusimgbtn);
            }

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

                llcenter.addView(plusimgbtn);
            }

            isCreatePressed = false;
        }

        if (isCreatePressed) {
            llcenter.removeAllViewsInLayout();
            Button[] boxBtn = new Button[totalHabit];
            Space[] spaces = new Space[totalHabit];

            //px을 dp로 변환
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,
                    getResources().getDisplayMetrics());           //박스버튼 너비
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                    getResources().getDisplayMetrics());           //박스버튼 높이
            int spaceHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60,
                    getResources().getDisplayMetrics());           //여백 높이

            //습관 수만큼 박스 생성
            for (int i = 0; i < totalHabit; i++) {
                boxBtn[i] = new Button(this);
                boxBtn[i].setWidth(width);
                boxBtn[i].setHeight(height);

                //다크모드에 따른 버튼 색변경(임시)
                if (!darkmode) {
                    boxBtn[i].setBackgroundColor(Color.WHITE);
                }
                else {
                    boxBtn[i].setBackgroundColor(Color.parseColor("#31313C"));
                }
                //태그설정
                boxBtn[i].setTag("box_" + i);

                spaces[i] = new Space(this);
                spaces[i].setMinimumHeight(spaceHeight);
                llcenter.addView(spaces[i]);
                llcenter.addView(boxBtn[i]);


                //클릭시 버튼 태그 토스트(테스트용)
                boxBtn[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, String.valueOf(v.getTag()), Toast.LENGTH_SHORT).show();
                    }
                });
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
                llcenter.addView(plusimgbtn);
            }

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

                llcenter.addView(plusimgbtn);
            }

            isCreatePressed = false;
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




