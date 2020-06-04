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

public class CustomView extends View {
    private Paint paint;

    public CustomView(Context context) {
        super(context);

        init(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
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

        //사각형을 그린다.
        paint.setColor(Color.RED);
        canvas.drawRect(100, 100, 200,200, paint);
        paint.setColor(Color.YELLOW);
        canvas.drawRect(500,500,600,600,paint);
    }

    private void init(Context context) {
        paint = new Paint();
        //paint.setColor(Color.RED);
    }
}
