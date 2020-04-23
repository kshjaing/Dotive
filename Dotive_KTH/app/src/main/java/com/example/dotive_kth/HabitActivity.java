package com.example.dotive_kth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/* */
public class HabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        //최종목표 페이지에서 받아온 버튼 id (string)
        TextView textView1 = (TextView)findViewById(R.id.Main_Btn);

        Intent intent = getIntent(); //데이터 받아옴

        //최종목표 명 :
        //MainActivity 에서 선언한 key 값 그대로 써야함.
        String final_target = intent.getExtras().getString("Final_Target");
        textView1.setText(final_target);

    }
}
