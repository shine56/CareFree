package com.example.a73233.carefree.bean;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

public class NoteBean {
    public final ObservableInt id = new ObservableInt();
    public final ObservableInt rank = new ObservableInt();
    public final ObservableInt idAbandon = new ObservableInt();
   // public final ObservableInt isComplete = new ObservableInt();
    public final ObservableField<String> text = new ObservableField<>();
    public final ObservableField<String> monthAndDay = new ObservableField<>();
    public final ObservableField<String> week = new ObservableField<>();
    public final ObservableField<String> time = new ObservableField<>();
    public final ObservableField<String> year = new ObservableField<>();
    public final ObservableInt hour = new ObservableInt();
    public final ObservableInt minutes = new ObservableInt();
    public final ObservableField<String> clockText = new ObservableField<>();
}
