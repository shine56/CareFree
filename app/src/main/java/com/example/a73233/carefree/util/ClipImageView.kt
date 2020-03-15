package com.example.a73233.carefree.util

import android.content.Context
import android.graphics.*
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.widget.Toast
import com.example.a73233.carefree.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.FileOutputStream
import java.io.IOException

class ClipImageView(context: Context, attributeSet: AttributeSet?) : AppCompatImageView(context, attributeSet)
{

    private val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    var clipWidth = 300
        set(value) {
            field = value
            if (isAttachedToWindow) {
                postInvalidate()
            }
        }

    var clipHeight = 300
        set(value) {
            field = value
            if (isAttachedToWindow)
            {
                postInvalidate()
            }
        }

    var minScale = 1.0f

    var maxScale = 1.0f

    private var rectColor = Color.BLACK

    private var lastTouchX = 0F

    private var lastTouchY = 0F

    private val transMatrix = Matrix()

    private var isTouching = false

    private var scale = 1.0f

    var onsaveClipImageListener: OnSaveClipImageListsner? = null

    private val scaleGestureDetectorListener = object : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(detector: ScaleGestureDetector?): Boolean
        {
            val curScaleFactor = detector?.scaleFactor ?: 1.0f
            var curScale = scale * curScaleFactor
            curScale = if (curScale >= 1.0f) Math.min(maxScale, curScale) else Math.max(minScale, curScale)
            val scaleFactor = if (curScale > scale) 1 + (curScale - scale) / scale else 1.0f - (scale - curScale) / scale
            transMatrix.postScale(scaleFactor, scaleFactor, detector?.focusX
                    ?: 0f, detector?.focusY ?: 0f)
            postInvalidate()
            scale = curScale
            return true
        }

        override fun onScaleEnd(detector: ScaleGestureDetector?)
        {
            super.onScaleEnd(detector)
        }
    }

    private var scaleGestureDetector: ScaleGestureDetector

    constructor(context: Context) : this(context, null)

    init {
        paint.strokeJoin = Paint.Join.ROUND
        scaleGestureDetector = ScaleGestureDetector(context, scaleGestureDetectorListener)
        if (attributeSet != null) {
            pareseAttributeSet(attributeSet)
        }
        setBackgroundColor(Color.WHITE)
    }

    private fun pareseAttributeSet(attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ClipImageView)
        clipWidth = typedArray.getDimensionPixelOffset(R.styleable.ClipImageView_clip_width, clipWidth)
        clipHeight = typedArray.getDimensionPixelOffset(R.styleable.ClipImageView_clip_width, clipHeight)
        rectColor = typedArray.getColor(R.styleable.ClipImageView_rect_color, rectColor)
        minScale = typedArray.getFloat(R.styleable.ClipImageView_min_scale, minScale)
        maxScale = typedArray.getFloat(R.styleable.ClipImageView_max_scale, maxScale)
        typedArray.recycle()
    }

    override fun layout(l: Int, t: Int, r: Int, b: Int) {
        super.layout(l, t, r, b)
        if (clipWidth > measuredWidth)
        {
            clipWidth = measuredWidth
        }
        if (clipHeight > measuredHeight)
        {
            clipHeight = measuredHeight
        }

    }


    override fun onTouchEvent(event: MotionEvent?): Boolean
    {
        if (event?.pointerCount ?: 1 >= 2) {
            isTouching = false
            return scaleGestureDetector.onTouchEvent(event)
        } else {
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    isTouching = true
                    lastTouchX = event.x
                    lastTouchY = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isTouching && event.pointerCount == 1) {
                        val offsetX = event.x - lastTouchX
                        val offsetY = event.y - lastTouchY
                        transMatrix.postTranslate(offsetX, offsetY)
                        lastTouchX = event.x
                        lastTouchY = event.y
                        postInvalidate()
                    }
                }

                MotionEvent.ACTION_UP ->
                {
                    isTouching = false
                }
            }
            return true
        }
    }

    override fun onDraw(canvas: Canvas?)
    {
        canvas?.let {
            val saveState = it.saveCount
            it.save()
            it.concat(transMatrix)
            super.onDraw(canvas)
            it.restoreToCount(saveState)
            drawMask(it)
            drawRect(it)

        }
    }

    private fun drawMask(canvas: Canvas) {
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#A0000000")
        canvas.drawRect(0.0f, 0.0f, width.toFloat(), (height / 2 - clipHeight / 2).toFloat(), paint)
        canvas.drawRect((width / 2 + clipWidth / 2).toFloat(), (height / 2 - clipHeight / 2).toFloat(), width.toFloat(), (height / 2 + clipHeight / 2).toFloat(), paint)
        canvas.drawRect(0.0f, (height / 2 + clipHeight / 2).toFloat(), width.toFloat(), height.toFloat(), paint)
        canvas.drawRect(0.0f, (height / 2 - clipHeight / 2).toFloat(), (width / 2 - clipWidth / 2).toFloat(), (height / 2 + clipHeight / 2).toFloat(), paint)
    }

    private fun drawRect(canvas: Canvas)
    {
        paint.style = Paint.Style.FILL_AND_STROKE
        paint.color = rectColor
        paint.strokeWidth = 4.0f
        val offset = paint.strokeWidth / 2
        val left: Float = (width / 2 - clipWidth / 2).toFloat() - offset
        val top: Float = (height / 2 - clipHeight / 2).toFloat() - offset
        val right: Float = (width / 2 + clipWidth / 2).toFloat() + offset
        val bottom: Float = (height / 2 + clipHeight / 2).toFloat() + offset
        canvas.drawLine(left, top, right, top, paint)
        canvas.drawLine(right, top, right, bottom, paint)
        canvas.drawLine(left, bottom, right, bottom, paint)
        canvas.drawLine(left, top, left, bottom, paint)
    }

    interface OnSaveClipImageListsner {
        fun onFinishedSave()
    }

    fun clipAndSaveImage(filePath: String) {
        savePhoto(filePath)
    }

    private fun savePhoto(filePath: String){
        Observable.create<Boolean> {
            it.onNext(saveClipImage(filePath))
        }.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    onsaveClipImageListener?.onFinishedSave()
                }
    }

    private fun saveClipImage(filePath: String) : Boolean{
        val clipBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val clipCanvas = Canvas(clipBitmap)
        draw(clipCanvas)
        try {
            val outputStream = FileOutputStream(filePath)
            val bitmap = Bitmap.createBitmap(clipBitmap, width / 2 - clipWidth / 2, height / 2 - clipHeight / 2, clipWidth, clipHeight, transMatrix, true)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.close()
            return true
        }
        catch (e: IOException) {
            e.printStackTrace()
            return false
        }
    }
}