package com.github.evan.common_library.framework.mvp.v;

import android.support.annotation.StringRes;

public interface IMvpView {

    void showToast(CharSequence text);

    void showToast(@StringRes int text);

    void showStateSwitcher(int state);

    void showLoadingDialog(int icon, CharSequence title, CharSequence message);

    void showLoadingDialog(int icon, CharSequence title, CharSequence message, boolean cancelable);

    void showMessageDialog(int icon, CharSequence title, CharSequence message, CharSequence positive);

    void showConfirmDialog(int icon, CharSequence title, CharSequence message, CharSequence positive, CharSequence cancel);

}
