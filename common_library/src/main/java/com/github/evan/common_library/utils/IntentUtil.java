package com.github.evan.common_library.utils;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Evan on 2019/1/11 0011.
 */
public class IntentUtil {

    public static void jumpActivity(Activity activity, Class<? extends Activity> clazz, Bundle bundle) {
        if(null == activity)    return;

        Intent intent = new Intent(activity, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    public static void jumpActivity(Activity activity, ComponentName componentName, Bundle bundle) {
        if(null == activity)    return;

        Intent intent = new Intent();
        intent.setComponent(componentName);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    public static void jumpActivity(Activity activity, String action, Bundle bundle) {
        if(null == activity)    return;

        Intent intent = new Intent();
        intent.setAction(action);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
    }

    public static void jumpActivity(Activity activity, String packageName, String dstActivityName, Bundle bundle) {
        if(null == activity)    return;

        Intent intent = new Intent();
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        intent.setClassName(packageName, dstActivityName);
        activity.startActivity(intent);
    }

    public static void startService(Activity activity, Class<? extends Service> clazz, Bundle bundle){
        if(null == activity)    return;

        Intent intent = new Intent(activity, clazz);
        if(null != bundle){
            intent.putExtras(bundle);
        }
        activity.startService(intent);
    }

    public static void sendBroadcast(Activity activity, String action, Bundle bundle){
        if(null == activity)  return;

        Intent intent = new Intent(action);
        if(null != bundle){
            intent.putExtras(bundle);
        }

        activity.sendBroadcast(intent);
    }

    public static void sendOrderedBroadcast(Activity activity, String action, String receivePermission, Bundle bundle){
        if(null == activity)    return;

        Intent intent = new Intent(action);
        if(null != bundle){
            intent.putExtras(bundle);
        }

        activity.sendOrderedBroadcast(intent, receivePermission);
    }


}
