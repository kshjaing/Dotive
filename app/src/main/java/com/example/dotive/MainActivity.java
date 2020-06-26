package com.example.dotive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.content.Context;
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
import android.media.JetPlayer;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;

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

    LinearLayout linearLayout;
    Button Btn_Habit_Add;
    Button[] Arr_Btn_Habit;
    Button button1;
    Button button2;

    LinearLayout linearLayout2;
    TextView[] Arr_TextView_Habit_Name;
    TextView textView1;
    TextView textView2;

    LinearLayout linearLayout3;  //이 값은 버튼 위에 paint 대신 버튼을 뿌린다.

    //습관 개수에따라 버튼 증가
    static int TotalHabit = 1;

    //습관 테이블 개수 (0이면 값이 존재하지 않음)
    int Habits_Table_Count = 0;

    //CreateActivity 에서 intent 값
    String Habit_Name = ""; //습관명
    //int i_Habit_Color = 0; //습관 색깔
    String Habit_Color = ""; //습관 색깔
    //int edit_Habit_Day_Num = 0; //습관 목표일 수
    String edit_Habit_Day_Num = ""; //습관 목표일 수
    String Habit_Progress = "";

    //DB에서 받아온 값
    String[] Arr_Habit_Name = {};//습관명
    String[] Arr_Habit_Color = {};//습관 색깔
    String[] Arr_edit_Habit_Day_Num = {}; //습관 목표일 수
    String[] Arr_Habit_Progress = {}; //습관 진행도

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //setContentView(new MyView(this));  (클래스 MyView를 바로 실행)


        //Habits 테이블에 습관이 존재하지 않다면 CreateActivity.java로 돌아간다.

        QUERY_Habits();

        Log.e("MainActivity.java", "Habits_Table_Count : " + Habits_Table_Count);
        //final Habits_Table_Count = 0;
        //습관 하나 이상 존재함
        //존재하면 DB 에서 값을 받아온다.
        if (Habits_Table_Count > 1) { //2부터 값이 1개 존재

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

            Log.e("MainActivity.java", "Habit_Name : " + Habit_Name);
            Log.e("MainActivity.java", "Habit_Color : " + Habit_Color);
            Log.e("MainActivity.java", "edit_Habit_Day_Num : " + edit_Habit_Day_Num);
            Log.e("MainActivity.java", "Habit_Progress 습관 진행도: " + Habit_Progress);

            Arr_Habit_Name = Habit_Name.split("_");
            Arr_Habit_Color = Habit_Color.split("_");
            Arr_edit_Habit_Day_Num = edit_Habit_Day_Num.split("_");
            Arr_Habit_Progress = Habit_Progress.split("_");

        } else {
            Intent intent2 = new Intent(MainActivity.this, CreateActivity.class);
            startActivity(intent2);
        }


        //객체 인스턴스 초기화.

        //xml inflater
        View view_main = (View) getLayoutInflater().inflate(R.layout.activity_main, null);

        scrollView = (ScrollView) view_main.findViewById(R.id.scrollView);
        scrollView.setBackgroundColor(Color.parseColor("#FFF7CD")); //뒷 배경 설정

        frameLayout = (FrameLayout) view_main.findViewById(R.id.frameLayout);

        linearLayout = (LinearLayout) view_main.findViewById(R.id.linearLayout);

        //버튼위에 제목 (물 1L 마시기 등)
        linearLayout2 = (LinearLayout) view_main.findViewById(R.id.linearLayout2);

        /*button1 = new Button(this);
        button2 = new Button(this);
        textView1 = new TextView(this);
        textView2 = new TextView(this);*/

        Painting_Circle1 painting_circle = new Painting_Circle1(this); //CustomView.java 파일을 불러와 실행

        //동적 view 생성
        frameLayout.addView(painting_circle); //버튼 위의 원 배열을 위해

        //main 액션 바 만들기

        //main_actionbar.xml inflater
        View actionview = (View) getLayoutInflater().inflate(R.layout.main_actionbar, null);

        //toolbar 위젯 객체 인스턴스 초기화
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) actionview.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //매니페스트에서 액션바 없앴으니 커스텀으로 추가하는 과정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스텀 액션바 보이게
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 false = > 안보이게 함. (제목 삭제)
        actionBar.setDisplayHomeAsUpEnabled(true); //이 값은 자동으로 뒤로가기 버튼 생성
        linearLayout.addView(actionview);


        /*linearLayout.addView(button1);
        linearLayout.addView(button2);

        linearLayout2.addView(textView1);
        linearLayout2.addView(textView2);*/

        //java 코드로 폰트 설정 (xml 에서 fontFamily)
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/katuri.ttf");

        //버튼 명, 버튼 id 설정

        /*button1.setText(Habit_Name);
        button1.setTypeface(typeface);

        button2.setText("다이어트");
        button2.setTypeface(typeface);

        textView1.setText(Habit_Name);
        textView1.setTypeface(typeface);

        textView2.setText(Habit_Name);
        textView2.setTypeface(typeface);*/

        //이렇게 안하면 픽셀로 값이 저장되기에 dp로 계산하는 것. value에서 원하는 숫자로 고치면 됨.
        //dp 값으로 가져오기
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, getResources().getDisplayMetrics());

        int textView_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 190, getResources().getDisplayMetrics());
        int textView_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        int btn_margin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 36, getResources().getDisplayMetrics());
        int btn_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int btn_margin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

        //textView만 따로 마진값을 준다.
        int textView_btn_margin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 115, getResources().getDisplayMetrics());
        int textView_btn_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());
        int textView_btn_margin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());

        //버튼들의 너비 높이 정함.
        /*button1.setWidth(width);
        button1.setHeight(height);

        button2.setWidth(width);
        button2.setHeight(height);

        textView1.setWidth(textView_width);
        textView1.setHeight(textView_height);
        textView1.setGravity(Gravity.CENTER);

        textView2.setWidth(textView_width);
        textView2.setHeight(textView_height);
        textView2.setGravity(Gravity.CENTER);*/

        //버튼 백그라운드 설정 = radius 값 설정을 위해 필요 (버튼 둥글게 하기 위해 xml으로 새로 짜야함)
        /*button1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radius));
        button2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radius));

        textView1.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_custom_css)); //StateListDrawable 속성이지만
        textView2.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_custom_css));*/

        //textview 제목 컬러 지정
        /*GradientDrawable bgShape = (GradientDrawable) textView1.getBackground().getCurrent(); //GradientDrawable 그대로 하면 오류남 마지막에 .getCurrent() 중요
        bgShape.setColor(Habit_Color);

        bgShape = (GradientDrawable) textView2.getBackground().getCurrent();
        bgShape.setColor(Habit_Color);*/


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

        //margin 값 dp로 설정해주는중. 버튼에 적용
        Button_LinearParams.leftMargin = btn_margin_left; //size
        Button_LinearParams.bottomMargin = btn_margin_bottom;
        Button_LinearParams.topMargin = btn_margin_top;

        //각각의 버튼들에게 위에 계산된 마진을 추가해줌.
        /*button1.setLayoutParams(Button_LinearParams);
        button2.setLayoutParams(Button_LinearParams);*/

        //margin 값 dp로 설정해주는중. textview 적용 (물 1L 마시기)
        textView_LinearParams.leftMargin = textView_btn_margin_left; //size
        textView_LinearParams.bottomMargin = textView_btn_margin_bottom;
        textView_LinearParams.topMargin = textView_btn_margin_top;

        /*textView1.setLayoutParams(textView_LinearParams);
        textView2.setLayoutParams(textView_LinearParams);*/

        int Table_Count = Habits_Table_Count;
        Arr_Btn_Habit = new Button[Table_Count - 1];
        Arr_TextView_Habit_Name = new TextView[Table_Count - 1];

        /*//페인트 버튼 생성 (버튼 위에 Paint 대신)
        //int Button_Count = Integer.parseInt(edit_Habit_Day_Num);
        //버튼 카운트는 버튼 생성 개수이며 for문을 돌려 총 습관 수만큼 반복하여
        //그 수마다 각각의 습관 목표일 수를 저장한다.


        //Button[] Arr_Paint_Button = {};
        //Button[] Arr_Paint_Button = new Button[Button_Count]; //페인트 버튼
        ArrayList<Button> list = new ArrayList<Button>();


        //test 버튼 위에 버튼 뿌리기
        for(int i = 0; i< Integer.parseInt(edit_Habit_Day_Num); i++) {
            Arr_Paint_Button[i] = new Button(this);
        }
        linearLayout3 = new LinearLayout(this);
        linearLayout3.setOrientation(LinearLayout.VERTICAL);
        frameLayout.addView(linearLayout3); //페인트 버튼을 뿌릴 공간

        for(int i = 0; i< Table_Count - 1; i++) {
            int Button_Count = Integer.parseInt(Arr_edit_Habit_Day_Num[i]); //각각의 습관의 목표일 수를 계속 변하여 저장.
            Button[] Arr_Paint_Button = list.toArray(new Button[i]);
            Arr_Paint_Button[i] = new Button(this);
            //Log.e("test","여기까진 도달");
            for(int j = 0; j< Button_Count; j++) {
                linearLayout3.addView(Arr_Paint_Button[0]); //Arr_Paint_Button[0] 이거 달라야 에러안떠
            }
        }
/*for(int j = 0; j < Button_Count; j++) { //목표일 수 만큼 페인트 버튼 생성
                Arr_Paint_Button[j] = new Button(this);
                linearLayout3.addView(Arr_Paint_Button[i]);
            }*/


        for (int i = 0; i < Table_Count - 1; i++) {

            //습관 버튼 인스턴스 초기화
            Arr_Btn_Habit[i] = new Button(this);
            linearLayout.addView(Arr_Btn_Habit[i]);

            //습관 제목 텍스트뷰 인스턴스 초기화
            Arr_TextView_Habit_Name[i] = new TextView(this);
            linearLayout2.addView(Arr_TextView_Habit_Name[i]);

            //습관 버튼 글꼴 및 Text
            Arr_Btn_Habit[i].setText(Arr_Habit_Name[i]);
            Arr_Btn_Habit[i].setTypeface(typeface);

            //습관 제목 텍스트뷰 글꼴 및 Text
            Arr_TextView_Habit_Name[i].setText(Arr_Habit_Name[i]);
            Arr_TextView_Habit_Name[i].setTypeface(typeface);

            //습관 버튼 Width, Height
            Arr_Btn_Habit[i].setWidth(width);
            Arr_Btn_Habit[i].setHeight(height);

            //습관 제목 텍스트뷰 Width, Height
            Arr_TextView_Habit_Name[i].setWidth(textView_width);
            Arr_TextView_Habit_Name[i].setHeight(textView_height);
            Arr_TextView_Habit_Name[i].setGravity(Gravity.CENTER);

            //습관 버튼 radius Drawable
            Arr_Btn_Habit[i].setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radius));

            //습관 제목 텍스트뷰 CSS
            Arr_TextView_Habit_Name[i].setBackground(ContextCompat.getDrawable(this, R.drawable.textview_custom_css));

            //습관 제목 텍스트뷰의 백그라운드 컬러 값 (CreateActivity에서 받아온 값)
            GradientDrawable bgShape = (GradientDrawable) Arr_TextView_Habit_Name[i].getBackground().getCurrent(); //GradientDrawable 그대로 하면 오류남 마지막에 .getCurrent() 중요
            bgShape.setColor(Integer.parseInt(Arr_Habit_Color[i]));

            //습관 버튼 절대 좌표
            Arr_Btn_Habit[i].setLayoutParams(Button_LinearParams);

            //습관 제목 텍스트뷰 절대 좌표
            Arr_TextView_Habit_Name[i].setLayoutParams(textView_LinearParams);

            final int a = i; //final 에러 방지 로컬변수로 선언해서 사용함.

            //습관 버튼 클릭 리스너
            Arr_Btn_Habit[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                    intent.putExtra("Final_Target", Arr_TextView_Habit_Name[a].getText()); //버튼의 이름을 HabitActivity에 전달.
                    startActivity(intent);
                }
            });
        }


        //습관 추가 버튼 (이 버튼은 동적 버튼 맨 아래에 위치해야 하므로 여기서 addView 했다.)
        //이 부분 버튼 가운데 정렬 필요 or 이미지 버튼이 아닌 css로 그리던지
        Btn_Habit_Add = new Button(this);
        Btn_Habit_Add.setBackgroundResource(R.drawable.habit_add_image);
        Btn_Habit_Add.setLayoutParams(Button_LinearParams);
        //Btn_Habit_Add.setWidth(30);
        //Btn_Habit_Add.setHeight(30);
        //Btn_Habit_Add.setGravity(Gravity.RIGHT);

        linearLayout.addView(Btn_Habit_Add);


        //습관 추가 버튼 클릭 리스너
        Btn_Habit_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(MainActivity.this, CreateActivity.class);
                startActivity(intent1);
            }
        });

        //제일 위에 있는것을 뿌려야 차례대로 child view 들이 보인다. (중간꺼든 다른거 넣으면 페이탈 에러)
        setContentView(scrollView);
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

    public void QUERY_Habits() {
        try {
            String uriString = "content://com.example.dotive/Habits";
            Uri uri = new Uri.Builder().build().parse(uriString);

            String[] columns = new String[]{"habitName", "habitColor", "objDays", "habitProgress"};
            Cursor cursor = getContentResolver().query(uri, columns, null, null, "id ASC");
            Log.e("MainActivity.java", "QUERY 결과 : " + cursor.getCount());

            int index = 1;

            while (cursor.moveToNext()) { //각 레코드 값을 출력함.
                String habitName = cursor.getString(cursor.getColumnIndex(columns[0]));
                String habitColor = cursor.getString(cursor.getColumnIndex(columns[1]));
                int objDays = cursor.getInt(cursor.getColumnIndex(columns[2]));
                String habitProgress = cursor.getString(cursor.getColumnIndex(columns[3]));

                Habit_Name += habitName + "_"; //습관명
                Habit_Color += habitColor + "_"; //습관 색깔
                edit_Habit_Day_Num += objDays + "_"; //습관 목표일 수
                Habit_Progress += habitProgress + "_"; //습관 진행도

                Log.e("MainActivity.java", "레코드 " + index + " :" + habitName + ", " + habitColor + ", " + objDays + ", " + habitProgress);
                index += 1;
            }
            Habits_Table_Count = index;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //버튼 위에 습관일수 배열로 그리기
    //CustomView 에 버튼 좌표를 구할 수 없어 여기서 그린다.

    public class Painting_Circle1 extends View {
        private Paint paint;

        public Painting_Circle1(Context context) {
            super(context);

            init(context);
        }

        public Painting_Circle1(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);

            init(context);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                Toast.makeText(super.getContext(), "모션 이벤트 다운" + event.getX() + "," + event.getY(), Toast.LENGTH_LONG).show();
            }
            return super.onTouchEvent(event);
        }

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
                //W/2 H/2 Center

                int Object_Days = Integer.parseInt(Arr_edit_Habit_Day_Num[i]);
                //원을 그린다.

                //X좌표 Y좌표 중앙 정렬
                float X_Center = X + W/2;
                float Y_Center = Y + H/2;
                int index_X = 0;
                int index_Y = 0;
                //1일 ~ 4일 반지름 100

                /*if(Object_Days < 5) {
                    paint.setColor(Color.RED);
                    for (int j = 0; j < Object_Days; j++) {
                        if(Object_Days == j+1)
                        {
                            for(int k = 0; k < Object_Days; k++)
                            {
                                //canvas.drawCircle(X_Center + index, Y_Center, 100, paint);
                                //index +=135;
                                canvas.drawCircle(X_Center, Y_Center, 100, paint);
                            }
                        }
                    }
                }*/

                if(Object_Days < 5) {
                    if(Object_Days == 1) {
                        //중앙
                        paint.setColor(Color.RED);
                        canvas.drawCircle(X + W/2, Y + H/2 , 100, paint);
                    }
                    else if(Object_Days == 2) {
                        //좌우 2개
                        paint.setColor(Color.YELLOW);
                        canvas.drawCircle(X + W/2 - 135, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 + 135, Y + H/2, 100, paint);
                    }
                    else if(Object_Days == 3) {
                        //3개
                        paint.setColor(Color.BLACK);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2, 100, paint);
                    }
                    else if(Object_Days == 4) {
                        //4개
                        paint.setColor(Color.BLUE);
                        canvas.drawCircle(X + W/2 - 400, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 - 135, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 + 135, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 + 400, Y + H/2, 100, paint);
                    }
                }

                index_X = -450;
                if(Object_Days > 4 && Object_Days <15) {
                    for(int a = 0; a<Object_Days; a++) {

                        canvas.drawCircle(X + W/2 + index_X, Y + H/2 - 140 + index_Y , 55, paint);
                        index_X +=150;
                        if(a == 6) {index_X = -450; index_Y = 180;} //7개 이상부터 즉 8개부터 위치 변경
                    }
                }
                else if(Object_Days >14) {
                    for(int a = 0; a<Object_Days; a++) {

                        canvas.drawCircle(X + W/2 + index_X, Y + H/2 - 140 + index_Y , 35, paint);
                        index_X +=100;
                        if(a == 9) {index_X = -450; index_Y = 100;} //10개 이상부터 즉 11개부터 위치 변경
                        if(a == 19) {index_X = -450; index_Y = 200;}
                        if(a == 29) {index_X = -450; index_Y = 300;}
                    }
                }

                //일단 수동으로 배치해봤고 자동화 할 것이다.
                /*if(Object_Days < 5) {
                    if(Object_Days == 1) {
                        //중앙
                        paint.setColor(Color.RED);
                        canvas.drawCircle(X + W/2, Y + H/2 , 100, paint);
                    }
                    else if(Object_Days == 2) {
                        //좌우 2개
                        paint.setColor(Color.YELLOW);
                        canvas.drawCircle(X + W/2 - 135, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 + 135, Y + H/2, 100, paint);
                    }
                    else if(Object_Days == 3) {
                        //3개
                        paint.setColor(Color.BLACK);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2, 100, paint);
                    }
                    else if(Object_Days == 4) {
                        //4개
                        paint.setColor(Color.BLUE);
                        canvas.drawCircle(X + W/2 - 400, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 - 135, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 + 135, Y + H/2, 100, paint);
                        canvas.drawCircle(X + W/2 + 400, Y + H/2, 100, paint);
                    }
                } //5일 ~ 14일 반지름 50*/
                //아래 주석은 14일치
                /*canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 - 140 , 55, paint);

                        canvas.drawCircle(X + W/2 - 450, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 + 40 , 55, paint);*/
                /*else if(Object_Days < 15) {
                    if(Object_Days == 5) {
                        paint.setColor(Color.GREEN);

                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        index = -450;
                        for(int a = 0; a<Object_Days; a++) {
                            canvas.drawCircle(X + W/2 + index, Y + H/2 - 140 , 55, paint);
                            index +=150;
                        }
                    }
                    else if(Object_Days == 6) {
                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                    }
                    else if(Object_Days == 7) {
                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 - 140 , 55, paint);
                    }
                    else if(Object_Days == 8) {
                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 - 140 , 55, paint);

                        canvas.drawCircle(X + W/2 - 450, Y + H/2 + 40 , 55, paint);
                    }
                    else if(Object_Days == 9) {
                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 - 140 , 55, paint);

                        canvas.drawCircle(X + W/2 - 450, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 + 40 , 55, paint);
                    }
                    else if(Object_Days == 10) {
                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 - 140 , 55, paint);

                        canvas.drawCircle(X + W/2 - 450, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 + 40 , 55, paint);
                    }
                    else if(Object_Days == 11) {
                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 - 140 , 55, paint);

                        canvas.drawCircle(X + W/2 - 450, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 + 40 , 55, paint);
                    }
                    else if(Object_Days == 12) {
                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 - 140 , 55, paint);

                        canvas.drawCircle(X + W/2 - 450, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 + 40 , 55, paint);
                    }
                    else if(Object_Days == 13) {
                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 - 140 , 55, paint);

                        canvas.drawCircle(X + W/2 - 450, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 + 40 , 55, paint);
                    }
                    else if (Object_Days == 14) {
                        canvas.drawCircle(X + W/2 - 450, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 - 140 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 - 140 , 55, paint);

                        canvas.drawCircle(X + W/2 - 450, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 300, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 - 150, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 + 150, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 + 300, Y + H/2 + 40 , 55, paint);
                        canvas.drawCircle(X + W/2 + 450, Y + H/2 + 40 , 55, paint);
                    }
                } //15일 이상 반지름 35
                else if(Object_Days > 14) {
                    //canvas Draw
                    //이 부분도 10일치 모양이 완성되면 그 패턴에 맞춰서 계속 증가만 시키면 된다.
                }*/
            }



            /*Y = Arr_Btn_Habit[0].getY();
            X = Arr_Btn_Habit[0].getX();
            //원을 그린다.

            //1일 ~ 4일 반지름 100
            //5일 ~ 14일 반지름 50
            //15일 ~ 무한정 반지름 35
            paint.setColor(Color.RED);
            canvas.drawCircle(X + 150, Y + 200, 35, paint);
            paint.setColor(Color.RED);
            canvas.drawCircle(X + 300, Y + 200, 50, paint);
            paint.setColor(Color.RED);
            canvas.drawCircle(X + 450, Y + 200, 100, paint);*/
        }

        private void init(Context context) {
            paint = new Paint();
        }
    }
}
