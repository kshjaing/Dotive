package com.example.dotive;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.dotive.DrawCircle.oneIndex;
import static com.example.dotive.MainActivity.calDate;
import static com.example.dotive.MainActivity.createDateArr;
import static com.example.dotive.MainActivity.createDateTimestamp;
import static com.example.dotive.MainActivity.curDateString;
import static com.example.dotive.MainActivity.dateDiff;
import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.endDateString;
import static com.example.dotive.MainActivity.endDateTimestamp;
import static com.example.dotive.MainActivity.habitProgressArr;
import static com.example.dotive.MainActivity.intDateDiff;
import static com.example.dotive.MainActivity.isDarkmode;
import static com.example.dotive.MainActivity.objectDays;
import static com.example.dotive.MainActivity.oneCount;
import static com.example.dotive.MainActivity.progressBuilderArr;
import static com.example.dotive.MainActivity.standardSize_X;
import static com.example.dotive.MainActivity.totalHabit;
import static com.example.dotive.MainActivity.typeface;

public class HabitActivity extends Activity {
    public static int dayIndex = 0;
    public static int[] oneIndex;                         //진행도 문자열에서 1이 어느 인덱스에 있는지 담는 배열
    Button[] boxHabitArr;                   //목표일수만큼 버튼 생성
    ImageButton ibtnBack, ibtnEdit;          //뒤로가기 버튼, 편집버튼
    TextView txtHabitName, txtObjectDays, txtDate;         //습관명, 버튼 속 날짜 텍스트
    SimpleDateFormat dateFormat;
    Calendar calendar;
    Date curDate;
    String habitName;
    String progressString;
    public static int boxNum;
    String objectDate;                                //최종목표날짜

    long todayTimestamp;
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
        ibtnBack = new ImageButton(this);
        ibtnEdit = new ImageButton(this);


        ibtnBack = findViewById(R.id.ibtnBack);
        ibtnEdit = findViewById(R.id.ibtnEdit);
        View view = getWindow().getDecorView();






        //메인액티비티에서 클릭한 박스의 태그를 받아옴
        Intent intent = getIntent();
        String boxTag = intent.getExtras().getString("tag");
        int tagStringIndex = boxTag.lastIndexOf("_");
        boxNum = Integer.parseInt(boxTag.substring(tagStringIndex + 1));   //메인액티비티에서 몇번째 박스를 클릭했는지 인덱스

        db = dbHelper.getWritableDatabase();


