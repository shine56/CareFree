package com.example.a73233.carefree.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.EditText;

import com.example.a73233.carefree.diary.view.WriteDiaryActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PhotoManager {
    private static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    //拍照
    public static String TakePhoto(Activity activity){
        String imagePath;
        String sdPath;
        String imageName;
        File outPutImage;
        Uri imageUri;

        //图片会随着APP删除而删除
       sdPath = activity.getExternalFilesDir(null).getPath();
       imageName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
       imagePath = sdPath +"/"+ imageName;
       outPutImage = new File(imagePath);
       if(Build.VERSION.SDK_INT > 24){
           imageUri = FileProvider.getUriForFile(activity,
                   "com.example.a73233.carefree.util.fileprovider",outPutImage);
       }else {
           imageUri = Uri.fromFile(outPutImage);
       }
       //启动相机
       Intent intent = new Intent();
       intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
       intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
       activity.startActivityForResult(intent, TAKE_PHOTO);
       Log.d("启动相机测试","启动相机完成");
       return imagePath;
    }

    /**
     * 根据Uri获取图片真实路径
     * @param activity
     * @param uri
     * @return
     */
    public static String GetPathFromUri(Activity activity, Uri uri){
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(activity, uri)) {
            // 如果是document类型的 uri, 则通过document id来进行处理
            String documentId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                // 使用':'分割
                String id = documentId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(activity, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content:" +
                        "//downloads/public_downloads"), Long.valueOf(documentId));
                imagePath = getImagePath(activity, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri
            imagePath = getImagePath(activity, uri, null);
        } else if ("file".equals(uri.getScheme())) {
            // 如果是 file 类型的 Uri,直接获取图片对应的路径
            imagePath = uri.getPath();
        }
        return imagePath;
    }
    private static String getImagePath(Activity activity, Uri uri, String selection){
        String imagePath = null;
        Cursor cursor = activity.getContentResolver().query(uri,null,selection,
                null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore
                .Images.Media.DATA));
            }
            cursor.close();
        }
        return imagePath;
    }

    /**
     * 根据路径将图片另存于activity.getExternalFilesDir().getPath();
     * @param activity
     * @param oldPAth
     * @return
     */
    public static String copyPhoto(Activity activity,String oldPAth){
        String sdPath = activity.getExternalFilesDir(null).getPath();
        String imageName =  new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        String newPath = sdPath + "/" + imageName;
        File newFile = new File(newPath);
        int byteReadLength = 0;

        try {
            if(!newFile.exists()){
                InputStream inputStream = new FileInputStream(oldPAth);
                OutputStream outputStream = new FileOutputStream(newFile);
                byte[] bytes = new byte[1024];

                while ((byteReadLength = inputStream.read(bytes)) != -1){
                    outputStream.write(bytes,0,byteReadLength);
                }
                inputStream.close();
                outputStream.close();
                return newPath;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("复制图片测试","复制图片失败 e :"+e);
        } catch (IOException e) {
            Log.d("复制图片测试","复制图片失败 e :"+e);
            e.printStackTrace();
        }
        return null;
    }
    //EditText添加图片
    public static void editViewInsertPhoto (Activity activity, EditText editText, String imagePath){
        Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();
        float newWidth = originalWidth;
        float newHeight = originalHeight;
        Log.d("图文编辑测试","原始高和宽："+originalWidth+"  "+originalHeight);
        int screenW = editText.getWidth();
        if(originalWidth>screenW){
            newWidth = screenW;
            newHeight = originalHeight * (newWidth/originalWidth);
            Log.d("图文编辑测试","新的高和宽："+newWidth+"  "+newHeight);
        }
        //计算宽、高缩放率
        float scanleWidth = (float)newWidth/originalWidth;
        float scanleHeight = (float)newHeight/originalHeight;
        //创建操作图片用的matrix对象 Matrix
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scanleWidth,scanleHeight);
        //旋转图片 动作
        //matrix.postRotate(45);
        // 创建新的图片Bitmap
        Bitmap bitmap = Bitmap.createBitmap(originalBitmap,0,0,originalWidth,originalHeight,matrix,true);

        ImageSpan imageSpan = new ImageSpan(activity, bitmap);
        SpannableString spannableString = new SpannableString("<img src=\""+imagePath+"\"/>");
        spannableString.setSpan(imageSpan, 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        int index = editText.getSelectionStart(); //获取光标所在位置
        Editable edit_text = editText.getEditableText();
        if(index <0 || index >= edit_text.length()){
            edit_text.append(spannableString);
        }else{
            edit_text.insert(index, spannableString);
        }
    }
}
