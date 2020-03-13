package com.example.a73233.carefree.me.viewModel

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.graphics.drawable.GradientDrawable
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.widget.TextView
import com.example.a73233.carefree.bean.SettingBean
import com.example.a73233.carefree.util.ConstantPool
import com.example.a73233.carefree.util.EmotionDataUtil
import com.example.a73233.carefree.util.LogUtil

class SettingVM {
    var activity : Activity
    lateinit var bean : SettingBean

    constructor(activity: Activity) {
        this.activity = activity
    }

    /**
     * 首页设置，下面三个函数分别是
     * 初始化
     * 保存
     * 更新
     */
    fun initHomeSetting() : SettingBean{
        val pref  = activity.getSharedPreferences("setting",MODE_PRIVATE)
        bean = SettingBean()
        bean.cardShow.set(pref.getString("cardShow", ConstantPool.CARD_SHOW_EMOTION))
        bean.homeShowNote.set(pref.getString("homeShowNote", ConstantPool.NOT_HOME_SHOW_NOTE))
        return bean
    }
    fun saveHomeSetting(){
        val editor = activity.getSharedPreferences("setting", MODE_PRIVATE).edit()
        editor.putString("homeShowNote",bean.homeShowNote.get())
        editor.putString("cardShow",bean.cardShow.get())
        editor.apply()
    }
    fun setHomeBean(homeShowNote: String?, cardShow: String?){
        if(homeShowNote != null)
            bean.homeShowNote.set(homeShowNote)
        if (cardShow != null)
            bean.cardShow.set(cardShow)
    }

    /**
     * 笔记设置，下面三个函数分别是
     * 初始化
     * 保存
     * 更新
     */
    fun initNoteSetting():SettingBean{
        val pref  = activity.getSharedPreferences("setting",MODE_PRIVATE)
        bean = SettingBean()
        bean.taskIsTop.set(pref.getString("taskIsTop", ConstantPool.TASK_IS_NOT_TOP))
        bean.clockType.set(pref.getString("clockType", ConstantPool.NOT_SYSTERM_CLOCK))
        return bean
    }
    fun saveNoteSetting(){
        val editor = activity.getSharedPreferences("setting", MODE_PRIVATE).edit()
        editor.putString("taskIsTop", bean.taskIsTop.get())
        editor.putString("clockType",bean.clockType.get())
        editor.apply()
    }
    fun setNoteBean(rinkIsTop : String?, clockType: String?){
        if(rinkIsTop != null)
            bean.taskIsTop.set(rinkIsTop)
        if(clockType != null)
            bean.clockType.set(clockType)
    }
    fun saveLockSetting(text : String){
        val editor = activity.getSharedPreferences("setting", MODE_PRIVATE).edit()
        editor.putString("lock", text)
        editor.apply()
    }
    fun getLockSetting() : String{
        val pref  = activity.getSharedPreferences("setting",MODE_PRIVATE)
        return pref.getString("lock","关")
    }

    /**
     * 保存背景设置
     */

    fun saveDiaryBgColor(){
        val editor = activity.getSharedPreferences("setting", MODE_PRIVATE).edit()
        editor.putInt("happy_color_start", bean.colors.get()!![0].toLong(16).toInt())
        editor.putInt("happy_color_end",bean.colors.get()!![1].toLong(16).toInt())
        editor.putInt("calm_color_start",bean.colors.get()!![2].toLong(16).toInt())
        editor.putInt("calm_color_end",bean.colors.get()!![3].toLong(16).toInt())
        editor.putInt("sad_color_start",bean.colors.get()!![4].toLong(16).toInt())
        editor.putInt("sad_color_end",bean.colors.get()!![5].toLong(16).toInt())
        editor.putInt("repression_color_start",bean.colors.get()!![6].toLong(16).toInt())
        editor.putInt("repression_color_end",bean.colors.get()!![7].toLong(16).toInt())

        editor.putBoolean("isOriginColor",bean.isOringinColor.get())
        LogUtil.LogD("保存设置"+bean.isOringinColor.get())
        editor.apply()
    }
    fun setDiaryColorType(boolean: Boolean){
        bean.isOringinColor.set(boolean)
    }
    fun getDiaryColorType():Boolean{
        return bean.isOringinColor.get()
    }

    /**
     * 将int转化成String设置到bean
     */
    fun getColorBean(context: Context) : SettingBean{
        bean = SettingBean()
        var colors = getColorFromPref(context)
        var colorStr : Array<String> = Array(10) {"0"}
        for(i in 0 until 8){
            colorStr[i] = String.format("%x",colors[i])
            LogUtil.LogD("颜色$i = "+String.format("%x",colors[i]))
        }
        bean.colors.set(colorStr)

        val pref = context.getSharedPreferences("setting", MODE_PRIVATE)
        bean.isOringinColor.set(pref.getBoolean("isOriginColor",true))
        return bean
    }

    /**
     * String 转成int
     */
    fun setExampleColor(con: ConstraintLayout, ii:Int){
        var i = ii
        if(i%2 != 0){
            i -= 1
        }
        val colors = IntArray(2)
        if (getColor(i) == null || getColor(i) == ""){
            colors[0] = 0
        }else{
            colors[0] = getColor(i).toLong(16).toInt()
        }
        if(getColor(i+1) == null ||getColor(i+1) == ""){
            colors[1] = 0
        }else{
            colors[1] = getColor(i+1).toLong(16).toInt()
        }
        var parentBackground = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,colors)
        parentBackground.cornerRadius = 30f
        con.background = parentBackground
    }
    private fun getColor(i : Int): String{
        return bean.colors.get()!![i]
    }
    fun setColor(i : Int, text : String){
        var colors = bean.colors.get()
        colors!![i] = text
        bean.colors.set(colors)
    }
    /**
     * 获取int型数组
     */
    private fun getColorFromPref(context: Context): IntArray {
        val colors = IntArray(10)
        val pref = context.getSharedPreferences("setting", MODE_PRIVATE)
        colors[0] = pref.getInt("happy_color_start", -0xc0542b)
        colors[1] = pref.getInt("happy_color_end", -0xc72a2a)
        colors[2] = pref.getInt("calm_color_start", -0xac851f)
        colors[3] = pref.getInt("calm_color_end", -0x9b4f18)
        colors[4] = pref.getInt("sad_color_start", -0x539625)
        colors[5] = pref.getInt("sad_color_end", -0x647a01)
        colors[6] = pref.getInt("repression_color_start", -0xf6dfc1)
        colors[7] = pref.getInt("repression_color_end", -0xd4a78a)
        return colors
    }
    fun isColorRight():Boolean{
        val colors = bean.colors.get()
        for(i in 0 until 8){
            val regex = Regex("^[A-Fa-f0-9]+$")
            if(colors!![i].length == 8){
                if(!colors[i].matches(regex)){
                    return false
                }
            }else{
                return false
            }
        }
        return true
    }
}