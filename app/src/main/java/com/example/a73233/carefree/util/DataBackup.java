package com.example.a73233.carefree.util;

import android.os.Environment;

import com.example.a73233.carefree.bean.Diary_db;
import com.example.a73233.carefree.bean.Note_db;
import com.example.a73233.carefree.bean.User_db;
import com.example.a73233.carefree.me.viewModel.MeVM;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * 数据备份与恢复
 * created by shine56
 * date: 2020/2/29
 */

public class DataBackup {

    /**
     *备份数据到getExternalStorageDirectory()
     */
    public static void BackupData(MeVM vm){
        List<Diary_db> diaryDbs = LitePal.findAll(Diary_db.class);
        List<Note_db> noteDbs = LitePal.findAll(Note_db.class);
        List<User_db> userDbs = LitePal.findAll(User_db.class);
        File sdCard = Environment.getExternalStorageDirectory();
        File parentFile = new File(sdCard,"无忧日记-备份");
        if(!parentFile.exists()){
            parentFile.mkdir();
        }

        File diaryFile = new File(parentFile,"Diary");
        File noteFile = new File(parentFile,"note");
        File userFile = new File(parentFile,"user");
        if(!diaryFile.exists()){
            diaryFile.mkdir();
        }
        if(!noteFile.exists()){
            noteFile.mkdir();
        }
        if(!userFile.exists()){
            userFile.mkdir();
        }

        for(int i=0; i<diaryDbs.size(); i++){
            File targetFile = new File(diaryFile,"d"+i);
            try {
                if(!targetFile.exists()){
                    targetFile.createNewFile();
                }
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(targetFile));
                oos.writeObject(diaryDbs.get(i));
                oos.close();
            }catch (IOException e){
                e.printStackTrace();
                vm.backupFail(e);
            }
        }
        for(int i=0; i<noteDbs.size(); i++){
            File targetFile = new File(noteFile,"n"+i);
            try {
                if(!targetFile.exists()){
                    targetFile.createNewFile();
                }
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(targetFile));
                oos.writeObject(noteDbs.get(i));
                oos.close();
            }catch (IOException e){
                e.printStackTrace();
                vm.backupFail(e);
            }
        }
        File targetFile = new File(userFile,"u0");
        try {
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(targetFile));
            oos.writeObject(userDbs.get(0));
            oos.close();
            vm.backupSuccess();
        }catch (IOException e){
            e.printStackTrace();
            vm.backupFail(e);
        }

        writeDataInFile("1","mark.txt");
    }

    /**
     * 恢复数据
     */
    public static Boolean ReStoreData(){
        File sdCard = Environment.getExternalStorageDirectory();
        File parentFile = new File(sdCard,"无忧日记-备份");
        if(!parentFile.exists()){
            parentFile.mkdir();
        }

        File diaryFile = new File(parentFile,"Diary");
        File noteFile = new File(parentFile,"note");
        File userFile = new File(parentFile,"user");
        if(!diaryFile.exists()){
            diaryFile.mkdir();
        }
        if(!noteFile.exists()){
            noteFile.mkdir();
        }
        if(!userFile.exists()){
            userFile.mkdir();
        }
        File[] dFiles = diaryFile.listFiles();
        File[] nFiles = noteFile.listFiles();
        File uFile = new File(userFile,"u0");

        for(int i=0; i<dFiles.length; i++){
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dFiles[i]));
                Diary_db diary_db = (Diary_db) ois.readObject();
                ois.close();
                diary_db.save();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        for(int i=0; i<nFiles.length; i++){
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nFiles[i]));
                Note_db note_db  = (Note_db) ois.readObject();
                ois.close();
                note_db.save();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        if(uFile.exists()){
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(uFile));
                User_db user_db  = (User_db) ois.readObject();
                ois.close();
                LitePal.deleteAll(User_db.class);
                user_db.save();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }else {
            try {
                uFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }
    }

    /**
     * 将字符串存入getExternalStorageDirectory()指定文件
     * @param data
     * @param fileName
     * @return
     */
    private static Boolean writeDataInFile(String data, String fileName){
        File sdCard = Environment.getExternalStorageDirectory();
        File parentFile = new File(sdCard,"无忧日记-备份");
        File targetFile = new File(parentFile,fileName);
        try {
            if(!parentFile.exists()){
                parentFile.mkdir();
            }
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            OutputStream outputStream = new FileOutputStream(targetFile);
            byte[] bytes = new byte[1024];
            bytes = data.getBytes();
            outputStream.write(bytes,0, bytes.length);
            outputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
    }
    public static Boolean getMarkData(){
        File sdCard = Environment.getExternalStorageDirectory();
        File parentFile = new File(sdCard,"无忧日记-备份");
        if (!parentFile.exists()){
            return false;
        }
        File targetFile = new File(parentFile,"mark.txt");

        if(targetFile.exists()){
            try {
                FileInputStream fis = new FileInputStream(targetFile);
                byte[] bytes = new byte[1024];
                int len = fis.read(bytes);
                String mark = new String(bytes,0, len);
                fis.close();

                if(mark.equals("1")){
                    return true;
                }else return false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }else {
            return false;
        }
    }
    public static void initPhotoFile(){
        File sdCard = Environment.getExternalStorageDirectory();
        File parentFile = new File(sdCard,"无忧日记-备份");
        File targetFile = new File(parentFile,"Photos");
        if (!targetFile.exists()){
            targetFile.mkdir();
        }
    }
}
