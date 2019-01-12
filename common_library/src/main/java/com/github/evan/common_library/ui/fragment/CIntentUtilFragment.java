package com.github.evan.common_library.ui.fragment;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.os.Bundle;

import com.github.evan.common_library.utils.IntentUtil;

/**
 * Created by Evan on 2019/1/11 0011.
 */
public class CIntentUtilFragment extends BLoggerFragment {

    public void jumpActivity(Class<? extends Activity> clazz, Bundle bundle) {
        IntentUtil.jumpActivity(getActivity(), clazz, bundle);
    }

    public void jumpActivity(ComponentName componentName, Bundle bundle) {
        IntentUtil.jumpActivity(getActivity(), componentName, bundle);
    }

    public void jumpActivity(String action, Bundle bundle) {
        IntentUtil.jumpActivity(getActivity(), action, bundle);
    }

    public void jumpActivity(String packageName, String dstActivityName, Bundle bundle) {
        IntentUtil.jumpActivity(getActivity(), packageName, dstActivityName, bundle);
    }

    public void startService(Class<? extends Service> clazz, Bundle bundle){
        IntentUtil.startService(getActivity(), clazz, bundle);
    }

    public void sendBroadcast(String action, Bundle bundle){
        IntentUtil.sendBroadcast(getActivity(), action, bundle);
    }

    public void sendOrderedBroadcast(String action, String receivePermission, Bundle bundle){
        IntentUtil.sendOrderedBroadcast(getActivity(), action, receivePermission, bundle);
    }

}
