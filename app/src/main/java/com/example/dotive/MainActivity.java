package com.example.dotive;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{
    public static Context context_main;
    public static int totalHabit = 0;  //총 습관 개수
    public static Boolean isCreatePressed = false;  //습관생성 버튼클릭여부
    public static Integer isDarkmode = 0;   //다크모드 여부, 0이 false, 1이 true
    public static SQLiteDatabase db = null;
    public static DBHelper dbHelper;
    public static Button[] boxBtnArr;       //습관전체 박스
    public static TextView[] txtViewArr;    //습관명 텍스트뷰
    public static String habitProgress;     //습관 진행도 표현 문자열
    Cursor cursor;
    TextView txtSettings, txtEdit;
    ScrollView sv;
    LinearLayout ll, ll2;
    FrameLayout fl;

    public static String curDateString;
    Date curDate;                      //현재날짜 Date변수
    long todayTimestamp;               //현재날짜 시간량
    long[] createDateTimestamp;        //습관생성날짜 시간량
    public static long[] calDate;      //현재날짜 시간량 - 습관생성날짜 시간량
    public static String[] dateDiff;   //calDate의 시간량을 숫자로 변환







    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_main = this;
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this, 4);
        db = dbHelper.getWritableDatabase();


        //DB에서 '다크모드' 인수 가져와서 변수 초기화
        cursor = db.rawQuery("SELECT darkmode FROM Settings", null);
        while(cursor.moveToNext()) {
            isDarkmode = Integer.parseInt(cursor.getString(0));
        }

        //DB에서 '총 습관 개수' 가져와서 변수 초기화
        cursor = db.rawQuery("SELECT COUNT(id) FROM Habits", null);
        while(cursor.moveToNext()) {
            totalHabit = Integer.parseInt(cursor.getString(0));
        }

        //DB에서 '습관 진행도' 문자열 가져와서 변수 초기화
        cursor = db.rawQuery("SELECT habitProgress FROM Habits", null);
        while(cursor.moveToNext()) {
            habitProgress = cursor.getString(0);
        }



        //현재날짜와 습관생성날짜의 차를 구하는 메서드
        calDateDiff();




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

        //다크모드에 따른 배경색 변화
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
        }


        //선형 레이아웃 생성 (ll에 박스, ll2에 텍스트뷰 배치)
        fl = new FrameLayout(this);
        ll = new LinearLayout(this);
        ll2 = new LinearLayout(this);
        fl = findViewById(R.id.fl);
        ll = findViewById(R.id.ll);
        ll2 = findViewById(R.id.ll2);




        //ll.setBackgroundColor(Color.YELLOW);
        //ll2.setBackgroundColor(Color.GREEN);






        //----------------------------앱 시작 시 총 습관 개수 계산해서 버튼 생성-----------------------------

        if (totalHabit > 0) {
            boxBtnArr = new Button[totalHabit];
            txtViewArr = new TextView[totalHabit];



            //px을 dp로 변환
            //박스 관련 dp값 설정
            int btn_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,220,
                    getResources().getDisplayMetrics());
            int btn_marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,57,
                    getResources().getDisplayMetrics());
            int btn_marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,42,
                    getResources().getDisplayMetrics());

            //습관제목 텍스트뷰 관련 dp값 설정
            int txt_Width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,210,
                    getResources().getDisplayMetrics());
            int txt_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,46,
                    getResources().getDisplayMetrics());
            int txt_paddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,
                    getResources().getDisplayMetrics());
            int txt_paddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,
                    getResources().getDisplayMetrics());
            int txt_marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,34,
                    getResources().getDisplayMetrics());
            int txt_marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,75,
                    getResources().getDisplayMetrics());
            int txt_marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,245,
                    getResources().getDisplayMetrics());



            Typeface typeface = Typeface.createFromAsset(getAssets(), "font/katuri.ttf");



            LinearLayout.LayoutParams btn_linearParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams txtView_linearParams = new LinearLayout.LayoutParams(
                    txt_Width, LinearLayout.LayoutParams.WRAP_CONTENT);

            //박스 마진 설정
            btn_linearParams.setMargins(0, btn_marginTop, 0, btn_marginBottom);

            //습관제목 텍스트뷰 마진 설정
            txtView_linearParams.setMargins(txt_marginLeft, txt_marginTop, 0, txt_marginBottom);


            //습관 수만큼 박스 및 습관제목 텍스트뷰 생성
            for (int i = 0; i < totalHabit; i++) {
                boxBtnArr[i] = new Button(this);
                boxBtnArr[i].setHeight(btn_Height);
                txtViewArr[i] = new TextView(this);
                txtViewArr[i].setHeight(txt_Height);
                boxBtnArr[i].setLayoutParams(btn_linearParams);
                txtViewArr[i].setLayoutParams(txtView_linearParams);


                //습관제목 텍스트뷰 각 속성들 설정
                txtViewArr[i].setTypeface(typeface);
                txtViewArr[i].setPadding(txt_paddingLeft, 0, txt_paddingRight, 0);
                txtViewArr[i].setGravity(Gravity.CENTER);
                txtViewArr[i].setTextColor(Color.WHITE);
                txtViewArr[i].setAutoSizeTextTypeUniformWithConfiguration(12, 22, 1, TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                txtViewArr[i].setMaxLines(1);
                //txtViewArr[i].setEllipsize(TextUtils.TruncateAt.END);


                //DB에서 각 습관의 색깔값을 가져와서 적용
                cursor = db.rawQuery("SELECT habitColor FROM Habits", null);
                cursor.moveToPosition(i);
                String strng = cursor.getString(0);
                switch (cursor.getString(0)) {
                    case "red" : txtViewArr[i].setBackgroundResource(R.drawable.txtview_round_red);
                    break;
                    case "orange" : txtViewArr[i].setBackgroundResource(R.drawable.txtview_round_orange);
                        break;
                    case "green" : txtViewArr[i].setBackgroundResource(R.drawable.txtview_round_green);
                        break;
                    case "blue" : txtViewArr[i].setBackgroundResource(R.drawable.txtview_round_blue);
                        break;
                    case "purple" : txtViewArr[i].setBackgroundResource(R.drawable.txtview_round_purple);
                        break;
                    case "gray" : txtViewArr[i].setBackgroundResource(R.drawable.txtview_round_gray);
                        break;
                }


                //다크모드에 따른 박스 색변경(임시)
                if (isDarkmode == 0) {
                    boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox);
                }
                else {
                    boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox_dark);
                }

                //각 박스 마다 태그설정
                boxBtnArr[i].setTag("box_" + i);
                cursor = db.rawQuery("SELECT habitName FROM Habits", null);

                //DB에서 습관명 가져와서 텍스트뷰에 적용
                cursor.moveToPosition(i);
                txtViewArr[i].setText(cursor.getString(0));


                //DrawCircle 클래스 객체 생성해서 레이아웃에 원 배치
                DrawCircle dc = new DrawCircle(this);
                fl.addView(dc);

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
        txtSettings = new TextView(this);
        txtSettings = findViewById(R.id.txtSettings);
        txtEdit = new TextView(this);
        txtEdit = findViewById(R.id.txtEdit);

        //메인액티비티로 돌아왔을 때 다크모드 체크
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
            txtSettings.setTextColor(Color.parseColor("#0a0d09"));
            txtEdit.setTextColor(Color.parseColor("#0a0d09"));

            for (int i = 0; i < totalHabit; i++) {
                boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox);
            }
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            txtSettings.setTextColor(Color.WHITE);
            txtEdit.setTextColor(Color.WHITE);

            for (int i = 0; i < totalHabit; i++) {
                boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox_dark);
            }
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
        dbHelper.close();
        cursor.close();
    }


    //뒤로가기 키 눌렀을 때 이벤트 설정
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("알림");
        builder.setMessage("앱을 종료하시겠습니까?");
        builder.setPositiveButton("취소", null);
        builder.setNegativeButton("종료", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }

    public void ibtnPlus_onClick(View view) {
        Intent intent = new Intent(MainActivity.this, CreateActivity.class);
        startActivity(intent);
    }

    //현재날짜와 습관 생성날짜 차이 계산 메서드
    public void calDateDiff() {
        createDateTimestamp = new long[totalHabit];
        calDate = new long[totalHabit];
        dateDiff = new String[totalHabit];

        if (totalHabit > 0) {
            //날짜 형식 지정
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

            try {
                //날짜 객체 생성해서 현재날짜 입력
                Calendar calendar = Calendar.getInstance();
                curDate = new Date(calendar.getTimeInMillis());
                curDateString = dateFormat.format(curDate);
                todayTimestamp = dateFormat.parse(curDateString).getTime();

                cursor = db.rawQuery("SELECT createDate FROM Habits", null);

                for (int i = 0; i < totalHabit; i++) {
                    cursor.moveToPosition(i);
                    createDateTimestamp[i] = dateFormat.parse(cursor.getString(0)).getTime();

                    //현재날짜와 습관생성날짜 두 시간량을 뺀 시간량을 calDate에 저장
                    calDate[i] = todayTimestamp - createDateTimestamp[i];
                    dateDiff[i] = String.valueOf(calDate[i] / (24*60*60*1000));
                }
            }

            catch (ParseException e) {

            }
        }
    }
}











