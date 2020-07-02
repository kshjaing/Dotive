package com.example.dotive;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.sql.Struct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

import static com.example.dotive.MainActivity.calDate;
import static com.example.dotive.MainActivity.context_main;
import static com.example.dotive.MainActivity.createDateArr;
import static com.example.dotive.MainActivity.db;
import static com.example.dotive.MainActivity.dbHelper;
import static com.example.dotive.MainActivity.habitProgressArr;
import static com.example.dotive.MainActivity.isDarkmode;
import static com.example.dotive.MainActivity.oneCount;
import static com.example.dotive.MainActivity.totalHabit;
import static com.example.dotive.MainActivity.dateDiff;

public class DrawCircle extends View {
    public static int[] oneIndex;                   //진행도 문자열에서 1이 어느 인덱스에 있는지 담는 배열
    Paint paint, strokePaint, completePaint;
    Cursor cursor;
    Integer[] objectDays = new Integer[totalHabit];
    float btn_x, btn_y;
    String[] progressWord;
    int[] progressLength;
    Calendar calendar;
    SimpleDateFormat dateFormat;



    int btn_Width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,360,
            getResources().getDisplayMetrics());
    int btn_Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,220,
            getResources().getDisplayMetrics());
    float radius = (float) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,36,
            getResources().getDisplayMetrics());

    public DrawCircle(Context context) {
        super(context);
        init(context);
    }

    public DrawCircle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        strokePaint = new Paint();
        completePaint = new Paint();
        dbHelper = new DBHelper(context_main, 4);
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 (E)", Locale.KOREAN);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        db = dbHelper.getWritableDatabase();

        for (int i = 0; i < totalHabit; i++) {
            //paint[i].setAntiAlias(true);
            //DB에서 각 습관별 목표일수 뽑아옴
            cursor = db.rawQuery("SELECT objDays FROM Habits", null);
            cursor.moveToPosition(i);
            objectDays[i] = Integer.parseInt(cursor.getString(0));

            progressWord = new String[objectDays[i]];



            btn_x = MainActivity.boxBtnArr[i].getX();
            btn_y = MainActivity.boxBtnArr[i].getY();
            paint.setAntiAlias(true);
            strokePaint.setStrokeWidth(25);
            strokePaint.setStyle(Paint.Style.STROKE);
            strokePaint.setAntiAlias(true);

            if (isDarkmode == 0) {
                paint.setColor(Color.parseColor("#d9d9d9"));
                strokePaint.setColor(Color.parseColor("#ffb313"));
            }
            else {
                paint.setColor(Color.parseColor("#7b879b"));
                strokePaint.setColor(Color.parseColor("#ffffff"));
            }

            //DB에서 각 습관의 색깔값을 가져와서 적용
            cursor = db.rawQuery("SELECT habitColor FROM Habits", null);
            cursor.moveToPosition(i);
            String color = cursor.getString(0);
            switch (color) {
                case "red" : completePaint.setColor(Color.parseColor("#e14f50"));
                    break;
                case "orange" : completePaint.setColor(Color.parseColor("#FF5722"));
                    break;
                case "green" :  completePaint.setColor(Color.parseColor("#74d78d"));
                    break;
                case "blue" : completePaint.setColor(Color.parseColor("#347ec7"));
                    break;
                case "purple" : completePaint.setColor(Color.parseColor("#c3a0dc"));
                    break;
                case "gray" : completePaint.setColor(Color.parseColor("#607d8b"));
                    break;
            }

            //-------------------------목표일수에 따른 원 크기와 위치 로직---------------------------

            //목표일수 1~7일
            switch (objectDays[i]) {
                case 1:
                    //현재날짜 테두리
                    if (Integer.parseInt(dateDiff[i]) == 0) {
                        canvas.drawCircle(btn_x + btn_Width / 2, btn_y + btn_Height / 2, radius, strokePaint);
                    }

                    //원 1개
                    if (oneCount[i] == 0) {
                        canvas.drawCircle(btn_x + btn_Width / 2, btn_y + btn_Height / 2, radius, paint);
                    }
                    else{
                        canvas.drawCircle(btn_x + btn_Width / 2, btn_y + btn_Height / 2, radius, completePaint);
                    }
                    break;

                case 2:
                    //현재날짜 테두리
                    if (Integer.parseInt(dateDiff[i]) == 0) {
                        canvas.drawCircle(btn_x + (btn_Width / 2) - radius * 1.5f, btn_y + btn_Height / 2, radius, strokePaint);
                    }
                    else if (Integer.parseInt(dateDiff[i]) == 1){
                        canvas.drawCircle(btn_x + (btn_Width / 2) + radius * 1.5f, btn_y + btn_Height / 2, radius, strokePaint);
                    }

                    switch(habitProgressArr[i]){
                        case "01":
                            canvas.drawCircle(btn_x + (btn_Width / 2) - radius * 1.5f, btn_y + btn_Height / 2, radius, paint);
                            canvas.drawCircle(btn_x + (btn_Width / 2) + radius * 1.5f, btn_y + btn_Height / 2, radius, completePaint);
                            break;
                        case "10":
                            canvas.drawCircle(btn_x + (btn_Width / 2) - radius * 1.5f, btn_y + btn_Height / 2, radius, completePaint);
                            canvas.drawCircle(btn_x + (btn_Width / 2) + radius * 1.5f, btn_y + btn_Height / 2, radius, paint);
                            break;
                        case "11":
                            canvas.drawCircle(btn_x + (btn_Width / 2) - radius * 1.5f, btn_y + btn_Height / 2, radius, completePaint);
                            canvas.drawCircle(btn_x + (btn_Width / 2) + radius * 1.5f, btn_y + btn_Height / 2, radius, completePaint);
                            break;
                        case "00":
                            canvas.drawCircle(btn_x + (btn_Width / 2) - radius * 1.5f, btn_y + btn_Height / 2, radius, paint);
                            canvas.drawCircle(btn_x + (btn_Width / 2) + radius * 1.5f, btn_y + btn_Height / 2, radius, paint);
                            break;
                    }
                    break;

                case 3:
                    for (int j = 0; j < 3; j++) {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j + 1) * 2.5f, btn_y + btn_Height / 2, radius, strokePaint);
                        }

                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }

                        if (progressWord[j].equals("1"))  {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 2.5f, btn_y + btn_Height / 2, radius, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 2.5f, btn_y + btn_Height / 2, radius, paint);
                        }

                    }
                    break;

                case 4:
                    for (int j = 0; j < 4; j++) {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j + 1) * 2, btn_y + btn_Height / 2, radius * 0.75f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1"))  {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 2, btn_y + btn_Height / 2, radius * 0.75f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 2, btn_y + btn_Height / 2, radius * 0.75f, paint);
                        }
                    }
                    break;

                case 5:
                    for (int j = 0; j < 5; j++) {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.68f, btn_y + btn_Height / 2, radius * 0.65f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.68f, btn_y + btn_Height / 2, radius * 0.65f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.68f, btn_y + btn_Height / 2, radius * 0.65f, paint);
                        }
                    }
                    break;
                case 6:
                    for (int j = 0; j < 6; j++) {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.43f, btn_y + btn_Height / 2, radius * 0.55f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.43f, btn_y + btn_Height / 2, radius * 0.55f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.43f, btn_y + btn_Height / 2, radius * 0.55f, paint);
                        }

                    }
                    break;
                case 7:
                    for (int j = 0; j < 7; j++) {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.26f, btn_y + btn_Height / 2, radius * 0.45f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.26f, btn_y + btn_Height / 2, radius * 0.45f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.26f, btn_y + btn_Height / 2, radius * 0.45f, paint);
                        }
                    }
                    break;
            }

            //목표일수 8~14일
            if (8 <= objectDays[i] && objectDays[i] <= 14) {
                for (int j = 0; j < objectDays[i]; j++) {
                    if (j < 7){
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.25f, btn_y + btn_Height / 2 - radius, radius * 0.45f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.25f, btn_y + btn_Height / 2 - radius, radius * 0.45f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 1.25f, btn_y + btn_Height / 2 - radius, radius * 0.45f, paint);
                        }

                    }
                    else {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j - 6) * 1.25f, btn_y + btn_Height / 2 + radius * 0.7f, radius * 0.45f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j - 6) * 1.25f, btn_y + btn_Height / 2 + radius * 0.7f, radius * 0.45f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j - 6) * 1.25f, btn_y + btn_Height / 2 + radius * 0.7f, radius * 0.45f, paint);
                        }

                    }
                }
            }

            //목표일수 15~20일
            else if (15 <= objectDays[i] && objectDays[i] <= 20) {
                for (int j = 0; j < objectDays[i]; j++) {
                    if (j < 10){
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j + 1) * 0.905f, btn_y + btn_Height / 2 - radius, radius * 0.3f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 0.905f, btn_y + btn_Height / 2 - radius, radius * 0.3f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 0.905f, btn_y + btn_Height / 2 - radius, radius * 0.3f, paint);
                        }

                    }
                    else {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j - 9) * 0.905f, btn_y + btn_Height / 2 + radius * 0.35f, radius * 0.3f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j - 9) * 0.905f, btn_y + btn_Height / 2 + radius * 0.35f, radius * 0.3f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j - 9) * 0.905f, btn_y + btn_Height / 2 + radius * 0.35f, radius * 0.3f, paint);
                        }

                    }
                }
            }

            //목표일수 21~30일
            else if (21 <= objectDays[i] && objectDays[i] <= 30) {
                for (int j = 0; j < objectDays[i]; j++) {
                    if (j < 10) {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j + 1) * 0.905f, btn_y + radius * 1.7f, radius * 0.3f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 0.905f, btn_y + radius * 1.7f, radius * 0.3f, completePaint);

                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j + 1) * 0.905f, btn_y + radius * 1.7f, radius * 0.3f, paint);
                        }

                    }
                    else if (10 <= j && j < 20) {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j - 9) * 0.905f, btn_y + radius * 2.7f, radius * 0.3f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j - 9) * 0.905f, btn_y + radius * 2.7f, radius * 0.3f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j - 9) * 0.905f, btn_y + radius * 2.7f, radius * 0.3f, paint);
                        }

                    }
                    else if (20 <= j) {
                        //현재날짜 테두리
                        if (j == (Integer.parseInt(dateDiff[i]))){
                            canvas.drawCircle(btn_x + radius * (j - 19) * 0.905f, btn_y + radius * 3.7f, radius * 0.3f, strokePaint);
                        }
                        for (int k = 0; k < objectDays[i]; k++) {
                            progressWord[k] = String.valueOf(habitProgressArr[i].charAt(k));
                        }
                        if (progressWord[j].equals("1")) {
                            canvas.drawCircle(btn_x + radius * (j - 19) * 0.905f, btn_y + radius * 3.7f, radius * 0.3f, completePaint);
                        }
                        else {
                            canvas.drawCircle(btn_x + radius * (j - 19) * 0.905f, btn_y + radius * 3.7f, radius * 0.3f, paint);
                        }

                    }
                }
            }
        }
    }
}
