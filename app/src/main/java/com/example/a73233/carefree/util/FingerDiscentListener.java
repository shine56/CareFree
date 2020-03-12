package com.example.a73233.carefree.util;

import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import com.example.a73233.carefree.baseView.FingerprintImpl;

public class FingerDiscentListener extends FingerprintManagerCompat.AuthenticationCallback {
    private CancellationSignal cancellationSignal;
    private FingerprintImpl activity;

    public FingerDiscentListener(CancellationSignal cancellationSignal, FingerprintImpl activity) {
        this.cancellationSignal = cancellationSignal;
        this.activity = activity;
    }
    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        super.onAuthenticationError(errMsgId, errString);
        LogUtil.LogD("id:"+errMsgId+"    "+errString);
        if (errMsgId == 5) {
            //取消识别
            LogUtil.LogD("取消识别");
        } else if (errMsgId == 7) {
            //tvHint.setText("操作过于频繁，请稍后重试");
            LogUtil.LogD("识别操作过于频繁，请稍后重试");
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
                cancellationSignal = null;
            }
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        //指纹识别成功
        cancellationSignal.cancel();//取消指纹的监听
        LogUtil.LogD("识别成功");
        activity.verifySuccess();

    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        //指纹识别失败
        LogUtil.LogD("识别失败");
        cancellationSignal.cancel();//取消指纹的监听
        activity.verifyFaile();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        super.onAuthenticationHelp(helpMsgId, helpString);
        LogUtil.LogD("555id:"+helpMsgId+"    "+helpString);
    }
}
