package com.github.evan.common_library.framework.mvp.p;

import com.github.evan.common_library.framework.mvp.v.IMvpView;

import java.lang.ref.SoftReference;

public abstract class BasePresenter<V extends IMvpView> implements IMvpPresenter {
    private SoftReference<V> mView;

    public BasePresenter(V view) {
        this.mView = new SoftReference<>(view);
    }

    public V getView() {
        return mView.get();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onRestart() {

    }
}
