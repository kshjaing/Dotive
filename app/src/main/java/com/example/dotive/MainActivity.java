package com.example.dotive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.media.Image;
import android.media.JetPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/*
 * 20200624 수정
 * MainActivity : 추가한 습관들 나열. */

/* 최종목표가 표시될 곳
 * 편집 버튼 (or + 버튼)을 눌러 최종목표를 만들어 이 페이지에 표시된다.
 * 만들어진 최종 목표에서 여러가지 습관이 있다. 그 습관 페이지로 intent 한다. 습관 페이지 = HabitActivity
 * 습관 페이지에서 예를들어 팔굽혀펴기 5회 있다면 그걸 누르면 또 intent 한다. 결과 페이지 = ResultActivity*/

//activity_main.xml을 inflater로 큰 레이아웃 틀을 만듦.
//내용물 (버튼,습관 제목 textview)들은 동적 생성.

public class MainActivity extends AppCompatActivity {

    //여기에 쓰이는 동적 컨트롤들 객체
    ScrollView scrollView;
    FrameLayout frameLayout;

    LinearLayout linearLayout; //버튼
    ImageButton Btn_Habit_Add;
    Button[] Arr_Btn_Habit;

    LinearLayout linearLayout2; //습관 제목 텍스트
    TextView[] Arr_TextView_Habit_Name;

    LinearLayout linearLayout3; //편집 버튼 누르면 삭제 버튼

    LinearLayout linearLayout4; //연속 일 수 레이아웃
    TextView[] Arr_TextView_continue_day;

    //습관 개수에따라 버튼 증가
    static int TotalHabit = 1;

    //습관 테이블 개수 (0이면 값이 존재하지 않음)
    int Habits_Table_Count = 0;

    //CreateActivity 에서 intent 값
    String ID = ""; //증가 id
    String Habit_Name = ""; //습관명
    //int i_Habit_Color = 0; //습관 색깔
    String Habit_Color = ""; //습관 색깔
    //int edit_Habit_Day_Num = 0; //습관 목표일 수
    String edit_Habit_Day_Num = ""; //습관 목표일 수
    String Habit_Progress = ""; //습관 진행도
    String Habit_Create_Day = "";// 습관 생성 날짜


    //DB에서 받아온 값
    public static String[] Arr_ID = {}; //습관 ID 증가 값
    public static String[] Arr_Habit_Name = {};//습관명
    public static String[] Arr_Habit_Color = {};//습관 색깔
    public static String[] Arr_edit_Habit_Day_Num = {}; //습관 목표일 수
    public static String[] Arr_Habit_Progress = {}; //습관 진행도
    public static String[] Arr_Habit_Create_Day = {}; //습관 생성 날짜

    public static long count; //생성날짜 오늘날짜 차이
    public static long[] COUNT; //생성날짜 오늘 날짜 차이 배열 저장

    //페인트 할때 습관 진행도 값 배열 (1,0,0,0,0)
    String Arr_Btn_Paint_Progress[] = {};

    //편집 버튼
    ImageButton ibtnEdit;
    //편집 시 삭제 버튼
    Button[] DeleteButton;

    //설정 버튼
    ImageButton ibtnSettings;

    //연속일수 더할 값
    int[] Continue_index = {};

    //db에서 다크모드 값 , 언어 값
    public int DB_darkmode = 0; // 1 = > 다크 , 기본값 : 0
    String DB_language = "";

    int Table_Count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //setContentView(new MyView(this));  (클래스 MyView를 바로 실행)


        //Habits 테이블에 습관이 존재하지 않다면 CreateActivity.java로 돌아간다.

        QUERY_Habits();

        QUERY_Settings();

        Log.e("MainActivity.java", "Habits_Table_Count : " + Habits_Table_Count);


