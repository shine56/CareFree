package com.example.a73233.carefree.diary.view;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.a73233.carefree.util.EmotionDataUtil;
import com.example.a73233.carefree.util.LogUtil;

public class CircularView2 extends View {
    private static Paint bigCirPaint2;
    private static Paint smallCirPaint2;
    private static Context context;
    public CircularView2(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CircularView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @BindingAdapter("circularColor2")
    public static void loadCircularColor2(CircularView2 view,int circularColor2){
        LogUtil.LogD("圆环值："+circularColor2);
        bigCirPaint2.setColor(EmotionDataUtil.GetColors(circularColor2,context)[1]);
    }

    private static void init(){
        bigCirPaint2 = new Paint();
        bigCirPaint2.setStyle(Paint.Style.FILL);
        bigCirPaint2.setAntiAlias(true);

        smallCirPaint2 = new Paint();
        smallCirPaint2.setStyle(Paint.Style.FILL);
        smallCirPaint2.setColor(Color.WHITE);
        smallCirPaint2.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth()/2;
        int y = getHeight()/2;

        canvas.drawCircle(x,y,x,bigCirPaint2);
        canvas.drawCircle(x,y,x/4,smallCirPaint2);
    }
}
