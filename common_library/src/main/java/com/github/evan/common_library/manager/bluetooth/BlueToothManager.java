package com.github.evan.common_library.manager.bluetooth;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.Context;
import android.os.Build;

/**
 * Created by Evan on 2019/2/21 0021.
 */
public class BlueToothManager {
    private static BlueToothManager mIns = null;
    private BluetoothManager mSystemBlueToothManager;

    public static BlueToothManager getIns(Context context){
        if(null == mIns){
            synchronized (BlueToothManager.class){
                if(null == mIns){
                    mIns = new BlueToothManager(context);
                    return mIns;
                }
            }
        }

        return mIns;
    }

    private BlueToothManager(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mSystemBlueToothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        }
    }

    /**
     * 蓝牙是否可用
     * @return
     */
    public boolean isBluetoothEnable(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return mSystemBlueToothManager.getAdapter().isEnabled();
        }
        return BluetoothAdapter.getDefaultAdapter().isEnabled();
    }

    /**
     * 开启蓝牙开关
     */
    public void enable(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mSystemBlueToothManager.getAdapter().enable();
            return;
        }

        BluetoothAdapter.getDefaultAdapter().enable();
    }

    /**
     * 关闭蓝牙开关
     */
    public void disable(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mSystemBlueToothManager.getAdapter().disable();
            return;
        }

        BluetoothAdapter.getDefaultAdapter().disable();
    }

    /**
     * 获取蓝牙地址
     * @return
     */
    public String getBluetoothAddress(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return mSystemBlueToothManager.getAdapter().getAddress();
        }

        return BluetoothAdapter.getDefaultAdapter().getAddress();
    }

    /**
     * 获取蓝牙名字
     * @return
     */
    public String getBluetoothName(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return mSystemBlueToothManager.getAdapter().getName();
        }

        return BluetoothAdapter.getDefaultAdapter().getName();
    }

    /**
     * 获取蓝牙状态
     * @return
     */
    public int getState(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return mSystemBlueToothManager.getAdapter().getState();
        }

        return BluetoothAdapter.getDefaultAdapter().getState();
    }

    /**
     * 获取蓝牙扫描器
     * @return
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BluetoothLeScanner getBluetoothLeScanner(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return mSystemBlueToothManager.getAdapter().getBluetoothLeScanner();
        }

        return null;
    }

    public void test(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

        }
    }

}
