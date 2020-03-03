package com.example.a73233.carefree.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import com.thegrizzlylabs.sardineandroid.DavResource;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 数据备份与恢复
 * created by shine56
 * date: 2020/2/29
 */

public class DataBackup {

    /**
     * 备份数据到本地
     *备份数据到getExternalStorageDirectory()
     */
    public static Boolean BackupDataSdCard(Activity context){
        int flag = 1;
        String sourcePath = context.getDatabasePath("diary.db").getPath();
        String dbTargetPath = Environment.getExternalStorageDirectory().getPath()+"/CareFree/diary.db";
        String photoTargetDirectory = Environment.getExternalStorageDirectory().getPath()+"/CareFree/Photos/";
        //复制数据库
        if(FileUtil.copyFile(sourcePath, dbTargetPath)){
            //复制图片
            File[] subFile = context.getExternalFilesDir(null).listFiles();
            for(int i=0; i<subFile.length; i++){
                if(!FileUtil.copyFile(subFile[i].getPath(),photoTargetDirectory+subFile[i].getName())){
                    flag = 0;
                }
            }
            if(flag == 1){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    /**
     * 从本地恢复数据
     */
    public static Boolean ReStoreDataFromSd(Activity context){
        int flag = 1;
        String dbTargetPath = context.getDatabasePath("diary.db").getPath();
        String dbSourcePath = Environment.getExternalStorageDirectory().getPath()+"/CareFree/diary.db";
        String photoSourceDirectory = Environment.getExternalStorageDirectory().getPath()+"/CareFree/Photos/";
        String photoTargetDirectory = context.getExternalFilesDir(null).getPath()+"/";
        //恢复数据库
        if(FileUtil.copyFile(dbSourcePath, dbTargetPath)){
            //恢复图片
            File[] subFiles = new File(photoSourceDirectory).listFiles();
            for(int i=0; i<subFiles.length; i++){
                if(!FileUtil.copyFile(subFiles[i].getPath(),photoTargetDirectory+subFiles[i].getName())){
                    flag = 0;
                }
            }
            if(flag == 1){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
    /**
     * 备份数据到云端
     *
     */
    public static Boolean BackupDataCloud(String name, String password, String url, Activity context){
        String dbUrl = url+"Carefree/";
        String photosUrl = dbUrl+"photos/";
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(name,password);
        try {
            if(!sardine.exists(dbUrl)){
                sardine.createDirectory(dbUrl);
            }
            if (!sardine.exists(photosUrl)){
                sardine.createDirectory(photosUrl);
            }

            if(sardine.exists(dbUrl+"diary.db")){
                sardine.delete(dbUrl+"diary.db");
            }
            //备份数据库
            File dbFile = context.getDatabasePath("diary.db");
            sardine.put(dbUrl+"diary.db",dbFile,null);

            //备份图片
            int sum =0;
            File[] subFile = context.getExternalFilesDir(null).listFiles();
            for(int i=0; i<subFile.length; i++){
                if(!sardine.exists(photosUrl+subFile[i].getName())){
                    sardine.put(photosUrl+subFile[i].getName(), subFile[i],null);
                    sum++;
                }
            }
            LogUtil.LogD("共上传"+sum+"张图片");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.LogD("上传失败"+e.getMessage());
            return false;
        }
    }

    /**
     * 从云端恢复数据
     * @param name
     * @param password
     * @param url
     * @param context
     * @return
     */
    public static Boolean RestoreDataCloud(String name, String password, String url, Activity context) {
        String dbUrl = url+"Carefree/";
        String photosUrl = dbUrl+"photos/";
        Sardine sardine = new OkHttpSardine();
        sardine.setCredentials(name,password);

        try {
            if(!sardine.exists(dbUrl)){
                return false;
            }
            if (!sardine.exists(photosUrl)){
                return false;
            }
            //恢复数据库
            File dbFile = context.getDatabasePath("diary.db");
            InputStream dbIs = sardine.get(dbUrl+"diary.db");
            OutputStream dbOs = new FileOutputStream(dbFile);
            byte[] bytes = new byte[1024];
            int length = 0;
            while ((length = dbIs.read(bytes)) != -1){
                dbOs.write(bytes,0,length);
            }
            dbIs.close();
            dbOs.close();

            //恢复图片
            int sum = 0;
            List<DavResource> resources = sardine.list(photosUrl);
            for (int i=1; i<resources.size(); i++){
                File targetFile = new File(context.getExternalFilesDir(null), resources.get(i).getName());
                if(!targetFile.exists()){
                    InputStream imgIs = sardine.get(photosUrl + resources.get(i).getName());
                    OutputStream imgOs = new FileOutputStream(targetFile);
                    byte[] bytes_ = new byte[1024];
                    int length_ = 0;
                    while ((length_ = imgIs.read(bytes_)) != -1){
                        imgOs.write(bytes_,0,length_);
                    }
                    imgIs.close();
                    imgOs.close();
                    sum++;
                }
            }
            LogUtil.LogD("共拉取"+sum+"张图片");
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            LogUtil.LogD("拉取失败"+e.getMessage());
            return false;
        }
    }
}
