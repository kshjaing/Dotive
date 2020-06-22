package com.example.dotive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.isDarkmode;

public class CreateActivity extends Activity {

    ConstraintLayout cl;
    Button btnCreate;
    ImageButton red, orange, green, blue, purple, gray;
    DBInterface DBin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    protected  void onStart() {
        super.onStart();
        cl = new ConstraintLayout(this);
        cl = findViewById(R.id.cl);


        //-----------------------------색상 버튼들 생성 및 이벤트 설정------------------------------------
        red = new ImageButton(this);
        orange = new ImageButton(this);
        green = new ImageButton(this);
        blue = new ImageButton(this);
        purple = new ImageButton(this);
        gray = new ImageButton(this);


        red = findViewById(R.id.btncolorRed);
        orange= findViewById(R.id.btncolorOrange);
        green = findViewById(R.id.btncolorGreen);
        blue = findViewById(R.id.btncolorBlue);
        purple = findViewById(R.id.btncolorPurple);
        gray = findViewById(R.id.btncolorBluegray);

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                red.setBackgroundResource(R.drawable.colorbutton_red_pressed);
            }
        });
        orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orange.setBackgroundResource(R.drawable.colorbutton_red_pressed);
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                green.setBackgroundResource(R.drawable.colorbutton_red_pressed);
            }
        });
        blue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blue.setBackgroundResource(R.drawable.colorbutton_red_pressed);
            }
        });
        purple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                purple.setBackgroundResource(R.drawable.colorbutton_red_pressed);
            }
        });
        gray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gray.setBackgroundResource(R.drawable.colorbutton_red_pressed);
            }
        });

        //---------------------------------------------------------------------------------------------


        


        //다크모드
        if (!isDarkmode) {
            cl.setBackgroundColor(Color.parseColor("#FFEBD3"));
        }

        else {
            cl.setBackgroundColor(Color.parseColor("#272B36"));
        }

        //습관 추가 버튼 클릭 이벤트 부여
        btnCreate = new Button(this);
        btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                MainActivity.totalHabit += 1;
                MainActivity.isCreatePressed = true;
                //DBin.dbInsertHabits();
                //intent.putExtra("totalHabit+", totalHabit);
                startActivity(intent);
                Log.d("total", String.valueOf(MainActivity.totalHabit));
            }
        });
    }

    protected void onResume() {
        super.onResume();

        //다크모드
        if (!isDarkmode) {
            cl.setBackgroundColor(Color.parseColor("#FFEBD3"));
        }

        else {
            cl.setBackgroundColor(Color.parseColor("#272B36"));
        }
    }


}

