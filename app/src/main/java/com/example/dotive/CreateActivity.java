package com.example.dotive;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class CreateActivity extends Activity {

    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ll = new LinearLayout(this);

        Button btnCreate = new Button(this);
        btnCreate.setText("생성");
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)MainActivity.context_main).totalHabit += 1;
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
            }
        });

        TextView txtWelcome = new TextView(this);
        txtWelcome.setText("습관 생성 액티비티입니다!");

        ll.addView(txtWelcome);
        ll.addView(btnCreate);
        setContentView(ll);
    }
}

