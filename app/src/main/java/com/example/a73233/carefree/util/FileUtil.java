package com.example.a73233.carefree.util;

import android.app.Activity;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.Scheduler;

public class FileUtil {
    /**
     * 复制文件
     * @param sourcePath
     * @param targetPath
     * @return
     */
    public static Boolean copyFile(String sourcePath, String targetPath){
        LogUtil.LogD("源文件路径"+sourcePath);
        LogUtil.LogD("目标文件路径"+targetPath);
        File sourceFile = new File(sourcePath);
        if(!sourceFile.exists()){
            LogUtil.LogD("复制文件失败->找不到源文件");
            return false;
        }
        File targetFile = new File(targetPath);
        try {
            if (!targetFile.exists()){
                targetFile.createNewFile();
            }
            InputStream inputStream = new FileInputStream(sourceFile);
            OutputStream outputStream = new FileOutputStream(targetFile);
            byte[] bytes = new byte[1024];
            int length = 0;

            while ((length = inputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,length);
            }
            inputStream.close();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            LogUtil.LogD("复制文件失败->"+e.getMessage());
            return false;
        }
    }


    public static void deleteFile(String path){
        File file = new File(path);
        file.delete();
    }
    /**
     * 创建APP必须文件夹
     */
    public static void createAppDirectory(Activity activity) {
        String sdPath = Environment.getExternalStorageDirectory().getPath();
        File parentDirectory = new File(sdPath, "CareFree");
        if (!parentDirectory.exists()) {
            parentDirectory.mkdirs();
        }
        File photosDirectory = new File(parentDirectory, "Photos");
        if (!photosDirectory.exists()) {
            photosDirectory.mkdirs();
        }
        File screenshotDirectory = new File(parentDirectory, "Screenshot");
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdirs();
        }

        File sdDirectory = activity.getExternalFilesDir(null);
        File externalFile = new File(sdDirectory, "0.txt");
        if (!externalFile.exists()){
            try {
                externalFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
