package com.example.a73233.carefree.bean;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

public class UserBean {
    public ObservableField<String> userName = new ObservableField<>();
    public ObservableField<String> userWords = new ObservableField<>();
    public ObservableField<String> userHeadIma = new ObservableField<>();
    public ObservableInt diaryNums = new ObservableInt();
    public ObservableInt noteNums = new ObservableInt();
    public ObservableInt abandonNums = new ObservableInt();
    public ObservableInt energyValue = new ObservableInt();
    public ObservableField<String> clockType = new ObservableField<>();
    public ObservableField<String> rank3Top = new ObservableField<>();
    public ObservableField<String> homeShowNote = new ObservableField<>();
}
