package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class CreateActivity extends AppCompatActivity {

    Button btn_finish; //앱 종료
    private Button btn_Color_Picker; //색 선택
    private ConstraintLayout layout;
    TextView View_Color_Pick; //색상 값 TextView 에 쓸 예정

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //객체 인스턴스 초기화
        btn_finish = (Button) findViewById(R.id.btn_finish);
        btn_Color_Picker = (Button) findViewById(R.id.btn_Color_Picker);
        View_Color_Pick = (TextView) findViewById(R.id.View_Color_Pick);
        layout = (ConstraintLayout) findViewById(R.id.layout);

        //버튼 클릭리스너

        btn_Color_Picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //앱 종료
            }
        });
    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this); //ColorPicker 객체 생성

        ArrayList<String> colors = new ArrayList<>(); //배열에 원하는 색 담아둔다.

        colors.add("#ffab91");
        colors.add("#F48FB1");
        colors.add("#ce93d8");
        colors.add("#b39ddb");
        colors.add("#9fa8da");
        colors.add("#90caf9");
        colors.add("#81d4fa");
        colors.add("#80deea");
        colors.add("#80cbc4");
        colors.add("#c5e1a5");
        colors.add("#e6ee9c");
        colors.add("#fff59d");
        colors.add("#ffe082");
        colors.add("#ffcc80");
        colors.add("#bcaaa4");

        //setColumns 00열 배치
        //setRoundColorButton 둥글게 이값 없으면 네모
        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        //View_Color_Pick.setText(color);
                        layout.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                }).show();
    }
}