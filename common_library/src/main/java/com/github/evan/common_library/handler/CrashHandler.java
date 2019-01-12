package com.github.evan.common_library.handler;


import com.github.evan.common_library.utils.Logger;

/**
 * Created by Evan on 2017/10/4.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        Logger.printStackTrace(e);
        System.exit(0);
    }

}
