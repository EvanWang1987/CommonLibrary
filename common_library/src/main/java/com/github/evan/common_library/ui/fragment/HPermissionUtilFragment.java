package com.github.evan.common_library.ui.fragment;

import android.app.Fragment;
import android.os.Build;
import android.support.annotation.NonNull;

import com.github.evan.common_library.ui.activity.GHandlerActivity;
import com.github.evan.common_library.utils.PackageUtil;

import java.util.List;

/**
 * Created by Evan on 2019/1/12 0012.
 */
public abstract class HPermissionUtilFragment extends Fragment {
    protected static final int REQUEST_PERMISSION_CODE = 101;

    /**
     * 检测是否已授予权限
     * @param permissions
     * @return 未授予权限, Android M以下系统返回null
     */
    public List<String> checkPermission(String... permissions){
        return PackageUtil.checkPermission(permissions);
    }

    public void requestPermission(String... permissions){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(permissions, REQUEST_PERMISSION_CODE);
        }
    }

    public String getPackageName(){
        return PackageUtil.getPackageName();
    }

    public String getVersionName(){
        return PackageUtil.getVersionName();
    }

    public int getVersionCode(){
        return PackageUtil.getVersionCode();
    }

    public String getVersionName(String packageName){
        return PackageUtil.getVersionName(packageName);
    }

    public int getVersionCode(String packageName){
        return PackageUtil.getVersionCode(packageName);
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
