package com.example.a73233.carefree.util

import android.content.Context
import android.graphics.*
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import com.example.a73233.carefree.R

class ZoomImageView(context: Context, attributeSet: AttributeSet?) : AppCompatImageView(context, attributeSet) {
    var minScale = 0.8f

    var maxScale = 3.0f

    private var lastTouchX = 0F

    private var lastTouchY = 0F

    private val transMatrix = Matrix()

    private var isTouching = false

    private var scale = 1.0f

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
        scaleGestureDetector = ScaleGestureDetector(context, scaleGestureDetectorListener)
        if (attributeSet != null) {
            pareseAttributeSet(attributeSet)
        }
        setBackgroundColor(Color.WHITE)
    }

    private fun pareseAttributeSet(attributeSet: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ClipImageView)
        minScale = typedArray.getFloat(R.styleable.ClipImageView_min_scale, minScale)
        maxScale = typedArray.getFloat(R.styleable.ClipImageView_max_scale, maxScale)
        typedArray.recycle()
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
        }
    }

}