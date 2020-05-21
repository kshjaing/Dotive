package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Canvas;
import android.os.Bundle;
import android.provider.Settings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public Settings.Panel panel = new Settings.Panel(this);

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < name.length; i++)
    }

}
