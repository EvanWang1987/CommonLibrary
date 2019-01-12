package com.github.evan.common_library.handler.soft_handler;

import android.os.Message;

/**
 * Created by Evan on 2017/10/2.
 */
public interface SoftHandlerReceiver {

    public void onHandleMessage(Message message);
}
