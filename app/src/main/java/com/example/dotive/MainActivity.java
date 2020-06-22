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
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
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
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/*
* 20200622 수정
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
    Button button1;
    Button button2;

    LinearLayout linearLayout2;
    TextView textView1;
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //setContentView(new MyView(this));  (클래스 MyView를 바로 실행)

        Intent intent = getIntent();
        final String Habit_Name = intent.getStringExtra("Habit_Name"); //습관명
        final int Habit_Color = Integer.parseInt(intent.getStringExtra("Habit_Color")); //습관 색깔
        final int edit_Habit_Day_Num = Integer.parseInt(intent.getStringExtra("edit_Habit_Day_Num")); //습관 목표일 수

        Log.e("습관명", Habit_Name);
        Log.e("습관컬러", Integer.toString(Habit_Color));
        Log.e("습관 목표일 수", Integer.toString(edit_Habit_Day_Num));

        //객체 인스턴스 초기화.

        //xml inflater
        View view_main = (View) getLayoutInflater().inflate(R.layout.activity_main, null);

        scrollView = (ScrollView) view_main.findViewById(R.id.scrollView);
        scrollView.setBackgroundColor(Color.parseColor("#FFF7CD")); //뒷 배경 설정

        frameLayout = (FrameLayout) view_main.findViewById(R.id.frameLayout);

        linearLayout = (LinearLayout) view_main.findViewById(R.id.linearLayout);

        button1 = new Button(this);
        button2 = new Button(this);

        //버튼위에 제목 (물 1L 마시기 등)
        linearLayout2 = (LinearLayout) view_main.findViewById(R.id.linearLayout2);

        textView1 = new TextView(this);
        textView2 = new TextView(this);

        Painting_Circle painting_circle = new Painting_Circle(this); //CustomView.java 파일을 불러와 실행

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

        linearLayout.addView(button1);
        linearLayout.addView(button2);

        linearLayout2.addView(textView1);
        linearLayout2.addView(textView2);

        //java 코드로 폰트 설정 (xml 에서 fontFamily)
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/katuri.ttf");

        //버튼 명, 버튼 id 설정

        button1.setText(Habit_Name);
        button1.setTypeface(typeface);

        button2.setText("다이어트");
        button2.setTypeface(typeface);

        textView1.setText(Habit_Name);
        textView1.setTypeface(typeface);

        textView2.setText(Habit_Name);
        textView2.setTypeface(typeface);

        //이렇게 안하면 픽셀로 값이 저장되기에 dp로 계산하는 것. value에서 원하는 숫자로 고치면 됨.
        //dp 값으로 가져오기
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 340, getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 160, getResources().getDisplayMetrics());

        int textView_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
        int textView_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

        int btn_margin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getResources().getDisplayMetrics());
        int btn_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        int btn_margin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());

        //textView만 따로 마진값을 준다.
        int textView_btn_margin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        int textView_btn_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        int textView_btn_margin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, getResources().getDisplayMetrics());

        //버튼들의 너비 높이 정함.
        button1.setWidth(width);
        button1.setHeight(height);

        button2.setWidth(width);
        button2.setHeight(height);

        textView1.setWidth(textView_width);
        textView1.setHeight(textView_height);
        textView1.setGravity(Gravity.CENTER);

        textView2.setWidth(textView_width);
        textView2.setHeight(textView_height);
        textView2.setGravity(Gravity.CENTER);

        //버튼 백그라운드 설정 = radius 값 설정을 위해 필요 (버튼 둥글게 하기 위해 xml으로 새로 짜야함)
        button1.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radius));
        button2.setBackgroundDrawable(ContextCompat.getDrawable(this, R.drawable.radius));

        textView1.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_custom_css)); //StateListDrawable 속성이지만
        textView2.setBackground(ContextCompat.getDrawable(this, R.drawable.textview_custom_css));

        //textview 제목 컬러 지정
        GradientDrawable bgShape = (GradientDrawable) textView1.getBackground().getCurrent(); //GradientDrawable 그대로 하면 오류남 마지막에 .getCurrent() 중요
        bgShape.setColor(Habit_Color);

        bgShape = (GradientDrawable) textView2.getBackground().getCurrent();
        bgShape.setColor(Habit_Color);


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
        button1.setLayoutParams(Button_LinearParams);
        button2.setLayoutParams(Button_LinearParams);

        //margin 값 dp로 설정해주는중. textview 적용 (물 1L 마시기)
        textView_LinearParams.leftMargin = textView_btn_margin_left; //size
        textView_LinearParams.bottomMargin = textView_btn_margin_bottom;
        textView_LinearParams.topMargin = textView_btn_margin_top;

        textView1.setLayoutParams(textView_LinearParams);
        textView2.setLayoutParams(textView_LinearParams);

        //클릭 이벤트리스너
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                intent.putExtra("Final_Target", textView1.getText()); //버튼의 이름을 HabitActivity에 전달.
                startActivity(intent);
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

    //버튼 위에 습관일수 배열로 그리기
    //CustomView 에 버튼 좌표를 구할 수 없어 여기서 그린다.

    public class Painting_Circle extends View {
        private Paint paint;

        public Painting_Circle(Context context) {
            super(context);

            init(context);
        }

        public Painting_Circle(Context context, @Nullable AttributeSet attrs) {
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
            Y = button1.getY();
            X = button1.getX();
            //원을 그린다.

            //1일 ~ 4일 반지름
            //5일 ~ 14일 반지름 50
            //15일 ~ 무한정 반지름
            paint.setColor(Color.RED);
            canvas.drawCircle(X + 150, Y + 200, 50, paint);
            paint.setColor(Color.RED);
            canvas.drawCircle(X + 300, Y + 200, 50, paint);
            /*
            Y = button2.getY();
            X = button2.getX();
            paint.setColor(Color.RED);
            canvas.drawCircle(X,Y,50,paint);
            */
        }

        private void init(Context context) {
            paint = new Paint();
        }
    }
}
