package com.github.evan.common_library.framework.observer;

/**
 * Created by Evan on 2019/1/12 0012.
 */
public interface IObservable<Data> {

    boolean register(IObserver<Data> observer);

    boolean unRegister(IObserver<Data> observer);

    void clearAll();

    void notifyObservers();
}
