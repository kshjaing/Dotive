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

        //선형 레이아웃 생성
        llhor = new LinearLayout(this);
        llleft = new LinearLayout(this);
        llcenter = new LinearLayout(this);
        llright = new LinearLayout(this);
        llhor.setBackgroundColor(Color.parseColor("#FFF7CD"));;
        llhor.setOrientation(LinearLayout.HORIZONTAL);
        llcenter.setOrientation(LinearLayout.VERTICAL);
        llcenter.setBackgroundColor(Color.BLUE);

        llhor.addView(llleft);
        llhor.addView(llcenter);
        llhor.addView(llright);
        final LinearLayout.LayoutParams linearParamsVertical1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.15f);
        final LinearLayout.LayoutParams linearParamsVertical2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 0.7f);
        final LinearLayout.LayoutParams linearParamsHorizontal = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        llhor.setLayoutParams(linearParamsHorizontal);
        llleft.setLayoutParams(linearParamsVertical1);
        llcenter.setLayoutParams(linearParamsVertical2);
        llright.setLayoutParams(linearParamsVertical1);

        //습관 추가 버튼
        Button btnCreate = new Button(this);
        btnCreate.setText("추가");
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                int totalHabit = ((MainActivity)MainActivity.context_main).totalHabit + 1;
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

