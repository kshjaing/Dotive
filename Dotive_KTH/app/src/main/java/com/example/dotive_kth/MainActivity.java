package com.example.dotive_kth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/* 최종목표가 표시될 곳
* 편집 버튼 (or + 버튼)을 눌러 최종목표를 만들어 이 페이지에 표시된다.
* 만들어진 최종 목표에서 여러가지 습관이 있다. 그 습관 페이지로 intent 한다. 습관 페이지 = HabitActivity
* 습관 페이지에서 예를들어 팔굽혀펴기 5회 있다면 그걸 누르면 또 intent 한다. 결과 페이지 = ResultActivity*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //최종목표 버튼 --> 다이어트 , 공부

        final Button btn1 = (Button)findViewById(R.id.btn1); //다이어트
        final Button btn2 = (Button)findViewById(R.id.btn2); //공부
        final Button btn3 = (Button)findViewById(R.id.btn3); //공부

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
        });
    }
}
