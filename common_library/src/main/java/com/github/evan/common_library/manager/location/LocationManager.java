package com.github.evan.common_library.manager.location;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.media.AudioManager;
import android.os.Build;

import com.github.evan.common_library.BaseApplication;
import com.github.evan.common_library.manager.net.NetManager;
import com.github.evan.common_library.manager.package_manager.PackageManager;

import java.util.List;

/**
 * Created by Evan on 2019/1/28 0028.
 */
public class LocationManager {
    private static LocationManager mIns = null;
    public static LocationManager getIns(Context context){
        if(null == mIns){
            synchronized (LocationManager.class){
                if(null == mIns){
                    mIns = new LocationManager(context);
                    return mIns;
                }
            }
        }

        return mIns;
    }

    private android.location.LocationManager mSystemLocationManager;

    private LocationManager(Context context){
        mSystemLocationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * 设备是否存在GPS模块
     * @return
     */
    public boolean isDeviceContainsGps(){
        List<String> allProviders = mSystemLocationManager.getAllProviders();
        return allProviders.contains(android.location.LocationManager.GPS_PROVIDER);
    }

    /**
     * 设备是否存在网络定位
     * @return
     */
    public boolean isDeviceContainsNetworkLocation(){
        List<String> allProviders = mSystemLocationManager.getAllProviders();
        return allProviders.contains(android.location.LocationManager.NETWORK_PROVIDER);
    }

    /**
     * GPS是否打开
     * @return
     */
    public boolean isGpsEnable(){
        return isDeviceContainsGps() && mSystemLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
    }

    /**
     * 是否允许网络定位(ping一下网络)
     * @return
     */
    public boolean isNetworkLocationEnable(){
        return isDeviceContainsNetworkLocation() && mSystemLocationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER) && NetManager.getInstance(BaseApplication.getApplication()).isNetAvailable();
    }

    /**
     * 先拿GPS定位，如果没开启，再拿网络定位
     * @return
     */
    public LocationProvider findBestLocationProvider(){
        return isGpsEnable() ? getGPSLocationProvider(false) : getNetworkLocationProvider(false);
    }

    /**
     * 获取基于GPS定位的信息提供者
     * @param isCareSwitchEnable 是否关心系统开关已开启
     * @return
     */
    public LocationProvider getGPSLocationProvider(boolean isCareSwitchEnable){
        if(isCareSwitchEnable){
            boolean isProviderEnabled = mSystemLocationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER);
            if(isProviderEnabled){
                LocationProvider provider = mSystemLocationManager.getProvider(android.location.LocationManager.GPS_PROVIDER);
                return provider;
            }else{
                return null;
            }
        }

        return mSystemLocationManager.getProvider(android.location.LocationManager.GPS_PROVIDER);
    }

    /**
     * 获取基于基站定位的信息提供者
     * @param isCareSwitchEnable 是否关心系统开关已开启
     * @return
     */
    public LocationProvider getNetworkLocationProvider(boolean isCareSwitchEnable){
        if(isCareSwitchEnable){
            boolean isProviderEnabled = mSystemLocationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER);
            if(isProviderEnabled){
                LocationProvider provider = mSystemLocationManager.getProvider(android.location.LocationManager.NETWORK_PROVIDER);
                return provider;
            }else{
                return null;
            }
        }

        return mSystemLocationManager.getProvider(android.location.LocationManager.NETWORK_PROVIDER);
    }

    /**
     * 获取上次GPS的精准定位
     * @return
     */
    public Location getLastKnownLocationWhichGprProvide(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //6.0
            List<String> unEnablePermission = PackageManager.getIns(BaseApplication.getApplication()).checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if(!unEnablePermission.isEmpty()){
                return null;
            }
        }
        Location lastKnownLocation = mSystemLocationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
        return lastKnownLocation;
    }

    /**
     * 获取上次网络的精准定位
     * @return
     */
    public Location getLastKnownLocationWhichNetworkProvide(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //6.0
            List<String> unEnablePermission = PackageManager.getIns(BaseApplication.getApplication()).checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION);
            if(!unEnablePermission.isEmpty()){
                return null;
            }
        }
        Location lastKnownLocation = mSystemLocationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
        return lastKnownLocation;
    }

    /**
     * 监听GPS定位
     * @param minTime       最少多久监听一次
     * @param minDistance   最少多远监听一次
     * @param listener      监听器
     * @return              是否开启监听成功
     */
    public boolean monitorGpsLocation(long minTime, long minDistance, LocationListener listener){
        return monitorLocationInner(android.location.LocationManager.GPS_PROVIDER, minTime, minDistance, listener);
    }

    /**
     * 监听网络定位
     * @param minTime       最少多久监听一次
     * @param minDistance   最少多远监听一次
     * @param listener      监听器
     * @return              是否开启监听成功
     */
    public boolean monitorNetworkLocation(long minTime, long minDistance, LocationListener listener){
        return monitorLocationInner(android.location.LocationManager.GPS_PROVIDER, minTime, minDistance, listener);
    }

    private boolean monitorLocationInner(String provider, long minTime, long minDistance, LocationListener listener){
        boolean isRequestGpsLocation = provider.equals(android.location.LocationManager.GPS_PROVIDER);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            String permission = isRequestGpsLocation ? Manifest.permission.ACCESS_FINE_LOCATION : Manifest.permission.ACCESS_COARSE_LOCATION;
            boolean isPermissionEnable = PackageManager.getIns(BaseApplication.getApplication()).checkPermission(permission).isEmpty();
            if(!isPermissionEnable){
                return false;
            }
        }

        if(isRequestGpsLocation && !isGpsEnable()){
            return false;
        }

        if(!isRequestGpsLocation && !isNetworkLocationEnable()){
            return false;
        }

        mSystemLocationManager.requestLocationUpdates(provider, minTime, minDistance, listener);
        return true;
    }

}
