package com.example.dotive_kth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;

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
    ScrollView scrollView1;
    FrameLayout frameLayout;
    LinearLayout linearLayout;
    ScrollView scrollView;
    LinearLayout linearLayout2;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //setContentView(new MyView(this));  (클래스 MyView를 바로 실행)

        //객체 인스턴스 초기화.
        CustomView view = new CustomView(this); //CustomView.java 파일을 불러와 실행
        //setContentView(view); (맨 아래에서 스크롤 뷰를 집어넣음.)

        scrollView1 = new ScrollView(this);
        frameLayout = new FrameLayout(this);
        //FrameLayout.LayoutParams Frameparams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
        //FrameLayout.LayoutParams.MATCH_PARENT);
        //frameLayout.setLayoutParams(Frameparams);

        linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        scrollView = new ScrollView(this);
        //ScrollView.LayoutParams params = new ScrollView.LayoutParams(ScrollView.LayoutParams.MATCH_PARENT,
        //ScrollView.LayoutParams.MATCH_PARENT);
        //params.width = 500;

        linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);

        button1 = new Button(this);
        button2 = new Button(this);
        button3 = new Button(this);
        button4 = new Button(this);
        button5 = new Button(this);
        button6 = new Button(this);

        //집합처럼 View를 순서대로 추가중. 마지막에 버튼들.
        //frameLayout에 2개를 추가했다는 점. (이 부분이 버튼과, Paint 를 동시에 보이게 해줌.)
        scrollView1.addView(frameLayout);
        frameLayout.addView(linearLayout);
        frameLayout.addView(view); //이 view는 CustomView.java에서 가져옴.
        linearLayout.addView(scrollView);
        scrollView.addView(linearLayout2);

        linearLayout2.addView(button1);
        linearLayout2.addView(button2);
        linearLayout2.addView(button3);
        linearLayout2.addView(button4);
        linearLayout2.addView(button5);
        linearLayout2.addView(button6);


        button1.setText("공부");
        button1.setId(R.id.btn1);

        button2.setText("다이어트");
        button2.setId(R.id.btn2);

        button3.setText("3");
        button3.setId(R.id.btn3);

        button4.setText("4");
        button4.setId(R.id.btn4);

        button5.setText("5");
        button5.setId(R.id.btn5);

        button6.setText("6");
        button6.setId(R.id.btn6);

        //이렇게 안하면 픽셀로 값이 저장되기에 dp로 계산하는 것. value에서 원하는 숫자로 고치면 됨.
        //dp 값으로 가져오기
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,300,getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,250,getResources().getDisplayMetrics());
        int btn_margin_left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics());
        int btn_margin_bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,getResources().getDisplayMetrics());

        //dp 값으로 가져오기 또 다른 방법

        //DisplayMetrics dm = getResources().getDisplayMetrics();
        //int size = Math.round(50 * dm.density);

        //버튼들의 너비 높이 정함.
        button1.setWidth(width);
        button1.setHeight(height);

        button2.setWidth(width);
        button2.setHeight(height);

        button3.setWidth(width);
        button3.setHeight(height);

        button4.setWidth(width);
        button4.setHeight(height);

        button5.setWidth(width);
        button5.setHeight(height);

        button6.setWidth(width);
        button6.setHeight(height);

        //버튼 백그라운드 설정 = radius 값 설정을 위해 필요 (버튼 둥글게 하기 위해 xml으로 새로 짜야함)
        button1.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button2.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button3.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button4.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button5.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));
        button6.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.radius));


        //마진 설정을 위해 (마진을 Layout에서 부모값을 정하고 그곳에 마진을 아까 dp 계산한 int값으로 지정.)
        LinearLayout.LayoutParams LinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearParams.leftMargin = btn_margin_left; //size
        LinearParams.bottomMargin = btn_margin_bottom;

        //각각의 버튼들에게 위에 계산된 마진을 추가해줌.
        button1.setLayoutParams(LinearParams);
        //button1.getResources().getDimension(size);
        button2.setLayoutParams(LinearParams);
        button3.setLayoutParams(LinearParams);
        button4.setLayoutParams(LinearParams);
        button5.setLayoutParams(LinearParams);
        button6.setLayoutParams(LinearParams);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                intent.putExtra("Final_Target",button1.getText()); //버튼의 이름을 HabitActivity에 전달.
                startActivity(intent);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                intent.putExtra("Final_Target",button2.getText()); //버튼의 이름을 HabitActivity에 전달.
                startActivity(intent);
            }
        });

        //제일 위에 있는 스크롤뷰를 뿌려야 차례대로 자식view들이 보인다. (중간꺼든 다른거 넣으면 페이탈 에러)
        setContentView(scrollView1);
        //setContentView(R.layout.activity_main);

        /*//최종목표 버튼 --> 다이어트 , 공부

        final Button btn1 = (Button)findViewById(R.id.btn1); //다이어트
        final Button btn2 = (Button)findViewById(R.id.btn2); //공부

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                intent.putExtra("Final_Target",btn1.getText()); //버튼의 이름을 HabitActivity에 전달.
                startActivity(intent);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HabitActivity.class);
                intent.putExtra("Final_Target",btn2.getText()); //버튼의 이름을 HabitActivity에 전달.
                startActivity(intent);
            }
        });*/
    }
}
