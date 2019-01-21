package com.github.evan.common_library.manager.net;

/**
 * Created by Evan on 2019/1/21 0021.
 */
public enum NetworkState {

    /**
     * UNKNOWN -2 未知
     * NONE -1 无网
     * WIFI 0 Wifi
     * TWO_G 1 2G网
     * THREE_G 2 3G网
     * FOUR_G 3 4G网
     * ETHERNET 4  以太网
     * AVAILABLE 5 可用的网络
     * */
    UNKNOWN(-2), NONE(-1), WIFI(0), TWO_G(1), THREE_G(2), FOUR_G(3), ETHERNET(4), AVAILABLE(5);


    private int value;

    NetworkState(int value) {
        this.value = value;
    }

    public static NetworkState valueOf(int value){
        NetworkState returnValue = UNKNOWN;
        NetworkState[] values = values();
        if(null != values && values.length > 0){
            for (int i = 0; i < values.length; i++) {
                NetworkState networkState = values[i];
                if(networkState.value == value){
                    returnValue = networkState;
                    break;
                }
            }
        }
        return returnValue;
    }

}
