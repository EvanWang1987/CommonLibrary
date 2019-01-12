package com.github.evan.common_library.ui.activity;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.os.Bundle;
import com.github.evan.common_library.utils.IntentUtil;

/**
 * Created by Evan on 2019/1/11 0011.
 */
public class CIntentUtilActivity extends BLoggerActivity {

    public void jumpActivity(Class<? extends Activity> clazz, Bundle bundle) {
        IntentUtil.jumpActivity(this, clazz, bundle);
    }

    public void jumpActivity(ComponentName componentName, Bundle bundle) {
        IntentUtil.jumpActivity(this, componentName, bundle);
    }

    public void jumpActivity(String action, Bundle bundle) {
        IntentUtil.jumpActivity(this, action, bundle);
    }

    public void jumpActivity(String packageName, String dstActivityName, Bundle bundle) {
        IntentUtil.jumpActivity(this, packageName, dstActivityName, bundle);
    }

    public void startService(Class<? extends Service> clazz, Bundle bundle){
        IntentUtil.startService(this, clazz, bundle);
    }

    public void sendBroadcast(String action, Bundle bundle){
        IntentUtil.sendBroadcast(this, action, bundle);
    }

    public void sendOrderedBroadcast(String action, String receivePermission, Bundle bundle){
        IntentUtil.sendOrderedBroadcast(this, action, receivePermission, bundle);
    }

}
