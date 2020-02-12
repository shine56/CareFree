package com.example.a73233.carefree.note.view;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.icu.util.Calendar;
import android.os.Build;
import android.provider.AlarmClock;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseview.BaseActivity;
import com.example.a73233.carefree.databinding.ActivityNoteWriteBinding;
import com.example.a73233.carefree.note.viewModel.NoteWriteVM;

public class NoteWriteActivity extends BaseActivity {
    private ActivityNoteWriteBinding binding;
    private NoteWriteVM noteWriteVM;
    private int hour = 8;
    private int minutes = 10;
    private Boolean isSetClock = false;  //判断用户是否选择子女的时间
    private int noteId;
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
                    noteWriteVM.setClock(hour,minutes);
                    noteWriteVM.saveNote(noteId);
                    showToast("保存成功");
                    finish();
                }
                if(noteWriteVM.getRank()!=0 && !isSetClock){
                    if(noteId == -1){
                        showToast("任务贴需点亮闹钟图标。");
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
            case R.id.note_clock_logo:
                if(noteId == -1){
                    if(noteWriteVM.getRank() == 0){
                        showToast("记录贴无法设置闹钟");
                    }else {
                        isSetClock = true;
                        binding.noteClockLogo.setImageResource(R.mipmap.clock_blue);
                        binding.noteClockTitleLogo.setImageResource(R.mipmap.title_blue);
                    }
                }
                break;
        }
    }
    private void initView(){
        ReviseStatusBar(TRANSPARENT_BLACK);
        refreshTextBg();
        initClock();
    }

    private void initClock(){
        binding.noteClockHour.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);  //设置点击事件不弹键盘
        binding.noteClockHour.setIs24HourView(true);   //设置时间显示为24小时
        binding.noteClockHour.setHour(noteWriteVM.getHour());  //设置当前小时
        binding.noteClockHour.setMinute(noteWriteVM.getMinutes()); //设置当前分（0-59）

        //监控闹钟选择
        binding.noteClockHour.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                minutes = minute;
                if(noteWriteVM.getRank() != 0){
                    isSetClock = true;
                    binding.noteClockLogo.setImageResource(R.mipmap.clock_blue);
                    binding.noteClockTitleLogo.setImageResource(R.mipmap.title_blue);
                }else {
                    showToast("记录贴提醒闹钟无效");
                }
            }
        });

        binding.noteClockTitle.setText(noteWriteVM.getNoteFirstText());
    }

    private void setClock(){
        Intent intent = new Intent(this,ClockService.class);
        intent.putExtra("hour",hour);
        intent.putExtra("minutes",minutes);
        intent.putExtra("text",noteWriteVM.getNoteFirstText());
        startService(intent);
    }
    private void cancelClock(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        Intent intent = new Intent(this, ClockReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pi);
    }
    private void refreshTextBg(){
        if(noteId == -1){
            binding.noteClockLogo.setImageResource(R.mipmap.clock_logo_gray);
            binding.noteClockTitleLogo.setImageResource(R.mipmap.title_gray);
        }
        switch (noteWriteVM.getRank()){
            case 0:
                binding.noteTextBg.setBackgroundResource(R.drawable.note_bg_0);
                binding.noteTextTitle.setText("临时记录贴");
                binding.noteClockLogo.setImageResource(R.mipmap.clock_logo_gray);
                binding.noteClockTitleLogo.setImageResource(R.mipmap.title_gray);
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
}
