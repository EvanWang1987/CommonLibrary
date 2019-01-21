package com.github.evan.common_library.ui.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

/**
 * Created by Evan on 2017/12/11.
 *
 * 对话框工厂
 */
public class SimpleDialogFactory {

    /** 生成进度对话框 */
    public static ProgressDialog createProgressDialog(Context context, CharSequence title, CharSequence message, int progressStyle, int max, int progress) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setProgressStyle(progressStyle);
        if(progressStyle == ProgressDialog.STYLE_HORIZONTAL){
            dialog.setMax(max);
            dialog.setProgress(progress);
        }
        return dialog;
    }

    /** 生成信息对话框 */
    public static Dialog createMessageDialog(Context context, @DrawableRes int icon, CharSequence title, CharSequence message, CharSequence positiveMessage, DialogInterface.OnClickListener positiveListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上弹出MD风格
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (icon != 0) {
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(positiveMessage, positiveListener);
            return builder.create();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            if (icon != 0) {
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(positiveMessage, positiveListener);
            return builder.create();
        }
    }

    /** 生成确定、取消对话框 */
    public static Dialog createConfirmDialog(Context context, @DrawableRes int icon, CharSequence title, CharSequence message, CharSequence positiveMessage, DialogInterface.OnClickListener positiveListener, CharSequence cancelMessage, DialogInterface.OnClickListener cancelListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上弹出MD风格
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (icon != 0) {
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(positiveMessage, positiveListener);
            builder.setNegativeButton(cancelMessage, cancelListener);
            return builder.create();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            if (icon != 0) {
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(positiveMessage, positiveListener);
            builder.setNegativeButton(cancelMessage, cancelListener);
            return builder.create();
        }
    }

    /** 生成三按钮对话框 */
    public static Dialog createThreeButtonsmDialog(Context context, @DrawableRes int icon, CharSequence title, CharSequence message, CharSequence positiveMessage, DialogInterface.OnClickListener positiveListener, CharSequence cancelMessage, DialogInterface.OnClickListener cancelListener, CharSequence neutralMessage, DialogInterface.OnClickListener neutralListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上弹出MD风格
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (icon != 0) {
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(positiveMessage, positiveListener);
            builder.setNegativeButton(neutralMessage, neutralListener);
            builder.setNegativeButton(cancelMessage, cancelListener);
            return builder.create();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            if (icon != 0) {
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton(positiveMessage, positiveListener);
            builder.setNegativeButton(neutralMessage, neutralListener);
            builder.setNegativeButton(cancelMessage, cancelListener);
            return builder.create();
        }
    }

    /** 生成列表对话框 */
    public static Dialog createListDialog(Context context, int icon, CharSequence message, String[] menuItems, CharSequence title, DialogInterface.OnClickListener itemClickListener){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上弹出MD风格
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (icon != 0) {
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setItems(menuItems, itemClickListener);
            return builder.create();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            if (icon != 0) {
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setItems(menuItems, itemClickListener);
            return builder.create();
        }
    }

    /** 生成单选对话框 */
    public static Dialog createSingleChoiceDialog(Context context, String[] menuItem, CharSequence title, CharSequence positiveTitle, DialogInterface.OnClickListener clickListener, DialogInterface.OnClickListener positiveListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0以上弹出MD风格
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setSingleChoiceItems(menuItem, 0, clickListener);
            builder.setPositiveButton(positiveTitle, positiveListener);
            return builder.create();
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setSingleChoiceItems(menuItem, 0, clickListener);
            builder.setPositiveButton(positiveTitle, positiveListener);
            return builder.create();
        }
    }

    /** 生成多选对话框 */
    public static Dialog createMultipleChoiceDialog(Context context, String[] menuItems, boolean[] checkState, CharSequence title, CharSequence positiveMessage, DialogInterface.OnMultiChoiceClickListener choiceListener, DialogInterface.OnClickListener positiveListener){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //5.0以上弹出MD风格
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMultiChoiceItems(menuItems, checkState, choiceListener);
            builder.setPositiveButton(positiveMessage, positiveListener);
            return builder.create();
        }else{
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            builder.setTitle(title);
            builder.setMultiChoiceItems(menuItems, checkState, choiceListener);
            builder.setPositiveButton(positiveMessage, positiveListener);
            return builder.create();
        }
    }

    /** 获取输入对话框 */
    public static Dialog createInputDialog(Context context, @DrawableRes int icon, CharSequence title, CharSequence hint, CharSequence positiveMessage, DialogInterface.OnClickListener positiveListener, CharSequence cancelMessage, DialogInterface.OnClickListener cancelListener){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            //5.0以上弹出MD风格
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if(icon != 0){
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            EditText editText = new EditText(context);
            editText.setMinLines(5);
            editText.setHint(hint);
            builder.setView(editText);
            builder.setPositiveButton(positiveMessage, positiveListener);
            builder.setNegativeButton(cancelMessage, cancelListener);
            return builder.create();
        }else{
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
            if(icon != 0){
                builder.setIcon(icon);
            }
            builder.setTitle(title);
            EditText editText = new EditText(context);
            editText.setMinLines(5);
            editText.setHint(hint);
            builder.setView(editText);
            builder.setPositiveButton(positiveMessage, positiveListener);
            builder.setNegativeButton(cancelMessage, cancelListener);
            return builder.create();
        }
    }
}
