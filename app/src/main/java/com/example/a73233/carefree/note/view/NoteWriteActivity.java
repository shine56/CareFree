package com.example.a73233.carefree.note.view;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseActivity;
import com.example.a73233.carefree.databinding.ActivityNoteWriteBinding;
import com.example.a73233.carefree.note.viewModel.NoteWriteVM;
import com.example.a73233.carefree.util.LogUtil;

public class NoteWriteActivity extends BaseActivity {
    private ActivityNoteWriteBinding binding;
    private NoteWriteVM noteWriteVM;
    private int hour = 8;
    private int minutes = 10;
    private int noteId;
    private Boolean isSetClock = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_note_write);
        noteId = getIntent().getExtras().getInt("noteId",-1);
        noteWriteVM = new NoteWriteVM();
        binding.setNoteWriteActivity(this);
        binding.setBean(noteWriteVM.refreshData(noteId));
        initView();
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.note_back:
                finish();
                break;
            case R.id.note_complete:
                if(isSetClock){
                    setClock();
                    noteWriteVM.saveNote(noteId);
                    showToast("保存成功");
                    finish();
                }
                if(noteWriteVM.getRank()!=0 && !isSetClock){
                    if(noteId == -1){
                        showToast("任务贴需设置闹钟");
                    }else {
                        noteWriteVM.saveNote(noteId);
                        showToast("保存成功");
                        finish();
                    }
                }
                if(noteWriteVM.getRank() == 0){
                    noteWriteVM.saveNote(noteId);
                    showToast("保存成功");
                    finish();
                }
                break;
            case R.id.note_write_type_0:
                noteWriteVM.refreshRank(0);
                refreshTextBg();
                //取消闹钟
                cancelClock();
                break;
            case R.id.note_write_type_1:
                noteWriteVM.refreshRank(1);
                refreshTextBg();
                break;
            case R.id.note_write_type_2:
                noteWriteVM.refreshRank(2);
                refreshTextBg();
                break;
            case R.id.note_write_type_3:
                noteWriteVM.refreshRank(3);
                refreshTextBg();
                break;
            case R.id.note_text_bg:
                binding.noteEditText.requestFocus();
                //显示软键盘
                InputMethodManager inputManager =
                        (InputMethodManager) binding.noteEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(binding.noteEditText, 0);
                Editable editable = binding.noteEditText.getText();
                Selection.setSelection(editable,editable.length());
                break;
            case R.id.note_write_clock_time:
                isSetClock = true;
                initClockTimeDialog();
                break;
            case R.id.note_write_clock_title:
                initClockTitleDialog();
                break;

        }
    }
    private void initView(){
        ReviseStatusBar(TRANSPARENT_BLACK);
        refreshTextBg();

        if(noteWriteVM.getRank() == 0){
            binding.noteWriteClockBg.setVisibility(View.GONE);
        }else {
            binding.noteWriteClockBg.setVisibility(View.VISIBLE);
        }

        //便贴书写监控
        binding.noteEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (v.getId() == R.id.note_edit_text && canVerticalScroll(binding.noteEditText)) {
                    // 请求父控件不拦截事件
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    if (MotionEvent.ACTION_UP == motionEvent.getAction()) {
                        // 可拦截事件
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }
                return false;
            }
        });
    }

    private void initClockTimeDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_time_pick,null, false);
        builder.setView(view);
        Dialog dialog = builder.create();

        TimePicker timePack = view.findViewById(R.id.dialog_time_pick);
        TextView confirm = view.findViewById(R.id.dialog_time_pick_confirm);
        TextView cancel = view.findViewById(R.id.dialog_time_pick_cancel);
        timePack.setIs24HourView(true);   //设置时间显示为24小时
        timePack.setHour(noteWriteVM.getHour());  //设置当前小时
        timePack.setMinute(noteWriteVM.getMinutes()); //设置当前分（0-59）
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = timePack.getHour();
                minutes = timePack.getMinute();
                noteWriteVM.setClock(hour,minutes);
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void initClockTitleDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_write,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();

        EditText editText = view.findViewById(R.id.dialog_write_edit);
        TextView confirm = view.findViewById(R.id.dialog_write_confirm);
        TextView cancel = view.findViewById(R.id.dialog_write_cancel);
        TextView textView = view.findViewById(R.id.dialog_write_title);
        textView.setText("闹钟内容");
        editText.setText(noteWriteVM.getClockTitle());
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteWriteVM.setClockTitle(editText.getText().toString());
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void setClock(){
        if(noteWriteVM.isSystemClock(this)){
            //设置系统闹钟
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                    //闹钟的小时
                    .putExtra(AlarmClock.EXTRA_HOUR, hour)
                    //闹钟的分钟
                    .putExtra(AlarmClock.EXTRA_MINUTES, minutes)
                    //响铃时提示的信息
                    .putExtra(AlarmClock.EXTRA_MESSAGE, noteWriteVM.getClockTitle())
                    //用于指定该闹铃触发时是否振动
                    .putExtra(AlarmClock.EXTRA_VIBRATE, true)
                    //如果为true，则调用startActivity()不会进入手机的闹钟设置界面
                    .putExtra(AlarmClock.EXTRA_SKIP_UI, false);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                showToast("确认你的闹钟");
            }
        }else {
            Intent intent = new Intent(this,ClockService.class);
            intent.putExtra("hour",hour);
            intent.putExtra("minutes",minutes);
            intent.putExtra("text",noteWriteVM.getClockTitle());
            intent.putExtra("noteDb_id",noteId);
            LogUtil.LogD("广播测试，放进服务接收器得文本是："+noteWriteVM.getClockTitle());
            startService(intent);
        }
    }

    private void cancelClock(){
        if(noteWriteVM.isSystemClock(this)){
            Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                    //闹钟的小时
                    .putExtra(AlarmClock.EXTRA_HOUR, noteWriteVM.getHour())
                    //闹钟的分钟
                    .putExtra(AlarmClock.EXTRA_MINUTES, noteWriteVM.getMinutes())
                    //响铃时提示的信息
                    .putExtra(AlarmClock.EXTRA_MESSAGE, noteWriteVM.getClockTitle())
                    //用于指定该闹铃触发时是否振动
                    .putExtra(AlarmClock.EXTRA_VIBRATE, true)
                    //如果为true，则调用startActivity()不会进入手机的闹钟设置界面
                    .putExtra(AlarmClock.EXTRA_SKIP_UI, false);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                showToast("系统闹钟需要主动取消");
            }
        }else {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
            Intent intent = new Intent(this, ClockReceiver.class);
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
            alarmManager.cancel(pi);
            showToast("闹钟取消");
        }
    }
    private void refreshTextBg(){
        binding.noteWriteClockBg.setVisibility(View.VISIBLE);
        switch (noteWriteVM.getRank()){
            case 0:
                binding.noteTextBg.setBackgroundResource(R.drawable.note_bg_0);
                binding.noteTextTitle.setText("临时记录贴");
                binding.noteWriteClockBg.setVisibility(View.GONE);
                break;
            case 1:
                binding.noteTextBg.setBackgroundResource(R.drawable.note_bg_1);
                binding.noteTextTitle.setText("一级任务贴");
                break;
            case 2:
                binding.noteTextBg.setBackgroundResource(R.drawable.note_bg_2);
                binding.noteTextTitle.setText("二级任务贴");
                break;
            case 3:
                binding.noteTextBg.setBackgroundResource(R.drawable.note_bg_3);
                binding.noteTextTitle.setText("三级任务贴");
                break;
        }
    }

    /**
     * EditText竖直方向是否可以滚动
     * @return true：可以滚动   false：不可以滚动
     */
    private Boolean canVerticalScroll(EditText editText){
        //滚动的距离
        int scrollY = editText.getScrollY();
        //控件内容的总高度
        int scrollRange = editText.getLayout().getHeight();
        //控件实际显示的高度
        int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() - editText.getCompoundPaddingBottom();
        //控件内容总高度与实际显示高度的差值
        int mOffsetHeight = scrollRange - scrollExtent;

        if (mOffsetHeight == 0) {
            return false;
        } else{
            return (scrollY > 0 || scrollY < mOffsetHeight - 1);
        }
    }
}
