package com.example.dotive;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

public class CustomMainBox extends AppCompatButton {
    FrameLayout fl;
    Context context;
    Button button;
    TextView textView;

    public CustomMainBox(Context context) {
        super(context);
        init();
    }

    public CustomMainBox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        fl = new FrameLayout(context);
        button = new Button(context);
        textView = new TextView(context);

        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,200,
                getResources().getDisplayMetrics());
        int txtwidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,170,
                getResources().getDisplayMetrics());
        int btnMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,
                getResources().getDisplayMetrics());
        int txtPaddinghor = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,24,
                getResources().getDisplayMetrics());
        int txtPaddingver = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,6,
                getResources().getDisplayMetrics());

        FrameLayout.LayoutParams btn_frameParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, height);
        FrameLayout.LayoutParams txtview_frameParams = new FrameLayout.LayoutParams(
                txtwidth, FrameLayout.LayoutParams.WRAP_CONTENT);

        btn_frameParams.topMargin = btnMargin;
        button.setLayoutParams(btn_frameParams);

        textView.setTextSize(20);
        textView.setPadding(txtPaddinghor, txtPaddingver, txtPaddinghor, txtPaddingver);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(TEXT_ALIGNMENT_CENTER);
        textView.setBackgroundResource(R.drawable.txtview_round_red);

        setHeight(height);
        setBackgroundResource(R.drawable.custom_mainbox);
    }

}
