package com.example.dotive;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.objectDays;
import static com.example.dotive.MainActivity.totalHabit;
import static com.example.dotive.MainActivity.curDateString;
import static com.example.dotive.MainActivity.typeface;

public class HabitActivity extends Activity {
    Integer whichBox;
    Button[] boxHabitArr;

    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

    }

    protected void onStart() {
        super.onStart();/*
        dbHelper.getWritableDatabase();
        ll = findViewById(R.id.ll_habit);
        setContentView(R.layout.activity_habit);

        int btn_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,
                getResources().getDisplayMetrics());

        for (int i = 0; i < totalHabit; i++) {
            for (int j = 0; j < objectDays[i]; j++) {
                boxHabitArr = new Button[totalHabit];
                boxHabitArr[j] = new Button(this);
                boxHabitArr[j].setHeight(btn_Height);
                boxHabitArr[j].setBackgroundResource(R.drawable.habitbtn_border_round);

                ll.addView(boxHabitArr[j]);
            }
        }*/
    }
}
