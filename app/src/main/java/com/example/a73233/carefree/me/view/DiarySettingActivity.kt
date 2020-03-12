package com.example.a73233.carefree.me.view

import android.app.Activity
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.example.a73233.carefree.R
import com.example.a73233.carefree.baseView.BaseActivity
import com.example.a73233.carefree.databinding.ActivityDiarySettingBinding
import com.example.a73233.carefree.me.viewModel.SettingVM
import android.widget.Toast


class DiarySettingActivity : BaseActivity(){
    private lateinit var binding : ActivityDiarySettingBinding
    private lateinit var vm : SettingVM
    public var flag : Boolean = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_setting)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_diary_setting)
        binding.activity = this
        vm = SettingVM(this)
        initView()
    }

    private fun initView(){
        ReviseStatusBar(TRANSPARENT_BLACK)
        binding.bean = vm.getColorBean(this)
        //初始化选择
        if(vm.getDiaryColorType()){
            binding.diarySettingOriginImg.setImageResource(R.mipmap.is_choose)
            binding.diarySettingCustomImg.setImageResource(R.mipmap.is_not_choose)
        }else{
            binding.diarySettingOriginImg.setImageResource(R.mipmap.is_not_choose)
            binding.diarySettingCustomImg.setImageResource(R.mipmap.is_choose)
        }
        //edit监控
        binding.settingHappyStartEdit.addTextChangedListener(MyTextWatcher(this,vm, binding.settingHappyBg,0))
        binding.settingHappyEndEdit.addTextChangedListener(MyTextWatcher(this,vm, binding.settingHappyBg,1))

        binding.settingCalmStartEdit.addTextChangedListener(MyTextWatcher(this,vm, binding.settingCalmBg,2))
        binding.settingCalmEndEdit.addTextChangedListener(MyTextWatcher(this,vm, binding.settingCalmBg,3))

        binding.settingSadStartEdit.addTextChangedListener(MyTextWatcher(this,vm,binding.settingSadBg,4))
        binding.settingSadEndEdit.addTextChangedListener(MyTextWatcher(this,vm, binding.settingSadBg,5))

        binding.settingRepressionStartEdit.addTextChangedListener(MyTextWatcher(this,vm, binding.settingRepressionBg,6))
        binding.settingRepressionEndEdit.addTextChangedListener(MyTextWatcher(this,vm, binding.settingRepressionBg,7))
    }
    fun onClick(view : View){
        when(view.id){
            R.id.diary_setting_origin_bg ->{
                vm.setDiaryColorType(true)
                binding.diarySettingOriginImg.setImageResource(R.mipmap.is_choose)
                binding.diarySettingCustomImg.setImageResource(R.mipmap.is_not_choose)
            }
            R.id.diary_setting_custom_bg ->{
                vm.setDiaryColorType(false)
                binding.diarySettingOriginImg.setImageResource(R.mipmap.is_not_choose)
                binding.diarySettingCustomImg.setImageResource(R.mipmap.is_choose)
            }
            R.id.diary_setting_toolbar_left -> finish()
        }
    }
    override fun finish() {
        super.finish()
        if(vm.isColorRight() && flag){
            vm.saveDiaryBgColor()
        }else{
            showToast("存在不合法颜色码，无法自定义颜色")
            vm.setDiaryColorType(true)
            vm.saveDiaryBgColor()
        }

    }

    class MyTextWatcher : TextWatcher{
        private var activity : DiarySettingActivity
        private var vm : SettingVM
        private var bg : ConstraintLayout
        private var i : Int

        constructor(activity : DiarySettingActivity,vm: SettingVM, bg: ConstraintLayout, i: Int) {
            this.activity = activity
            this.vm = vm
            this.bg = bg
            this.i = i
        }
        override fun afterTextChanged(p0: Editable?) {
            val regex = Regex("^[A-Fa-f0-9]+$")
            if(p0.toString().length == 8){
                if(p0.toString().matches(regex)){
                    vm.setColor(i, p0.toString())
                    vm.setExampleColor(bg,i)
                    activity.flag = true
                }else{
                    activity.flag = false
                    Toast.makeText(activity,"你输入的颜色码不合法",Toast.LENGTH_SHORT).show()
                }
            }else{
                activity.flag = false
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

    }
}