        //편집버튼 클릭이벤트
        ibtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HabitActivity.this, EditActivity.class);
                intent.putExtra("boxnumber", boxNum);
                startActivity(intent);
            }
        });


        //DB에서 습관명을 받아와 txtHabitName 에 삽입, 목표일수도 받아와 txtObjectDays 에 삽입
        cursor = db.rawQuery("SELECT habitName FROM Habits", null);
        cursor.moveToPosition(boxNum);
        habitName = cursor.getString(0);
        txtHabitName.setText(habitName);
        txtObjectDays.setText("\n\n(목표 " + objectDays[boxNum] + "일)");


        ll = findViewById(R.id.ll_habit);
        sv = findViewById(R.id.sv_habit);
        fl = findViewById(R.id.fl_habit);



        int btn_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,standardSize_X * 0.18f,
                getResources().getDisplayMetrics());
        int btn_Width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, standardSize_X * 0.85f,
                getResources().getDisplayMetrics());

        LinearLayout.LayoutParams btn_linearParams = new LinearLayout.LayoutParams(
                btn_Width, btn_Height, 1);
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



        //다크모드에 따른 배경색, 텍스트색 변화
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
            getWindow().setStatusBarColor(Color.parseColor("#FFEBD3"));
            view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            txtHabitName.setTextColor(Color.BLACK);
            txtObjectDays.setTextColor(Color.BLACK);
            ibtnBack.setBackgroundResource(R.drawable.arrow_back_dark);
            ibtnEdit.setBackgroundResource(R.drawable.edit_dark);
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            getWindow().setStatusBarColor(Color.parseColor("#272B36"));
            txtHabitName.setTextColor(Color.WHITE);
            txtObjectDays.setTextColor(Color.WHITE);
            ibtnBack.setBackgroundResource(R.drawable.arrow_back);
            ibtnEdit.setBackgroundResource(R.drawable.edit);
        }



        //메인액티비티에서 클릭한 박스의 인덱스에 해당하는 목표일수를 obj 에 삽입
        int obj = objectDays[boxNum];

        //습관 생성날짜에 목표일수를 더해 최종 목표날짜를 구함
        //calendar.setTime(createDateArr[boxNum]);
        //calendar.add(Calendar.DATE, objectDays[boxNum]);
        //objectDate = dateFormat.format(calendar.getTime());


        curDate = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(curDate);

        if (endDateTimestamp[boxNum] >= todayTimestamp) {
            for (int i = 0; i < intDateDiff[boxNum] + 1; i++) {
                int tagNum = intDateDiff[boxNum] - i;

                //-----------------------------현재날짜 구함------------------------------
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");


                curDateString = dateFormat.format(calendar.getTime());
                //-----------------------------------------------------------------------

                calendar.add(Calendar.DATE, -1);
                boxHabitArr = new Button[i + 1];
                boxHabitArr[i] = new Button(this);
                boxHabitArr[i].setLayoutParams(btn_linearParams);
                boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round);
                boxHabitArr[i].setTag("dateBox_" + tagNum);
                boxHabitArr[i].setText(curDateString);
                boxHabitArr[i].setTypeface(typeface);
                boxHabitArr[i].setTextSize(20);

                boxHabitArr[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //몇번째 날짜 버튼을 눌렀는지 dayIndex에 담음
                        int obj_index = v.getTag().toString().lastIndexOf("_");
                        String dayNum = v.getTag().toString().substring(obj_index + 1);
                        dayIndex = Integer.parseInt(dayNum);
                        //Toast.makeText(HabitActivity.this, String.valueOf(dayIndex), Toast.LENGTH_SHORT).show();



                        //클릭한 뷰의 백그라운드와 drawable 파일과의 비교
                        if (isDarkmode == 0) {
                            if (v.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.habitbtn_border_round_pressed).getConstantState())) {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '0');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                v.setBackgroundResource(R.drawable.habitbtn_border_round);
                                String btnText = ((Button) v).getText().toString();
                                ((Button) v).setText(btnText.substring(0, btnText.length() - 5));
                                ((Button) v).setTextColor(Color.BLACK);
                            } else {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '1');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                v.setBackgroundResource(R.drawable.habitbtn_border_round_pressed);
                                ((Button) v).setText(((Button) v).getText() + "  완료!");
                                ((Button) v).setTextColor(Color.WHITE);
                            }
                        }

                        else {
                            if (v.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.habitbtn_border_round_pressed_dark).getConstantState())) {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '0');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                v.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                                String btnText = ((Button) v).getText().toString();
                                ((Button) v).setText(btnText.substring(0, btnText.length() - 5));
                                ((Button) v).setTextColor(Color.WHITE);
                            } else {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '1');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                v.setBackgroundResource(R.drawable.habitbtn_border_round_pressed_dark);
                                ((Button) v).setText(((Button) v).getText() + "  완료!");
                                ((Button) v).setTextColor(Color.BLACK);
                            }
                        }


                        cursor = db.rawQuery("SELECT habitProgress FROM Habits", null);
                        cursor.moveToPosition(boxNum);
                        //진행도 문자열 잘 변하는지 테스트용
                        //Toast.makeText(HabitActivity.this, cursor.getString(0), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(HabitActivity.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    }
                });


                //오늘날짜는 테두리로 표시
                if (isDarkmode == 0) {
                    if (i == 0) {
                        boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round_stroke);
                    } else
                        boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round);
                } else {
                    boxHabitArr[i].setTextColor(Color.WHITE);
                    if (i == 0) {
                        boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round_stroke_dark);
                    } else
                        boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                }


                ll.addView(boxHabitArr[i]);
            }
        }

        else {
            calendar = Calendar.getInstance();
            calendar.setTime(createDateArr[boxNum]);
            calendar.add(Calendar.DATE, obj);
            for (int i = 0; i < intDateDiff[boxNum] + 1; i++) {
                calendar.add(Calendar.DATE, -1);
                int tagNum = intDateDiff[boxNum] - i;

                boxHabitArr = new Button[obj];
                boxHabitArr[i] = new Button(this);
                boxHabitArr[i].setLayoutParams(btn_linearParams);
                boxHabitArr[i].setBackgroundResource(R.drawable.habitbtn_border_round);
                boxHabitArr[i].setTag("dateBox_" + tagNum);
                boxHabitArr[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //몇번째 날짜 버튼을 눌렀는지 dayIndex에 담음
                        int obj_index = v.getTag().toString().lastIndexOf("_");
                        String dayNum = v.getTag().toString().substring(obj_index);

                        if (isDarkmode == 0) {
                            //클릭한 뷰의 백그라운드와 drawable 파일과의 비교
                            if (v.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.habitbtn_border_round_pressed).getConstantState())) {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '0');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                v.setBackgroundResource(R.drawable.habitbtn_border_round);
                                String btnText = ((Button) v).getText().toString();
                                ((Button) v).setText(btnText.substring(0, btnText.length() - 5));
                            } else {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '1');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                v.setBackgroundResource(R.drawable.habitbtn_border_round_pressed);
                                ((Button) v).setText(((Button) v).getText() + "  완료!");
                                ((Button) v).setTextColor(Color.BLACK);
                                Toast.makeText(HabitActivity.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (v.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.habitbtn_border_round_pressed_dark).getConstantState())) {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '0');
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                ((Button) v).setTextColor(Color.WHITE);
                                v.setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                                String btnText = ((Button) v).getText().toString();
                                ((Button) v).setText(btnText.substring(0, btnText.length() - 5));
                            } else {
                                progressBuilderArr[boxNum].setCharAt(dayIndex, '1');
                                v.setBackgroundResource(R.drawable.habitbtn_border_round_pressed_dark);
                                updateProgress(boxNum, progressBuilderArr[boxNum].toString());
                                ((Button) v).setTextColor(Color.BLACK);
                                ((Button) v).setText(((Button) v).getText() + "  완료!");
                                Toast.makeText(HabitActivity.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }


                        //진행도 문자열 잘 변하는지 테스트용
                        //Toast.makeText(HabitActivity.this, habitProgressArr[boxNum], Toast.LENGTH_SHORT).show();
                        //Toast.makeText(HabitActivity.this, v.getTag().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                boxHabitArr[i].setText(endDateString);
                boxHabitArr[i].setTypeface(typeface);
                boxHabitArr[i].setTextSize(20);

                ll.addView(boxHabitArr[i]);
            }
        }

            if (oneCount[boxNum] > 0) {
                //습관 진행도 문자열의 1의 개수만큼 oneIndex 배열 크기 지정
                oneIndex = new int[oneCount[boxNum]];

                //습관 진행도 문자열에서 1이 있는 인덱스를 연속으로 뽑아서 oneIndex 배열에 삽입
                for (int i = 0; i < oneCount[boxNum]; i++) {
                    if (oneCount[boxNum] == 1) {
                        oneIndex[0] = habitProgressArr[boxNum].indexOf('1');
                    }
                    else {
                        oneIndex[0] = habitProgressArr[boxNum].indexOf('1');
                        if (i > 0) {
                            oneIndex[i] = habitProgressArr[boxNum].indexOf('1', oneIndex[i - 1] + 1);
                        }
                    }
                }

                //1이 있는 개수만큼 각 인덱스마다 완료표시로 지정 반복
                /*
                for (int j = 0; j < oneCount[boxNum]; j++) {
                    if (isDarkmode == 0) {
                        ll.findViewWithTag("dateBox_" + oneIndex[j]).setBackgroundResource(R.drawable.habitbtn_border_round_pressed);
                        ((Button)ll.findViewWithTag("dateBox_" + oneIndex[j])).setTextColor(Color.WHITE);
                    }
                    else {
                        ll.findViewWithTag("dateBox_" + oneIndex[j]).setBackgroundResource(R.drawable.habitbtn_border_round_pressed_dark);
                        ((Button)ll.findViewWithTag("dateBox_" + oneIndex[j])).setTextColor(Color.BLACK);
                    }
                    ((Button)ll.findViewWithTag("dateBox_" + oneIndex[j])).setText(((Button)ll.findViewWithTag("dateBox_" + oneIndex[j])).getText() + "  완료!");
                }*/
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

        //다크모드에 따른 배경색, 텍스트색 변화
        if (isDarkmode == 0) {
            sv.setBackgroundColor(Color.parseColor("#FFEBD3"));
            txtHabitName.setTextColor(Color.BLACK);
            txtObjectDays.setTextColor(Color.BLACK);
            ibtnBack.setBackgroundResource(R.drawable.arrow_back_dark);
            ibtnEdit.setBackgroundResource(R.drawable.edit_dark);
        }

        else {
            sv.setBackgroundColor(Color.parseColor("#272B36"));
            txtHabitName.setTextColor(Color.WHITE);
            txtObjectDays.setTextColor(Color.WHITE);
            ibtnBack.setBackgroundResource(R.drawable.arrow_back);
            ibtnEdit.setBackgroundResource(R.drawable.edit);
        }

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
        db.execSQL("UPDATE Habits SET habitProgress='" + habitProgress +"' WHERE ROWID = (SELECT ROWID FROM Habits LIMIT 1 OFFSET "+ id +")");
    }



}
