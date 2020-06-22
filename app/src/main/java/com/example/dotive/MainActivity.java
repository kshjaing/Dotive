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

/* 최종목표가 표시될 곳
* 편집 버튼 (or + 버튼)을 눌러 최종목표를 만들어 이 페이지에 표시된다.
* 만들어진 최종 목표에서 여러가지 습관이 있다. 그 습관 페이지로 intent 한다. 습관 페이지 = HabitActivity
* 습관 페이지에서 예를들어 팔굽혀펴기 5회 있다면 그걸 누르면 또 intent 한다. 결과 페이지 = ResultActivity*/

//xml 에서는 디자인 모양을 대충 만들어보고 그대로 코드로 동적 레이아웃을 구성함.
//그렇게 하는 이유는, paint로 그린 그림과 xml상의 각종 디자인들이 동시에 setContentView를 할 수 없음.
//그래서 코드로 최상단 동적 레이아웃을 setContentView에 넣었고
//FrameLayout의 기능을 이용하여 두가지를 겹쳐서 보이게 함. (버튼 위에 paint 그림)
//최상단은 ScrollView이기에 그 안에 들어있는 framelayout은 스크롤 되므로 결국 버튼과 paint가 동시에 스크롤 됨.

public class MainActivity extends AppCompatActivity {

    //여기에 쓰이는 동적 컨트롤들 객체
    ScrollView scrollView;
    LinearLayout linearLayout;
    Button button1;
    Button button2;
    /*Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;*/
    FrameLayout frameLayout;
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

        Log.e("습관명",Habit_Name);
        Log.e("습관컬러",Integer.toString(Habit_Color));
        Log.e("습관 목표일 수",Integer.toString(edit_Habit_Day_Num));

       //객체 인스턴스 초기화.


        scrollView = new ScrollView(this);
        scrollView.setBackgroundColor(Color.parseColor("#FFF7CD")); //뒷 배경 설정

        frameLayout = new FrameLayout(this);

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        button1 = new Button(this);
        button2 = new Button(this);
        /*button2 = new Button(this);
        button3 = new Button(this);
        button4 = new Button(this);
        button5 = new Button(this);
        button6 = new Button(this);*/


