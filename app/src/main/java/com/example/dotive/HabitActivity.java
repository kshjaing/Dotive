package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* */
public class HabitActivity extends AppCompatActivity {

    ImageButton ibtnBack;
    String id;
    String name;
    String objdays;
    String progress;
    String createday;
    String Get_i;
    String[] Arr_Progress = {};
    String New_Progress = ""; //변경된 습관 진행도 값 //이건 static 하면 누적되서 안됨.
    Button[] Habit_Buttons;
    LinearLayout ll_habit;
    int ButtonCount = 0; //목표일수 같거나 초과 : 목표일수 , 목표일수 아래 : 오늘날짜 수 만큼
    String[] DateCount = {}; //생성일 - > 오늘날짜 까지의 더한 날짜값
    String[] Week = {"일요일","월요일","화요일","수요일","목요일","금요일","토요일"};
    //String[] Year_Month_Day = {}; //모양 배치를 위해 날짜를 자름 (ex: 2020-05-05 - > 2020 , 05,  05)
    int index = 0;
    int[] Arr_index = {};
    int i = 0;

    TextView txtHabitName;
    TextView txtObjDays;

    //db에서 다크모드 값 , 언어 값
    public int DB_darkmode = 0; // 1 = > 다크 , 기본값 : 0

    ScrollView sv_habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_habit);

        //최종목표 페이지에서 받아온 버튼 id (string)
        /*TextView t_Name = (TextView)findViewById(R.id.t_Name);
        TextView t_Progress = (TextView)findViewById(R.id.t_Progress);
        TextView t_Create_Day = (TextView)findViewById(R.id.t_Create_Day);
        TextView t_Objdays = (TextView)findViewById(R.id.t_Objdays);
        TextView t_Get_i = (TextView)findViewById(R.id.t_Get_i);
        TextView t_Get_Count = (TextView)findViewById(R.id.t_Get_Count);

        Button button = (Button)findViewById(R.id.button);*/

        //xml inflater
        View view_habit = (View) getLayoutInflater().inflate(R.layout.activity_habit, null);

        //버튼 담을 리니어 레이아웃
        ll_habit = (LinearLayout) view_habit.findViewById(R.id.ll_habit);
        //뒤로가기 버튼 - > MainActivity 이동
        ibtnBack = (ImageButton) view_habit.findViewById(R.id.ibtnBack);

        txtHabitName = (TextView) view_habit.findViewById(R.id.txtHabitName);
        txtObjDays = (TextView) view_habit.findViewById(R.id.txtObjDays);

        sv_habit = (ScrollView) view_habit.findViewById(R.id.sv_habit);

        long Long_Get_Count = 0;
        Intent intent = getIntent(); //데이터 받아옴

        //최종목표 명 :
        //MainActivity 에서 선언한 key 값 그대로 써야함.
        id = intent.getExtras().getString("id"); //습관 id증가 값
        name = intent.getExtras().getString("Habit_Name"); //습관명
        objdays = intent.getExtras().getString("Habit_ObjDays"); //습관 목표일 수
        progress = intent.getExtras().getString("Habit_Progress"); //습관 진행도 (ex: 0,0,0,1,0,0,)
        createday = intent.getExtras().getString("Habit_Create_Day"); //습관 생성 날짜
        Get_i = intent.getExtras().getString("Get_i"); //현재 무슨 버튼인지 알아야함.
        final Long Get_Count = intent.getLongExtra("Count",Long_Get_Count); //오늘 위치 계산값

        /*t_Name.setText(name);
        t_Objdays.setText(objdays);
        t_Progress.setText(progress);
        t_Create_Day.setText(createday);
        t_Get_i.setText(Get_i);
        t_Get_Count.setText(Long.toString(Get_Count));*/

        Log.e("HabitActivity.java", "습관 id : "+id);
        Log.e("HabitActivity.java", "습관명 : "+name);
        Log.e("HabitActivity.java", "습관 목표일 수 : "+objdays);
        Log.e("HabitActivity.java", "습관 진행도 : "+progress);
        Log.e("HabitActivity.java", "습관 생성 날짜 : "+createday);
        Log.e("HabitActivity.java", "현재 버튼 번호 : "+Get_i);
        Log.e("HabitActivity.java", "목표일 수에서 오늘 날짜 위치 : "+Get_Count);

        //t_Get_Count.setText(Long.toString(MainActivity.count));
        //버튼을 누르면 다시 메인 페이지로 가면서 동그라미 버튼이 변한다.
        //동그라미 변화하는 코딩은 ondraw에서 다시 해야할 것이고

        //DB에서 습관 진행도 0000에서 변환된 값은 1로 변환시킨다.
        //DB 변환은 여기서 업데이트를 한 후  값만 넘긴다.
        //습관명
        int textSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics());
        //습관 목표일 수
        int testSize2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics());
        //습관 각 요일
        int testSize3 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());

        //습관 각 요일 패딩 좌우
        int habit_padding_Left_Right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        int habit_padding_Top_Bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());

        //습관 각 요일 마진 탑 바텀
        int habit_Margin_Top = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
        int habit_Margin_Bottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());

        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/katuri.ttf");
        QUERY_Settings();

        txtHabitName.setTextSize(textSize);
        txtObjDays.setTextSize(testSize2);
        txtHabitName.setTypeface(typeface);
        txtObjDays.setTypeface(typeface);
        txtHabitName.setText(name);
        txtObjDays.setText("(목표 "+ objdays+"일)");


        if(DB_darkmode == 0) {
            txtHabitName.setTextColor(Color.BLACK);
            txtObjDays.setTextColor(Color.BLACK);
            sv_habit.setBackgroundColor(Color.parseColor("#FFEBD3"));
            ibtnBack.setBackgroundResource(R.drawable.arrow_back_dark);
            getWindow().setStatusBarColor(Color.parseColor("#FFEBD3"));
        }
        else {
            txtHabitName.setTextColor(Color.WHITE);
            txtObjDays.setTextColor(Color.WHITE);
            sv_habit.setBackgroundColor(Color.parseColor("#272B36"));
            ibtnBack.setBackgroundResource(R.drawable.arrow_back);
            getWindow().setStatusBarColor(Color.parseColor("#272B36"));
        }

        //들어온 습관 수 ex : 5일을 목표로 한다면
        //그중 오늘 날짜 위치만큼 버튼을 생성한다.
        //그전에 목표일 수보다 오늘날짜 위치가 같거나 높아지는 경우 목표일 수만큼 채우도록 한다.
        //날짜는 날짜 더하기 함수로 더해줘서 버튼이름에 적용시킨다.


        if(Integer.parseInt(objdays) == Integer.parseInt(Long.toString(Get_Count)) ||
                Integer.parseInt(objdays) < Integer.parseInt(Long.toString(Get_Count)))
        {
            Habit_Buttons = new Button[Integer.parseInt(objdays)];
            ButtonCount = Integer.parseInt(objdays);
        }
        else
        {
            Habit_Buttons = new Button[Integer.parseInt(Long.toString(Get_Count)) + 1];
            ButtonCount = Integer.parseInt(Long.toString(Get_Count)) + 1;
        }

        DateCount = new String[ButtonCount];
        index = ButtonCount;
        Arr_index = new int[index];
        //Date 클래스
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Arr_Progress = new String[Integer.parseInt(objdays)]; //습관 진행도 배열 공간 목표일 수 만큼 생성;

        Arr_Progress = progress.split(","); //습관 진행도 값 (Ex: 0,0,0,0,0 ) 잘라 배열에 저장.

        //margin 설정
        LinearLayout.LayoutParams textView_LinearParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        //textView_LinearParams.weight = 1.0f; //일정한 비율을 맞춘다.
        textView_LinearParams.gravity = Gravity.CENTER;
        textView_LinearParams.setMargins(0,habit_Margin_Top,0,habit_Margin_Bottom);
        //반복해서 버튼 추가
        for(int i = 0; i<ButtonCount; i++)
        {
            try {
                Date date = dateFormat.parse(createday);

                //날짜 더하기 생성된 버튼 카운트 만큼
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, i);

                int w = calendar.get(Calendar.DAY_OF_WEEK) -1;
                String day = Week[w];
                DateCount[i] = dateFormat.format(calendar.getTime()) + " " + day;
                Log.e("HabitActivity.java", "버튼 수만큼 날짜 더한 값 : "+ dateFormat.format(calendar.getTime()));
                Log.e("HabitActivity.java", "각 날짜당 요일 :"+ Week[w]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //마지막날은 디자인이 다르다.
        for(int i = 0; i < ButtonCount; i++)
        {

            index -=1;
            Arr_index[i] = index;
            String a = DateCount[index];
            a = a.replace("-",".");
            Habit_Buttons[i] = new Button(this);
            ll_habit.addView(Habit_Buttons[i]);
            Habit_Buttons[i].setText(a);//DateCount[index]

            Habit_Buttons[i].setLayoutParams(textView_LinearParams);
            Habit_Buttons[i].setTypeface(typeface);
            Habit_Buttons[i].setTextSize(testSize3);
            Habit_Buttons[i].setPadding(habit_padding_Left_Right,habit_padding_Top_Bottom,habit_padding_Left_Right,habit_padding_Top_Bottom);

            if(DB_darkmode == 0) Habit_Buttons[i].setTextColor(Color.BLACK);
            else Habit_Buttons[i].setTextColor(Color.WHITE);


            if(Integer.parseInt(Arr_Progress[index]) == 1)
            {
                if(DB_darkmode == 0) Habit_Buttons[i].setBackgroundResource(R.drawable.habitbtn_border_round_pressed);
                else Habit_Buttons[i].setBackgroundResource(R.drawable.habitbtn_border_round);

                Habit_Buttons[i].setTag("1");
                Habit_Buttons[i].setTextColor(Color.BLACK);
            }
            else
            {
                Habit_Buttons[i].setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                Habit_Buttons[i].setTag("0");
                Habit_Buttons[i].setTextColor(Color.WHITE);
            }

            if(index == 0) {
                a = DateCount[i];
                a = a.replace("-",".");
                Habit_Buttons[index].setText(a + " (오늘)");
                if(DB_darkmode == 0) Habit_Buttons[index].setBackgroundResource(R.drawable.habitbtn_border_round_stroke);
                else Habit_Buttons[index].setBackgroundResource(R.drawable.habitbtn_border_round_stroke_dark);

                Habit_Buttons[index].setLayoutParams(textView_LinearParams);
                Habit_Buttons[index].setTypeface(typeface);
                Habit_Buttons[index].setTextSize(testSize3);
                Habit_Buttons[index].setPadding(habit_padding_Left_Right,habit_padding_Top_Bottom,habit_padding_Left_Right,habit_padding_Top_Bottom);

                if(Integer.parseInt(Arr_Progress[i]) == 1)
                {
                    Habit_Buttons[index].setBackgroundResource(R.drawable.habitbtn_border_round);
                    Habit_Buttons[index].setTag("1");
                }
            }

            final int q = i;
            //버튼 클릭 리스너 (누를 시 DB에서 달성한 값 적용 , 한번 더 누르면 다시 적용)
            Habit_Buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    New_Progress = "";
                    if(Habit_Buttons[q].getTag() == "1")
                    {
                        String a = Integer.toString(Arr_index[q]);
                        int b = Integer.parseInt(a);
                        Arr_Progress[b] = "0"; //변환된 값

                        ///

                        if(Habit_Buttons[q].getText().toString().contains("(오늘)")) {
                            if(DB_darkmode == 0) Habit_Buttons[q].setBackgroundResource(R.drawable.habitbtn_border_round_stroke);
                            else Habit_Buttons[q].setBackgroundResource(R.drawable.habitbtn_border_round_stroke_dark);
                        }
                        else {
                            if(DB_darkmode == 0) Habit_Buttons[q].setBackgroundResource(R.drawable.habitbtn_border_round_pressed);
                            else Habit_Buttons[q].setBackgroundResource(R.drawable.habitbtn_border_round_dark);
                        }

                        Habit_Buttons[q].setTextColor(Color.WHITE);
                        Habit_Buttons[q].setTag("0");
                        //UPDATE_Habits2();

                        for(int j = 0; j<Integer.parseInt(objdays); j++){
                            New_Progress += Arr_Progress[j] + ",";
                        }
                        New_Progress = New_Progress.substring(0, New_Progress.length() - 1); //마지막 쉼표 제거
                    }
                    else
                    {
                        String a = Integer.toString(Arr_index[q]);
                        int b = Integer.parseInt(a);
                        Arr_Progress[b] = "1"; //변환된 값

                        ///
                        Habit_Buttons[q].setBackgroundResource(R.drawable.habitbtn_border_round);
                        Habit_Buttons[q].setTextColor(Color.BLACK);
                        Habit_Buttons[q].setTag("1");
                        //UPDATE_Habits2();
                        for(int j = 0; j<Integer.parseInt(objdays); j++){
                            New_Progress += Arr_Progress[j] + ",";
                        }
                        New_Progress = New_Progress.substring(0, New_Progress.length() - 1); //마지막 쉼표 제거
                    }
                    UPDATE_Habits2();
                }
            });
        }

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        setContentView(view_habit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(this,MainActivity.class));
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

    public void UPDATE_Habits2() {
        String uriString = "content://com.example.dotive/Habits";
        Uri uri = new Uri.Builder().build().parse(uriString);

        //String sqlInsert = "UPDATE Habits SET New_Progress = '" + New_Progress +"' WHERE id="+Integer.parseInt(id);
        String selection = "id = ?";
        String[] selectionArgs = new String[] {id}; //습관 id증가 값
        ContentValues updateValue = new ContentValues();
        updateValue.put("habitProgress", New_Progress); //습관 진행도 (오늘날짜 변경됨)
        int count = getContentResolver().update(uri, updateValue, selection, selectionArgs);
        Log.e("HabitActivity.java","습관 변경한 레코드:"+ count);
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

    /*public void UPDATE_Habits() {
        String uriString = "content://com.example.dotive/Habits";
        Uri uri = new Uri.Builder().build().parse(uriString);

        String selection = "habitName = ?";
        String[] selectionArgs = new String[] {name};  //습관명
        ContentValues updateValue = new ContentValues();
        updateValue.put("habitProgress",New_Progress); //습관 진행도 (오늘날짜 변경됨)
        int count = getContentResolver().update(uri, updateValue, selection, selectionArgs);
        Log.e("HabitActivity.java","습관 변경한 레코드:"+ count);
    }*/

    /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //1. 습관 진행도를 배열로 나눈다.
                //2. 배열 0 1 2 3 4 5 이렇게 있는데
                //3. 목표일 수에서 오늘 날짜 위치 번호 즉 7 이렇게 오면
                // 배열 [7] 이렇게 담아서 그 값을 1로 변환한다.
                //다시 그 배열을 string 으로 하나로 붙인다.
                //그 값을 UPDATE문을 이용해서 현재 버튼 번호 4를 이용해서 그 값 업데이트 한다.

                //일단 실험으로
                //현재 버튼 번호 값에 맞는 습관을 찾아 그 습관 명을 변경시켜본다.
                /*UPDATE_Habits();
                Intent intent1 = new Intent(HabitActivity.this, MainActivity.class);
                startActivity(intent1);
                //잘 변경된다.////////////////////

                Arr_Progress = new String[Integer.parseInt(objdays)]; //습관 진행도 배열 공간 목표일 수 만큼 생성;

                Arr_Progress = progress.split(","); //습관 진행도 값 (Ex: 0,0,0,0,0 ) 잘라 배열에 저장.
                String a = Long.toString(Get_Count);
                int b = Integer.parseInt(a);
                Arr_Progress[b] = "1"; //변환된 값

                for(int i = 0; i<Integer.parseInt(objdays); i++){
                    New_Progress += Arr_Progress[i] + ",";
                }
                New_Progress = New_Progress.substring(0, New_Progress.length() - 1); //마지막 쉼표 제거

                UPDATE_Habits();
                Log.e("HabitActivity.java", "변경된 진행도 값 : " + New_Progress);
                Intent intent1 = new Intent(HabitActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });*/
}
