package com.example.dotive;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity {

    Paint[] paint;
    double lon = 30;
    double lat = 20;
    int width;
    int height;
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

        Display display = getWindowManager().getDefaultDisplay();  // in Activity
        /* getActivity().getWindowManager().getDefaultDisplay() */ // in Fragment
        Point size = new Point();
        display.getSize(size); // or getSize(size)
        int width = size.x;
        int height = size.y;
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

                x = (int) ((width / 360.0) * (180 + longitude[i]));
                y = (int) ((height / 180.0) * (90 - latitude[i]));

                canvas.drawColor(Color.WHITE);
                canvas.drawCircle(x, y, 3, paint[i]);

                System.out.println(x + "x" + name[i]);
                System.out.println(y + "y" + name[i]);
            }
        }
    }
}