        //버튼위에 제목 (물 1L 마시기 등)
        linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);

        textView1 = new TextView(this);
        textView2 = new TextView(this);

        CustomView123 view = new CustomView123(this); //CustomView.java 파일을 불러와 실행

        //집합처럼 View를 순서대로 추가중. 마지막에 버튼들.
        //frameLayout에 2개를 추가했다는 점. (이 부분이 버튼과, Paint 를 동시에 보이게 해줌.)

        scrollView.addView(frameLayout);
        frameLayout.addView(linearLayout); //button을 위해
        frameLayout.addView(linearLayout2); //textview를 위해
        frameLayout.addView(view); //버튼 위의 원 배열을 위해


        //main 액션 바 만들기
        //main_actionbar.xml 가져옴
        View view1 = (View) getLayoutInflater().inflate(R.layout.main_actionbar, null);

        //위 액션바 xml 의 레이아웃 객체 인스턴스 초기화
        ConstraintLayout constraintLayout = (ConstraintLayout) view1.findViewById(R.id.Actionbar_Layout);

        //toolbar 위젯 객체 인스턴스 초기화
        androidx.appcompat.widget.Toolbar toolbar = (Toolbar) view1.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar); //매니페스트에서 액션바 없앴으니 커스텀으로 추가하는 과정
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true); //커스텀 액션바 보이게
        actionBar.setDisplayShowTitleEnabled(false); //기본 제목 false = > 안보이게 함. (제목 삭제)
        actionBar.setDisplayHomeAsUpEnabled(true); //이 값은 자동으로 뒤로가기 버튼 생성
        linearLayout.addView(view1);

        linearLayout.addView(button1);
        linearLayout.addView(button2);
        /*linearLayout.addView(button2);
        linearLayout.addView(button3);
        linearLayout.addView(button4);
        linearLayout.addView(button5);
        linearLayout.addView(button6);*/

        linearLayout2.addView(textView1);
        linearLayout2.addView(textView2);

        //java 코드로 폰트 설정 (xml 에서 fontFamily)
        Typeface typeface = Typeface.createFromAsset(getAssets(),"font/katuri.ttf");
        //버튼 명, 버튼 id 설정

        //button1.setText("공부");
        button1.setText(Habit_Name);
        button1.setId(R.id.btn1);
        button1.setTypeface(typeface);

        button2.setText("다이어트");
        button2.setId(R.id.btn2);
        button2.setTypeface(typeface);

        /*button2.setText("다이어트");
        button2.setId(R.id.btn2);
        button2.setTypeface(typeface);

        button3.setText("3");
        button3.setId(R.id.btn3);
        button3.setTypeface(typeface);

        button4.setText("4");
        button4.setId(R.id.btn4);
        button4.setTypeface(typeface);

        button5.setText("5");
        button5.setId(R.id.btn5);
        button5.setTypeface(typeface);

        button6.setText("6");
        button6.setId(R.id.btn6);
        button6.setTypeface(typeface);*/

        textView1.setText(Habit_Name);
        textView1.setId(R.id.Habit_Title1);
        textView1.setTypeface(typeface);

        textView2.setText(Habit_Name);
        textView2.setId(R.id.Habit_Title2);
        textView2.setTypeface(typeface);

        //이렇게 안하면 픽셀로 값이 저장되기에 dp로 계산하는 것. value에서 원하는 숫자로 고치면 됨.
        //dp 값으로 가져오기
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,340,getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,160,getResources().getDisplayMetrics());

        int textView_width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,getResources().getDisplayMetrics());
        int textView_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics());

        int btn_margin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,35,getResources().getDisplayMetrics());
        int btn_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics());
        //2020_06_06 (마진 top을 주어 버튼을 좀 아래로 내리고 그 위에 또 버튼을 만들어 제목을 부여한다. (ex: 물 1L 마시기))
        int btn_margin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,getResources().getDisplayMetrics());

        //textView만 따로 마진값을 준다.
        int textView_btn_margin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,130,getResources().getDisplayMetrics());
        int textView_btn_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,130,getResources().getDisplayMetrics());
        int textView_btn_margin_top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,70,getResources().getDisplayMetrics());

        //textView padding 값
        int textView1_padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,16,getResources().getDisplayMetrics());

        //dp 값으로 가져오기 또 다른 방법

        //DisplayMetrics dm = getResources().getDisplayMetrics();
        //int size = Math.round(50 * dm.density);

        //버튼들의 너비 높이 정함.
        button1.setWidth(width);
        button1.setHeight(height);

        button2.setWidth(width);
        button2.setHeight(height);

        /*button2.setWidth(width);
        button2.setHeight(height);

        button3.setWidth(width);
        button3.setHeight(height);

        button4.setWidth(width);
        button4.setHeight(height);

        button5.setWidth(width);
        button5.setHeight(height);

        button6.setWidth(width);
        button6.setHeight(height);*/

        textView1.setWidth(textView_width);
        textView1.setHeight(textView_height);
        //Gravity 속성 가운데 정렬 (버튼과 다르게 직접 설정해줘야 글씨가 가운데로 온다.)
        textView1.setGravity(Gravity.CENTER);

        textView2.setWidth(textView_width);
        textView2.setHeight(textView_height);
        textView2.setGravity(Gravity.CENTER);

        //버튼 백그라운드 설정 = radius 값 설정을 위해 필요 (버튼 둥글게 하기 위해 xml으로 새로 짜야함)
        button1.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button2.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        /*button2.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button3.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button4.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button5.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button6.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));*/

        //extView1.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.textview_custom_css));
        textView1.setBackground(ContextCompat.getDrawable(this,R.drawable.textview_custom_css)); //StateListDrawable 속성이지만
        /*StateListDrawable bgShape = (StateListDrawable)textView1.getBackground();
        bgShape.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(Color.RED));
        textView1.setBackground(bgShape);*/
        textView2.setBackground(ContextCompat.getDrawable(this,R.drawable.textview_custom_css));

        GradientDrawable bgShape = (GradientDrawable)textView1.getBackground().getCurrent(); //GradientDrawable 그대로 하면 오류남 마지막에 .getCurrent() 중요
        bgShape.setColor(Habit_Color);

        bgShape = (GradientDrawable)textView2.getBackground().getCurrent();
        bgShape.setColor(Habit_Color);

        //마진은 설정값을 다르게 할때마다 부모 값을 이렇게 정해야한다.

        //마진 설정을 위해 (마진을 Layout에서 부모값을 정하고 그곳에 마진을 아까 dp 계산한 int값으로 지정.)
        LinearLayout.LayoutParams Button_LinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //마진 설정 2 (textview 설정 (물 1L 마시기)
        LinearLayout.LayoutParams textView_LinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //마진값 dp로 설정해주는중. 버튼에 적용
        Button_LinearParams.leftMargin = btn_margin_left; //size
        Button_LinearParams.bottomMargin = btn_margin_bottom;
        Button_LinearParams.topMargin = btn_margin_top;

        //각각의 버튼들에게 위에 계산된 마진을 추가해줌.
        button1.setLayoutParams(Button_LinearParams);
        button2.setLayoutParams(Button_LinearParams);
        //button1.getResources().getDimension(size);
        /*button2.setLayoutParams(Button_LinearParams);
        button3.setLayoutParams(Button_LinearParams);
        button4.setLayoutParams(Button_LinearParams);
        button5.setLayoutParams(Button_LinearParams);
        button6.setLayoutParams(Button_LinearParams);*/

        //마진값 dp로 설정해주는중. textview 적용 (물 1L 마시기)
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
                intent.putExtra("Final_Target",textView1.getText()); //버튼의 이름을 HabitActivity에 전달.
                startActivity(intent);
            }
        });

        //제일 위에 있는것을 뿌려야 차례대로 자식view들이 보인다. (중간꺼든 다른거 넣으면 페이탈 에러)
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
    //CustomView에 버튼 좌표를 구할 수 없어 여기서 그린다.

    public class CustomView123 extends View {
        private Paint paint;

        public CustomView123(Context context) {
            super(context);

            init(context);
        }

        public CustomView123(Context context, @Nullable AttributeSet attrs) {
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
            //5일 ~ 14일 반지름 50
            //15일 ~ 무한정 반지름
            paint.setColor(Color.RED);
            canvas.drawCircle(X+150,Y+200,50,paint);
            paint.setColor(Color.RED);
            canvas.drawCircle(X+300,Y+200,50,paint);
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
