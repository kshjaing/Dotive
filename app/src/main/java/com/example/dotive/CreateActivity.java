package com.example.dotive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class CreateActivity extends Activity {

    LinearLayout llhor;
    LinearLayout llleft;
    LinearLayout llcenter;
    LinearLayout llright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //선형 레이아웃 생성(가장 밑에 수평, 그 위에 수직 레이아웃3개 배치)
        llhor = new LinearLayout(this);
        llleft = new LinearLayout(this);
        llcenter = new LinearLayout(this);
        llright = new LinearLayout(this);
        llhor.setOrientation(LinearLayout.HORIZONTAL);
        llcenter.setOrientation(LinearLayout.VERTICAL);
        llleft.setBackgroundColor(Color.parseColor("#FFF7CD"));
        llcenter.setBackgroundColor(Color.parseColor("#FFF7CD"));
        llright.setBackgroundColor(Color.parseColor("#FFF7CD"));



        //양쪽 레이아웃과 가운데 레이아웃 비율을 1:10으로 설정
        final LinearLayout.LayoutParams linearParamsVerticalSide = new LinearLayout.LayoutParams(
                1, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        final LinearLayout.LayoutParams linearParamsVerticalCenter = new LinearLayout.LayoutParams(
                1, LinearLayout.LayoutParams.MATCH_PARENT, 10);
        final LinearLayout.LayoutParams linearParamsHorizontal = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final LinearLayout.LayoutParams button = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        llhor.setLayoutParams(linearParamsHorizontal);
        llleft.setLayoutParams(linearParamsVerticalSide);
        llcenter.setLayoutParams(linearParamsVerticalCenter);
        llright.setLayoutParams(linearParamsVerticalSide);


        llhor.addView(llleft);
        llhor.addView(llcenter);
        llhor.addView(llright);

        //습관 추가 버튼
        Button btnCreate = new Button(this);
        btnCreate.setText("추가");
        btnCreate.setLayoutParams(button);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                int totalHabit = MainActivity.totalHabit + 1;
                MainActivity.isCreatePressed = true;
                intent.putExtra("totalHabit+", totalHabit);
                startActivity(intent);
            }
        });

        //습관 입력 텍스트
        EditText editText = new EditText(this);
        editText.setTag("inputHabit");
        editText.setBackgroundColor(Color.WHITE);

        llcenter.setGravity(Gravity.CENTER);
        llcenter.addView(editText);
        llcenter.addView(btnCreate);
        setContentView(llhor);
    }
}

