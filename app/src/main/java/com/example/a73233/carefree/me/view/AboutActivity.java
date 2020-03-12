package com.example.a73233.carefree.me.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.a73233.carefree.R;
import com.example.a73233.carefree.baseView.BaseActivity;
import com.example.a73233.carefree.databinding.ActivityAboutBinding;

public class AboutActivity extends BaseActivity {
    ActivityAboutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_about);
        binding.setAboutActivity(this);

        //initView
        ReviseStatusBar(TRANSPARENT_BLACK);
        binding.aboutAppName.setText(getAppName());
        binding.aboutVersion.setText("version "+getVersionName());
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.about_toolbar_left:
                finish();
                break;
            case R.id.about_agreement:
                showAgreement();
                break;
            case R.id.about_open_source_address:
                openSourceAddress();
                break;
            case R.id.about_contact_author:
                showContactWay();
                break;
        }
    }
    private void showAgreement(){
        String agreement = "        无忧日记是一款提供用户记录日记、便笺的工具。\n        " +
                "卸载应用前请在设置中对数据进行备份，否则将会造成数据丢失。" +
                "对于用户在此应用记录的内容不作任何保证：不保证日记和便笺等记录内容永久不会损坏、丢失。因任何原因导致您不能正常使用此应用，无忧日记不承担任何法律责任。" +
                "\n        应用尊重并保护所有使用无忧的用户的个人隐私权。您在无忧日记写下的所有日记和便笺都保存在手机本地，非经过您亲自许可或根据相关法律、法规强制性规定，应用不会以以任何形式主动泄露给第三方。" +
                "你同意上传至第三方的数据，因任何原因造成信息泄露、经济损失，无忧日记将不会承担任何法律责任。备份时请务必确认选择你信任的第三方云盘上传数据。";
        showMyDialog("用户协议说明", agreement);
    }
    private void showContactWay(){
        String text = "QQ交流群: 727508907\nGithub: https://github.com/shine56" ;
        showMyDialog("联系方式",text);
    }
    private void openSourceAddress(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("https://github.com/shine56/CareFree"));
        startActivity(intent);
    }
    private void showMyDialog(String title, String text){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_text,null,false);
        builder.setView(view);

        TextView textView = view.findViewById(R.id.dialog_text);
        TextView titleTextView = view.findViewById(R.id.dialog_text_title);
        titleTextView.setText(title);
        textView.setText(text);
        builder.create().show();
    }


    public String getAppName() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     * @return 当前应用的版本名称
     */
    public  String getVersionName() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * [获取应用程序版本名称信息]
     * @return 当前应用的版本名称
     */
    public int getVersionCode() {
        try {
            PackageManager packageManager = getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
