package com.example.a73233.carefree.home.view;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class EmotionReportView extends View {
    private Paint LinePaint;
    private Paint TitlePaint;
    private Paint YtextPaint;
    private Paint XtextPaint;
    private static Paint Day1Paint;
    private static Paint Day2Paint;
    private static Paint Day3Paint;
    private static Paint Day4Paint;
    private static Paint Day5Paint;
    private static Paint Day6Paint;
    private static Paint Day7Paint;

    private static String[] DayNum = {"01","02","03","04","05","06","07"};
    private static int[] DayColor = {0XFF38D5D6,0XFF64B0E8,0XFF9B85FF,0XFF38D5D6,0XFF2B5876,0XFF38D5D6,0XFF9B85FF};
    private static int[] DayValue = {0, 0, 0, 0, 0, 0, 0};

    public EmotionReportView(Context context) {
        super(context);
    }

    public EmotionReportView(Context context, AttributeSet emotion_report_bg_attr) {
        super(context, emotion_report_bg_attr);
        init();
    }

    @BindingAdapter(value = {"dayValues","dayColors","dayNums"},requireAll = false)
    public static void GetViewData(EmotionReportView view,int[] dayValues,int[] dayColors,String[] dayNums){
        DayValue = dayValues;
        DayNum = dayNums;
        setDayColor(dayColors);
    }

    public static void setDayColor(int[] dayColor) {
        DayColor = dayColor;
        Day1Paint.setColor(DayColor[0]);
        Day2Paint.setColor(DayColor[1]);
        Day3Paint.setColor(DayColor[2]);
        Day4Paint.setColor(DayColor[3]);
        Day5Paint.setColor(DayColor[4]);
        Day6Paint.setColor(DayColor[5]);
        Day7Paint.setColor(DayColor[6]);
    }

    private void init(){
        //虚线
        LinePaint = new Paint();
        LinePaint.setStyle(Paint.Style.FILL);
        LinePaint.setStrokeWidth(1f);
        LinePaint.setColor(Color.argb(50,151,151,151));
        //LinePaint.setColor(Color.WHITE);

        //标题
        TitlePaint = new Paint();
        //TitlePaint.setStrokeWidth(80f);
        TitlePaint.setAntiAlias(true);
        TitlePaint.setStyle(Paint.Style.FILL);
        TitlePaint.setColor(Color.BLACK);
        TitlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        TitlePaint.setTextSize(52);

        //Y轴数据
        YtextPaint = new Paint();
        YtextPaint.setAntiAlias(true);
        YtextPaint.setStyle(Paint.Style.FILL);
        YtextPaint.setColor(Color.rgb(138, 138, 138));
        YtextPaint.setTextSize(30);
        YtextPaint.setTextAlign(Paint.Align.RIGHT);

        //X轴数据
        XtextPaint = new Paint();
        XtextPaint.setAntiAlias(true);
        XtextPaint.setStyle(Paint.Style.FILL);
        XtextPaint.setColor(Color.BLACK);
        XtextPaint.setTextSize(45);
        XtextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        //显示情绪的矩形
        Day1Paint = new Paint();
        Day1Paint.setAntiAlias(true);
        Day1Paint.setColor(DayColor[0]);
        Day1Paint.setStyle(Paint.Style.FILL);

        Day2Paint = new Paint();
        Day2Paint.setAntiAlias(true);
        Day2Paint.setColor(DayColor[1]);
        Day2Paint.setStyle(Paint.Style.FILL);

        Day3Paint = new Paint();
        Day3Paint.setAntiAlias(true);
        Day3Paint.setColor(DayColor[2]);
        Day3Paint.setStyle(Paint.Style.FILL);

        Day4Paint = new Paint();
        Day4Paint.setAntiAlias(true);
        Day4Paint.setColor(DayColor[3]);
        Day4Paint.setStyle(Paint.Style.FILL);

        Day5Paint = new Paint();
        Day5Paint.setAntiAlias(true);
        Day5Paint.setColor(DayColor[4]);
        Day5Paint.setStyle(Paint.Style.FILL);

        Day6Paint = new Paint();
        Day6Paint.setAntiAlias(true);
        Day6Paint.setColor(DayColor[5]);
        Day6Paint.setStyle(Paint.Style.FILL);

        Day7Paint = new Paint();
        Day7Paint.setAntiAlias(true);
        Day7Paint.setColor(DayColor[6]);
        Day7Paint.setStyle(Paint.Style.FILL);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int Width = getWidth();
        int Height = getHeight();
        Rect rect = new Rect();
        XtextPaint.getTextBounds("25",0,"25".length(),rect);

        Log.d("测试",getWidth()+"");

        //画线
        int startX = Width * 1/12;  //线的初始位置
        int endX = Width - Width * 1/17;  //线的结束位置
        int Height1 = Height - rect.height() + 2;
        int startY = Height1 * 3/14;
        canvas.drawLines(new float[]{
                startX,startY,endX,startY,
                startX,Height1 * 5/14,endX,Height1 * 5/14,
                startX,(Height * 7/14) - rect.height()/2 - 2,endX,(Height * 7/14) - rect.height()/2 - 2,
                startX,(Height * 7/14) + rect.height()/2,endX,(Height * 7/14) + rect.height()/2,
                startX,Height * 9/14,endX,Height * 9/14,
                startX,Height * 11/14,endX,Height * 11/14,},LinePaint);
        //画标题
        int textX = Width * 1/35;
        canvas.drawText("情绪报表",textX,(int)(textX*2.7), TitlePaint);
        //Y轴数据
        int numY = Width * 1/90;
        canvas.drawText("50",Width * 1/15,Height * 3/14 ,YtextPaint);
        canvas.drawText("25",Width * 1/15,Height * 5/14,YtextPaint);
        canvas.drawText("0",Width * 1/15,Height * 7/14+ numY,YtextPaint);
        canvas.drawText("-25",Width * 1/15,Height * 9/14+ numY,YtextPaint);
        canvas.drawText("-50",Width * 1/15,Height * 11/14+ numY,YtextPaint);
        //X轴数据
        int seventhLine = (endX - startX)/7;
        int numMaxWidth = seventhLine*2/3 , numYY = Width * 1/60;
        Log.d("自定义EmotionView测试",""+DayNum[6]);
        canvas.drawText(DayNum[0],(startX+seventhLine) -numMaxWidth, Height * 7/14 + numYY,XtextPaint);
        canvas.drawText(DayNum[1],(startX+seventhLine*2)-numMaxWidth, Height * 7/14 + numYY,XtextPaint);
        canvas.drawText(DayNum[2],(startX+seventhLine*3)-numMaxWidth, Height * 7/14 + numYY,XtextPaint);
        canvas.drawText(DayNum[3],(startX+seventhLine*4)-numMaxWidth, Height * 7/14 + numYY,XtextPaint);
        canvas.drawText(DayNum[4],(startX+seventhLine*5)- numMaxWidth, Height * 7/14 + numYY,XtextPaint);
        canvas.drawText(DayNum[5],(startX+seventhLine*6)- numMaxWidth, Height * 7/14 + numYY,XtextPaint);
        canvas.drawText(DayNum[6],(startX+seventhLine*7)-numMaxWidth, Height * 7/14 + numYY,XtextPaint);

        //情绪值的矩形
        int bottom = 0,top = 0, DayValue1 = 0;
        RectF rectF;

        if(DayValue[0] >0 || DayValue[0] == 0){
            bottom = (Height * 7/14) - rect.height()/2 - 5;
            top = bottom - ((bottom - startY) * DayValue[0]/50);
        }else {
            DayValue1 = -1 * DayValue[0];
            top = (Height * 7/14) + rect.height()/2+3;
            bottom = top + ((Height * 11/14 - top) * DayValue1/50);
        }
        rectF = new RectF((startX+seventhLine) -numMaxWidth + rect.width()/6,top,
                (startX+seventhLine) -numMaxWidth +rect.width()*5/6,bottom);
        canvas.drawRoundRect(rectF,20,20,Day1Paint);

        if(DayValue[1] >0 || DayValue[1] == 0){
            bottom = (Height * 7/14) - rect.height()/2 - 5;
            top = bottom - ((bottom - startY) * DayValue[1]/50);
        }else {
            DayValue1 = -1 * DayValue[1];
            top = (Height * 7/14) + rect.height()/2+3;
            bottom = top + ((Height * 11/14 - top) * DayValue1/50);
        }
        rectF = new RectF((startX+seventhLine*2) -numMaxWidth + rect.width()/6,top,
                (startX+seventhLine*2) -numMaxWidth +rect.width()*5/6,bottom);
        canvas.drawRoundRect(rectF,20,20,Day2Paint);

        if(DayValue[2] >0 || DayValue[2] == 0){
            bottom = (Height * 7/14) - rect.height()/2 - 5;
            top = bottom - ((bottom - startY) * DayValue[2]/50);
        }else {
            DayValue1 = -1 * DayValue[2];
            top = (Height * 7/14) + rect.height()/2+3;
            bottom = top + ((Height * 11/14 - top) * DayValue1/50);
        }
        rectF = new RectF((startX+seventhLine*3) -numMaxWidth + rect.width()/6,top,
                (startX+seventhLine*3) -numMaxWidth +rect.width()*5/6,bottom);
        canvas.drawRoundRect(rectF,20,20,Day3Paint);

        if(DayValue[3] >0 || DayValue[3] == 0){
            bottom = (Height * 7/14) - rect.height()/2 - 5;
            top = bottom - ((bottom - startY) * DayValue[3]/50);
        }else {
            DayValue1 = -1 * DayValue[3];
            top = (Height * 7/14) + rect.height()/2+3;
            bottom = top + ((Height * 11/14 - top) * DayValue1/50);
        }
        rectF = new RectF((startX+seventhLine*4) -numMaxWidth + rect.width()/6,top,
                (startX+seventhLine*4) -numMaxWidth +rect.width()*5/6,bottom);
        canvas.drawRoundRect(rectF,20,20,Day4Paint);

        if(DayValue[4] >0 || DayValue[4] == 0){
            bottom = (Height * 7/14) - rect.height()/2 - 5;
            top = bottom - ((bottom - startY) * DayValue[4]/50);
        }else {
            DayValue1 = -1 * DayValue[4];
            top = (Height * 7/14) + rect.height()/2+3;
            bottom = top + ((Height * 11/14 - top) * DayValue1/50);
        }
        rectF = new RectF((startX+seventhLine*5) -numMaxWidth + rect.width()/6,top,
                (startX+seventhLine*5) -numMaxWidth +rect.width()*5/6,bottom);
        canvas.drawRoundRect(rectF,20,20,Day5Paint);

        if(DayValue[5] >0 || DayValue[5] == 0){
            bottom = (Height * 7/14) - rect.height()/2 - 5;
            top = bottom - ((bottom - startY) * DayValue[5]/50);
        }else {
            DayValue1 = -1 * DayValue[5];
            top = (Height * 7/14) + rect.height()/2+3;
            bottom = top + ((Height * 11/14 - top) * DayValue1/50);
        }
        rectF = new RectF((startX+seventhLine*6) -numMaxWidth + rect.width()/6,top,
                (startX+seventhLine*6) -numMaxWidth +rect.width()*5/6,bottom);
        canvas.drawRoundRect(rectF,20,20,Day6Paint);
        Log.d("测试","top="+top+"bottom="+bottom);

        if(DayValue[6] >0 || DayValue[6] == 0){
            bottom = (Height * 7/14) - rect.height()/2 - 5;
            top = bottom - ((bottom - startY) * DayValue[6]/50);
        }else {
            DayValue1 = -1 * DayValue[6];
            top = (Height * 7/14) + rect.height()/2+3;
            bottom = top + ((Height * 11/14 - top) * DayValue1/50);
        }
        rectF = new RectF((startX+seventhLine*7) -numMaxWidth + rect.width()/6,top,
                (startX+seventhLine*7) -numMaxWidth +rect.width()*5/6,bottom);
        canvas.drawRoundRect(rectF,20,20,Day7Paint);
    }


}
