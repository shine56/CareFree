package com.example.a73233.carefree.util;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ViewGroup;

import com.example.a73233.carefree.note.view.NoteListAdapter;

public class CustomItemTouchCallback extends ItemTouchHelper.Callback {
    private NoteListAdapter adapter;
    private int max = 350;

    public CustomItemTouchCallback(NoteListAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        // 上下拖动
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        adapter.removeItem(viewHolder.getAdapterPosition());
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        max = getMaxWidth(viewHolder);
        int scrollX = viewHolder.itemView.getScrollX();
        //LogUtil.LogD("dx = "+dX+"      sX = " + scrollX);

        if(dX >= 0){
            viewHolder.itemView.scrollTo(max - (int)dX,0);
            if(scrollX <= (max*1/3) ){
                viewHolder.itemView.scrollTo(0,0);
            }
        }else {
            if(scrollX < (max*2/3)){
                viewHolder.itemView.scrollTo(-(int)dX,0);
            }else {
                viewHolder.itemView.scrollTo(max,0);
            }
        }
    }

    private int getMaxWidth(RecyclerView.ViewHolder viewHolder){
        ViewGroup viewGroup = (ViewGroup) viewHolder.itemView;
        return viewGroup.getChildAt(1).getWidth()+40;
    }

    //当 侧滑滑动的距离 / RecyclerView的宽大于该方法返回值，那么就会触发侧滑删除的操作
    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return Integer.MAX_VALUE;
    }

    //当侧滑的速度大于该方法的返回值，也会触发侧滑删除的操作。
    @Override
    public float getSwipeEscapeVelocity(float defaultValue) {
        return Integer.MAX_VALUE;
    }
}
