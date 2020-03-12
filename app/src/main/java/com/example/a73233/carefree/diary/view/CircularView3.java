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

public class CircularView3 extends View {
    private static Paint bigCirPaint;
    private static Paint smallCirPaint;
    private static Context context;
    public CircularView3(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public CircularView3(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    @BindingAdapter("circularColor3")
    public static void loadCircularColor3(CircularView3 view,int circularColor){
        LogUtil.LogD("圆环值："+circularColor);
        bigCirPaint.setColor(EmotionDataUtil.GetColors(circularColor,context)[1]);
    }

    private static void init(){
        bigCirPaint = new Paint();
        bigCirPaint.setStyle(Paint.Style.FILL);
        bigCirPaint.setAntiAlias(true);

        smallCirPaint = new Paint();
        smallCirPaint.setStyle(Paint.Style.FILL);
        smallCirPaint.setColor(Color.WHITE);
        smallCirPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int x = getWidth()/2;
        int y = getHeight()/2;

        canvas.drawCircle(x,y,x,bigCirPaint);
        canvas.drawCircle(x,y,x/4,smallCirPaint);
    }
}
