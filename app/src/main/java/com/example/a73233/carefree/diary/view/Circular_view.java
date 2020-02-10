package com.example.a73233.carefree.diary.view;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.a73233.carefree.util.EmotionUtil;
import com.example.a73233.carefree.util.LogUtil;
import com.example.a73233.carefree.util.NoteUtil;

public class Circular_view extends View {
    private static Paint bigCirPaint;
    private static Paint smallCirPaint;
    public Circular_view(Context context) {
        super(context);
        init();
    }

    public Circular_view(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @BindingAdapter("circularColor")
    public static void loadCircularColor(Circular_view view,int circularColor){
        bigCirPaint.setColor(EmotionUtil.GetColors(circularColor)[1]);
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
