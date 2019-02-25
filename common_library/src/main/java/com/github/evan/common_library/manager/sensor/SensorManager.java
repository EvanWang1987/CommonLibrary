package com.github.evan.common_library.manager.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;

/**
 * Created by Evan on 2019/2/25 0025.
 */
public class SensorManager {
    private static SensorManager mIns;
    private final android.hardware.SensorManager mSystemSensorManager;

    public static SensorManager getIns(Context context){
        if(null == mIns){
            synchronized (SensorManager.class){
                mIns = new SensorManager(context);
                return mIns;
            }
        }
        return mIns;
    }

    private SensorManager(Context context){
        mSystemSensorManager = (android.hardware.SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    /**
     * 注册陀螺仪监听
     * @param listener
     * @param type
     */
    public void register(SensorEventListener listener, int type){
        Sensor gyroscope = mSystemSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mSystemSensorManager.registerListener(listener, gyroscope, type);
    }

    /**
     * 取消监听
     * @param listener
     */
    public void unRegister(SensorEventListener listener){
        mSystemSensorManager.unregisterListener(listener);
    }

}
