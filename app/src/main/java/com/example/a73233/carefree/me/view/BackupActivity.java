package com.example.a73233.carefree.me.view;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.ActivityManager;
import com.example.a73233.carefree.baseView.BaseActivity;
import com.example.a73233.carefree.databinding.ActivityBackupBinding;
import com.example.a73233.carefree.me.viewModel.BackupVM;
import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.LogUtil;
import com.example.a73233.carefree.util.PhotoManager;
import com.example.a73233.carefree.util.TimeUtil;

import static com.example.a73233.carefree.util.PhotoManager.CHOOSE_PHOTO;

public class BackupActivity extends BaseActivity {
    private ActivityBackupBinding binding;
    private BackupVM vm;
    private static final int put = 1;
    private static final int get = 2;

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
        binding.backupLocalAutoText.setText(TimeUtil.GetAutoFText(vm.getAuto()));
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
                showRestoreDialog(ConstantPool.CLOUND);
                break;
            case R.id.backup_local_put:
                if(getWritePermission(put)){
                    showBackupDialog(ConstantPool.SDCARD);
                }
                break;
            case R.id.backup_local_get:
                if(getWritePermission(get)){
                    showRestoreDialog(ConstantPool.SDCARD);
                }
                break;
            case R.id.backup_toolbar_left:
                onBackPressed();
                break;
            case R.id.backup_how_user:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://sspai.com/post/55874?hippy=1"));
                startActivity(intent);
                break;

            case R.id.backup_local_auto:
                setAutoF();
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
    private void setAutoF(){
        String[] items = {
                "关",
                "每新增2条日记",
                "每新增3条日记",
                "每新增5条日记",
                "每新增7条日记",
                "每新增10条日记",
        };
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0 :
                                vm.setAuto(-1);
                                break;
                            case 1 :
                                vm.setAuto(2);
                                break;
                            case 2:
                                vm.setAuto(3);
                                break;
                            case 3:
                                vm.setAuto(5);
                                break;
                            case 4:
                                vm.setAuto(7);
                                break;
                            case 5:
                                vm.setAuto(10);
                                break;
                        }
                        binding.backupLocalAutoText.setText(TimeUtil.GetAutoFText(vm.getAuto()));
                    }
                }).create();
        alertDialog.show();
    }
    private void restoreData(int type){
        ActivityManager.finishAllActivity();
        vm.restoreData(showLoadDialog(),type);
    }
    private void showRestoreDialog(int type){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_text_confim,null,false);
        builder.setView(view);
        Dialog dialog = builder.create();
        TextView text = view.findViewById(R.id.dialog_confirm_content);
        TextView confirm = view.findViewById(R.id.dialog_confirm_confirm);
        TextView title = view.findViewById(R.id.dialog_confirm_title);
        TextView cancel = view.findViewById(R.id.dialog_confirm_cancel);
        title.setText("恢复");

        if(type == ConstantPool.SDCARD){
            text.setText("        将从恢复你上次备份在本地的数据");
        }else {
            text.setText("        将恢复你上次备份在云盘的数据");
        }
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restoreData(type);
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

    private Boolean getWritePermission(int type){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            LogUtil.LogD("开始动态申请write权限");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},type);
            return false;
        }else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case put:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showBackupDialog(ConstantPool.SDCARD);
                }else {
                    LogUtil.LogD("获取"+permissions[0]+"权限失败");
                    showToast("没有权限无法正常备份哟");
                }
                break;
            case get:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    showRestoreDialog(ConstantPool.SDCARD);
                }else {
                    LogUtil.LogD("获取"+permissions[0]+"权限失败");
                    showToast("没有权限无法正常恢复备份哟");
                }
                break;
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
