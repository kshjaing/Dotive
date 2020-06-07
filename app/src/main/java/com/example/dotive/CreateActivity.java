package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;

public class CreateActivity extends AppCompatActivity {

    Button btn_finish; //앱 종료
    Button btn_OK; //확인 버튼
    private Button btn_Color_Picker; //색 선택
    private ConstraintLayout layout;
    TextView View_Color_Pick; //색상 값 TextView 에 쓸 예정, 습관 색 안정할 경우 경고
    EditText edit_Habit_Name; //습관명 (EditText)
    int int_Color; //컬러 값 int로 전달
    EditText edit_Habit_Day_Num; //습관 목표일 수 (ex: 30일 목표 공부하기, -> 30일 동그라미 배열 생성)
    TextView Warning_Habit_Name; //습관명 비어있을 경우 경고
    TextView Warning_Habit_Num; //습관 목표일 수 비어있을 경우 경고

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        //객체 인스턴스 초기화
        btn_finish = (Button) findViewById(R.id.btn_finish); //앱 종료
        btn_Color_Picker = (Button) findViewById(R.id.btn_Color_Picker); //컬러 선택 버튼
        View_Color_Pick = (TextView) findViewById(R.id.View_Color_Pick); //컬러 선택 값 (int)
        layout = (ConstraintLayout) findViewById(R.id.layout); //레이아웃
        btn_OK = (Button) findViewById(R.id.btn_OK); //확인 버튼
        edit_Habit_Name = (EditText) findViewById(R.id.edit_Habit_Name); //습관명 (EditText)
        edit_Habit_Day_Num = (EditText) findViewById(R.id.edit_Habit_Day_Num); //습관 목표일 수
        Warning_Habit_Name = (TextView) findViewById(R.id.Warning_Habit_Name); //습관명 비어있을 경우 경고
        Warning_Habit_Num = (TextView) findViewById(R.id.Warning_Habit_Num); //습관 목표일 수 비었을 경우 경고

        //버튼 클릭리스너

        //컬러 선택(습관앱 색상 정함)
        btn_Color_Picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openColorPicker();
            }
        });

        //앱 종료
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //앱 종료
            }
        });

        //확인 버튼
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_Habit_Name.getText().toString().length() > 0 && edit_Habit_Day_Num.getText().toString().length() > 0
                        && int_Color < 0) //습관명, 목표일 수, 습관 색이 전부 정해져야 넘어간다.
                {
                    Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                    intent.putExtra("Habit_Name",edit_Habit_Name.getText().toString()); //습관명
                    intent.putExtra("Habit_Color",Integer.toString(int_Color)); //습관 색깔
                    intent.putExtra("edit_Habit_Day_Num",edit_Habit_Day_Num.getText().toString()); //습관 목표일 수
                    startActivity(intent);
                }
                else
                {
                    if(edit_Habit_Name.getText().toString().length() <= 0) //습관명이 안써진 경우
                    {
                        edit_Habit_Name.requestFocus(); //포커스 준다.
                        //키보드 보이게 한다
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                        Warning_Habit_Name.setTextColor(Color.RED);
                        Warning_Habit_Name.setText("습관명을 입력하세요.");
                    }
                    else if(edit_Habit_Name.getText().toString().length() > 0 &&
                            edit_Habit_Day_Num.getText().toString().length() <= 0) //습관 목표일 수 안쓴 경우
                    {
                        edit_Habit_Day_Num.requestFocus(); //포커스 준다.
                        //키보드 보이게
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                        Warning_Habit_Name.setText("");
                        Warning_Habit_Num.setTextColor(Color.RED);
                        Warning_Habit_Num.setText("습관 목표일을 정하세요.");
                    }
                    else if (edit_Habit_Name.getText().toString().length() > 0 &&
                            edit_Habit_Day_Num.getText().toString().length() > 0 && int_Color == 0) //습관 색 입력 안된 경우
                    {
                        Warning_Habit_Name.setText("");
                        Warning_Habit_Num.setText("");
                        View_Color_Pick.setTextColor(Color.RED);
                        View_Color_Pick.setText("습관 색을 정하세요.");
                    }
                }
            }
        });
    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this); //ColorPicker 객체 생성

        final ArrayList<String> colors = new ArrayList<>(); //배열에 원하는 색 담아둔다.

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

                        layout.setBackgroundColor(color);//목표색 배경에 뿌림 (test 용)
                        //View_Color_Pick.setText(Integer.toString(color)); //컬러값 int로 전달 (ex : -21615)
                        int_Color = color; //intent 할 변수
                        String hexColor = String.format("#%06X", (0xFFFFFFF & color)); //습관 색 int - > hex 변환
                        View_Color_Pick.setText(hexColor);
                        Log.e("습관컬러 hex 변환",View_Color_Pick.getText().toString());
                    }

                    @Override
                    public void onCancel() {
                        //취소할때
                    }
                }).show();
    }
}