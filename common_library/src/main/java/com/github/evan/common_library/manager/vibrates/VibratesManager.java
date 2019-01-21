package com.github.evan.common_library.manager.vibrates;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.github.evan.common_library.utils.PackageUtil;
import java.util.List;

/**
 * Created by Evan on 2019/1/21 0021.
 */
public class VibratesManager {
    private static VibratesManager mIns = null;

    public static VibratesManager getIns(Context context) {
        if (null == mIns) {
            synchronized (VibratesManager.class) {
                if (null == mIns) {
                    mIns = new VibratesManager(context);
                    return mIns;
                }
            }
        }

        return mIns;
    }

    private Vibrator mVibrator;

    private VibratesManager(Context context) {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    /**
     * 设备是否有震动装置
     */
    public boolean isDeviceHasVibrate() {
        return mVibrator.hasVibrator();
    }

    /**
     * 停止震动
     */
    public boolean cancelVibrate() {
        List<String> permission = PackageUtil.checkPermission(Manifest.permission.VIBRATE);
        if (null != permission && !permission.isEmpty()) {
            //没权限
            return false;
        }
        mVibrator.cancel();
        return true;
    }

    /**
     * 震动一次
     * @param duration  震动时长
     * @param amplitude 震动强度    Android 8.0后有效 1-255 -1默认强度
     * @return false为没有权限
     */
    public boolean vibrateOnce(long duration, int amplitude) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permission = PackageUtil.checkPermission(Manifest.permission.VIBRATE);
            if (null != permission && !permission.isEmpty()) {
                //没权限
                return false;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //8.0以上可以设置震动强度
            if (amplitude <= 0 || amplitude > 255) {
                amplitude = VibrationEffect.DEFAULT_AMPLITUDE;
            }
            VibrationEffect vibrationEffect = VibrationEffect.createOneShot(duration, amplitude);
            mVibrator.vibrate(vibrationEffect);
        } else {
            mVibrator.vibrate(duration);
        }
        return true;
    }

    /**
     * 波形震动
     * @param duration
     * @param repeatCount   -1 不重复， 0循环震动
     * @return
     */
    public boolean vibrate(long[] duration, int repeatCount) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permission = PackageUtil.checkPermission(Manifest.permission.VIBRATE);
            if (null != permission && !permission.isEmpty()) {
                //没权限
                return false;
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            VibrationEffect vibrationEffect = VibrationEffect.createWaveform(duration, repeatCount);
            mVibrator.vibrate(vibrationEffect);
        }else{
            mVibrator.vibrate(duration, repeatCount);
        }

        return true;
    }

}
