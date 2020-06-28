package com.example.dotive;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.dotive.MainActivity.curDateString;
import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.isDarkmode;
import static com.example.dotive.MainActivity.objectDays;
import static com.example.dotive.MainActivity.progressBuilderArr;
import static com.example.dotive.MainActivity.totalHabit;
import static com.example.dotive.MainActivity.typeface;

public class HabitActivity extends Activity {
    public static int dayIndex = 0;
    Button[] boxHabitArr;                   //목표일수만큼 버튼 생성
    ImageButton ibtnBack, ibtnBar;          //뒤로가기 버튼, 액션바 버튼
    TextView txtHabitName, txtObjectDays, txtDate;         //습관명, 버튼 속 날짜 텍스트
    SimpleDateFormat dateFormat;
    Calendar calendar;
    Date curDate;
    String habitName;
    int boxNum;

    Cursor cursor;

    LinearLayout ll;
    ScrollView sv;
    FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        txtDate = new TextView(this);
        txtHabitName = new TextView(this);
        txtObjectDays = new TextView(this);
        dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 (E)", Locale.KOREAN);

        ibtnBack = findViewById(R.id.ibtnBack);
        ibtnBar = findViewById(R.id.ibtnBar);

        //메인액티비티에서 클릭한 박스의 태그를 받아옴
        Intent intent = getIntent();
        String boxTag = intent.getExtras().getString("tag");
        int boxIndex = boxTag.lastIndexOf("_");
        boxNum = Integer.parseInt(boxTag.substring(boxIndex + 1));
        String test1 = progressBuilderArr[2].toString();

        db = dbHelper.getWritableDatabase();
        cursor = db.rawQuery("SELECT habitName FROM Habits", null);
        cursor.moveToPosition(boxNum);
        habitName = cursor.getString(0);
        txtHabitName.setText(habitName);
        txtObjectDays.setText("\n\n(목표 " + objectDays[boxNum] + "일)");


        ll = findViewById(R.id.ll_habit);
        sv = findViewById(R.id.sv_habit);
        fl = findViewById(R.id.fl_habit);



        int btn_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,80,
                getResources().getDisplayMetrics());

        LinearLayout.LayoutParams btn_linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, btn_Height, 1);
        LinearLayout.LayoutParams txt_linearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        btn_linearParams.setMargins(0,btn_Height / 2, 0, btn_Height / 8);

        txtHabitName.setTextSize(30);
        txtHabitName.setTypeface(typeface);
        txtHabitName.setGravity(Gravity.CENTER);
        txtObjectDays.setTextSize(20);
        txtObjectDays.setTypeface(typeface);
        txtObjectDays.setGravity(Gravity.CENTER);

        ll.addView(txtHabitName);
        ll.addView(txtObjectDays);

        //다크모드에 따른 배경색 변화
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
            txtHabitName.setTextColor(Color.BLACK);
            txtObjectDays.setTextColor(Color.BLACK);
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            txtHabitName.setTextColor(Color.WHITE);
            txtObjectDays.setTextColor(Color.WHITE);
        }

        int obj = objectDays[boxNum];

            for (int i = 0; i < obj; i++) {
                curDate = new Date();
                calendar = Calendar.getInstance();
                calendar.setTime(curDate);
                calendar.add(Calendar.DATE, -1 * i);

                curDateString = dateFormat.format(calendar.getTime());



                boxHabitArr = new Button[i + 1];
                boxHabitArr[i] = new Button(this);
                boxHabitArr[i].setLayoutParams(btn_linearParams);
                boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round);
                boxHabitArr[i].setTag("dateBox_" + i);
                boxHabitArr[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //몇번째 날짜 버튼을 눌렀는지 dayIndex에 담음
                        int obj_index = v.getTag().toString().lastIndexOf("_");
                        String dayNum = v.getTag().toString().substring(obj_index + 1);
                        dayIndex = Integer.parseInt(dayNum);

                        //임시 테스트용
                        if (isDarkmode == 0) {
                            if (v.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.habitbtn_border_round_pressed).getConstantState())) {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '0');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                v.setBackgroundResource(R.drawable.habitbtn_border_round);
                                String btnText = ((Button) v).getText().toString();
                                ((Button) v).setText(btnText.substring(0, btnText.length() - 5));
                            }

                            else {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '1');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                v.setBackgroundResource(R.drawable.habitbtn_border_round_pressed);
                                ((Button) v).append("  완료!");
                            }
                        }
                        else {
                            if (v.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.habitbtn_border_round_pressed_dark).getConstantState())) {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '0');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                ((Button) v).setTextColor(Color.WHITE);
                                v.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                                String btnText = ((Button) v).getText().toString();
                                ((Button) v).setText(btnText.substring(0, btnText.length() - 5));
                            }

                            else {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '1');
                                v.setBackgroundResource(R.drawable.habitbtn_border_round_pressed_dark);
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                ((Button) v).setTextColor(Color.BLACK);
                                ((Button) v).append("  완료!");
                            }
                        }
                        cursor = db.rawQuery("SELECT habitProgress FROM Habits", null);
                        cursor.moveToPosition(boxNum);
                        String test = cursor.getString(0);

                        Toast.makeText(HabitActivity.this, cursor.getString(0), Toast.LENGTH_SHORT).show();
                    }
                });
                boxHabitArr[i].setText(curDateString);
                boxHabitArr[i].setTypeface(typeface);
                boxHabitArr[i].setTextSize(20);

                //오늘날짜는 테두리로 표시
                if (isDarkmode == 0) {
                    if (i == 0) {
                        boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round_stroke);
                    }
                    else
                    boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round);
                }
                else {
                    boxHabitArr[i].setTextColor(Color.WHITE);
                    if (i == 0) {
                        boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round_stroke_dark);
                    }
                    else
                    boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                }

                ll.addView(boxHabitArr[i]);
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

    //뒤로가기 키 눌렀을 때 이벤트 설정
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HabitActivity.this, MainActivity.class);
        startActivity(intent);
    }

    //Habits 테이블 습관진행도 업데이트
    public void updateProgress(Integer id, String habitProgress) {
        MainActivity.dbHelper.getWritableDatabase();
        db.execSQL("UPDATE Habits SET habitProgress='" + habitProgress +"' WHERE id='" + (id + 1) +"'");
    }
}
