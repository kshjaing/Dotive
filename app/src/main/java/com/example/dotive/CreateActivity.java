package com.example.dotive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
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

import static com.example.dotive.MainActivity.isDarkmode;

public class CreateActivity extends Activity {

    LinearLayout ll;
    Button btnCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ll = new LinearLayout(this);
        ll = findViewById(R.id.ll);
        //다크모드
        if (!isDarkmode) {
            ll.setBackgroundColor(Color.parseColor("#FFEBD3"));
        }

        else {
            ll.setBackgroundColor(Color.parseColor("#272B36"));
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
                //intent.putExtra("totalHabit+", totalHabit);
                startActivity(intent);
                Log.d("total", String.valueOf(MainActivity.totalHabit));
            }
        });

        setContentView(R.layout.activity_create);
    }

    protected void onResume() {
        super.onResume();

        //다크모드
        if (!isDarkmode) {
            ll.setBackgroundColor(Color.parseColor("#FFEBD3"));
        }

        else {
            ll.setBackgroundColor(Color.parseColor("#272B36"));
        }
    }
}

