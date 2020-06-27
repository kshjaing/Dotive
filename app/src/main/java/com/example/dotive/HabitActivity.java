package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

/* */
public class HabitActivity extends AppCompatActivity {

    public static String name;
    public static String objdays;
    public static String progress;
    public static String createday;
    public static String Get_i;
    public static String[] Arr_Progress = {};
    String New_Progress = ""; //변경된 습관 진행도 값 //이건 static 하면 누적되서 안됨.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        //최종목표 페이지에서 받아온 버튼 id (string)
        TextView t_Name = (TextView)findViewById(R.id.t_Name);
        TextView t_Progress = (TextView)findViewById(R.id.t_Progress);
        TextView t_Create_Day = (TextView)findViewById(R.id.t_Create_Day);
        TextView t_Objdays = (TextView)findViewById(R.id.t_Objdays);
        TextView t_Get_i = (TextView)findViewById(R.id.t_Get_i);
        TextView t_Get_Count = (TextView)findViewById(R.id.t_Get_Count);
        long Long_Get_Count = 0;
        Button button = (Button)findViewById(R.id.button);

        Intent intent = getIntent(); //데이터 받아옴

        //최종목표 명 :
        //MainActivity 에서 선언한 key 값 그대로 써야함.
        name = intent.getExtras().getString("Habit_Name"); //습관명
        objdays = intent.getExtras().getString("Habit_ObjDays"); //습관 목표일 수
        progress = intent.getExtras().getString("Habit_Progress"); //습관 진행도 (ex: 0,0,0,1,0,0,)
        createday = intent.getExtras().getString("Habit_Create_Day"); //습관 생성 날짜
        Get_i = intent.getExtras().getString("Get_i"); //현재 무슨 버튼인지 알아야함.
        final Long Get_Count = intent.getLongExtra("Count",Long_Get_Count); //오늘 위치 계산값

        t_Name.setText(name);
        t_Objdays.setText(objdays);
        t_Progress.setText(progress);
        t_Create_Day.setText(createday);
        t_Get_i.setText(Get_i);
        t_Get_Count.setText(Long.toString(Get_Count));

        //t_Get_Count.setText(Long.toString(MainActivity.count));
        //버튼을 누르면 다시 메인 페이지로 가면서 동그라미 버튼이 변한다.
        //동그라미 변화하는 코딩은 ondraw에서 다시 해야할 것이고

        //DB에서 습관 진행도 0000에서 변환된 값은 1로 변환시킨다.
        //DB 변환은 여기서 업데이트를 한 후  값만 넘긴다.

        button.setOnClickListener(new View.OnClickListener() {
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
                startActivity(intent1);*/
                //잘 변경된다.

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
                Intent intent1 = new Intent(HabitActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }

    public void UPDATE_Habits() {
        String uriString = "content://com.example.dotive/Habits";
        Uri uri = new Uri.Builder().build().parse(uriString);

        String selection = "habitName = ?";
        String[] selectionArgs = new String[] {name};
        ContentValues updateValue = new ContentValues();
        updateValue.put("habitProgress",New_Progress);
        int count = getContentResolver().update(uri, updateValue, selection, selectionArgs);
        Log.e("HabitActivity.java","습관 변경한 레코드:"+ count);
    }
}
