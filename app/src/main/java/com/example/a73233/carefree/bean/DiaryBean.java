package com.example.a73233.carefree.bean;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import java.util.List;

public class DiaryBean {
    public final ObservableField<List<String>> photoList = new ObservableField<>();
    public final ObservableField<String> diaryContent = new ObservableField<>();
    public final ObservableField<String> yearAndMonth = new ObservableField<>();
    public final ObservableField<String> day = new ObservableField<>();
    public final ObservableField<String> week = new ObservableField<>();
    public final ObservableInt diaryEmotionValue = new ObservableInt();
    public final ObservableInt id = new ObservableInt();
    public final ObservableBoolean isShowImgButton = new ObservableBoolean();
    public final ObservableBoolean isShowSingleLine = new ObservableBoolean();
    public final ObservableBoolean isShowDeleteButton = new ObservableBoolean();

}
