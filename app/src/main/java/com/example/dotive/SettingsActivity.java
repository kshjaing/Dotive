package com.example.dotive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import static com.example.dotive.MainActivity.activityMoveCount;
import static com.example.dotive.MainActivity.darkmode;

public class SettingsActivity extends Activity {

    ScrollView sv;
    LinearLayout llhor;
    LinearLayout llleft;
    LinearLayout llcenter;
    LinearLayout llright;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //스크롤뷰 생성
        sv = new ScrollView(this);
        if (!darkmode) {
            sv.setBackgroundColor(Color.parseColor("#FFF7CD"));
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#1C1C1F"));
        }
        setContentView(sv);


        //선형 레이아웃 생성(가장 밑에 수평, 그 위에 수직 레이아웃3개 배치)
        llhor = new LinearLayout(this);
        llleft = new LinearLayout(this);
        llcenter = new LinearLayout(this);
        llright = new LinearLayout(this);
        llhor.setOrientation(LinearLayout.HORIZONTAL);
        llcenter.setOrientation(LinearLayout.VERTICAL);


        /* 색 지정으로 선형 레이아웃 비율 확인(테스트용)
        llcenter.setBackgroundColor(Color.BLUE);
        llleft.setBackgroundColor(Color.RED);
        llright.setBackgroundColor(Color.GREEN);*/


        //양쪽 레이아웃과 가운데 레이아웃 비율을 1:10으로 설정
        final LinearLayout.LayoutParams linearParamsVerticalSide = new LinearLayout.LayoutParams(
                1, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        final LinearLayout.LayoutParams linearParamsVerticalCenter = new LinearLayout.LayoutParams(
                1, LinearLayout.LayoutParams.MATCH_PARENT, 10);
        final LinearLayout.LayoutParams linearParamsHorizontal = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        final LinearLayout.LayoutParams buttons = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);


        llhor.setLayoutParams(linearParamsHorizontal);
        llleft.setLayoutParams(linearParamsVerticalSide);
        llcenter.setLayoutParams(linearParamsVerticalCenter);
        llright.setLayoutParams(linearParamsVerticalSide);

        sv.addView(llhor);
        llhor.addView(llleft);
        llhor.addView(llcenter);
        llhor.addView(llright);


        final Button darkmodeBtn = new Button(this);
        darkmodeBtn.setText("라이트모드");
        darkmodeBtn.setLayoutParams(buttons);
        darkmodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!darkmode) {
                    darkmode = true;
                    darkmodeBtn.setText("다크모드");
                    sv.setBackgroundColor(Color.parseColor("#1C1C1F"));
                }
                else {
                    darkmode = false;
                    darkmodeBtn.setText("라이트모드");
                    sv.setBackgroundColor(Color.parseColor("#FFF7CD"));
                }
            }
        });

        llcenter.addView(darkmodeBtn);
    }
}
