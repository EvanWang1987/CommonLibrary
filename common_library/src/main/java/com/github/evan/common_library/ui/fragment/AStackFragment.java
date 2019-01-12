package com.github.evan.common_library.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Evan on 2018/12/14 0014.
 */
public abstract class AStackFragment extends Fragment {
    private static LinkedList<Fragment> mFragmentStack = new LinkedList<>();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!mFragmentStack.contains(this)){
            mFragmentStack.addFirst(this);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(!mFragmentStack.contains(this)){
            mFragmentStack.addFirst(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(mFragmentStack.contains(this)){
            mFragmentStack.remove(this);
        }
    }

    /**
     * 当前Activity是否处于栈顶
     * @return
     */
    public boolean isFragmentAtTopStack(){
        return this.equals(mFragmentStack.getFirst());
    }

    /**
     * 获取当前Activity栈角标
     * @return
     */
    public int getActivityStackIndex(){
        int index = -1;
        Iterator<Fragment> iterator = mFragmentStack.iterator();
        while (iterator.hasNext()){
            Fragment fragment = iterator.next();
            index++;
            if(fragment.equals(this)){
                return index;
            }
        }

        return -1;
    }
}
