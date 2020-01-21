package com.example.a73233.carefree.home.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingAdapter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.a73233.carefree.R;

public class MoodView extends View {
    private Paint mHeartPaint; //心形画笔
    private Paint mOuterPaint, mInnerPaint;//外弧，内弧画笔
    private static Paint mPoint;
    private Paint mTextPaint;
    private int MaxValue = 50;
    private static int CurrentValue = 36;
    private static int mPointColor = 0XFF3FABD5;
    private int mBorderWidth = 45; //圆弧宽度


    public MoodView(Context context) {
        super(context);
        Log.d("mood颜色测试","一个参数");
        init();
    }

    public MoodView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @BindingAdapter(value = {"app:emotionValue","app:pointColor"},requireAll = false)
    public static void GetViewData(MoodView moodView,int emotionValue, int pointColor){
        CurrentValue = emotionValue;
        mPointColor = pointColor;
        mPoint.setColor(mPointColor);
    }

    private void init(){
        //内弧
        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setColor(Color.argb(100,255,255,255)); //半透明白色
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);//设置下方为圆形
        mInnerPaint.setStyle(Paint.Style.STROKE);//设置内部为空心

        //外弧
        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setColor(Color.WHITE);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);//设置下方为圆形
        mOuterPaint.setStyle(Paint.Style.STROKE);//设置内部为空心

        //弧头点
        mPoint = new Paint();
        mPoint.setAntiAlias(true);
        mPoint.setColor(mPointColor);
        mPoint.setStrokeWidth(20);
        mPoint.setStrokeCap(Paint.Cap.ROUND);//设置下方为圆形
        mPoint.setStyle(Paint.Style.STROKE);//设置内部为空心

        //情绪值
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(100);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        //心形画笔
        mHeartPaint = new Paint();
        mHeartPaint.setAntiAlias(true);
        mHeartPaint.setColor(Color.WHITE);
        mHeartPaint.setStrokeWidth(5);
        mHeartPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centerX = getWidth()/2;
        int centerY = getHeight()/2;

        //直线
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3f);
        paint.setStyle(Paint.Style.FILL);
        int lineHeight = (int)(getHeight() * 0.7);
        int startX = (int)(getWidth() * 1/25);
        canvas.drawLine(startX,centerY-(lineHeight/2),startX,centerY+(lineHeight/2),paint);

        //圆内弧
        int left = centerX - (getWidth()*11/32);
        int right = left + (getHeight()*13/24);
        int top = centerY - (getHeight()*13/48),bottom = centerY + (getHeight()*13/48);
        RectF rectF = new RectF(left,top,right,bottom);
        canvas.drawArc(rectF,-90,360,false,mInnerPaint);
        //外弧
        float sweepAngle = (float)CurrentValue/MaxValue * 360;
        canvas.drawArc(rectF,-90, sweepAngle,false,mOuterPaint);
        //文字
        /*String stepText = CurrentValue + "";
        Rect rect = new Rect();
        mTextPaint.getTextBounds(stepText,0,stepText.length(),rect);
        int textCenter = left + ((right - left)/2);
        int dx = textCenter - rect.width() / 2;*/
        //第一种方式获取高度
        //int dy = getWidth() / 2 + rect.width()/2;
        //第二种表达方式获取高度
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        //获取中心(fontMetrics.bottom - fontMetrics.top) / 2
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = centerY + dy;
        canvas.drawText(CurrentValue + "",left + (right - left)/2,baseLine,mTextPaint);
        //弧头的原点
        canvas.drawArc(rectF,sweepAngle-92, 1,false,mPoint);
        /*//心形
        int originX = getWidth() * 5/6;
        int originY = getHeight() * 1/5;
        int heartHight = 15;
        PointF heartStart = new PointF(0,0);
        PointF heartEnd = new PointF(0,0);
        PointF heartControl = new PointF(0,0);
        PointF heartControl2 = new PointF(0,0);

        //右上
        heartStart.x = originX;
        heartStart.y = originY-heartHight;
        heartEnd.x = originX+heartHight*2;
        heartEnd.y = originY;
        heartControl.x = originX+heartHight;
        heartControl.y = originY-heartHight*2;
        heartControl2.x = originX+heartHight*2;
        heartControl2.y = originY-heartHight*3/2;
        Path path = new Path();
        path.moveTo(heartStart.x, heartStart.y);
        path.cubicTo(heartControl.x, heartControl.y,heartControl2.x, heartControl2.y, heartEnd.x, heartEnd.y);
        canvas.drawPath(path, mHeartPaint);

        //右下
        heartStart.x = originX;
        heartStart.y = originY+heartHight*2;
        heartEnd.x = originX+heartHight*2;
        heartEnd.y = originY;
        heartControl.x = originX+heartHight;
        heartControl.y = originY+heartHight*5/4;
        heartControl2.x = originX+heartHight*5/3;
        heartControl2.y = originY+heartHight*5/4;
        path.moveTo(heartStart.x, heartStart.y);
        path.cubicTo(heartControl.x, heartControl.y,heartControl2.x, heartControl2.y, heartEnd.x, heartEnd.y);
        canvas.drawPath(path, mHeartPaint);

        //左上部分
        heartStart.x = originX;
        heartStart.y = originY-heartHight;
        heartEnd.x = originX-heartHight*2;
        heartEnd.y = originY;
        heartControl.x = originX-heartHight;
        heartControl.y = originY-heartHight*2;
        heartControl2.x = originX-heartHight*2;
        heartControl2.y = originY-heartHight*3/2;
        path.moveTo(heartStart.x, heartStart.y);
        path.cubicTo(heartControl.x, heartControl.y,heartControl2.x, heartControl2.y, heartEnd.x, heartEnd.y);
        canvas.drawPath(path, mHeartPaint);

        //左下
        heartStart.x = originX;
        heartStart.y = originY+heartHight*2;
        heartEnd.x = originX-heartHight*2;
        heartEnd.y = originY;
        heartControl.x = originX-heartHight;
        heartControl.y = originY+heartHight*5/4;
        heartControl2.x = originX-heartHight*5/3;
        heartControl2.y = originY+heartHight*5/4;
        path.moveTo(heartStart.x, heartStart.y);
        path.cubicTo(heartControl.x, heartControl.y,heartControl2.x, heartControl2.y, heartEnd.x, heartEnd.y);
        canvas.drawPath(path, mHeartPaint);


        //上下左右四个点
        mHeartPaint.setStyle(Paint.Style.FILL);
        float[] points = {originX, originY-heartHight
        ,originX, originY+heartHight*2
        *//*,originX+heartHight*2,originY
        ,originX-heartHight*2,originY*//*};
        canvas.drawPoints(points,mHeartPaint);
        mHeartPaint.setStyle(Paint.Style.STROKE);
*/
    }

}
