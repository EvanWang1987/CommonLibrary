package com.github.evan.common_library.ui.fragment;

import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.ServiceInfo;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.github.evan.common_library.manager.package_manager.PackageManager;
import com.github.evan.common_library.ui.activity.GHandlerActivity;
import com.github.evan.common_library.utils.PackageUtil;

import java.util.List;

/**
 * Created by Evan on 2019/1/12 0012.
 */
public abstract class HPackageUtilFragment extends Fragment {
    protected static final int REQUEST_PERMISSION_CODE = 101;

    /**
     * 检测是否已授予权限
     * @param permissions
     * @return 未授予权限, Android M以下系统返回null
     */
    public List<String> checkPermission(String... permissions){
        return PackageManager.getIns(getActivity()).checkPermission(permissions);
    }

    public void requestPermission(String... permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, REQUEST_PERMISSION_CODE);
        }
    }

    public String getPackageName(){
        return PackageManager.getIns(getActivity()).getPackageName();
    }

    public String getVersionName(){
        return PackageManager.getIns(getActivity()).getVersionName();
    }

    public int getVersionCode(){
        return PackageManager.getIns(getActivity()).getVersionCode();
    }

    public List<PackageInfo> getAllInstalledPackage(int flag) {
        return PackageManager.getIns(getActivity()).getAllInstalledPackage(flag);
    }

    public ActivityInfo getActivityInfo(ComponentName componentName, int flag) {
        return PackageManager.getIns(getActivity()).getActivityInfo(componentName, flag);
    }

    public ServiceInfo getServiceInfo(ComponentName componentName, int flag) {
        return PackageManager.getIns(getActivity()).getServiceInfo(componentName, flag);
    }

    public List<PermissionGroupInfo> getAllPermissionGroups(int flag) {
        return PackageManager.getIns(getActivity()).getAllPermissionGroups(flag);
    }

    public ApplicationInfo getApplicationInfo(String packageName, int flag) {
        return PackageManager.getIns(getActivity()).getApplicationInfo(packageName, flag);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<ActivityManager.AppTask> getAppTasks() {
        return PackageManager.getIns(getActivity()).getAppTasks();
    }

    public List<ActivityManager.RunningAppProcessInfo> getRunningAppProcesses(){
        return PackageManager.getIns(getActivity()).getRunningAppProcesses();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_PERMISSION_CODE){
            onRequestPermissionsResult(permissions, grantResults);
        }
    }

    protected void onRequestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults){

    }
}
