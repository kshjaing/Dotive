package com.example.dotive;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class Painting_Circle extends View {
    private Paint paint;

    public Painting_Circle(Context context) {
        super(context);

        init(context);
    }

    public Painting_Circle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Toast.makeText(super.getContext(), "모션 이벤트 다운" + event.getX() + "," + event.getY(), Toast.LENGTH_LONG).show();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    private void init(Context context) {
        paint = new Paint();
        //paint.setColor(Color.RED);
    }
}
