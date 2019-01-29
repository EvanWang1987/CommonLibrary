package com.github.evan.common_library.manager.package_manager;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;

import com.github.evan.common_library.BaseApplication;
import com.github.evan.common_library.utils.UnitConvertUil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2019/1/21 0021.
 */
public class PackageManager {
    private static PackageManager mIns = null;

    public static PackageManager getIns(Context context) {
        if (null == mIns) {
            synchronized (PackageManager.class) {
                if (null == mIns) {
                    mIns = new PackageManager(context);
                }
            }
        }
        return mIns;
    }

    private android.content.pm.PackageManager mSystemPackageManager;
    private ActivityManager mActivityManager;

    private PackageManager(Context context) {
        mSystemPackageManager = context.getPackageManager();
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    /**
     * 获取当前包名
     *
     * @return
     */
    public String getPackageName() {
        Context application = BaseApplication.getApplication();
        return application.getPackageName();
    }

    /**
     * 获取版本名
     *
     * @return
     */
    public String getVersionName() {
        try {
            return mSystemPackageManager.getPackageInfo(getPackageName(), 0).versionName;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @return
     */
    public int getVersionCode() {
        try {
            return mSystemPackageManager.getPackageInfo(getPackageName(), 0).versionCode;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 检查权限
     *
     * @param permissions
     * @return 没有的权限, 返回List不为null
     */
    public @NonNull List<String> checkPermission(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> returnValue = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                boolean isExistsPermission = ContextCompat.checkSelfPermission(BaseApplication.getApplication(), permission) == android.content.pm.PackageManager.PERMISSION_GRANTED;
                if (!isExistsPermission) {
                    returnValue.add(permission);
                }
            }
            return returnValue;
        }

        return null;
    }

    /**
     * 获取所有安装的App信息
     *
     * @param flag
     * @return
     */
    public List<PackageInfo> getAllInstalledPackage(int flag) {
        List<PackageInfo> installedPackages = mSystemPackageManager.getInstalledPackages(flag);
        return installedPackages;
    }

    /**
     * 获取指定Activity信息
     *
     * @param componentName
     * @param flag
     * @return
     */
    public ActivityInfo getActivityInfo(ComponentName componentName, int flag) {
        try {
            return mSystemPackageManager.getActivityInfo(componentName, flag);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取指定Service信息
     *
     * @param componentName
     * @param flag
     * @return
     */
    public ServiceInfo getServiceInfo(ComponentName componentName, int flag) {
        try {
            ServiceInfo serviceInfo = mSystemPackageManager.getServiceInfo(componentName, flag);
            return serviceInfo;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取所有权限信息
     *
     * @param flag
     * @return
     */
    public List<PermissionGroupInfo> getAllPermissionGroups(int flag) {
        List<PermissionGroupInfo> allPermissionGroups = mSystemPackageManager.getAllPermissionGroups(flag);
        return allPermissionGroups;
    }

    /**
     * 获取Application信息
     *
     * @param packageName
     * @param flag
     * @return
     */
    public ApplicationInfo getApplicationInfo(String packageName, int flag) {
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = mSystemPackageManager.getApplicationInfo(packageName, flag);
            return applicationInfo;
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取App栈信息
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<ActivityManager.AppTask> getAppTasks() {
        return mActivityManager.getAppTasks();
    }

    /**
     * 获取正在运行的进程
     * @return
     */
    public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses(){
        return mActivityManager.getRunningAppProcesses();
    }

    public long getTotalRam() {
        try {
            FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
            String memTotal = bufferedReader.readLine();
            StringBuffer sb = new StringBuffer();
            for (char c : memTotal.toCharArray()) {
                if (c >= '0' && c <= '9') {
                    sb.append(c);
                }
            }
            long totalMemory = Long.parseLong(sb.toString()) * 1024;
            return totalMemory;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public long getAvailableRam(){
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        mActivityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public String getAvailableRamWithUnit(){
        long availableRam = getAvailableRam();
        Object[] result = new Object[2];
        UnitConvertUil.byte2MaxUnit(availableRam, 3, result);
        return result[0] + result[1].toString();
    }

    public String getTotalRamWithUnit(){
        long totalRam = getTotalRam();
        Object[] result = new Object[2];
        UnitConvertUil.byte2MaxUnit(totalRam, 3, result);
        return result[0] + result[1].toString();
    }

}
