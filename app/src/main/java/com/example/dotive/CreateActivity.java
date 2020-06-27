package com.example.dotive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.util.zip.DataFormatException;

import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.isDarkmode;

public class CreateActivity extends Activity {

    ConstraintLayout cl;
    Button btnCreate;
    EditText edtHabitName, edtObjectDays;
    TextView txtHabit, txtObjdays;
    ImageButton red, orange, green, blue, purple, gray;
    Integer objectDays = 1;
    String curColor = "red";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    protected  void onStart() {
        super.onStart();
        cl = new ConstraintLayout(this);
        cl = findViewById(R.id.cl);
        edtHabitName = findViewById(R.id.edtHabit);
        edtObjectDays = findViewById(R.id.edtObjDays);


        //-----------------------------색상 버튼들 생성 및 이벤트 설정------------------------------------
        red = new ImageButton(this);
        orange = new ImageButton(this);
        green = new ImageButton(this);
        blue = new ImageButton(this);
        purple = new ImageButton(this);
        gray = new ImageButton(this);
        txtHabit = new TextView(this);
        txtObjdays = new TextView(this);


        red = findViewById(R.id.btncolorRed);
        orange= findViewById(R.id.btncolorOrange);
        green = findViewById(R.id.btncolorGreen);
        blue = findViewById(R.id.btncolorBlue);
        purple = findViewById(R.id.btncolorPurple);
        gray = findViewById(R.id.btncolorBluegray);
        txtHabit = findViewById(R.id.txtHabit);
        txtObjdays = findViewById(R.id.txtObjdays);



        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                curColor = "red";
                red.setBackgroundResource(R.drawable.colorbutton_red_pressed);
                orange.setBackgroundResource(R.drawable.colorbutton_orange);
                green.setBackgroundResource(R.drawable.colorbutton_green);
                blue.setBackgroundResource(R.drawable.colorbutton_blue);
                purple.setBackgroundResource(R.drawable.colorbutton_purple);
                gray.setBackgroundResource(R.drawable.colorbutton_bluegray);

            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curColor = "orange";
                red.setBackgroundResource(R.drawable.colorbutton_red);
                green.setBackgroundResource(R.drawable.colorbutton_green);
                blue.setBackgroundResource(R.drawable.colorbutton_blue);
                purple.setBackgroundResource(R.drawable.colorbutton_purple);
                gray.setBackgroundResource(R.drawable.colorbutton_bluegray);
                orange.setBackgroundResource(R.drawable.colorbutton_orange_pressed);
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curColor = "green";
                red.setBackgroundResource(R.drawable.colorbutton_red);
                orange.setBackgroundResource(R.drawable.colorbutton_orange);
                blue.setBackgroundResource(R.drawable.colorbutton_blue);
                purple.setBackgroundResource(R.drawable.colorbutton_purple);
                gray.setBackgroundResource(R.drawable.colorbutton_bluegray);
                green.setBackgroundResource(R.drawable.colorbutton_green_pressed);
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curColor = "blue";
                red.setBackgroundResource(R.drawable.colorbutton_red);
                orange.setBackgroundResource(R.drawable.colorbutton_orange);
                green.setBackgroundResource(R.drawable.colorbutton_green);
                purple.setBackgroundResource(R.drawable.colorbutton_purple);
                gray.setBackgroundResource(R.drawable.colorbutton_bluegray);
                blue.setBackgroundResource(R.drawable.colorbutton_blue_pressed);
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curColor = "purple";
                red.setBackgroundResource(R.drawable.colorbutton_red);
                orange.setBackgroundResource(R.drawable.colorbutton_orange);
                green.setBackgroundResource(R.drawable.colorbutton_green);
                gray.setBackgroundResource(R.drawable.colorbutton_bluegray);
                blue.setBackgroundResource(R.drawable.colorbutton_blue);
                purple.setBackgroundResource(R.drawable.colorbutton_purple_pressed);
            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curColor = "gray";
                red.setBackgroundResource(R.drawable.colorbutton_red);
                orange.setBackgroundResource(R.drawable.colorbutton_orange);
                green.setBackgroundResource(R.drawable.colorbutton_green);
                purple.setBackgroundResource(R.drawable.colorbutton_purple);
                blue.setBackgroundResource(R.drawable.colorbutton_blue);
                gray.setBackgroundResource(R.drawable.colorbutton_bluegray_pressed);
            }
        });

        //---------------------------------------------------------------------------------------------


        


        //액티비티 들어올때 다크모드 체크
        if (isDarkmode == 0) {
            cl.setBackgroundColor(Color.parseColor("#FFEBD3"));
            txtHabit.setTextColor(Color.BLACK);
            txtObjdays.setTextColor(Color.BLACK);
        }

        else {
            cl.setBackgroundColor(Color.parseColor("#272B36"));
            txtHabit.setTextColor(Color.WHITE);
            txtObjdays.setTextColor(Color.WHITE);
        }

        //습관 추가 버튼 클릭 이벤트 부여
        btnCreate = new Button(this);
        btnCreate = findViewById(R.id.btnCreate);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtHabitName.length() == 0) {
                    Toast.makeText(CreateActivity.this, "습관명을 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                else if (edtObjectDays.length() == 0) {
                    Toast.makeText(CreateActivity.this, "목표일수를 입력해주세요!", Toast.LENGTH_SHORT).show();
                    return;
                }

                //목표일수 EditText에 텍스트가 있으면 그 숫자를 변수에 삽입
                if (edtObjectDays.length() != 0) {
                    objectDays = Integer.parseInt(edtObjectDays.getText().toString());
                }

                //습관 진행도 표시 문자열
                String progressString = new String();
                for (int i = 0; i < objectDays; i++) {
                    progressString = progressString + "0";
                }
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                MainActivity.isCreatePressed = true;
                dbInsertHabits(edtHabitName.getText().toString(), curColor, objectDays, progressString);
                db.close();
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(CreateActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //Habits 테이블에 습관 추가
    public void dbInsertHabits(String habitName, String habitColor, Integer objDays, String habitProgress) {
        MainActivity.dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO Habits (habitName, habitColor, objDays, habitProgress, createDate) Values ('" + habitName + "', '" + habitColor + "', '" + objDays + "', '" + habitProgress + "', CURRENT_TIMESTAMP);");
    }
}

