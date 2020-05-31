package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    Paint[] paint;
    double lon = 30;
    double lat = 20;
    int scrWidth;
    int scrHeight;
    int x;
    int y;


    String name[] = new String[]{"Joseph", "jj"};
    Double longitude[] = new Double[]{30.2, 30.2};
    Double latitude[] = new Double[]{30.2, 45.2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        Panel panel = new Panel(this);
        panel.setBackgroundColor(Color.WHITE);
        setContentView(panel);

        scrWidth = getWindowManager().getDefaultDisplay().getWidth();
        scrHeight = getWindowManager().getDefaultDisplay().getHeight();
    }

    class Panel extends View {

        public Panel(Context context) {
            super(context);
        }

        @SuppressLint("DrawAllocation")
        @Override
        public void onDraw(Canvas canvas) {
            for (int i = 0; i < name.length; i++) {
                paint = new Paint[i];
                paint[i].setColor(Color.BLACK);
                paint[i].setStrokeWidth(1);
                paint[i].setTextSize(20);

                x = (int) ((scrWidth / 360.0) * (180 + longitude[i]));
                y = (int) ((scrHeight / 180.0) * (90 - latitude[i]));

                canvas.drawColor(Color.WHITE);
                canvas.drawCircle(x, y, 3, paint[i]);

                System.out.println(x + "x" + name[i]);
                System.out.println(y + "y" + name[i]);
            }
        }
    }
}
