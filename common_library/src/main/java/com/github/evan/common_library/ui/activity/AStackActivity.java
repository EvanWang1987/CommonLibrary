package com.github.evan.common_library.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Evan on 2018/12/14 0014.
 */
public abstract class AStackActivity extends AppCompatActivity {
    private static LinkedList<Activity> mActivityStack = new LinkedList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityStack.addFirst(this);
    }

    @Override
    protected void onRestart() {
        Activity first = mActivityStack.getFirst();
        if(first != this){
            mActivityStack.remove(this);
            mActivityStack.addFirst(this);
        }
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        mActivityStack.remove(this);
        super.onDestroy();
    }

    /**
     * 当前Activity是否处于栈顶
     * @return
     */
    public boolean isActivityAtTopStack(){
        return this.equals(mActivityStack.getFirst());
    }

    /**
     * 获取当前Activity栈角标
     * @return
     */
    public int getActivityStackIndex(){
        int index = -1;
        Iterator<Activity> iterator = mActivityStack.iterator();
        while (iterator.hasNext()){
            Activity activity = iterator.next();
            index++;
            if(activity.equals(this)){
                return index;
            }
        }

        return -1;
    }
}
