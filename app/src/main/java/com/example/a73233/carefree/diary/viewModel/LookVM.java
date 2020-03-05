package com.example.a73233.carefree.diary.viewModel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;

import com.example.a73233.carefree.bean.DiaryBean;
import com.example.a73233.carefree.diary.Model.DiaryModel;
import com.example.a73233.carefree.diary.view.LookDiaryActivity;
import com.example.a73233.carefree.diary.view.PhotoListAdapter;
import com.example.a73233.carefree.util.LogUtil;
import com.example.a73233.carefree.util.PhotoManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.a73233.carefree.util.ConstantPool.REQUEST_MEDIA_PROJECTION;

public class LookVM{
    protected DiaryModel model;
    protected DiaryBean bean;
    protected PhotoListAdapter adapter;

    public LookVM(PhotoListAdapter adapter) {
        this.adapter = adapter;
        model = new DiaryModel();
    }

    /**
     * 刷新bean
     * @param id
     * @return
     */
    public DiaryBean refreshBean(int id){
        bean =  model.findDataById(id);
        return bean;
    }

    /**
     * 刷新图片
     */
    public void refreshPhoto(){
        adapter.setPhotoPathList(bean.photoList.get());
    }

    /**
     * 获取当前日记情绪值
     * @return
     */
    public int getValue(){
        return  bean.diaryEmotionValue.get();
    }
    public void shareDiary(LookDiaryActivity activity){
        String imgPath = PhotoManager.screenshot(activity);
        if(imgPath != null){
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, PhotoManager.getImageContentUri(activity,new File(imgPath)));
            shareIntent.setType("image/jpg");
            shareIntent.putExtra(Intent.EXTRA_TEXT,bean.diaryContent.get());
            activity.startActivity(Intent.createChooser(shareIntent, "Share to..."));
            activity.showToast("你触发了长按分享的菜单的彩蛋(´⊙ω⊙`)！");
        }
//        ArrayList<Uri> imgUris = new ArrayList<>();
//        for(String path : bean.photoList.get()){
//            Uri uri = PhotoManager.getImageContentUri(activity,new File(path));
//            LogUtil.LogD("返回的Uri是："+uri.toString());
//            imgUris.add(uri);
//        }
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
//        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,imgUris);
//        shareIntent.setType("image/*");
//        activity.startActivity(Intent.createChooser(shareIntent, "Share to..."));
//        activity.showToast("你触发了长按分享的菜单的彩蛋(´⊙ω⊙`)！");
    }
}