        //final Habits_Table_Count = 0;
        //습관 하나 이상 존재함
        //존재하면 DB 에서 값을 받아온다.
        if (Habits_Table_Count > 1) { //2부터 값이 1개 존재
            COUNT = new long[Habits_Table_Count - 1];
            /*Intent intent = getIntent();
            Habit_Name = intent.getStringExtra("Habit_Name"); //습관명
            Habit_Color = Integer.parseInt(intent.getStringExtra("Habit_Color")); //습관 색깔
            edit_Habit_Day_Num = Integer.parseInt(intent.getStringExtra("edit_Habit_Day_Num")); //습관 목표일 수
            Log.e("습관명", Habit_Name);
            Log.e("습관컬러", Integer.toString(Habit_Color));
            Log.e("습관 목표일 수", Integer.toString(edit_Habit_Day_Num));*/

            /*for (int i = 0; i<Habits_Table_Count; i++) {
                Log.e("습관명 " + i, Arr_Habit_Name[i]);
                Log.e("습관컬러 " + i, Integer.toString(Arr_Habit_Color[i]));
                Log.e("습관 목표일 수 " + i, Integer.toString(Arr_edit_Habit_Day_Num[i]));
            }*/

            //DB에서 받은 값 확인
            Log.e("MainActivity.java", "id : " + ID);
            Log.e("MainActivity.java", "Habit_Name : " + Habit_Name);
            Log.e("MainActivity.java", "Habit_Color : " + Habit_Color);
            Log.e("MainActivity.java", "edit_Habit_Day_Num : " + edit_Habit_Day_Num);
            Log.e("MainActivity.java", "Habit_Progress 습관 진행도: " + Habit_Progress);
            Log.e("MainActivity.java", "habitCreateDay : " + Habit_Create_Day);

            Arr_ID = ID.split("_");
            Arr_Habit_Name = Habit_Name.split("_");
            Arr_Habit_Color = Habit_Color.split("_");
            Arr_edit_Habit_Day_Num = edit_Habit_Day_Num.split("_");
            Arr_Habit_Progress = Habit_Progress.split("_");
            Arr_Habit_Create_Day = Habit_Create_Day.split("_");

        } else {
            Intent intent2 = new Intent(MainActivity.this, CreateActivity.class);
            startActivity(intent2);
        }

        //객체 인스턴스 초기화.

        //xml inflater
        View view_main = (View) getLayoutInflater().inflate(R.layout.activity_main, null);

        scrollView = (ScrollView) view_main.findViewById(R.id.scrollView);



        frameLayout = (FrameLayout) view_main.findViewById(R.id.frameLayout);

        linearLayout = (LinearLayout) view_main.findViewById(R.id.linearLayout);

        //버튼위에 제목 (물 1L 마시기 등)
        linearLayout2 = (LinearLayout) view_main.findViewById(R.id.linearLayout2);

        //습관 삭제 버튼 위치를 위해
        linearLayout3 = (LinearLayout) view_main.findViewById(R.id.linearLayout3);

        //연속 일 수 레이아웃
        linearLayout4 = (LinearLayout) view_main.findViewById(R.id.linearLayout4);

        Day();
        Painting_Circle1 painting_circle = new Painting_Circle1(this); //CustomView.java 파일을 불러와 실행

        //동적 view 생성
        frameLayout.addView(painting_circle); //버튼 위의 원 배열을 위해

        /*//main 액션 바 만들기

        //main_actionbar.xml inflater
        View actionview = (View) getLayoutInflater().inflate(R.layout.main_actionbar, null);

        //toolbar 위젯 객체 인스턴스 초기화
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) actionview.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //매니페스트에서 액션바 없앴으니 커스텀으로 추가하는 과정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스텀 액션바 보이게
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 false = > 안보이게 함. (제목 삭제)
        actionBar.setDisplayHomeAsUpEnabled(true); //이 값은 자동으로 뒤로가기 버튼 생성
        linearLayout.addView(actionview);*/


        //java 코드로 폰트 설정 (xml 에서 fontFamily)
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/katuri.ttf");


