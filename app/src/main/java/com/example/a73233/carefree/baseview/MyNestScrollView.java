package com.example.a73233.carefree.baseview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.view.MotionEvent;

public class MyNestScrollView extends NestedScrollView {

    public MyNestScrollView(@NonNull Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }
}
