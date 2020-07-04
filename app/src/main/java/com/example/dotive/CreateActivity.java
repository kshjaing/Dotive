package com.example.dotive;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.karn.notify.Notify;
import io.karn.notify.NotifyCreator;
import io.karn.notify.entities.NotifyConfig;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;
import petrov.kristiyan.colorpicker.ColorPicker;

public class CreateActivity extends AppCompatActivity {

    //Button btn_finish; //앱 종료
    Button btn_OK; //확인 버튼
    private Button btn_Color_Picker; //색 선택
    private ConstraintLayout layout;
    TextView View_Color_Pick; //색상 값 TextView 에 쓸 예정, 습관 색 안정할 경우 경고
    EditText edit_Habit_Name; //습관명 (EditText)
    int int_Color; //컬러 값 int로 전달
    EditText edit_Habit_Day_Num; //습관 목표일 수 (ex: 30일 목표 공부하기, -> 30일 동그라미 배열 생성)
    TextView Warning_Habit_Name; //습관명 비어있을 경우 경고
    TextView Warning_Habit_Num; //습관 목표일 수 비어있을 경우 경고
    TextView txtHabit;
    TextView txtObjdays;

    //db에서 다크모드 값 , 언어 값
    public int DB_darkmode = 3; // 1 = > 다크 , 기본값 : 0

    static String[] Arr_habit_progress; //습관 진행도 배열로 저장

    String habit_progress = "";

    int Get_intColor = 0;

    //최초실행
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        context = this;

        //객체 인스턴스 초기화
        //btn_finish = (Button) findViewById(R.id.btn_finish); //앱 종료
        btn_Color_Picker = (Button) findViewById(R.id.btn_Color_Picker); //컬러 선택 버튼
        View_Color_Pick = (TextView) findViewById(R.id.View_Color_Pick); //컬러 선택 값 (int)
        layout = (ConstraintLayout) findViewById(R.id.layout); //레이아웃
        btn_OK = (Button) findViewById(R.id.btn_OK); //확인 버튼
        edit_Habit_Name = (EditText) findViewById(R.id.edit_Habit_Name); //습관명 (EditText)
        edit_Habit_Day_Num = (EditText) findViewById(R.id.edit_Habit_Day_Num); //습관 목표일 수
        Warning_Habit_Name = (TextView) findViewById(R.id.Warning_Habit_Name); //습관명 비어있을 경우 경고
        Warning_Habit_Num = (TextView) findViewById(R.id.Warning_Habit_Num); //습관 목표일 수 비었을 경우 경고
        txtHabit = (TextView) findViewById(R.id.txtHabit);
        txtObjdays = (TextView) findViewById(R.id.txtObjdays);

