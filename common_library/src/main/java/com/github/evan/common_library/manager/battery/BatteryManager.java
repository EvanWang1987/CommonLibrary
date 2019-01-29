package com.github.evan.common_library.manager.battery;

import android.content.Context;
import android.os.Build;

import com.github.evan.common_library.model.BatteryInfo;
import com.github.evan.common_library.utils.BatteryUtil;

/**
 * Created by Evan on 2019/1/23 0023.
 */
public class BatteryManager {
    private static BatteryManager mInstance = null;
    private final android.os.BatteryManager mSysBatteryManager;

    public static BatteryManager getIns(Context context){
        if(null == mInstance){
            synchronized (BatteryManager.class){
                if(null == mInstance){
                    mInstance = new BatteryManager(context);
                    return mInstance;
                }
            }
        }

        return mInstance;
    }

    private BatteryManager(Context context){
        mSysBatteryManager = (android.os.BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
    }

    /**
     * 是否充电
     * @return
     */
    public boolean isCharging(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return mSysBatteryManager.isCharging();
        }else{
            return BatteryUtil.isCharging();
        }
    }

    /**
     * 获取电池剩余电量
     * @return
     */
    public int getBatteryLevel(){
        return BatteryUtil.getBatteryLevel();
    }

    /**
     * 获取电池温度
     * @return
     */
    public int getBatteryTemperature(){
        return BatteryUtil.getBatteryTemperature();
    }

    /**
     * 获取电池信息
     * @return
     */
    public BatteryInfo getBatteryInfo(){
        return BatteryUtil.getBatteryInfo();
    }

}
