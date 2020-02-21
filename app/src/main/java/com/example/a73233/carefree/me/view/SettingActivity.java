package com.example.a73233.carefree.me.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseActivity;
import com.example.a73233.carefree.databinding.ActivitySettingBinding;
import com.example.a73233.carefree.me.viewModel.MeVM;
import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.PhotoManager;

public class SettingActivity extends BaseActivity {
    ActivitySettingBinding binding;
    MeVM vm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_setting);
        vm = new MeVM(this);
        binding.setSettingActivity(this);
        binding.setBean(vm.refreshData());
        ReviseStatusBar(TRANSPARENT_BLACK);
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.setting_head:
                setHeadImg();
                break;
            case R.id.setting_name:
                setName();
                break;
            case R.id.setting_words:
                setWords();
                break;
            case R.id.setting_home_show_note:
                setHomeShowNote();
                break;
            case R.id.setting_rank3_top:
                setRank3Top();
                break;
            case R.id.setting_note_clock_type:
                setClockType();
                break;
            case R.id.setting_feed_back:
                startActivity(FeedBackActivity.class);
                break;
            case R.id.setting_about:
                startActivity(AboutActivity.class);
                break;
            case R.id.setting_toolbar_left:
                finish();
                break;
        }
    }
    private void setHomeShowNote(){
        String[] items = {"显示任务","不显示任务"};
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                vm.setHomeShowNote("显示任务");
                                break;
                            case 1 :
                                vm.setHomeShowNote("不显示任务");
                                break;
                        }
                    }
                }).create();
        alertDialog.show();
    }
    private void setRank3Top(){
        String[] items = {"置顶","不置顶"};
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                vm.setRank3Top("置顶");
                                break;
                            case 1 :
                                vm.setRank3Top("不置顶");
                                break;
                        }
                    }
                }).create();
        alertDialog.show();
    }
    private void setClockType(){
        String[] items = {"系统闹钟","非系统闹钟"};
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                vm.setClockType("系统闹钟");
                                break;
                            case 1 :
                                vm.setClockType("非系统闹钟");
                                break;
                        }
                    }
                }).create();
        alertDialog.show();
    }
    private void setWords(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_write,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        EditText editText = view.findViewById(R.id.dialog_write_edit);
        TextView confirm = view.findViewById(R.id.dialog_write_confirm);
        TextView title = view.findViewById(R.id.dialog_write_title);
        TextView cancel = view.findViewById(R.id.dialog_write_cancel);
        title.setText("签名");

        editText.setText(vm.getWords());
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.setWords(editText.getText().toString());
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
    private void setName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_write,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        EditText editText = view.findViewById(R.id.dialog_write_edit);
        TextView textView = view.findViewById(R.id.dialog_write_confirm);

        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
        editText.setHint("最多9个字");
        editText.setText(vm.getName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.setName(editText.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void setHeadImg(){
        String[] items = {"相机","相册","取消"};
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                String imgUrl = PhotoManager.TakePhoto(SettingActivity.this);
                                vm.setImgUrl(imgUrl);
                                break;
                            case 1 :
                                Intent intentAlbum = new Intent(Intent.ACTION_GET_CONTENT);
                                intentAlbum.setType("image/*");
                                startActivityForResult(intentAlbum,PhotoManager.CHOOSE_PHOTO);
                                break;
                            case 3:
                                break;
                        }
                    }
                }).create();
        alertDialog.show();
    }
    @BindingAdapter("setting_head_url")
    public static void LoadHeadIma(ImageView imageView, String imgUrl){
        if(imgUrl != null){
            Glide.with(imageView.getContext()).load(imgUrl)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .error(R.mipmap.find_photo_fail)
                    .into(imageView);
        }else {
            Glide.with(imageView.getContext()).load(R.drawable.user_head_ima)
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(15)))
                    .skipMemoryCache(true) // 不使用内存缓存
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 不使用磁盘缓存
                    .error(R.mipmap.find_photo_fail)
                    .into(imageView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PhotoManager.CHOOSE_PHOTO:
                String url = PhotoManager.GetPathFromUri(this,data.getData());
                vm.setImgUrl(url);
                break;
        }
    }

    @Override
    public void finish() {
        super.finish();
        vm.saveUser();
    }
}