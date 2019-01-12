package com.github.evan.common_library.framework.mvp.p;

public interface IMvpPresenter {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void onRestart();
}
