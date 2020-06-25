package com.example.dotive;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorRes;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.isDarkmode;
import static com.example.dotive.MainActivity.totalHabit;

public class SettingsActivity extends Activity {

    ConstraintLayout cl;
    Button darkmodeBtn, resetBtn;
    Integer intDarkmodeCount;
    TextView txtConfirm;
    Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper.getWritableDatabase();
        //db.execSQL("DELETE FROM Settings");

        //설정 테이블에 자료가 있는지 판단할수있는 count 변수
        cursor = db.rawQuery("SELECT COUNT(darkmode) FROM Settings", null);
        while(cursor.moveToNext()) {
            intDarkmodeCount = Integer.parseInt(cursor.getString(0));
        }

        //테이블에 아무런 내용이 없을때, 즉 처음에만 insert문 실행
        if (intDarkmodeCount == 0) {
            insertSettings();
        }

        cursor = db.rawQuery("SELECT darkmode FROM Settings", null);
        while(cursor.moveToNext()) {
            isDarkmode = Integer.parseInt(cursor.getString(0));
        }
    }

    protected void onStart() {
        super.onStart();
        cl = new ConstraintLayout(this);
        darkmodeBtn = new Button(this);
        resetBtn = new Button(this);
        txtConfirm = new TextView(this);

        setContentView(R.layout.activity_settings);

        cl = findViewById(R.id.clSettings);
        darkmodeBtn = findViewById(R.id.btnDarkmode);
        resetBtn = findViewById(R.id.btnReset);
        txtConfirm = findViewById(R.id.txtConfirm);
        if (isDarkmode == 0) {
            darkmodeBtn.setText("라이트모드");
            cl.setBackgroundColor(Color.parseColor("#FFEBD3"));
            txtConfirm.setTextColor(Color.BLACK);
        }

        else {
            darkmodeBtn.setText("다크모드");
            cl.setBackgroundColor(Color.parseColor("#1C1C1F"));
            txtConfirm.setTextColor(Color.WHITE);
        }

        darkmodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDarkmode == 0) {
                    isDarkmode = 1;
                    darkmodeBtn.setText("다크모드");
                    cl.setBackgroundColor(Color.parseColor("#1C1C1F"));
                    txtConfirm.setTextColor(Color.WHITE);
                }
                else {
                    isDarkmode = 0;
                    darkmodeBtn.setText("라이트모드");
                    cl.setBackgroundColor(Color.parseColor("#FFEBD3"));
                    txtConfirm.setTextColor(Color.BLACK);
                }
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalHabit = 0;
                deleteHabits();
                Toast.makeText(SettingsActivity.this, "모든 데이터가 초기화되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });

        txtConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onDestroy() {
        super.onDestroy();
        db.close();
        dbHelper.close();
        cursor.close();
    }

    @Override
    public void onBackPressed() {
        updateSettings();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }


    //Habits 테이블의 모든 데이터 지우기
    public void deleteHabits() {
        MainActivity.dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM Habits");
    }

    public void insertSettings() {
        MainActivity.dbHelper.getWritableDatabase();
        db.execSQL("INSERT INTO Settings (darkmode, habitStrength, language) Values('" + isDarkmode + "', '기본', '한국어')");
    }

    public void updateSettings() {
        MainActivity.dbHelper.getWritableDatabase();
        db.execSQL("UPDATE Settings SET darkmode = '" + isDarkmode +"', habitStrength = '기본', language = '한국어'");
    }
}
