package com.example.dotive;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SettingActivity extends AppCompatActivity {

    Button button;
    public int new_darkmode = 0; //이제 변할 값.
    public int old_darkmode = 0; //MainActivity 에서 받아온 값
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Intent intent2 = getIntent();
        old_darkmode = intent2.getIntExtra("DARK_MODE",3);

        Log.e("SettingActivity.java", "다크모드 값 : " + old_darkmode);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(old_darkmode == 0) new_darkmode = 1;
                else new_darkmode = 0;

                String uriString = "content://com.example.dotive/Settings";
                Uri uri = new Uri.Builder().build().parse(uriString);

                //String selection = "darkmode = ?";
                //String[] selectionArgs = new String[] {String.valueOf(new_darkmode)};
                ContentValues updateValue = new ContentValues();
                updateValue.put("darkmode",new_darkmode);
                int count = getContentResolver().update(uri,updateValue,null,null);
                Log.e("SettingActivity.java", "다크모드 변경 : "+ old_darkmode + " - > " + new_darkmode);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}