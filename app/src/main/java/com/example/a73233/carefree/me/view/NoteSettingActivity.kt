package com.example.a73233.carefree.me.view

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import com.example.a73233.carefree.R
import com.example.a73233.carefree.baseView.BaseActivity
import com.example.a73233.carefree.databinding.ActivityNoteSettingBinding
import com.example.a73233.carefree.me.viewModel.SettingVM
import com.example.a73233.carefree.util.ConstantPool

class NoteSettingActivity : BaseActivity() {
    private lateinit var binding : ActivityNoteSettingBinding
    private lateinit var vm : SettingVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_note_setting)
        vm = SettingVM(this)
        binding.activity = this
        binding.bean = vm.initNoteSetting()
        ReviseStatusBar(TRANSPARENT_BLACK)
    }
    fun onClick(v : View){
        when(v.id){
            R.id.note_setting_clock_type -> setClockType()
            R.id.note_setting_task_top -> setTaskTop()
            R.id.note_setting_toolbar_left -> finish()
        }
    }
    private fun setClockType() {
        val items = arrayOf(ConstantPool.SYSTERM_CLOCK, ConstantPool.NOT_SYSTERM_CLOCK)
        val alertDialog = AlertDialog.Builder(this)
                .setItems(items) { _, i ->
                    when (i) {
                        0 -> vm.setNoteBean(null,ConstantPool.SYSTERM_CLOCK)
                        1 -> vm.setNoteBean( null,ConstantPool.NOT_SYSTERM_CLOCK)
                    }
                }.create()
        alertDialog.show()
    }
    private fun setTaskTop() {
        val items = arrayOf(ConstantPool.TASK_IS_TOP, ConstantPool.TASK_IS_NOT_TOP)
        val alertDialog = AlertDialog.Builder(this)
                .setItems(items) { _, i ->
                    when (i) {
                        0 -> vm.setNoteBean(ConstantPool.TASK_IS_TOP,null)
                        1 -> vm.setNoteBean(ConstantPool.TASK_IS_NOT_TOP,null)
                    }
                }.create()
        alertDialog.show()
    }

    override fun finish() {
        super.finish()
        vm.saveNoteSetting()
    }
}