        QUERY_Settings();
        if(DB_darkmode == 0 || DB_darkmode == 3) {
            layout.setBackgroundColor(Color.rgb(255,235,211));
            getWindow().setStatusBarColor(Color.parseColor("#FFEBD3"));
            txtHabit.setTextColor(Color.BLACK);
            txtObjdays.setTextColor(Color.BLACK);
            //status bar 글자 색상
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        else {
            layout.setBackgroundColor(Color.rgb(39,43,54));
            getWindow().setStatusBarColor(Color.parseColor("#272B36"));

            txtHabit.setTextColor(Color.WHITE);
            txtObjdays.setTextColor(Color.WHITE);
        }

        //버튼 클릭리스너

        //컬러 선택(습관앱 색상 정함)
        btn_Color_Picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Color();
                //openColorPicker();
            }
        });

        //앱 종료
        /*btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); //앱 종료
            }
        });*/

        //확인 버튼
        btn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_Habit_Name.getText().toString().length() > 0 &&
                        edit_Habit_Day_Num.getText().toString().length() > 0
                        && int_Color < 0) //습관명, 목표일 수, 습관 색이 전부 정해져야 넘어간다.
                {
                    if(Integer.parseInt(edit_Habit_Day_Num.getText().toString()) > 30)
                    {
                        Toast.makeText(CreateActivity.this,"습관을 30일까지 지정가능", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                    intent.putExtra("Habit_Name",edit_Habit_Name.getText().toString()); //습관명
                    //intent.putExtra("Habit_Color",Integer.toString(int_Color)); //습관 색깔
                    intent.putExtra("Habit_Color",Get_intColor);
                    intent.putExtra("edit_Habit_Day_Num",edit_Habit_Day_Num.getText().toString()); //습관 목표일 수
                    MainActivity.TotalHabit +=1; //습관 버튼 개수


                    //Setting 테이블 최초 한번 실행
                    /*String Settings_Check = PreferenceManager.getString(context,"최초");

                    if(Settings_Check.equals("")) {
                        PreferenceManager.setString(context,"최초","0");
                        //최초 실행
                        INSERT_Settings();
                    }*/

                    //QUERY_Settings();
                    if(DB_darkmode ==3)
                        INSERT_Settings();

                    //habits 테이블을 추가한다.
                    INSERT_Habits();



                    startActivity(intent);
                }
                else
                {
                    if(edit_Habit_Name.getText().toString().length() <= 0) //습관명이 안써진 경우
                    {
                        edit_Habit_Name.requestFocus(); //포커스 준다.
                        //키보드 보이게 한다
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                        Warning_Habit_Name.setTextColor(Color.RED);
                        Warning_Habit_Name.setText("습관명을 입력하세요.");
                    }
                    else if(edit_Habit_Name.getText().toString().length() > 0 &&
                            edit_Habit_Day_Num.getText().toString().length() <= 0) //습관 목표일 수 안쓴 경우
                    {
                        edit_Habit_Day_Num.requestFocus(); //포커스 준다.
                        //키보드 보이게
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.toggleSoftInput(inputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

                        Warning_Habit_Name.setText("");
                        Warning_Habit_Num.setTextColor(Color.RED);
                        Warning_Habit_Num.setText("습관 목표일을 정하세요.");
                    }
                    else if (edit_Habit_Name.getText().toString().length() > 0 &&
                            edit_Habit_Day_Num.getText().toString().length() > 0 && int_Color == 0) //습관 색 입력 안된 경우
                    {
                        Warning_Habit_Name.setText("");
                        Warning_Habit_Num.setText("");
                        View_Color_Pick.setTextColor(Color.RED);
                        View_Color_Pick.setText("습관 색을 정하세요.");
                    }
                }
            }
        });
    }

    //설정값 추가 한번만 실행
    public void INSERT_Settings() {
        Log.e("CreateActivity.java", " INSERT_Settings 메서드 호출됨.");

        String uriString = "content://com.example.dotive/Settings";
        Uri uri = new Uri.Builder().build().parse(uriString);

        Cursor cursor = getContentResolver().query(uri, null,null,null,null);
        String[] columns = cursor.getColumnNames();
        Log.e("CreateActivity.java", "columns count = " + columns.length);

        for(int i = 0; i < columns.length; i++) {
            Log.e("CreateActivity.java", "settings 레코드 " + i + " : " + columns[i]);
        }

        ContentValues values = new ContentValues();
        values.put("darkmode", 0); //기본값 0
        Log.e("CreateActivity.java", "다크모드 기본 값 0");
        values.put("language", "한국어"); //기본값 한국어 , English 등.
        Log.e("CreateActivity.java", "언어 : 한국어");

        uri = getContentResolver().insert(uri, values);
        Log.e("CreateActivity.java", "(settings) INSERT 결과 : " + uri.toString());
    }

    //습관을 추가한다.
    public void INSERT_Habits() {
        Log.e("CreateActivity.java", " INSERT_Habits 메서드 호출됨.");

        String uriString = "content://com.example.dotive/Habits";
        Uri uri = new Uri.Builder().build().parse(uriString);

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String[] columns = cursor.getColumnNames();
        Log.e("CreateActivity.java", "columns count = " + columns.length);

        for (int i = 0; i < columns.length; i++) {
            Log.e("CreateActivity.java", "레코드 " + i + " : " + columns[i]);
        }

        ContentValues values = new ContentValues();
        values.put("habitName", edit_Habit_Name.getText().toString()); //습관명
        Log.e("CreateActivity.java","습관명 : " + edit_Habit_Name.getText().toString());
        values.put("habitColor", Integer.toString(int_Color)); //습관 색깔
        Log.e("CreateActivity.java","습관 색깔 : " + int_Color);
        values.put("objDays", edit_Habit_Day_Num.getText().toString()); //습관 목표일 수
        Log.e("CreateActivity.java","습관 목표일 수 : " + edit_Habit_Day_Num.getText().toString());


        int progress = Integer.parseInt(edit_Habit_Day_Num.getText().toString());

        //습관 목표일 수 만큼 0을 그려서 habitsProgress 값에 문자열으로 저장.
        for (int i = 0; i <progress; i++) {
                habit_progress += "0,";
        }

        habit_progress = habit_progress.substring(0, habit_progress.length() - 1); //마지막 쉼표 제거

        values.put("habitProgress",habit_progress); //배열로 추가시키고 문자열로 변환
        Log.e("CreateActivity.java","습관 프로그레스 : " + habit_progress);

        //날짜 포맷 형식
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //현재 습관 생성한 날짜를 저장한다.

        Calendar CreateDay = Calendar.getInstance(); //현재 날짜 (=습관 생성 날짜)
        String create_day = simpleDateFormat.format(CreateDay.getTime());

        values.put("habitCreateDay", create_day);
        //values.put("habitCreateDay", "2020-06-26"); //테스트용
        Log.e("CreateActivity.java", "습관 생성 날짜: " + create_day);

        uri = getContentResolver().insert(uri, values);
        Log.e("CreateActivity.java", "INSERT 결과 : " + uri.toString());
    }

    public void QUERY_Settings() {
        try {
            String uriString = "content://com.example.dotive/Settings";
            Uri uri = new Uri.Builder().build().parse(uriString);

            String[] columns = new String[] {"darkmode","language"};
            Cursor cursor = getContentResolver().query(uri, columns, null, null, null);
            Log.e("MainActivity.java", "QUERY 결과 (Settings) : " + cursor.getCount());

            while (cursor.moveToNext()) {
                int darkmode = cursor.getInt(cursor.getColumnIndex(columns[0]));
                //String language = cursor.getString(cursor.getColumnIndex(columns[1]));

                DB_darkmode = darkmode;
                //DB_language = language;

                Log.e("MainActivity.java", "Settings 레코드 " + darkmode + ", ");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Color() {
        new ColorPickerDialog()
                .withTheme(R.style.ColorPickerDialog_Dark)
                .withCornerRadius(30)
                .withTitle("습관 색상 선택")
                .withAlphaEnabled(false)
                .withColor(Color.parseColor("#b39ddb")) //default color
                .withPresets(
                        Color.parseColor("#ffab91"),
                        Color.parseColor("#F48FB1"),
                        Color.parseColor("#ce93d8"),
                        Color.parseColor("#b39ddb"),
                        Color.parseColor("#9fa8da"),
                        Color.parseColor("#90caf9"),
                        Color.parseColor("#81d4fa"),
                        Color.parseColor("#80deea"),
                        Color.parseColor("#80cbc4"),
                        Color.parseColor("#c5e1a5"),
                        Color.parseColor("#e6ee9c"),
                        Color.parseColor("#fff59d"),
                        Color.parseColor("#ffe082"),
                        Color.parseColor("#bcaaa4"),

                        Color.parseColor("#ffebee"),
                        Color.parseColor("#fce4ec"),
                        Color.parseColor("#f3e5f5"),
                        Color.parseColor("#ede7f6"),
                        Color.parseColor("#e8eaf6"),
                        Color.parseColor("#e3f2fd"),
                        Color.parseColor("#e1f5fe"),
                        Color.parseColor("#e0f7fa"),
                        Color.parseColor("#e0f2f1"),
                        Color.parseColor("#e8f5e9"),
                        Color.parseColor("#f1f8e9"),
                        Color.parseColor("#f9fbe7"),
                        Color.parseColor("#fffde7"),
                        Color.parseColor("#fff3e0"),
                        Color.parseColor("#fbe9e7"),
                        Color.parseColor("#efebe9"),
                        Color.parseColor("#fafafa"),
                        Color.parseColor("#eceff1"),

                        Color.parseColor("#f44336"),
                        Color.parseColor("#e91e63"),
                        Color.parseColor("#9c27b0"),
                        Color.parseColor("#673ab7"),
                        Color.parseColor("#3f51b5"),
                        Color.parseColor("#2196f3"),
                        Color.parseColor("#03a9f4"),
                        Color.parseColor("#00bcd4"),
                        Color.parseColor("#009688"),
                        Color.parseColor("#4caf50"),
                        Color.parseColor("#8bc34a"),
                        Color.parseColor("#cddc39"),
                        Color.parseColor("#ffeb3b"),
                        Color.parseColor("#ffc107"),
                        Color.parseColor("#ff9800"),
                        Color.parseColor("#ff5722"),
                        Color.parseColor("#795548"),
                        Color.parseColor("#9e9e9e"),
                        Color.parseColor("#607d8b"),
                        Color.parseColor("#ffffff"),
                        Color.parseColor("#000000")
                )
                .withListener(new OnColorPickedListener<ColorPickerDialog>() {
                    @Override
                    public void onColorPicked(@Nullable ColorPickerDialog pickerView, int color) {
                        //layout.setBackgroundColor(color);//목표색 배경에 뿌림 (test 용)
                        //View_Color_Pick.setText(Integer.toString(color)); //컬러값 int로 전달 (ex : -21615)
                        int_Color = color; //intent 할 변수
                        String hexColor = String.format("#%06X", (0xFFFFFFF & color)); //습관 색 int - > hex 변환
                        View_Color_Pick.setText(hexColor);
                        Log.e("습관컬러 hex 변환",View_Color_Pick.getText().toString());
                        Get_intColor = color;

                        if(Get_intColor != 0)
                            btn_Color_Picker.setBackgroundColor(color);
                    }
                })
                .show(getSupportFragmentManager(), "Color Picker");

    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(this); //ColorPicker 객체 생성

        final ArrayList<String> colors = new ArrayList<>(); //배열에 원하는 색 담아둔다.

        colors.add("#ffab91");
        colors.add("#F48FB1");
        colors.add("#ce93d8");
        colors.add("#b39ddb");
        colors.add("#9fa8da");
        colors.add("#90caf9");
        colors.add("#81d4fa");
        colors.add("#80deea");
        colors.add("#80cbc4");
        colors.add("#c5e1a5");
        colors.add("#e6ee9c");
        colors.add("#fff59d");
        colors.add("#ffe082");
        colors.add("#ffcc80");
        colors.add("#bcaaa4");

        //setColumns 00열 배치
        //setRoundColorButton 둥글게 이값 없으면 네모
        colorPicker.setColors(colors)
                .setColumns(5)
                .setRoundColorButton(true)
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {

                        //layout.setBackgroundColor(color);//목표색 배경에 뿌림 (test 용)
                        //View_Color_Pick.setText(Integer.toString(color)); //컬러값 int로 전달 (ex : -21615)
                        int_Color = color; //intent 할 변수
                        String hexColor = String.format("#%06X", (0xFFFFFFF & color)); //습관 색 int - > hex 변환
                        View_Color_Pick.setText(hexColor);
                        Log.e("습관컬러 int 값 ",String.valueOf(color));
                        Log.e("습관컬러 hex 변환",View_Color_Pick.getText().toString());
                        Get_intColor = color;

                        if(Get_intColor != 0)
                            btn_Color_Picker.setBackgroundColor(Get_intColor);
                    }

                    @Override
                    public void onCancel() {
                        //취소할때
                    }
                }).show();
    }
}