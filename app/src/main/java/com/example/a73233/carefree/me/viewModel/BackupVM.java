package com.example.a73233.carefree.me.viewModel;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.a73233.carefree.util.ConstantPool;
import com.example.a73233.carefree.util.DataBackup;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.MODE_PRIVATE;

public class BackupVM {
    private Activity activity;

    public BackupVM(Activity activity) {
        this.activity = activity;
    }
    public void backupData(Dialog dialog, int type){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                if(type == ConstantPool.CLOUND){
                    if(getName() == null){
                        e.onNext(0);
                    }else if(getPas() == null){
                        e.onNext(1);
                    }else if(getUrl() == null){
                        e.onNext(2);
                    }else if(DataBackup.BackupDataCloud(getName(), getPas(),getUrl(),activity)){
                        e.onNext(3);
                    }else {
                        e.onNext(4);
                    }
                }else {
                    if(DataBackup.BackupDataSdCard(activity)){
                        e.onNext(3);
                    }else {
                        e.onNext(4);
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer i) throws Exception {
                        dialog.dismiss();
                        switch (i){
                            case 0:
                                Toast.makeText(activity, "没有配置账号", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(activity, "没有配置密码", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(activity, "没有配置服务器地址", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(activity, "备份成功", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(activity, "备份失败", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                });
    }

    public void restoreData(Dialog dialog, int type){
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                if(type == ConstantPool.CLOUND){
                    if(getName() == null){
                        e.onNext(0);
                    }else if(getPas() == null){
                        e.onNext(1);
                    }else if(getUrl() == null){
                        e.onNext(2);
                    }else if(DataBackup.RestoreDataCloud(getName(), getPas(),getUrl(),activity)){
                        e.onNext(3);
                    }else {
                        e.onNext(4);
                    }
                }else {
                    if(DataBackup.ReStoreDataFromSd(activity)){
                        e.onNext(3);
                    }else {
                        e.onNext(4);
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer i) throws Exception {
                        dialog.dismiss();
                        switch (i){
                            case 0:
                                Toast.makeText(activity, "没有配置账号", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(activity, "没有配置密码", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(activity, "没有配置服务器地址", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(activity, "恢复成功", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                Toast.makeText(activity, "恢复失败", Toast.LENGTH_SHORT).show();
                                break;

                        }
                    }
                });
    }


    public void setName(String name){
        saveData("name", name);
    }
    public void setPas(String pas){
        saveData("password", pas);
    }
    public void setUrl(String url){
        saveData("url", url);
    }
    public String getName(){
        return getData("name");
    }
    public String getPas(){
        return getData("password");
    }
    public String getUrl(){
        return getData("url");
    }

    private void saveData(String flag, String content){
        SharedPreferences.Editor editor = activity.getSharedPreferences("backup_data",MODE_PRIVATE).edit();
        editor.putString(flag, content);
        editor.apply();
    }
    private String getData(String flag){
        SharedPreferences pref = activity.getSharedPreferences("backup_data",MODE_PRIVATE);
        return pref.getString(flag,null);
    }
}
