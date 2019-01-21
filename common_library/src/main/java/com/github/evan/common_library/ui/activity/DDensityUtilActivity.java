package com.github.evan.common_library.ui.activity;

import com.github.evan.common_library.utils.DensityUtil;

/**
 * Created by Evan on 2019/1/12 0012.
 */
public abstract class DDensityUtilActivity extends CIntentUtilActivity {


    public int getRealScreenWidthOfPx(){
        return DensityUtil.getRealScreenWidthOfPx();
    }

    public int getRealScreenHeightOfPx(){
        return DensityUtil.getRealScreenHeightOfPx();
    }

    public int getAppScreenWidthOfPx(){
        return DensityUtil.getAppScreenWidthOfPx();
    }

    public int getAppScreenHeightOfPx(){
        return DensityUtil.getAppScreenHeightOfPx();
    }

    public float getDensity(){
        return DensityUtil.getDensity();
    }

    public float getScaleDensity(){
        return DensityUtil.getScaleDensity();
    }

    public float getScreenSize(){
        return DensityUtil.getScreenSize();
    }

    public float getStatusBarHeight(){
        return DensityUtil.getStatusBarHeight();
    }

    public float getScreenXDpi(){
        return DensityUtil.getXDpi();
    }

    public float getScreenYDpi(){
        return DensityUtil.getYDpi();
    }

    public int getScreenDpi(){
        return DensityUtil.getScreenDpi();
    }

    public float dp2Px(float value){
        return DensityUtil.dp2px(value);
    }

    public float sp2Px(float value){
        return DensityUtil.sp2px(value);
    }

    public float px2Dp(float value){
        return DensityUtil.px2dp(value);
    }

    public float px2Sp(float value){
        return DensityUtil.px2sp(value);
    }
}
