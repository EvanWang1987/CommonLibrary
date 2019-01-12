package com.github.evan.common_library.ui.fragment;


import com.github.evan.common_library.utils.SpUtil;

import java.util.Set;

/**
 * Created by Evan on 2019/1/11 0011.
 */
public abstract class ESpFragment extends CIntentUtilFragment {

    //------------------------------保存-------------------------------------------------------------------------
    /** 保存String集合 */
    public boolean commitStringSet(String key, Set<String> value, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().commitStringSet(key, value, useDefaultSp, spName);
    }

    /** 保存整数 */
    public boolean commitInt(String key, int value, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().commitInt(key, value, useDefaultSp, spName);
    }

    /** 保存long */
    public boolean commitLong(String key, long value, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().commitLong(key, value, useDefaultSp, spName);
    }

    /** 保存布尔 */
    public boolean commitBoolean(String key, boolean value, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().commitBoolean(key, value, useDefaultSp, spName);
    }

    /** 保存Float */
    public boolean commitFloat(String key, float value, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().commitFloat(key, value, useDefaultSp, spName);
    }

    /** 保存字符串 */
    public boolean commitString(String key, String value, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().commitString(key, value, useDefaultSp, spName);
    }
    //------------------------------保存-------------------------------------------------------------------------

    //------------------------------获取-------------------------------------------------------------------------
    /** 获取字符串 */
    public String getString(String key, String defValue, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().getString(key, defValue, useDefaultSp, spName);
    }

    /** 获取字符串集合 */
    public Set<String> getStringSet(String key, Set<String> defValue, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().getStringSet(key, defValue, useDefaultSp, spName);
    }

    /** 获取整数 */
    public int getInt(String key, int defValue, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().getInt(key, defValue, useDefaultSp, spName);
    }

    /** 获取long */
    public long getLong(String key, long defValue, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().getLong(key, defValue, useDefaultSp, spName);
    }

    /** 获取布尔 */
    public boolean getBoolean(String key, boolean defValue, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().getBoolean(key, defValue, useDefaultSp, spName);
    }

    /** 获取Float */
    public float getFloat(String key, float defValue, boolean useDefaultSp, String spName) {
        return SpUtil.getIns().getFloat(key, defValue, useDefaultSp, spName);
    }
    //------------------------------获取-------------------------------------------------------------------------
}
