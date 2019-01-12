package com.github.evan.common_library.ui.activity;

import android.content.res.ColorStateList;

import com.github.evan.common_library.utils.FileUtil;
import com.github.evan.common_library.utils.ResourceUtil;

import java.io.File;
import java.io.InputStream;
import java.util.List;


/**
 * Created by Evan on 2019/1/11 0011.
 */
public abstract class FResourceUtilActivity extends ESpActivity {


    public ColorStateList getSelector(int resId){
        return ResourceUtil.getSelector(resId);
    }

    public String[] getStringArray(int resId){

        return ResourceUtil.getStringArray(resId);
    }

    public int getInteger(int resId){
        return ResourceUtil.getInteger(resId);
    }

    public int[] getIntegerArray(int resId){
        return ResourceUtil.getIntegerArray(resId);
    }

    public float getDimenOfPixel(int resId){
        return ResourceUtil.getDimenOfPixel(resId);
    }

    public InputStream openAssetFile(String fileFullName, int mode){
        return ResourceUtil.openAssetFile(fileFullName, mode);
    }

    public InputStream openAssetFile(String fileFullName){
        return ResourceUtil.openAssetFile(fileFullName);
    }

    public List<File> listAssetFiles(String fullPath){
        return ResourceUtil.listAssetFile(fullPath);
    }

    public static FileUtil.FileStatus copyAsset2File(String assetFileName, String targetPath, boolean isDeleteIfExists){
        return ResourceUtil.copyAsset2File(assetFileName, targetPath, isDeleteIfExists);
    }
}
