package com.example.a73233.carefree.util;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
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
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    /**
     * 拍照
     * @param activity
     * @return
     */
    public static String TakePhoto(Activity activity){
        String imageName;
        File outPutImage;
        Uri imageUri;
        //图片会随着APP删除而删除
        File sdDirectory = activity.getExternalFilesDir(null);
        imageName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        outPutImage = new File(sdDirectory,imageName);

       if(Build.VERSION.SDK_INT > 24){
           imageUri = FileProvider.getUriForFile(activity,
                   "com.example.a73233.carefree.util.fileprovider",outPutImage);
       }else {
           imageUri = Uri.fromFile(outPutImage);
       }
     //  启动相机
       Intent intent = new Intent();
       intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
       intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
       activity.startActivityForResult(intent, TAKE_PHOTO);
       return outPutImage.getPath();
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
     * Gets the content:// URI from the given corresponding path to a file
     * 绝对路径转uri
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID}, MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
    /**
     * 根据路径将图片另存于activity.getExternalFilesDir(null);
     * @param activity
     * @param sourcePAth
     * @return
     */
    public static String copyPhoto(Activity activity,String sourcePAth){
        String imageName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".jpg";
        String targetPath = activity.getExternalFilesDir(null).getPath()+"/"+imageName;
        if(FileUtil.copyFile(sourcePAth, targetPath)){
            return targetPath;
        }else {
            return null;
        }
    }
    /**
     *截图，除去状态栏
     */
    public static String screenshot(Activity activity){
        View dView = activity.getWindow().getDecorView();
        dView.destroyDrawingCache();
        dView.setDrawingCacheEnabled(true);
        dView.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(dView.getDrawingCache());
        if (bitmap != null) {
            Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,getStatusBarHeight(activity),
                    bitmap.getWidth(), bitmap.getHeight()-getStatusBarHeight(activity));
            try {
                String sdPath = Environment.getExternalStorageDirectory().getPath() +"/CareFree/Screenshot/";
                String imageName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "screenshot.jpg";
                File file = new File(sdPath,imageName);
                FileOutputStream os = new FileOutputStream(file);
                newBitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.flush();
                return file.getPath();
            } catch (Exception e) {
                LogUtil.LogD("截图失败"+e.getMessage());
                return null;
            }
        }else {
            LogUtil.LogD("截图失败");
            return null;
        }
    }
    //获取状态栏高度
    private static int getStatusBarHeight(Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    /**
     * edit插入图片
     * @param activity
     * @param editText
     * @param imagePath
     */
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


    public static void clipImage(Activity activity, String inputPath, String outputPath, int requestCode){
        ClipImageActivity.Companion.startCropImage(activity,
               requestCode,
                inputPath,
                outputPath,
                (int)(260 * activity.getResources().getDisplayMetrics().density),
                (int)(260 * activity.getResources().getDisplayMetrics().density), 2.0f, 0.5f);
    }
}
