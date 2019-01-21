package com.github.evan.common_library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;

/**
 * Created by Evan on 2017/10/2.
 */
public class BitmapUtil {

    /**
     * 释放位图
     * @param bitmap
     * @return
     */
    public static boolean recycleBitmap(Bitmap bitmap){
        boolean isRecyclable = null != bitmap && !bitmap.isRecycled();
        if(isRecyclable){
            bitmap.recycle();
        }
        return isRecyclable;
    }

    /**
     * 获取位图
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap createBitmapFromResource(Context context, int resId){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        }catch (OutOfMemoryError e){
            Logger.printStackTrace(e);
        }
        return bitmap;
    }

    /**
     * 获取较小位图
     * @param bitmap
     * @return
     */
    public static Bitmap getSmallerBitmap(Bitmap bitmap){
        int size = bitmap.getWidth()*bitmap.getHeight() / 160000;
        if (size <= 1) return bitmap; // 如果小于
        else {
            Matrix matrix = new Matrix();
            matrix.postScale((float) (1 / Math.sqrt(size)), (float) (1 / Math.sqrt(size)));
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return resizeBitmap;
        }
    }

    /**
     * 裁剪Bitmap
     * @param sourceBitmap
     * @param dstWidth
     * @param dstHeight
     * @param recycleSource
     * @return
     */
    public static Bitmap clipBitmap(Bitmap sourceBitmap, int dstWidth, int dstHeight, boolean recycleSource){
        if(null == sourceBitmap || sourceBitmap.isRecycled()){
            return null;
        }

        int sourceHeight = sourceBitmap.getHeight();
        int sourceWidth = sourceBitmap.getWidth();

        float heightScale = (float) dstHeight / (float)sourceHeight;
        float widthScale = (float) dstWidth / (float)sourceWidth;
        if(heightScale > widthScale){
            widthScale = heightScale;
        }else if(widthScale > heightScale){
            heightScale = widthScale;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(widthScale, heightScale);
        Bitmap bitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceWidth, sourceHeight, matrix, true);
        if(recycleSource){
            sourceBitmap.recycle();
        }
        return bitmap;
    }

    /**
     * 复制Bitmap
     * @param source
     * @param isMutable
     * @return
     */
    public static Bitmap copyBitmap(Bitmap source, boolean isMutable){
        if(null == source || source.isRecycled()){
            return null;
        }

        Bitmap copy = source.copy(source.getConfig(), isMutable);
        return copy;
    }

    /**
     * 替换Bitmap中的颜色
     * @param bitmap    位图
     * @param oldColor  旧颜色
     * @param newColor  新颜色
     */
    public static void relaceColor(Bitmap bitmap, @ColorInt int oldColor, @ColorInt int newColor){
        if(null == bitmap){
            return;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = bitmap.getPixel(i, j);
                if(color == oldColor){
                    bitmap.setPixel(i, j, newColor);
                }
            }
        }
    }

    /** 替换Bitmap颜色，并生成新的Bitmap */
    public static Bitmap replaceColorWithNewBitmap(Bitmap bitmap, @ColorInt int oldColor, @ColorInt int newColor){
        if(null == bitmap){
            return null;
        }

        Bitmap copiedBitmap = copyBitmap(bitmap, false);

        int width = copiedBitmap.getWidth();
        int height = copiedBitmap.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int color = copiedBitmap.getPixel(i, j);
                if(color == oldColor){
                    copiedBitmap.setPixel(i, j, newColor);
                }
            }
        }

        return copiedBitmap;
    }


}
