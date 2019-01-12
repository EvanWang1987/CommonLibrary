package com.github.evan.common_library.framework.mvp.v;

public interface IMvpView {

    void showToast(CharSequence text);

    void showLoadingDialog(CharSequence title, CharSequence message);

    void showLoadingDialog(CharSequence title, CharSequence message, boolean cancelable);

    void showMessageDialog(CharSequence title, CharSequence message, CharSequence positive);

}
