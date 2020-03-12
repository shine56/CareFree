package com.example.a73233.carefree.me.view

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.example.a73233.carefree.R
import com.example.a73233.carefree.baseView.BaseActivity
import com.example.a73233.carefree.databinding.ActivityHomeSettingBinding
import com.example.a73233.carefree.me.viewModel.SettingVM
import com.example.a73233.carefree.util.ConstantPool

class HomeSettingActivity : BaseActivity() {
    private lateinit var vm :SettingVM
    private lateinit var binding : ActivityHomeSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_home_setting)
        binding.homeSettingActivity = this
        vm = SettingVM(this)
        binding.bean = vm.initHomeSetting()
        ReviseStatusBar(TRANSPARENT_BLACK)
    }

    fun onClick(view : View){
        when(view.id){
            R.id.setting_home_note -> setHomeShowNote()
            R.id.setting_home_card -> setHomeCard()
            R.id.home_setting_toolbar_left -> finish()
        }
    }
    private fun setHomeShowNote() {
        val items = arrayOf(ConstantPool.HOME_SHOW_NOTE, ConstantPool.NOT_HOME_SHOW_NOTE)
        val alertDialog = AlertDialog.Builder(this)
                .setItems(items) { _, i ->
                    when (i) {
                        0 -> vm.setHomeBean(ConstantPool.HOME_SHOW_NOTE,null)
                        1 -> vm.setHomeBean( ConstantPool.NOT_HOME_SHOW_NOTE,null)
                    }
                }.create()
        alertDialog.show()
    }
    private fun setHomeCard() {
        val items = arrayOf(ConstantPool.CARD_SHOW_EMOTION, ConstantPool.CARD_SHOW_ENAGRY)
        val alertDialog = AlertDialog.Builder(this)
                .setItems(items) { _, i ->
                    when (i) {
                        0 -> vm.setHomeBean(null, ConstantPool.CARD_SHOW_EMOTION)
                        1 -> vm.setHomeBean( null, ConstantPool.CARD_SHOW_ENAGRY)
                    }
                }.create()
        alertDialog.show()
    }

    override fun finish() {
        super.finish()
        vm.saveHomeSetting()
    }
}
