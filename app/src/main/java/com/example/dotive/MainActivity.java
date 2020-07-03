package com.example.dotive;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity{
    public static Context context_main;
    public static int totalHabit = 0;               //총 습관 개수
    public static Boolean isCreatePressed = false;  //습관생성 버튼클릭여부
    public static Integer isDarkmode = 0;           //다크모드 여부, 0이 false, 1이 true
    public static SQLiteDatabase db = null;
    public static DBHelper dbHelper;
    public static Button[] boxBtnArr;               //습관전체 박스
    public static TextView[] txtViewArr;            //습관명 텍스트뷰
    public static String[] habitProgressArr;        //각 습관의 진행도 문자열을 담는 배열
    public static StringBuilder[] progressBuilderArr;
    public static Typeface typeface;
    public static int[] objectDays;                 //각 습관의 목표일수를 담는 변수
    public static String curDateString, createDateString;
    public static long[] calDate;      //현재날짜 시간량 - 습관생성날짜 시간량
    public static String[] dateDiff;   //calDate의 시간량을 숫자로 변환
    public static Date[] createDateArr, objectDateArr;
    public static int[] oneCount;                           //진행도 문자열에서 1이 몇개 있는지 담는 배열
    public static int standardSize_X, standardSize_Y;       //해상도별 디스플레이 가로, 세로크기
    public static float density;
    int eraseNum, boxNum1;
    int seqAmount;
    float btn_x, btn_y;

    Date curDate;                      //현재날짜 Date변수
    long todayTimestamp;               //현재날짜 시간량
    long[] createDateTimestamp;        //습관생성날짜 시간량

    Cursor cursor;
    ImageButton ibtnSettings, ibtnEdit;
    ImageButton[] ibtnErase;
    ImageView[] imgFire;
    TextView[] txtSequence;
    ScrollView sv;
    LinearLayout ll, ll2, ll3, ll4, ll5; //ll은 버튼, ll2는 습관명, ll3는 삭제버튼, ll4는 불아이콘, ll5는 연속일수 텍스트
    FrameLayout fl;
    String[] boxTags;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context_main = this;
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(this, 4);
        db = dbHelper.getWritableDatabase();
        View view = getWindow().getDecorView();

        ibtnSettings = new ImageButton(this);
        ibtnEdit = new ImageButton(this);

        ibtnSettings = (ImageButton) findViewById(R.id.ibtnSettings);
        ibtnEdit = (ImageButton) findViewById(R.id.ibtnEdit);

        getScreenSize(this);
        getStandardSize();





        //DB에서 '다크모드' 인수 가져와서 isDarkmode 변수에 삽입
        cursor = db.rawQuery("SELECT darkmode FROM Settings", null);
        while(cursor.moveToNext()) {
            isDarkmode = Integer.parseInt(cursor.getString(0));
        }

        //DB에서 '총 습관 개수' 가져와서 totalHabit 변수에 삽입
        cursor = db.rawQuery("SELECT COUNT(id) FROM Habits", null);
        while(cursor.moveToNext()) {
            totalHabit = Integer.parseInt(cursor.getString(0));
        }

        //DB에서 '습관 진행도' 문자열 가져와서 habitProgressArr 배열에 삽입
        //최종적으로는 StringBuilder 인 progressBuilderArr 에 각 진행도 문자열을 저장
        cursor = db.rawQuery("SELECT habitProgress FROM Habits", null);
        habitProgressArr = new String[totalHabit];
        progressBuilderArr = new StringBuilder[totalHabit];

        for (int i = 0; i < totalHabit; i++) {
            cursor.moveToPosition(i);
            habitProgressArr[i] = cursor.getString(0);
            progressBuilderArr[i] = new StringBuilder(habitProgressArr[i]);
            imgFire = new ImageView[totalHabit];
            txtSequence = new TextView[totalHabit];
        }




        //DB에서 '각 습관의 목표일수' 가져와서 objectDays 변수에 삽입
        objectDays = new int[totalHabit];
        cursor = db.rawQuery("SELECT objDays FROM Habits", null);
        for (int i = 0; i < totalHabit; i++) {
            cursor.moveToPosition(i);
            objectDays[i] = Integer.parseInt(cursor.getString(0));
        }



        //현재날짜와 습관생성날짜의 차를 구하는 메서드(원들 중에 현재날짜를 표현하기 위함)
        //결과값은 DateDiff 에 저장됨
        calDateDiff();




        //설정버튼 클릭이벤트 부여
        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        //편집버튼 클릭이벤트 부여
        ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (totalHabit > 0) {
                    for (int i = 0; i < totalHabit; i++) {
                        if (ibtnErase[i].getVisibility() == View.VISIBLE) {
                            ibtnErase[i].setVisibility(View.INVISIBLE);
                        }
                        else
                            ibtnErase[i].setVisibility(View.VISIBLE);
                    }
                }

            }
        });



        //스크롤뷰 생성
        sv = new ScrollView(this);
        sv = findViewById(R.id.sv);
        //선형 레이아웃 생성 (ll에 박스, ll2에 텍스트뷰 배치)
        fl = new FrameLayout(this);
        ll = new LinearLayout(this);
        ll2 = new LinearLayout(this);
        ll3 = new LinearLayout(this);
        ll4 = new LinearLayout(this);
        ll5 = new LinearLayout(this);
        fl = findViewById(R.id.fl);
        ll = findViewById(R.id.ll);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        ll4 = findViewById(R.id.ll4);
        ll5 = findViewById(R.id.ll5);
        //ll3.setBackgroundColor(Color.BLACK);




        //다크모드에 따른 배경색 변화
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
            getWindow().setStatusBarColor(Color.parseColor("#FFEBD3"));
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            ibtnEdit.setBackgroundResource(R.drawable.edit_dark);
            ibtnSettings.setBackgroundResource(R.drawable.settings_dark);
            ibtnEdit.setAlpha(0);
            ibtnSettings.setAlpha(0);
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            getWindow().setStatusBarColor(Color.parseColor("#272B36"));
            ibtnEdit.setBackgroundResource(R.drawable.edit);
            ibtnSettings.setBackgroundResource(R.drawable.settings);
            ibtnEdit.setAlpha(0);
            ibtnSettings.setAlpha(0);
        }


        //----------------------------앱 시작 시 총 습관 개수 계산해서 버튼 생성-----------------------------

        if (totalHabit > 0) {
            boxBtnArr = new Button[totalHabit];
            txtViewArr = new TextView[totalHabit];
            ibtnErase = new ImageButton[totalHabit];



            //px을 dp로 변환
            int fl_Width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.9f,
                    getResources().getDisplayMetrics());
            //박스 관련 dp값 설정
            int btn_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.54f,
                    getResources().getDisplayMetrics());
            int btn_marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.057f,
                    getResources().getDisplayMetrics());
            int btn_marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.15f,
                    getResources().getDisplayMetrics());

            //습관제목 텍스트뷰 관련 dp값 설정
            int txt_Width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.48f,
                    getResources().getDisplayMetrics());
            int txt_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.11f,
                    getResources().getDisplayMetrics());
            int txt_paddingSide = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,
                    getResources().getDisplayMetrics());
            int txt_marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.002f,
                    getResources().getDisplayMetrics());
            int txt_marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.54f + standardSize_X * 0.207f - standardSize_X * 0.11f,  //+5
                    getResources().getDisplayMetrics());
            int erase_btn_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.06f,
                    getResources().getDisplayMetrics());
            int erase_marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.11f,
                    getResources().getDisplayMetrics());
            int erase_marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.83f,
                    getResources().getDisplayMetrics());
            int erase_marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.637f - standardSize_X * 0.06f,
                    getResources().getDisplayMetrics());
            int fire_size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.08f,
                    getResources().getDisplayMetrics());
            int fire_marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.35f,
                    getResources().getDisplayMetrics());
            int fire_marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.54f - standardSize_X * 0.055f,
                    getResources().getDisplayMetrics());
            int fire_marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.262f - standardSize_X * 0.08f,
                    getResources().getDisplayMetrics());
            int txtSeq_marginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.43f,
                    getResources().getDisplayMetrics());
            int txtSeq_marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.54f - standardSize_X * 0.0355f,
                    getResources().getDisplayMetrics());
            int txtSeq_marginBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.262f - standardSize_X * 0.0654f,
                    getResources().getDisplayMetrics());
            int txtSeq_fontSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.016f,
                    getResources().getDisplayMetrics());
            float radius = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.09f,
                    getResources().getDisplayMetrics());




            typeface = Typeface.createFromAsset(getAssets(), "font/katuri.ttf");



            LinearLayout.LayoutParams btn_linearParams = new LinearLayout.LayoutParams(
                    fl_Width, btn_Height, 1);
            LinearLayout.LayoutParams txtView_linearParams = new LinearLayout.LayoutParams(
                    txt_Width, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
            LinearLayout.LayoutParams erasebtn_linearParams = new LinearLayout.LayoutParams(
                    erase_btn_size, erase_btn_size);
            LinearLayout.LayoutParams imgFire_linearParams = new LinearLayout.LayoutParams(
                    fire_size, fire_size, 1);
            LinearLayout.LayoutParams txtSeq_linearParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            //박스 마진 설정
            btn_linearParams.setMargins(0, btn_marginTop, 0, btn_marginBottom);
            btn_linearParams.gravity= Gravity.CENTER_HORIZONTAL;

            erasebtn_linearParams.setMargins(erase_marginLeft, erase_marginTop, 0, erase_marginBottom);
            imgFire_linearParams.setMargins(fire_marginLeft, fire_marginTop, 0, fire_marginBottom);
            txtSeq_linearParams.setMargins(txtSeq_marginLeft, txtSeq_marginTop, 0, txtSeq_marginBottom);

            //습관제목 텍스트뷰 마진 설정
            txtView_linearParams.setMargins(0, txt_marginTop, 0, txt_marginBottom);
            txtView_linearParams.gravity = Gravity.CENTER_HORIZONTAL;
/*
            LinearLayout.LayoutParams ibtnEdit_relativeParams = new LinearLayout.LayoutParams(
                    ibtnEditSetting_size, ibtnEditSetting_size);
            LinearLayout.LayoutParams ibtnSetting_relativeParams = new LinearLayout.LayoutParams(
                    ibtnEditSetting_size, ibtnEditSetting_size);
            ibtnEdit_relativeParams.setMargins(ibtnEditSetting_marginSide, ibtnEditSetting_marginTop, 0, 0);
            ibtnSetting_relativeParams.setMargins(0, ibtnEditSetting_marginTop, ibtnEditSetting_marginSide, 0);

            ibtnSetting_relativeParams.gravity = Gravity.RIGHT;
            ibtnEdit.setLayoutParams(ibtnEdit_relativeParams);
            ibtnEdit.setLayoutParams(ibtnSetting_relativeParams);*/


            //습관 수만큼 박스 및 습관제목 텍스트뷰 생성
            for (int i = 0; i < totalHabit; i++) {
                boxTags = new String[totalHabit];
                boxBtnArr[i] = new Button(this);
                boxBtnArr[i].setHeight(btn_Height);
                txtViewArr[i] = new TextView(this);
                txtViewArr[i].setHeight(txt_Height);
                ibtnErase[i] = new ImageButton(this);
                ibtnErase[i].setLayoutParams(erasebtn_linearParams);
                ibtnErase[i].setVisibility(View.INVISIBLE);
                boxBtnArr[i].setLayoutParams(btn_linearParams);
                txtViewArr[i].setLayoutParams(txtView_linearParams);
                imgFire[i] = new ImageView(this);
                imgFire[i].setBackgroundResource(R.drawable.fire);
                imgFire[i].setLayoutParams(imgFire_linearParams);
                btn_x = boxBtnArr[i].getX();
                btn_y = boxBtnArr[i].getY();

                //연속일수 표시 로직
                txtSequence[i] = new TextView(this);
                seqAmount = 0;
                if (habitProgressArr[i].length() >= Integer.parseInt(dateDiff[i])) {
                    if (Integer.parseInt(String.valueOf(habitProgressArr[i].charAt(Integer.parseInt(dateDiff[i])))) == 1){
                        seqAmount = habitProgressArr[i].lastIndexOf("1", Integer.parseInt(dateDiff[i])) - habitProgressArr[i].lastIndexOf("0", habitProgressArr[i].lastIndexOf("1", Integer.parseInt(dateDiff[i])) - 1);
                    }
                    else {
                        if (habitProgressArr[i].indexOf("0") > 0) {
                            if (Integer.parseInt(String.valueOf(habitProgressArr[i].charAt(Integer.parseInt(dateDiff[i]) - 1))) == 1) {
                                seqAmount = habitProgressArr[i].lastIndexOf("1", Integer.parseInt(dateDiff[i]) - 1) - habitProgressArr[i].lastIndexOf("0", habitProgressArr[i].lastIndexOf("1", Integer.parseInt(dateDiff[i])) - 1);
                            }
                        }
                    }
                }
                txtSequence[i].setText("연속 "+ seqAmount +"일째!");
                txtSequence[i].setTypeface(typeface);
                txtSequence[i].setTextSize(18);
                txtSequence[i].setLayoutParams(txtSeq_linearParams);



                //습관제목 텍스트뷰 각 속성들 설정
                txtViewArr[i].setTypeface(typeface);
                txtViewArr[i].setPadding(txt_paddingSide, 0, txt_paddingSide, 0);
                txtViewArr[i].setGravity(Gravity.CENTER);
                txtViewArr[i].setTextColor(Color.WHITE);
                txtViewArr[i].setAutoSizeTextTypeUniformWithConfiguration(12, 18, 1, TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
                txtViewArr[i].setMaxLines(1);
                //txtViewArr[i].setEllipsize(TextUtils.TruncateAt.END);


                //DB에서 각 습관의 색깔값을 가져와서 적용
                cursor = db.rawQuery("SELECT habitColor FROM Habits", null);
                cursor.moveToPosition(i);
                String color = cursor.getString(0);
                switch (color) {
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


                //다크모드에 따른 박스 색변경
                if (isDarkmode == 0) {
                    boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox);
                    ibtnErase[i].setBackgroundResource(R.drawable.erase_button_dark);
                    txtSequence[i].setTextColor(Color.BLACK);
                }
                else {
                    boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox_dark);
                    ibtnErase[i].setBackgroundResource(R.drawable.erase_button);
                    txtSequence[i].setTextColor(Color.WHITE);
                }

                //각 박스,습관명 마다 태그설정
                boxBtnArr[i].setTag("box_" + i);
                String boxTag = boxBtnArr[i].getTag().toString();
                int boxidx = boxTag.lastIndexOf("_");
                boxNum1 = Integer.parseInt(boxTag.substring(boxidx + 1));
                ibtnErase[i].setTag("erase_" + boxNum1);

                cursor = db.rawQuery("SELECT habitName FROM Habits", null);

                //DB에서 습관명 가져와서 텍스트뷰에 적용
                cursor.moveToPosition(i);
                txtViewArr[i].setText(cursor.getString(0));

                ibtnErase[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context_main);
                        builder.setTitle("알림");
                        builder.setMessage("습관을 삭제하시겠습니까?");
                        builder.setPositiveButton("취소", null);

                        String eraseTag = v.getTag().toString();
                        int eraseStringIndex = eraseTag.lastIndexOf("_");
                        eraseNum = Integer.parseInt(eraseTag.substring(eraseStringIndex + 1));

                        builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteHabits(eraseNum);
                                Intent intent = new Intent(MainActivity.context_main, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                        });
                        builder.show();
                    }
                });


                //DrawCircle 클래스 객체 생성해서 레이아웃에 원 배치
                DrawCircle dc = new DrawCircle(this);
                fl.addView(dc);

                ll.addView(boxBtnArr[i]);
                ll2.addView(txtViewArr[i]);
                ll3.addView(ibtnErase[i]);
                ll4.addView(imgFire[i]);
                ll5.addView(txtSequence[i]);


                //클릭시 버튼 태그 토스트(테스트용)
                boxBtnArr[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(MainActivity.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                        intent.putExtra("tag", String.valueOf(v.getTag()));
                        startActivity(intent);
                    }
                });

                boxTags[i] = boxBtnArr[i].getTag().toString();
            }
        }
        oneCount = new int[totalHabit];
        for (int j = 0; j < totalHabit; j++) {
            //진행도 문자열에서 1의 개수를 계산해서 oneCount 에 삽입
            oneCount[j] = getCharNumber(habitProgressArr[j], '1');
        }
    }

    protected void onStart() {
        super.onStart();
        calDateDiff();
    }



    @SuppressLint("ResourceType")
    protected void onResume() {
        super.onResume();


        calDateDiff();

        //메인액티비티로 돌아왔을 때 다크모드 체크
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
            ibtnEdit.setBackgroundResource(R.drawable.edit_dark);
            ibtnSettings.setBackgroundResource(R.drawable.settings_dark);
            ibtnEdit.setAlpha(0);
            ibtnSettings.setAlpha(0);
            for (int i = 0; i < totalHabit; i++) {
                boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox);
                ibtnErase[i].setVisibility(View.INVISIBLE);
            }
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            ibtnEdit.setBackgroundResource(R.drawable.edit);
            ibtnSettings.setBackgroundResource(R.drawable.settings);
            ibtnEdit.setAlpha(0);
            ibtnSettings.setAlpha(0);
            for (int i = 0; i < totalHabit; i++) {
                boxBtnArr[i].setBackgroundResource(R.drawable.custom_mainbox_dark);
                ibtnErase[i].setVisibility(View.INVISIBLE);
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
                //모든 액티비티 종료
                finishAffinity();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        builder.show();
    }

    public void ibtnPlus_onClick(View view) {
        Intent intent = new Intent(MainActivity.this, CreateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //현재날짜와 습관 생성날짜 차이 계산 메서드
    public void calDateDiff() {
        createDateTimestamp = new long[totalHabit];
        createDateArr = new Date[totalHabit];
        calDate = new long[totalHabit];
        dateDiff = new String[totalHabit];

        if (totalHabit > 0) {
            //날짜 형식 지정
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            TimeZone korea;
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
            korea = TimeZone.getTimeZone("Asia/Seoul");
            dateFormat2.setTimeZone(korea);

            try {
                //날짜 객체 생성해서 현재날짜 입력
                Calendar calendar = Calendar.getInstance();
                curDate = new Date(calendar.getTimeInMillis());
                curDateString = dateFormat.format(curDate);
                todayTimestamp = dateFormat.parse(curDateString).getTime();

                cursor = db.rawQuery("SELECT createDate FROM Habits", null);

                for (int i = 0; i < totalHabit; i++) {
                    cursor.moveToPosition(i);
                    Date test = dateFormat2.parse(cursor.getString(0));
                    createDateArr[i] = dateFormat2.parse(cursor.getString(0));

                    createDateString = dateFormat.format(test);
                    createDateTimestamp[i] = dateFormat.parse(createDateString).getTime();

                    //현재날짜와 습관생성날짜 두 시간량을 뺀 시간량을 calDate 에 저장
                    calDate[i] = todayTimestamp - createDateTimestamp[i];
                    if (calDate[i] >= 0) {
                        dateDiff[i] = String.valueOf(calDate[i] / (24*60*60*1000));
                    }
                }
            }

            catch (ParseException e) {
                //string 에서 date format 으로 parse 하는것 때문에 예외처리 해줘야함
            }
        }
    }

    //문자열에서 특정 문자의 개수를 구하는 메서드
    public int getCharNumber(String str, char c)
    {
        int count = 0;
        for(int i=0;i<str.length();i++)
        {
            if(str.charAt(i) == c)
                count++;
        }
        return count;
    }

    //Habits 테이블 습관 삭제
    public void deleteHabits(Integer id) {
        MainActivity.dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Habits WHERE ROWID = (SELECT ROWID FROM Habits LIMIT 1 OFFSET "+ id +")");
    }

    public Point getScreenSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        return size;
    }

    public void getStandardSize() {
        Point ScreenSize = getScreenSize(this);
        density  = getResources().getDisplayMetrics().density;

        standardSize_X = (int) (ScreenSize.x / density);
        standardSize_Y = (int) (ScreenSize.y / density);
    }
}











