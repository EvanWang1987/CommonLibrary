package com.github.evan.common_library.framework.mvp.p;

import com.github.evan.common_library.framework.mvp.v.IMvpView;

import java.lang.ref.SoftReference;

public abstract class BasePresenter<View extends IMvpView, Model> implements IMvpPresenter {
    public abstract void onRefreshView(View view, Model model);


    private SoftReference<View> mView;
    private Model mModel;

    public BasePresenter(View view, Model model) {
        if(null == view || null == model){
            throw new NullPointerException("View and model can not be null !");
        }
        this.mView = new SoftReference<>(view);
        this.mModel = model;
    }

    public View getView(){
        return mView.get();
    }

    public Model getModel(){
        return mModel;
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