        //이렇게 안하면 픽셀로 값이 저장되기에 dp로 계산하는 것. value에서 원하는 숫자로 고치면 됨.
        //dp 값으로 가져오기
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, getResources().getDisplayMetrics());

        //textView (습관 제목)
        int textView_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, getResources().getDisplayMetrics());
        int textView_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        //delete 버튼 (x 버튼)
        int DeleteButton_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int DeleteButton_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        //textView( 연속 0일째)
        int Continue_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
        int Continue_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());

        int btn_margin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());
        int btn_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int btn_margin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

        //textView (습관 제목 마진)
        int textView_btn_margin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 115, getResources().getDisplayMetrics());
        int textView_btn_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 215, getResources().getDisplayMetrics());
        int textView_btn_margin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());

        //delete 버튼 마진 (X 버튼)
        int delete_marin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 325, getResources().getDisplayMetrics());
        int delete_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 210, getResources().getDisplayMetrics());
        int delete_margin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());

        //textView (연속 0일째)
        int Continue_magin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int Continue_magin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 550, getResources().getDisplayMetrics());
        int Continue_magin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());

        //margin 설정을 위해 (margin을 Layout에서 부모값을 정하고 그곳에 마진을 아까 dp 계산한 int 값으로 지정.)
        LinearLayout.LayoutParams Button_LinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //margin 설정 2 (textview 설정 (물 1L 마시기)
        LinearLayout.LayoutParams textView_LinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //margin 설정 3 (delete 버튼 위치 (x 버튼))
        LinearLayout.LayoutParams DeleteButton_LinearParams = new LinearLayout.LayoutParams(
                DeleteButton_width,
                DeleteButton_height
        );

        //margin 설정 4 (연속 0일째)
        LinearLayout.LayoutParams Arr_TextView_continue_day_LinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //margin 값 dp로 설정해주는중. 버튼에 적용
        Button_LinearParams.leftMargin = btn_margin_left; //size
        Button_LinearParams.bottomMargin = btn_margin_bottom;
        Button_LinearParams.topMargin = btn_margin_top;

        //margin 값 dp로 설정해주는중. textview 적용 (물 1L 마시기)
        textView_LinearParams.leftMargin = textView_btn_margin_left; //size
        textView_LinearParams.bottomMargin = textView_btn_margin_bottom;
        textView_LinearParams.topMargin = textView_btn_margin_top;

        //margin 값 dp로 설정해주는중. delete 버튼 적용
        DeleteButton_LinearParams.setMargins(delete_marin_left,delete_margin_top,0,delete_margin_bottom);

        //delete 버튼 크기를 위해
        //delete 버튼 width, height
        DeleteButton_LinearParams.width = 100;
        DeleteButton_LinearParams.height = 100;

        //마진값 dp로 설정. (연속 0일째)
        Arr_TextView_continue_day_LinearParams.setMargins(Continue_magin_left,Continue_magin_top,0,Continue_magin_bottom);

        Table_Count = Habits_Table_Count;
        Arr_Btn_Habit = new Button[Table_Count - 1];
        Arr_TextView_Habit_Name = new TextView[Table_Count - 1];

        //편집 delete 버튼 (X 버튼)
        DeleteButton = new Button[Table_Count - 1];
        //연속일 수 저장
        Arr_TextView_continue_day = new TextView[Table_Count - 1];


        for (int i = 0; i < Table_Count - 1; i++) {

            //여기부터 습관 버튼 관련
            //습관 버튼 인스턴스 초기화
            Arr_Btn_Habit[i] = new Button(this);
            linearLayout.addView(Arr_Btn_Habit[i]);


            //습관 제목 텍스트뷰 인스턴스 초기화
            Arr_TextView_Habit_Name[i] = new TextView(this);
            linearLayout2.addView(Arr_TextView_Habit_Name[i]);

            //delete 버튼 초기화
            DeleteButton[i] = new Button(this);
            linearLayout3.addView(DeleteButton[i]);

            //연속 일 수 (ex 연속 0일째)
            Arr_TextView_continue_day[i] = new TextView(this);
            linearLayout4.addView(Arr_TextView_continue_day[i]);

            //습관 버튼 글꼴 및 Text
            //Arr_Btn_Habit[i].setText(Arr_Habit_Name[i]);
            Arr_Btn_Habit[i].setTypeface(typeface);

            //습관 제목 텍스트뷰 글꼴 및 Text
            Arr_TextView_Habit_Name[i].setText(Arr_Habit_Name[i]);
            Arr_TextView_Habit_Name[i].setTypeface(typeface);

            //연속 0일째
            //Arr_edit_Habit_Day_Num , Arr_Habit_Progress
            String Btn_Paint_Progress = Arr_Habit_Progress[i];
            Arr_Btn_Paint_Progress = Btn_Paint_Progress.split(","); //Ex : [0] : 1, [1] : 0, [2] : 0 이렇게 잘라 저장.

            Arr_TextView_continue_day[i].setText("연속 " + "..." + "일째");
            Arr_TextView_continue_day[i].setTypeface(typeface);

            //습관 버튼 Width, Height
            Arr_Btn_Habit[i].setWidth(width);
            Arr_Btn_Habit[i].setHeight(height);

            //습관 제목 텍스트뷰 Width, Height
            Arr_TextView_Habit_Name[i].setWidth(textView_width);
            Arr_TextView_Habit_Name[i].setHeight(textView_height);
            Arr_TextView_Habit_Name[i].setGravity(Gravity.CENTER);


            //연속 0일째 width,height
            Arr_TextView_continue_day[i].setWidth(Continue_width);
            Arr_TextView_continue_day[i].setHeight(Continue_height);
            Arr_TextView_continue_day[i].setGravity(Gravity.CENTER);
            Arr_TextView_continue_day[i].setCompoundDrawablesWithIntrinsicBounds(R.drawable.fire,0,0,0);

            //습관 버튼 radius Drawable
            Arr_Btn_Habit[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radius));

            //다크모드 버튼 색상변경
            if(DB_darkmode == 0) {
                GradientDrawable bgShape = (GradientDrawable) Arr_Btn_Habit[i].getBackground().getCurrent(); //GradientDrawable 그대로 하면 오류남 마지막에 .getCurrent() 중요
                bgShape.setColor(Color.parseColor("#fcfcfc"));
            }
            else {
                GradientDrawable bgShape = (GradientDrawable) Arr_Btn_Habit[i].getBackground().getCurrent(); //GradientDrawable 그대로 하면 오류남 마지막에 .getCurrent() 중요
                bgShape.setColor(Color.parseColor("#414b5c"));
            }

            //습관 제목 텍스트뷰 CSS
            Arr_TextView_Habit_Name[i].setBackground(ContextCompat.getDrawable(this, R.drawable.textview_custom_css));
            Arr_TextView_Habit_Name[i].setTextColor(Color.WHITE);

            //delete 버튼 이미지 씌움
            DeleteButton[i].setBackgroundResource(R.drawable.erase_button_dark);
            //DeleteButton[i].setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.erase_button));

            //습관 제목 텍스트뷰의 백그라운드 컬러 값 (CreateActivity에서 받아온 값)
            GradientDrawable bgShape = (GradientDrawable) Arr_TextView_Habit_Name[i].getBackground().getCurrent(); //GradientDrawable 그대로 하면 오류남 마지막에 .getCurrent() 중요
            bgShape.setColor(Integer.parseInt(Arr_Habit_Color[i]));

            //습관 버튼 절대 좌표
            Arr_Btn_Habit[i].setLayoutParams(Button_LinearParams);

            //습관 제목 텍스트뷰 절대 좌표
            Arr_TextView_Habit_Name[i].setLayoutParams(textView_LinearParams);

            //delete 버튼 절대 좌표
            DeleteButton[i].setLayoutParams(DeleteButton_LinearParams);

            //delete 버튼 기본 감춤
            DeleteButton[i].setVisibility(View.INVISIBLE);
            final int a = i; //final 에러 방지 로컬변수로 선언해서 사용함.

            //습관 버튼 클릭 리스너
            Arr_Btn_Habit[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                    intent.putExtra("id", Arr_ID[a]); //습관 id 증가 값.
                    intent.putExtra("Habit_Name", Arr_TextView_Habit_Name[a].getText()); //버튼의 이름을 HabitActivity에 전달.
                    intent.putExtra("Habit_ObjDays",Arr_edit_Habit_Day_Num[a]);//습관 목표일 수
                    intent.putExtra("Habit_Progress", Arr_Habit_Progress[a]); //습관 진행도
                    intent.putExtra("Habit_Create_Day", Arr_Habit_Create_Day[a]); //습관 생성 날짜
                    intent.putExtra("Get_i",Integer.toString(a)); //현재 무슨 버튼인지 알아야함. (0부터 시작)
                    intent.putExtra("Count", COUNT[a]);
                    startActivity(intent);
                }
            });

            //편집 삭제 버튼 리스너
            DeleteButton[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("삭제");
                            builder.setMessage("습관을 삭제하시겠습니까?");
                            builder.setCancelable(true); //뒤로가기 누르면 취소
                            builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        String uriString = "content://com.example.dotive/Habits";
                                        Uri uri = new Uri.Builder().build().parse(uriString);

                                        String selection = "id=?";
                                        String[] selectionArgs = new String[] {Arr_ID[a]};

                                        int count = getContentResolver().delete(uri, selection, selectionArgs);
                                        Log.e("MainActivity.java", "delete 실행 : " + count);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Toast myToast = Toast.makeText(getApplicationContext(),"습관을 삭제하였습니다", Toast.LENGTH_SHORT);
                                    myToast.show();

                                    //액티비티 갱신 (db 업데이트 사항 적용)
                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);

                                }
                            });
                            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                        return;
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                }
            });
        }


        //습관 추가 버튼 (이 버튼은 동적 버튼 맨 아래에 위치해야 하므로 여기서 addView 했다.)
        //이 부분 버튼 가운데 정렬 필요 or 이미지 버튼이 아닌 css로 그리던지
        Btn_Habit_Add = (ImageButton) view_main.findViewById(R.id.ibtnPlus);
        //Btn_Habit_Add.setBackgroundResource(R.drawable.habit_add_image);
        //Btn_Habit_Add.setLayoutParams(Button_LinearParams);
        //Btn_Habit_Add.setWidth(30);
        //Btn_Habit_Add.setHeight(30);
        //Btn_Habit_Add.setGravity(Gravity.RIGHT);

        //linearLayout.addView(Btn_Habit_Add);

        //습관 추가 버튼 클릭 리스너
        Btn_Habit_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent1);
            }
        });

        //편집 버튼 누르면 버튼 위에 x 표시 및 DB 삭제
        ibtnEdit = (ImageButton) view_main.findViewById(R.id.ibtnEdit);

        ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int k = 0; k < Table_Count - 1; k++) {
                    if(DeleteButton[k].getVisibility() == View.VISIBLE)
                        DeleteButton[k].setVisibility(View.INVISIBLE);
                    else DeleteButton[k].setVisibility(View.VISIBLE);
                }
            }
        });

        //설정 버튼 (다크모드 등)
        ibtnSettings = (ImageButton) view_main.findViewById(R.id.ibtnSettings);

        ibtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("DARK_MODE", DB_darkmode); //습관 id 증가 값.
                Log.e("MainActivity.java","다크모드 값 전달 : " + DB_darkmode);
                startActivity(intent);
            }
        });

        //db에서 다크모드 여부 확인 하고 변경
        if (DB_darkmode == 0) {
            scrollView.setBackgroundColor(Color.parseColor("#FFEBD3")); //뒷 배경 설정
            ibtnEdit.setBackgroundResource(R.drawable.edit_dark);
            ibtnSettings.setBackgroundResource(R.drawable.settings_dark);
        }
        else {
            scrollView.setBackgroundColor(Color.parseColor("#272B36")); //뒷 배경 설정
            ibtnEdit.setBackgroundResource(R.drawable.edit);
            ibtnSettings.setBackgroundResource(R.drawable.settings);
        }

        //제일 위에 있는것을 뿌려야 차례대로 child view 들이 보인다. (중간꺼든 다른거 넣으면 페이탈 에러)
        setContentView(scrollView);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Color_Change:
                Toast.makeText(this, "색 변경 클릭", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Target_Days_Change:
                Toast.makeText(this, "목표일수 변경 클릭", Toast.LENGTH_SHORT).show();
                break;
            case R.id.Habit_Delete:
                Toast.makeText(this, "습관 삭제 클릭", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void QUERY_Settings() {
        try {
            String uriString = "content://com.example.dotive/Settings";
            Uri uri = new Uri.Builder().build().parse(uriString);

            String[] columns = new String[] {"darkmode","language"};
            Cursor cursor = getContentResolver().query(uri, columns, null, null, null);
            Log.e("MainActivity.java", "QUERY 결과 (Settings) : " + cursor.getCount());

            while (cursor.moveToNext()) {
                int darkmode = cursor.getInt(cursor.getColumnIndex(columns[0]));
                String language = cursor.getString(cursor.getColumnIndex(columns[1]));

                DB_darkmode = darkmode;
                DB_language = language;

                Log.e("MainActivity.java", "Settings 레코드 " + darkmode + ", " + language);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void QUERY_Habits() {
        try {
            String uriString = "content://com.example.dotive/Habits";
            Uri uri = new Uri.Builder().build().parse(uriString);

            String[] columns = new String[]{"id","habitName", "habitColor", "objDays", "habitProgress","habitCreateDay"};
            Cursor cursor = getContentResolver().query(uri, columns, null, null, "id ASC");
            Log.e("MainActivity.java", "QUERY 결과 : " + cursor.getCount());

            int index = 1;

            while (cursor.moveToNext()) { //각 레코드 값을 출력함.
                String id = cursor.getString(cursor.getColumnIndex(columns[0]));
                String habitName = cursor.getString(cursor.getColumnIndex(columns[1]));
                String habitColor = cursor.getString(cursor.getColumnIndex(columns[2]));
                int objDays = cursor.getInt(cursor.getColumnIndex(columns[3]));
                String habitProgress = cursor.getString(cursor.getColumnIndex(columns[4]));
                String habitCreateDay = cursor.getString(cursor.getColumnIndex(columns[5]));

                ID += id + "_"; //습관 ID 증가 값
                Habit_Name += habitName + "_"; //습관명
                Habit_Color += habitColor + "_"; //습관 색깔
                edit_Habit_Day_Num += objDays + "_"; //습관 목표일 수
                Habit_Progress += habitProgress + "_"; //습관 진행도
                Habit_Create_Day += habitCreateDay + "_"; //습관 생성 날짜

                Log.e("MainActivity.java", "레코드 " + index + " :" + id + ", " + habitName + ", " + habitColor + ", " + objDays + ", " + habitProgress + ", " + habitCreateDay);
                index += 1;
            }
            Habits_Table_Count = index;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Day() {
        //습관 생성날짜 계산
        for(int s = 0; s<Habits_Table_Count - 1; s++) {

            final int ONE_DAY = 24 * 60 * 60 * 1000;

            //날짜 배열로 저장
            String[] Y_M_D =  Arr_Habit_Create_Day[s].split("-");

            //DB에 저장된 날짜들 (습관 생성한 날)
            int year = Integer.parseInt(Y_M_D[0]);
            int month = Integer.parseInt(Y_M_D[1]);
            int day = Integer.parseInt(Y_M_D[2]);;

            //포맷
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar Create_dayCal = Calendar.getInstance(); //생성 날짜
            //String a = simpleDateFormat.format(Create_dayCal.getTime()); //db에 생성 날짜 저장 이렇게
            Calendar dDayCal = Calendar.getInstance(); //오늘 날짜

            month -= 1; //달력

            Create_dayCal.set(year,month,day); //생성 날짜 입력
            Log.e("테스트",simpleDateFormat.format(Create_dayCal.getTime()) +"");
            Log.e("테스트", simpleDateFormat.format(dDayCal.getTime()) + "");

            long today = Create_dayCal.getTimeInMillis()/ONE_DAY;
            long dday = dDayCal.getTimeInMillis()/ONE_DAY;
            COUNT[s] = dday - today; //배열로 각각의 날마다 차이 저장해둠.
            //count = dday - today;
        }
    }

    //버튼 위에 습관일수 배열로 그리기
    //CustomView 에 버튼 좌표를 구할 수 없어 여기서 그린다.

    public class Painting_Circle1 extends View {
        private Paint paint;
        private Paint stroke;

        public Painting_Circle1(Context context) {
            super(context);

            init(context);
        }

        public Painting_Circle1(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        /*@Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Toast.makeText(super.getContext(), "모션 이벤트 다운" + event.getX() + "," + event.getY(), Toast.LENGTH_LONG).show();
            }
            return super.onTouchEvent(event);
        }*/

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            float X, Y;

            for(int i = 0; i< Habits_Table_Count - 1; i++) {
                Y = Arr_Btn_Habit[i].getY(); //버튼 Y좌표
                X = Arr_Btn_Habit[i].getX(); //버튼 X좌표

                //버튼 width, height
                int W = Arr_Btn_Habit[i].getWidth();
                int H = Arr_Btn_Habit[i].getHeight();

                //목표일
                int Object_Days = Integer.parseInt(Arr_edit_Habit_Day_Num[i]);

                //////////////////////////////////
                count = COUNT[i];

                Log.e("테스트 마지막",COUNT[i] + "");

                //원을 그린다.

                int index_X = 0;
                int index_Y = 0;

                //1일 ~ 4일 반지름 100
                paint.setStyle(Paint.Style.FILL);

                if(DB_darkmode == 0) paint.setColor(Color.parseColor("#d6d6d6")); //다크모드 0;
                else paint.setColor(Color.parseColor("#7a8599")); //다크모드 1;

                stroke.setStyle(Paint.Style.STROKE);
                if(DB_darkmode == 0) stroke.setColor(Color.parseColor("#fcb114")); //다크모드 0;
                else stroke.setColor(Color.parseColor("#fcfcfc")); //다크모드 1;
                stroke.setStrokeWidth(15);

                //습관 진행도 값을 i를 통해 배열로 가져온다.
                //그 배열엔 레코드 1 : 1,0,0,0,0  , 레코드 2: 0 , 레코드 3 : 0,0,0  이런식으로 저장된 값
                //
                //Arr_Habit_Progress[0]; //습관 진행도 배열에 저장된 것.
                String Btn_Paint_Progress = Arr_Habit_Progress[i];

                Arr_Btn_Paint_Progress = Btn_Paint_Progress.split(","); //Ex : [0] : 1, [1] : 0, [2] : 0 이렇게 잘라 저장.
                /*for(int k = 0; k < Arr_Btn_Paint_Progress.length; k++) {
                    if(Arr_Btn_Paint_Progress[k] == "1") {
                        paint.setColor(Color.GREEN);
                    }
                    else if(Arr_Btn_Paint_Progress[k] == "0") {
                        paint.setColor(Color.GRAY);
                    }
                }*/

                /////////////////////////
                if(Object_Days < 5) {
                    if(Object_Days == 1) {

                        for(int q = 0; q < Arr_Btn_Paint_Progress.length; q++) {
                            if(Integer.parseInt(Arr_Btn_Paint_Progress[q]) == 1) paint.setColor(Integer.parseInt(Arr_Habit_Color[q]));
                            if(Integer.parseInt(Arr_Btn_Paint_Progress[q]) == 0) { //빈칸
                                if(DB_darkmode == 0) paint.setColor(Color.parseColor("#d6d6d6")); //다크모드 0;
                                else paint.setColor(Color.parseColor("#7a8599"));
                            }
                            if(q == 0) canvas.drawCircle(X + W/2, Y + H/2 , 100, paint);
                            if(count == 0) canvas.drawCircle(X + W/2, Y + H/2 , 100, stroke);
                        }

                    }
                    else if(Object_Days == 2) {
                        for(int q = 0; q<Arr_Btn_Paint_Progress.length; q++) {
                            if(Integer.parseInt(Arr_Btn_Paint_Progress[q]) == 1) paint.setColor(Integer.parseInt(Arr_Habit_Color[q]));
                            if(Integer.parseInt(Arr_Btn_Paint_Progress[q]) == 0) {
                                if(DB_darkmode == 0) paint.setColor(Color.parseColor("#d6d6d6")); //다크모드 0;
                                else paint.setColor(Color.parseColor("#7a8599"));
                            }
                            if(q == 0) canvas.drawCircle(X + W/2 - 135, Y + H/2, 100, paint);
                            if(q == 1) canvas.drawCircle(X + W/2 + 135, Y + H/2, 100, paint);
                            if(count == 0) canvas.drawCircle(X + W/2 - 135, Y + H/2, 100, stroke);
                            if(count == 1) canvas.drawCircle(X + W/2 + 135, Y + H/2, 100, stroke);
                        }
                    }
                    else if(Object_Days == 3) {
                        for(int q = 0; q<Arr_Btn_Paint_Progress.length; q++){
                            if(Integer.parseInt(Arr_Btn_Paint_Progress[q]) == 1) paint.setColor(Integer.parseInt(Arr_Habit_Color[q]));
                            if(Integer.parseInt(Arr_Btn_Paint_Progress[q]) == 0) {
                                if(DB_darkmode == 0) paint.setColor(Color.parseColor("#d6d6d6")); //다크모드 0;
                                else paint.setColor(Color.parseColor("#7a8599"));
                            }
                            if(q == 0) canvas.drawCircle(X + W/2 - 300, Y + H/2, 100, paint);
                            if(q == 1) canvas.drawCircle(X + W/2, Y + H/2, 100, paint);
                            if(q == 2) canvas.drawCircle(X + W/2 + 300, Y + H/2, 100, paint);
                            if(count == 0) canvas.drawCircle(X + W/2 - 300, Y + H/2, 100, stroke);
                            if(count == 1) canvas.drawCircle(X + W/2, Y + H/2, 100, stroke);
                            if(count == 2) canvas.drawCircle(X + W/2 + 300, Y + H/2, 100, stroke);
                        }
                    }
                    else if(Object_Days == 4) {
                        for(int q = 0; q<Arr_Btn_Paint_Progress.length; q++) {
                            if(Integer.parseInt(Arr_Btn_Paint_Progress[q]) == 1) paint.setColor(Integer.parseInt(Arr_Habit_Color[q]));
                            if(Integer.parseInt(Arr_Btn_Paint_Progress[q]) == 0) {
                                if(DB_darkmode == 0) paint.setColor(Color.parseColor("#d6d6d6")); //다크모드 0;
                                else paint.setColor(Color.parseColor("#7a8599"));
                            }
                            if(q == 0) canvas.drawCircle(X + W/2 - 400, Y + H/2, 100, paint);
                            if(q == 1) canvas.drawCircle(X + W/2 - 135, Y + H/2, 100, paint);
                            if(q == 2) canvas.drawCircle(X + W/2 + 135, Y + H/2, 100, paint);
                            if(q == 3) canvas.drawCircle(X + W/2 + 400, Y + H/2, 100, paint);
                            if(count == 0) canvas.drawCircle(X + W/2 - 400, Y + H/2, 100, stroke);
                            if(count == 1) canvas.drawCircle(X + W/2 - 135, Y + H/2, 100, stroke);
                            if(count == 2) canvas.drawCircle(X + W/2 + 135, Y + H/2, 100, stroke);
                            if(count == 3) canvas.drawCircle(X + W/2 + 400, Y + H/2, 100, stroke);
                        }
                    }
                }

                index_X = -450;
                if(Object_Days > 4 && Object_Days <15) { //목표일수 15일 이하
                    stroke.setStrokeWidth(12);
                    for(int b = 0; b<Object_Days; b++) { //목표일 수만큼 동그라미 반복해서 그림
                        if(Integer.parseInt(Arr_Btn_Paint_Progress[b]) == 1) paint.setColor(Integer.parseInt(Arr_Habit_Color[b]));
                        if(Integer.parseInt(Arr_Btn_Paint_Progress[b]) == 0) {
                            if(DB_darkmode == 0) paint.setColor(Color.parseColor("#d6d6d6")); //다크모드 0;
                            else paint.setColor(Color.parseColor("#7a8599"));
                        }
                        canvas.drawCircle(X + W/2 + index_X, Y + H/2 - 140 + index_Y , 55, paint);
                        if(count == b) canvas.drawCircle(X + W/2 + index_X, Y + H/2 - 140 + index_Y , 55, stroke);
                        index_X +=150;
                        if(b == 6) {index_X = -450; index_Y = 180;} //7개 이상부터 즉 8개부터 위치 변경
                    }
                }
                if(Object_Days >14) {
                    stroke.setStrokeWidth(10);
                    for(int b = 0; b<Object_Days; b++) {
                        if(Integer.parseInt(Arr_Btn_Paint_Progress[b]) == 1) paint.setColor(Integer.parseInt(Arr_Habit_Color[b]));
                        if(Integer.parseInt(Arr_Btn_Paint_Progress[b]) == 0) {
                            if(DB_darkmode == 0) paint.setColor(Color.parseColor("#d6d6d6")); //다크모드 0;
                            else paint.setColor(Color.parseColor("#7a8599"));
                        }

                        canvas.drawCircle(X + W/2 + index_X, Y + H/2 - 140 + index_Y , 35, paint);
                        if(count == b) canvas.drawCircle(X + W/2 + index_X, Y + H/2 - 140 + index_Y , 35, stroke);
                        index_X +=100;
                        if(b == 9) {index_X = -450; index_Y = 100;} //10개 이상부터 즉 11개부터 위치 변경
                        if(b == 19) {index_X = -450; index_Y = 200;}
                        if(b == 29) { index_X = -450; index_Y = 300; break;} //31일 이상부터 그리지마
                    }
                }
            }
        }

        private void init(Context context) {
            paint = new Paint();
            stroke = new Paint();
        }
    }
}
