package com.example.a73233.carefree.Util;

import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration{
    private int space;
    private int orientation;

    public SpacesItemDecoration(int orientation, int space) {
        this.orientation = orientation;
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        //设置item之间的间距（上下左右）
       // outRect.top = space;
        //设置item与parent的间距
        if(orientation == 0){
            if (parent.getChildLayoutPosition(view) == 0)
                outRect.top = space;
            outRect.bottom = space;
        }else {
            if (parent.getChildLayoutPosition(view) == 0)
                outRect.left = space;
            outRect.right = space;
        }

    }}
