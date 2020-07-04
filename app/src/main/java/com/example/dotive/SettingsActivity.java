package com.example.dotive;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
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

import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.w3c.dom.Text;

public class SettingsActivity extends Activity {

    ConstraintLayout cl;
    ScrollView sv;
    Button btnDarkmode, btnLanguage, btnRating, btnContact, btnReset, btnConfirm;
    public int new_darkmode = 0; //이제 변할 값.
    public int old_darkmode = 0; //MainActivity 에서 받아온 값
    public int darkmode = 0;
    TextView txtDotive;
    TextView txtDev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent2 = getIntent();
        old_darkmode = intent2.getIntExtra("DARK_MODE",3);
        darkmode = old_darkmode;
        Log.e("SettingActivity.java", "다크모드 값 : " + old_darkmode);

        cl = new ConstraintLayout(this);
        sv = new ScrollView(this);
        btnDarkmode = new Button(this);
        btnLanguage = new Button(this);
        btnRating  = new Button(this);
        btnContact = new Button(this);
        btnReset = new Button(this);
        btnConfirm = new Button(this);
        txtDev = new TextView(this);
        txtDotive = new TextView(this);

        cl = findViewById(R.id.clSettings);
        btnDarkmode = findViewById(R.id.btn_darkmode);
        btnLanguage = findViewById(R.id.btn_language);
        btnRating = findViewById(R.id.btn_rating);
        btnContact = findViewById(R.id.btn_contact);
        btnReset = findViewById(R.id.btn_reset);
        btnConfirm = findViewById(R.id.btn_confirm);
        txtDotive = findViewById(R.id.txtDotive);
        txtDev = findViewById(R.id.txtDev);

        if (darkmode == 1) {
            btnDarkmode.setText("라이트모드로 전환");
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            cl.setBackgroundColor(Color.parseColor("#272B36"));
            getWindow().setStatusBarColor(Color.parseColor("#272B36"));
            txtDev.setTextColor(Color.WHITE);
            txtDotive.setTextColor(Color.WHITE);
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
            btnDarkmode.setText("다크모드로 전환");
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
            cl.setBackgroundColor(Color.parseColor("#FFEBD3"));
            getWindow().setStatusBarColor(Color.parseColor("#FFEBD3"));
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //status bar 글자 색상
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            txtDotive.setTextColor(Color.BLACK);
            txtDev.setTextColor(Color.BLACK);
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

        btnDarkmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(old_darkmode == 0) new_darkmode = 1;
                else new_darkmode = 0;


                String uriString = "content://com.example.dotive/Settings";
                Uri uri = new Uri.Builder().build().parse(uriString);

                ContentValues updateValue = new ContentValues();
                updateValue.put("darkmode",new_darkmode);
                int count = getContentResolver().update(uri,updateValue,null,null);
                Log.e("SettingActivity.java", "다크모드 변경 : "+ old_darkmode + " - > " + new_darkmode);

                if(new_darkmode == 0) old_darkmode = 0;
                else old_darkmode = 1;

                darkmode = new_darkmode;
                setDarkmode();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                truncateHabits();

                DynamicToast.makeWarning(getApplicationContext(),"모든 습관을 삭제했습니다").show();
                //Toast.makeText(SettingsActivity.this, "모든 습관을 삭제했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setDarkmode() {
        if (darkmode == 1) {
            btnDarkmode.setText("라이트모드로 전환");
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            cl.setBackgroundColor(Color.parseColor("#272B36"));
            getWindow().setStatusBarColor(Color.parseColor("#272B36"));
            txtDev.setTextColor(Color.WHITE);
            txtDotive.setTextColor(Color.WHITE);
            //status bar 글자 색상
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
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
            btnDarkmode.setText("다크모드로 전환");
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
            cl.setBackgroundColor(Color.parseColor("#FFEBD3"));
            getWindow().setStatusBarColor(Color.parseColor("#FFEBD3"));
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            txtDotive.setTextColor(Color.BLACK);
            txtDev.setTextColor(Color.BLACK);
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }


    //Habits 테이블의 모든 데이터 지우기
    public void truncateHabits() {
        try {
            String uriString = "content://com.example.dotive/Habits";
            Uri uri = new Uri.Builder().build().parse(uriString);

            int count = getContentResolver().delete(uri, null, null);
            Log.e("SettingsActivity.java", "delete 실행 : " + count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
