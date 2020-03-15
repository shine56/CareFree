package com.example.a73233.carefree.diary.viewModel;

import android.widget.Toast;

import com.example.a73233.carefree.bean.DiaryBean;
import com.example.a73233.carefree.diary.view.PhotoListAdapter;
import com.example.a73233.carefree.diary.view.WriteDiaryActivity;
import com.example.a73233.carefree.util.DataBackup;
import com.example.a73233.carefree.util.LogUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;
import static com.example.a73233.carefree.util.ConstantPool.AI;
import static com.example.a73233.carefree.util.ConstantPool.CALM;
import static com.example.a73233.carefree.util.ConstantPool.HAPPY;
import static com.example.a73233.carefree.util.ConstantPool.REPRESSION;
import static com.example.a73233.carefree.util.ConstantPool.SAD;

public class WriteVM extends LookVM implements DiaryVMImpl{
    private WriteDiaryActivity activity;
    public WriteVM(PhotoListAdapter adapter,WriteDiaryActivity activity) {
        super(adapter);
        this.activity = activity;
    }

    @Override
    public DiaryBean refreshBean(int id) {
        bean = model.findDataById(id);
        if(id == -1){
            Date date = new Date();
            String day = new SimpleDateFormat("dd").format(date);
            String yearAndMonth = new SimpleDateFormat("yyyy年MM月").format(date);
            String week = new SimpleDateFormat("EEEE").format(date);
            bean.yearAndMonth.set(yearAndMonth);
            bean.day.set(day);
            bean.week.set(week);
            bean.photoList.set(new ArrayList<>());
            bean.isShowSingleLine.set(true);
            bean.isShowDeleteButton.set(false);
        }
        return bean;
    }

    /**
     * 刷新书写提示线
     */
    public void refreshSingle(){
        String text = bean.diaryContent.get();
        if(text==null || text.equals("")){
            LogUtil.LogD("无文字");
            bean.isShowSingleLine.set(true);
        }else {
            LogUtil.LogD(text);
            bean.isShowSingleLine.set(false);
        }
    }
    /**
     * 移除一张图片
     * @param position
     */
    public void reMovePhoto(int position){
        File file = new File(bean.photoList.get().get(position));
        if(file.exists()){
            file.delete();
        }
        bean.photoList.get().remove(position);
        bean.photoList.set(bean.photoList.get());
        refreshPhoto();
    }

    /**
     * 添加一张图片
     * @param imgUrl
     */
    public void addPhoto(String imgUrl){
        bean.photoList.get().add(imgUrl);
        bean.photoList.set(bean.photoList.get());
        refreshPhoto();
    }

    /**
     * 通过UI刷新bean，保存日记
     * @param addType
     * @param diaryId
     */
    public void saveDiary(int addType, int diaryId){
        //addType != -1表示日记是新增的
        if(addType != -1){
            int value = 0;
            if(addType != AI){
                Random random = new Random();
                switch (addType){
                    case HAPPY:
                        value = random.nextInt(35)+15;
                        break;
                    case CALM:
                        value = random.nextInt(25)-10;
                        break;
                    case SAD:
                        value = random.nextInt(20)-30;
                        break;
                    case REPRESSION:
                        value = random.nextInt(20)-50;
                        break;
                }
            }
            bean.diaryEmotionValue.set(value);
        }
        model.saveData(bean,diaryId,this);
    }

    /**
     * 删除日记
     */
    public void abandonDiary(int diaryId){
        model.abandonData(diaryId,this);
    }

    @Override
    public void findAllDataSuccess(List<DiaryBean> beanList) {

    }

    @Override
    public void abandonDataSuccess(){
        activity.showToast("日记放进废纸篓，可前往恢复");
        activity.setResult(RESULT_OK-10);
        activity.onBackPressed();
    }
    public void saveDataSuccess(){
        model.setAddDataSum(activity,model.getAddDataSum(activity)+1);
        //判断是否需要自动备份数据
        if(model.getAutoData(activity)!=-1 && model.getAddDataSum(activity) >= model.getAutoData(activity)){
            backupData();
        }
        activity.showToast("日记保存成功");
        activity.setResult(RESULT_OK);
        activity.onBackPressed();
    }
    private void backupData(){
        Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(DataBackup.BackupDataSdCard(activity));
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean){
                            model.setAddDataSum(activity,0);
                            Toast.makeText(activity,"已经自动备份数据到本地", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(activity,"已经自动备份数据失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
