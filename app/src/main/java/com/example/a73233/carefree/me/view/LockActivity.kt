package com.example.a73233.carefree.me.view

import android.app.Dialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal
import android.support.v7.app.AlertDialog
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.example.a73233.carefree.R
import com.example.a73233.carefree.baseView.BaseActivity
import com.example.a73233.carefree.baseView.FingerprintImpl
import com.example.a73233.carefree.me.viewModel.SettingVM
import com.example.a73233.carefree.util.ConstantPool
import com.example.a73233.carefree.util.FingerDiscentListener

class LockActivity : BaseActivity() ,FingerprintImpl{
    private lateinit var vm : SettingVM
    private lateinit var textView: TextView
    private lateinit var back : ImageView
    private lateinit var lock : ConstraintLayout
    private lateinit var fingerprintManagerCompat: FingerprintManagerCompat
    //用于取消指纹识别器监听的对象
    private lateinit var cancellationSignal: CancellationSignal
    private lateinit var dialog : Dialog
    private var flag = -1


    override fun verifyFaile() {
        showToast("认证失败，请验证手机系统已录入的指纹")
        dialog.dismiss()
    }

    override fun verifySuccess() {
        when (flag){
            0 -> {
                vm.saveLockSetting("开")
                textView.text = "开"
            }
            1 -> {
                vm.saveLockSetting("关")
                textView.text = "关"
            }
        }
        dialog.dismiss()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
        ReviseStatusBar(TRANSPARENT_BLACK)
        vm = SettingVM(this)
        textView = findViewById(R.id.fingerprint_lock_text)
        textView.text = vm.getLockSetting()
        lock = findViewById(R.id.fingerprint_lock)
        lock.setOnClickListener {setLock()}

        back = findViewById(R.id.lock_toolbar_left)
        back.setOnClickListener{finish()}

    }
    private fun setLock() {
        val items = arrayOf("开","关")
        val alertDialog = AlertDialog.Builder(this)
                .setItems(items) { _, i ->
                    when (i) {
                        0 -> {
                            if (textView.text != "开") verifyFingerprint(i)
                        }
                        1 -> {
                            if (textView.text != "关") verifyFingerprint(i)
                        }
                    }
                }.create()
        alertDialog.show()
    }
    private fun verifyFingerprint(i : Int){
        flag = i
        fingerprintManagerCompat = FingerprintManagerCompat.from(this)
        if (fingerprintManagerCompat.hasEnrolledFingerprints()) {
            cancellationSignal = CancellationSignal()
            fingerprintManagerCompat.authenticate(null, 0, cancellationSignal,
                    FingerDiscentListener(cancellationSignal, this), null)
            showFingerPrintDialog()
        } else {
            showToast("手机未设置指纹指纹，无法设置指纹锁")
        }
    }
    private fun showFingerPrintDialog() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_figerprint, null, false)
        dialog = Dialog(this, R.style.ActionSheetDialogStyle)
        dialog.setContentView(view)
        //获取当前Activity所在的窗体
        val dialogWindow = dialog.getWindow()
        //设置Dialog从窗体底部弹出
        dialogWindow!!.setGravity(Gravity.BOTTOM)
        //获得窗体的属性
        val lp = dialogWindow.attributes
        //宽度填满
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = (this.windowManager.defaultDisplay.height * 0.4).toInt()
        //将属性设置给窗体
        dialogWindow.setAttributes(lp)
        //不可被取消
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        //点击监控
        val textView = view.findViewById<TextView>(R.id.dialog_fingerprint_cancel)
        textView.setOnClickListener {
            cancellationSignal.cancel()
            dialog.dismiss()
        }
        dialog.show()
    }
}
