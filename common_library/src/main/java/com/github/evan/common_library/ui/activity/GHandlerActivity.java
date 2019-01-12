package com.github.evan.common_library.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import com.github.evan.common_library.handler.soft_handler.SoftHandler;
import com.github.evan.common_library.handler.soft_handler.SoftHandlerReceiver;

/**
 * Created by Evan on 2019/1/11 0011.
 */
public abstract class GHandlerActivity extends DDensityUtilActivity implements SoftHandlerReceiver {
    private static SoftHandler<GHandlerActivity> mMainHandler = new SoftHandler<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMainHandler.setReceiver(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMainHandler.setReceiver(this);
    }

    @Override
    protected void onDestroy() {
        mMainHandler.removeCallbacksAndMessages(null);
        mMainHandler.clearReceiver();
        super.onDestroy();
    }

    /**
     * Handle your message in this method.
     * @param message
     */
    @Override
    public void onHandleMessage(Message message) {

    }

    public boolean sendEmptyMessage(int what){
        return mMainHandler.sendEmptyMessage(what);
    }

    public boolean sendEmptyMessageDelay(int what, long delayTime){
        return mMainHandler.sendEmptyMessageDelayed(what, delayTime);
    }

    public boolean sendEmptyMessageAtTime(int what, long updateTime){
        return mMainHandler.sendEmptyMessageAtTime(what, updateTime);
    }

    public boolean sendMessage(int what, int arg1, int arg2, Object obj, Bundle bundle, long delayTime){
        Message message = Message.obtain();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        if(null != bundle){
            message.setData(bundle);
        }
        if(delayTime < 0){
            delayTime = 0;
        }
        return mMainHandler.sendMessageDelayed(message, delayTime);
    }

    public boolean sendMessageAtTime(int what, int arg1, int arg2, Object obj, Bundle bundle, long updateTime){
        Message message = Message.obtain();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        if(null != bundle){
            message.setData(bundle);
        }
        if(updateTime < 0){
            updateTime = 0;
        }
        return mMainHandler.sendMessageAtTime(message, updateTime);
    }

    public boolean sendMessageAtFront(int what, int arg1, int arg2, Object obj, Bundle bundle){
        Message message = Message.obtain();
        message.what = what;
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        if(null != bundle){
            message.setData(bundle);
        }
        return mMainHandler.sendMessageAtFrontOfQueue(message);
    }

    public boolean post(Runnable runnable){
        return mMainHandler.post(runnable);
    }

    public boolean postDelay(Runnable runnable, long delayTime){
        return mMainHandler.postDelayed(runnable, delayTime);
    }

    public boolean postAtFront(Runnable runnable){
        return mMainHandler.postAtFrontOfQueue(runnable);
    }


}
