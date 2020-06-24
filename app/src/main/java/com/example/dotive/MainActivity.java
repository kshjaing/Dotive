package com.example.dotive;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
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

import org.w3c.dom.Text;

import java.lang.reflect.Type;

public class MainActivity extends AppCompatActivity{
    public static Context context_main;
    public static int totalHabit = 0;  //총 습관 개수
    public static Boolean isCreatePressed = false;  //습관생성 버튼클릭여부
    public static Integer isDarkmode = 0;   //다크모드 여부, 0이 false, 1이 true
    public static SQLiteDatabase db = null;
    public static DBHelper dbHelper;
    Cursor cursor;

    Space space;
    TextView txtSettings, txtEdit;
    ScrollView sv;
    LinearLayout ll, ll2;
    RelativeLayout rl;
    FrameLayout fl;
    Button[] boxBtnArr;
    TextView[] txtViewArr;
    CustomMainBox[] mainBoxes;
    String habitName;





    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_main = this;
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this, 4);
        db = dbHelper.getWritableDatabase();

        //다크모드 인수 가져옴
        cursor = db.rawQuery("SELECT darkmode FROM Settings", null);
        while(cursor.moveToNext()) {
            isDarkmode = Integer.parseInt(cursor.getString(0));
        }

        //총 습관 개수 뽑아옴
        cursor = db.rawQuery("SELECT COUNT(id) FROM Habits", null);
        while(cursor.moveToNext()) {
            totalHabit = Integer.parseInt(cursor.getString(0));
        }

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



        //스크롤뷰 생성
        sv = new ScrollView(this);
        sv = findViewById(R.id.sv);

        //다크모드
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
        }


        //선형 레이아웃 생성
        ll = new LinearLayout(this);
        ll2 = new LinearLayout(this);
        ll = findViewById(R.id.ll);
        ll2 = findViewById(R.id.ll2);






        //----------------------------앱 시작 시 총 습관 개수 계산해서 버튼 생성-----------------------------

        if (totalHabit > 0) {
            boxBtnArr = new Button[totalHabit];
            txtViewArr = new TextView[totalHabit];
            Space[] spaces = new Space[totalHabit];

            //px을 dp로 변환
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                    getResources().getDisplayMetrics());           //박스버튼 높이
            int spaceHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,
                    getResources().getDisplayMetrics());           //여백 높이

            //습관제목 텍스트뷰 관련 dp값들 선언
            int txtWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,170,
                    getResources().getDisplayMetrics());
            int paddingHor = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,
                    getResources().getDisplayMetrics());
            int paddingVer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,8,
                    getResources().getDisplayMetrics());
            int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,34,
                    getResources().getDisplayMetrics());
            int marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,90,
                    getResources().getDisplayMetrics());
            int btn_marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,
                    getResources().getDisplayMetrics());



            //습관 수만큼 박스 생성
            for (int i = 0; i < totalHabit; i++) {
                boxBtnArr[i] = new Button(this);
                boxBtnArr[i].setHeight(height);
                txtViewArr[i] = new TextView(this);

                Typeface typeface = Typeface.createFromAsset(getAssets(), "font/katuri.ttf");

                LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams txtView_linearParams = new LinearLayout.LayoutParams(
                        txtWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
                linearParams.setMargins(0, btn_marginTop, 0, spaceHeight);
                txtView_linearParams.setMargins(marginLeft, marginTop, 0, 0);
                boxBtnArr[i].setLayoutParams(linearParams);
                txtViewArr[i].setLayoutParams(txtView_linearParams);
                boxBtnArr[i].setTypeface(typeface);
                txtViewArr[i].setTypeface(typeface);
                txtViewArr[i].setPadding(paddingHor, paddingVer, paddingHor, paddingVer);
                txtViewArr[i].setTextSize(20);
                txtViewArr[i].setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                txtViewArr[i].setTextColor(Color.WHITE);
                txtViewArr[i].setBackgroundResource(R.drawable.txtview_round);

                //다크모드에 따른 버튼 색변경(임시)
                if (isDarkmode == 0) {
                    boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox);
                }
                else {
                    boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox_dark);
                }
                //태그설정
                boxBtnArr[i].setTag("box_" + i);
                cursor = db.rawQuery("SELECT habitName FROM Habits", null);
                cursor.moveToNext();
                txtViewArr[i].setText(cursor.getString(i));


                spaces[i] = new Space(this);
                spaces[i].setMinimumHeight(spaceHeight);
                //ll.addView(spaces[i]);
                ll.addView(boxBtnArr[i]);
                ll2.addView(txtViewArr[i]);


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

    @SuppressLint("ResourceType")
    protected void onResume() {
        super.onResume();
        Log.d("total", String.valueOf(totalHabit));
        Log.d("total", String.valueOf(isDarkmode));
        txtSettings = new TextView(this);
        txtSettings = findViewById(R.id.txtSettings);
        txtEdit = new TextView(this);
        txtEdit = findViewById(R.id.txtEdit);

        //메인액티비티로 돌아왔을 때 다크모드 체크
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
            txtSettings.setTextColor(Color.parseColor("#232323"));
            txtEdit.setTextColor(Color.parseColor("#232323"));

            for (int i = 0; i < totalHabit; i++) {
                ll.findViewWithTag("box_" + i).setBackgroundResource(R.drawable.custom_mainbox);
            }
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            txtSettings.setTextColor(Color.WHITE);
            txtEdit.setTextColor(Color.WHITE);

            for (int i = 0; i < totalHabit; i++) {
                ll.findViewWithTag("box_" + i).setBackgroundResource(R.drawable.custom_mainbox);
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
        dbHelper.close();
        cursor.close();
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




