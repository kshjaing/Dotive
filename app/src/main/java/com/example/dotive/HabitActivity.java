package com.example.dotive;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.isDarkmode;
import static com.example.dotive.MainActivity.objectDays;
import static com.example.dotive.MainActivity.totalHabit;
import static com.example.dotive.MainActivity.curDateString;
import static com.example.dotive.MainActivity.typeface;

public class HabitActivity extends Activity {
    int whichBox;
    Button[] boxHabitArr;
    ImageButton ibtnBack, ibtnBar;

    LinearLayout ll;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBar = findViewById(R.id.ibtnBar);

        Intent intent = getIntent();
        String boxTag = intent.getExtras().getString("tag");
        int index = boxTag.lastIndexOf("_");
        String boxnum = boxTag.substring(index + 1);

        dbHelper.getWritableDatabase();
        ll = findViewById(R.id.ll_habit);
        sv = findViewById(R.id.sv_habit);

        int btn_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,
                getResources().getDisplayMetrics());

        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, btn_Height, 1);

        linearParams.setMargins(0,btn_Height / 2, 0, btn_Height / 8);


        //다크모드에 따른 배경색 변화
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
        }

        int obj = objectDays[Integer.parseInt(boxnum)];

            for (int j = 0; j < obj; j++) {
                boxHabitArr = new Button[j + 1];
                boxHabitArr[j] = new Button(this);
                boxHabitArr[j].setLayoutParams(linearParams);
                boxHabitArr[j].setBackgroundResource(R.drawable.habitbtn_border_round);

                ll.addView(boxHabitArr[j]);
            }
    }

    protected void onStart() {
        super.onStart();

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
