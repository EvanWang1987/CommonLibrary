package com.github.evan.common_library.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.evan.common_library.utils.Logger;
import com.github.evan.common_library.utils.PackageUtil;

/**
 * Created by Evan on 2019/1/12 0012.
 */
public class BLoggerFragment extends AStackFragment {
    public String DEFAULT_TAG = PackageUtil.getPackageName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        DEFAULT_TAG = PackageUtil.getPackageName();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        DEFAULT_TAG = PackageUtil.getPackageName();
    }

    public void logV(String tag, String log){
        Logger.v(tag, log);
    }

    public void logD(String tag, String log){
        Logger.d(tag, log);
    }

    public void logI(String tag, String log){
        Logger.i(tag, log);
    }

    public void logW(String tag, String log){
        Logger.w(tag, log);
    }

    public void logE(String tag, String log){
        Logger.e(tag, log);
    }
}
