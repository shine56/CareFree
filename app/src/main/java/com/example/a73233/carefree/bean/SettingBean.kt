package com.example.a73233.carefree.bean

import android.databinding.ObservableBoolean
import android.databinding.ObservableField

class SettingBean {
    var clockType = ObservableField<String>()
    var taskIsTop = ObservableField<String>()
    var homeShowNote = ObservableField<String>()
    var cardShow = ObservableField<String>()
    var isOringinColor = ObservableBoolean()
    var colors = ObservableField<Array<String>>()
}