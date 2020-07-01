package com.example.dotive;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.isDarkmode;
import static com.example.dotive.MainActivity.totalHabit;

public class SettingsActivity extends Activity {

    Context context_settings;
    ConstraintLayout cl;
    Button btnDarkmode, btnLanguage, btnRating, btnContact, btnReset, btnConfirm;
    Integer intDarkmodeCount;
    TextView txtSettingLetters;
    Cursor cursor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        cl = new ConstraintLayout(this);
        btnDarkmode = new Button(this);
        btnLanguage = new Button(this);
        btnRating  = new Button(this);
        btnContact = new Button(this);
        btnReset = new Button(this);
        btnConfirm = new Button(this);
        txtSettingLetters = new TextView(this);
        cl = findViewById(R.id.clSettings);
        btnDarkmode = findViewById(R.id.btn_darkmode);
        btnLanguage = findViewById(R.id.btn_language);
        btnRating = findViewById(R.id.btn_rating);
        btnContact = findViewById(R.id.btn_contact);
        btnReset = findViewById(R.id.btn_reset);
        btnConfirm = findViewById(R.id.btn_confirm);
        txtSettingLetters = findViewById(R.id.txtSettingLetters);

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

        btnDarkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDarkmode == 0) {
                    isDarkmode = 1;
                    ((Button) v).setText("라이트모드로 전환");
                    cl.setBackgroundColor(Color.parseColor("#272B36"));
                    getWindow().setStatusBarColor(Color.parseColor("#272B36"));
                    txtSettingLetters.setTextColor(Color.WHITE);
                    btnDarkmode.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                    btnLanguage.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                    btnRating.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                    btnContact.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                    btnReset.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                    btnConfirm.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                    btnDarkmode.setTextColor(Color.WHITE);
                    btnLanguage.setTextColor(Color.WHITE);
                    btnRating.setTextColor(Color.WHITE);
                    btnContact.setTextColor(Color.WHITE);
                    btnReset.setTextColor(Color.WHITE);
                    btnConfirm.setTextColor(Color.WHITE);
                }
                else {
                    View view = getWindow().getDecorView();
                    isDarkmode = 0;
                    ((Button) v).setText("다크모드로 전환");
                    cl.setBackgroundColor(Color.parseColor("#FFEBD3"));
                    getWindow().setStatusBarColor(Color.parseColor("#FFEBD3"));
                    view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    txtSettingLetters.setTextColor(Color.BLACK);
                    btnDarkmode.setBackgroundResource(R.drawable.habitbtn_border_round);
                    btnLanguage.setBackgroundResource(R.drawable.habitbtn_border_round);
                    btnRating.setBackgroundResource(R.drawable.habitbtn_border_round);
                    btnContact.setBackgroundResource(R.drawable.habitbtn_border_round);
                    btnReset.setBackgroundResource(R.drawable.habitbtn_border_round);
                    btnConfirm.setBackgroundResource(R.drawable.habitbtn_border_round);
                    btnDarkmode.setTextColor(Color.BLACK);
                    btnLanguage.setTextColor(Color.BLACK);
                    btnRating.setTextColor(Color.BLACK);
                    btnContact.setTextColor(Color.BLACK);
                    btnReset.setTextColor(Color.BLACK);
                    btnConfirm.setTextColor(Color.BLACK);
                }
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSettings();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalHabit = 0;
                truncateHabits();
                Toast.makeText(SettingsActivity.this, "모든 것이 잿더미가 됐어요!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onStart() {
        super.onStart();

        if (isDarkmode == 0) {
            View view = getWindow().getDecorView();
            btnDarkmode.setText("다크모드로 전환");
            cl.setBackgroundColor(Color.parseColor("#FFEBD3"));
            getWindow().setStatusBarColor(Color.parseColor("#FFEBD3"));
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            txtSettingLetters.setTextColor(Color.BLACK);
            btnDarkmode.setBackgroundResource(R.drawable.habitbtn_border_round);
            btnLanguage.setBackgroundResource(R.drawable.habitbtn_border_round);
            btnRating.setBackgroundResource(R.drawable.habitbtn_border_round);
            btnContact.setBackgroundResource(R.drawable.habitbtn_border_round);
            btnReset.setBackgroundResource(R.drawable.habitbtn_border_round);
            btnConfirm.setBackgroundResource(R.drawable.habitbtn_border_round);
            btnDarkmode.setTextColor(Color.BLACK);
            btnLanguage.setTextColor(Color.BLACK);
            btnRating.setTextColor(Color.BLACK);
            btnContact.setTextColor(Color.BLACK);
            btnReset.setTextColor(Color.BLACK);
            btnConfirm.setTextColor(Color.BLACK);
        }

        else {
            btnDarkmode.setText("라이트모드로 전환");
            cl.setBackgroundColor(Color.parseColor("#272B36"));
            getWindow().setStatusBarColor(Color.parseColor("#272B36"));
            txtSettingLetters.setTextColor(Color.WHITE);
            btnDarkmode.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
            btnLanguage.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
            btnRating.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
            btnContact.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
            btnReset.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
            btnConfirm.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
            btnDarkmode.setTextColor(Color.WHITE);
            btnLanguage.setTextColor(Color.WHITE);
            btnRating.setTextColor(Color.WHITE);
            btnContact.setTextColor(Color.WHITE);
            btnReset.setTextColor(Color.WHITE);
            btnConfirm.setTextColor(Color.WHITE);
        }
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
    public void truncateHabits() {
        MainActivity.dbHelper.getWritableDatabase();
        db.execSQL("DROP TABLE Habits");
        db.execSQL("CREATE TABLE Habits (id INTEGER PRIMARY KEY AUTOINCREMENT, habitName TEXT, habitColor TEXT," +
                "objDays INTEGER, habitProgress TEXT, createDate TEXT)");
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
