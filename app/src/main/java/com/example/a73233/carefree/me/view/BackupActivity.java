package com.example.a73233.carefree.me.view;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseActivity;
import com.example.a73233.carefree.databinding.ActivityBackupBinding;
import com.example.a73233.carefree.me.viewModel.BackupVM;
import com.example.a73233.carefree.util.ConstantPool;

public class BackupActivity extends BaseActivity {
    private ActivityBackupBinding binding;
    private BackupVM vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_backup);
        binding.setBackupActivity(this);
        vm = new BackupVM(this);
        ReviseStatusBar(TRANSPARENT_BLACK);
        binding.backupNameText.setText(vm.getName() == null ? "未设置" : vm.getName());
        binding.backupPasText.setText(vm.getPas() == null ? "未设置" : vm.getPas());
        binding.backupUrlText.setText(vm.getUrl() == null ? "未设置" : vm.getUrl());
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.backup_name:
                setName();
                break;
            case R.id.backup_pas:
                setPassWord();
                break;
            case R.id.backup_url:
                setUrl();
                break;
            case R.id.backup_put:
                showBackupDialog(ConstantPool.CLOUND);
                break;
            case R.id.backup_get:
                restoreData(ConstantPool.CLOUND);
                break;
            case R.id.backup_local_put:
                showBackupDialog(ConstantPool.SDCARD);
                break;
            case R.id.backup_local_get:
                restoreData(ConstantPool.SDCARD);
                break;
            case R.id.backup_toolbar_left:
                onBackPressed();
                break;
            case R.id.backup_how_user:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://sspai.com/post/55874?hippy=1"));
                startActivity(intent);
                break;
        }
    }
    private void setName(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_write,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        EditText editText = view.findViewById(R.id.dialog_write_edit);
        TextView textView = view.findViewById(R.id.dialog_write_confirm);
        TextView title = view.findViewById(R.id.dialog_write_title);
        TextView cancel = view.findViewById(R.id.dialog_write_cancel);
        title.setText("账号");

        editText.setText(vm.getName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.setName(editText.getText().toString());
                binding.backupNameText.setText(vm.getName());
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
    private void setPassWord(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_write,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        EditText editText = view.findViewById(R.id.dialog_write_edit);
        TextView textView = view.findViewById(R.id.dialog_write_confirm);
        TextView title = view.findViewById(R.id.dialog_write_title);
        TextView cancel = view.findViewById(R.id.dialog_write_cancel);
        editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        title.setText("密码");

        editText.setText(vm.getPas());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.setPas(editText.getText().toString());
                binding.backupPasText.setText(vm.getPas());
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
    private void setUrl(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_write,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        EditText editText = view.findViewById(R.id.dialog_write_edit);
        TextView textView = view.findViewById(R.id.dialog_write_confirm);
        TextView title = view.findViewById(R.id.dialog_write_title);
        TextView cancel = view.findViewById(R.id.dialog_write_cancel);
        title.setText("服务器地址");

        editText.setText(vm.getUrl());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.setUrl(editText.getText().toString());
                binding.backupUrlText.setText(vm.getUrl());
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
    private void restoreData(int type){
        vm.restoreData(showLoadDialog(),type);
    }
    private void showBackupDialog(int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_text_confim,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        TextView text = view.findViewById(R.id.dialog_confirm_content);
        TextView confirm = view.findViewById(R.id.dialog_confirm_confirm);
        TextView title = view.findViewById(R.id.dialog_confirm_title);
        TextView cancel = view.findViewById(R.id.dialog_confirm_cancel);
        title.setText("备份");

        if(type == ConstantPool.SDCARD){
            text.setText("        你的数据将会备份在本地 mnt/sdcard/CareFree/ 目录下");
        }else {
            text.setText("        确认即表示你将同意将数据上传至第三方网盘，请确保你选择了信任的网盘上传数据。\n        你的数据将会备份在 网盘/CareFree/ 目录下");
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vm.backupData(showLoadDialog(), type);
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
    private Dialog showLoadDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_progressbar,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }
    @Override
    public void onBackPressed() {
        Intent mIntent = new Intent();
        mIntent.setClass(this, SettingActivity.class);
        mIntent.putExtra("transition", "slide");
        startActivity(mIntent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
