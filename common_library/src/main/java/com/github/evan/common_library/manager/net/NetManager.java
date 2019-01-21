package com.github.evan.common_library.manager.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.github.evan.common_library.framework.observer.IObservable;
import com.github.evan.common_library.framework.observer.IObserver;
import com.github.evan.common_library.manager.thread.ThreadManager;
import com.github.evan.common_library.utils.Logger;
import com.github.evan.common_library.utils.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Observer;
import java.util.Vector;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by Evan on 2017/10/6.
 */
public class NetManager {
    private static WifiSignalTask mObserveWifiSignalLevelTask;
    private static final String PING_URL = "www.qq.com";

    private static NetManager mInstance = null;
    private Future<?> mWifiLevelFuture;

    public static NetManager getInstance(Context context) {
        if (null == mInstance) {
            synchronized (NetManager.class) {
                mInstance = new NetManager(context);
            }
        }
        return mInstance;
    }

    private NetManager(Context context) {
        this.mApplicationContext = context.getApplicationContext();
        mConnectivityManager = (ConnectivityManager) mApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mObserveWifiSignalLevelTask = new WifiSignalTask(mWifiManager);
    }

    private Context mApplicationContext;
    private ConnectivityManager mConnectivityManager;
    private WifiManager mWifiManager;

    /** 获取网络类型 */
    public NetworkState getNetworkState(){
        if(!isNetConnected()){
            return NetworkState.NONE;
        }

        if(isWifiNetwork()){
            return NetworkState.WIFI;
        }

        if(isEthernetwork()){
            return NetworkState.ETHERNET;
        }

        if(isMobileNetwork()){
            NetworkInfo networkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if(null == networkInfo){
                return NetworkState.AVAILABLE;
            }

            int subtype = networkInfo.getSubtype();
            switch (subtype){
                case TelephonyManager.NETWORK_TYPE_1xRTT: // 2G ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_CDMA:// 2G ~ 14-64 kbps
                case TelephonyManager.NETWORK_TYPE_EDGE:// 2G ~ 50-100 kbps
                case TelephonyManager.NETWORK_TYPE_GPRS:// 2G ~ 100 kbps
                    return NetworkState.TWO_G;

                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:// 3G ~ 400-1000 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_A:// 3G ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_EVDO_B:// 3G ~ 600-1400 kbps
                case TelephonyManager.NETWORK_TYPE_HSDPA:// 3G ~ 2-14 Mbps
                case TelephonyManager.NETWORK_TYPE_HSPA:// 3G ~ 700-1700 kbps
                case TelephonyManager.NETWORK_TYPE_HSUPA:// 3G ~ 1-23 Mbps
                case TelephonyManager.NETWORK_TYPE_UMTS:// 3G ~ 400-7000 kbps
                    return NetworkState.THREE_G;

                case TelephonyManager.NETWORK_TYPE_LTE:
                    return NetworkState.FOUR_G;

                default:
                    return NetworkState.AVAILABLE;
            }
        }

        return NetworkState.UNKNOWN;
    }

    /**
     * 判断网络是否已连接
     */
    public boolean isNetConnected() {
        NetworkInfo mobileNetwork = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);       //手机网络
        NetworkInfo wifiNetwork = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);           //wifi网络
        NetworkInfo etherNetwork = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);      //以太网, 某些安卓设备带以太网卡
        return mobileNetwork.isConnected() || wifiNetwork.isConnected() || etherNetwork.isConnected();
    }

    /**
     * 判断网络是否可用(走一下ping命令)
     */
    public boolean isNetAvailable() {
        return isNetConnected() && ping(PING_URL);
    }

    /**
     * 判断网络是否正在连接
     */
    public boolean isNetConnecting() {
        NetworkInfo mobileNetwork = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);       //手机网络
        NetworkInfo wifiNetwork = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);           //wifi网络
        NetworkInfo etherNetwork = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);      //以太网, 某些安卓设备带以太网卡
        return mobileNetwork.getState() == NetworkInfo.State.CONNECTING || wifiNetwork.getState() == NetworkInfo.State.CONNECTING || etherNetwork.getState() == NetworkInfo.State.CONNECTING;
    }

    /**
     * 是否是wifi网络
     */
    public boolean isWifiNetwork() {
        return mConnectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 是否是手机网络
     */
    public boolean isMobileNetwork() {
        return mConnectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_MOBILE;
    }

    /**
     * 是否是以太网络
     */
    public boolean isEthernetwork() {
        return mConnectivityManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_ETHERNET;
    }

    /**
     * 移动网络是否连接
     */
    public boolean isMobileConnected() {
        NetworkInfo activeNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return activeNetworkInfo.isConnected();
    }

    /**
     * Wifi网络是否连接
     */
    public boolean isWifiConnected() {
        NetworkInfo activeNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return activeNetworkInfo.isConnected();
    }

    /**
     * 以太网络是否连接
     */
    public boolean isEthernetConnected() {
        NetworkInfo activeNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        return activeNetworkInfo.isConnected();
    }

    /**
     * 获取Wifi状态
     */
    public int getWifiStatus() {
        return mWifiManager.getWifiState();
    }

    /**
     * Wifi开关是否开启
     */
    public boolean isWifiEnable() {
        return mWifiManager.isWifiEnabled();
    }

    /**
     * 打开Wifi开关
     */
    public boolean enableWifi() {
        if (!mWifiManager.isWifiEnabled()) {
            return mWifiManager.setWifiEnabled(true);
        }
        return false;
    }

    /**
     * 关闭Wifi开关
     */
    public boolean disableWifi() {
        if (mWifiManager.isWifiEnabled()) {
            return mWifiManager.setWifiEnabled(false);
        }
        return false;
    }

    /**
     * 断开Wifi
     */
    public boolean disconnectWifi() {
        if (isWifiEnable() && isWifiConnected()) {
            return mWifiManager.disconnect();
        }
        return false;
    }

    /**
     * 是否支持5GWifi模式
     */
    public boolean isSupport5GHz() {
        return mWifiManager.is5GHzBandSupported();
    }

    /**
     * 获取Wifi在网关内的信息
     */
    public DhcpInfo getWifiDhcpInfo() {
        if (isWifiEnable() && isWifiConnected()) {
            return mWifiManager.getDhcpInfo();
        }
        return null;
    }

    /**
     * 在已保存的Wifi配置中转换networkId
     */
    public int getNetworkIdFromSSID(String SSID) {
        int networkId = -1;
        if (!StringUtil.isEmpty(SSID)) {
            List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
            if (null != configuredNetworks && !configuredNetworks.isEmpty()) {
                for (int i = 0; i < configuredNetworks.size(); i++) {
                    WifiConfiguration wifiConfiguration = configuredNetworks.get(i);
                    String ssid = wifiConfiguration.SSID;
                    if (!StringUtil.isEmpty(ssid)) {
                        if (ssid.equals(SSID)) {
                            networkId = wifiConfiguration.networkId;
                        }
                    }
                }
            }
        }
        return networkId;
    }

    /**
     * 获取本机IP地址
     */
    public String getDeviceIP() {
        if (isNetConnected()) {
            try {
                Enumeration<NetworkInterface> enumerationNi = NetworkInterface.getNetworkInterfaces();
                while (enumerationNi.hasMoreElements()) {
                    NetworkInterface networkInterface = enumerationNi.nextElement();
                    String interfaceName = networkInterface.getDisplayName();
                    if (interfaceName.equals("eth0")) {
                        // 如果是有限网卡
                        Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses();
                        while (enumIpAddress.hasMoreElements()) {
                            // 返回枚举集合中的下一个IP地址信息
                            InetAddress inetAddress = enumIpAddress.nextElement();
                            // 不是回环地址，并且是ipv4的地址
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    } else if (interfaceName.equals("wlan0")) {
                        //  如果是无限网卡
                        Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses();
                        while (enumIpAddress.hasMoreElements()) {
                            // 返回枚举集合中的下一个IP地址信息
                            InetAddress inetAddress = enumIpAddress.nextElement();
                            // 不是回环地址，并且是ipv4的地址
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                }
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean startObserveWifiSignalStrength(WifiSignalObserver observer) {
        if (!isWifiConnected()) {
            return false;
        }

        if (!mObserveWifiSignalLevelTask.getWifiSignalObservers().contains(observer)) {
            ListIterator<WifiSignalObserver> iterator = mObserveWifiSignalLevelTask.getWifiSignalObservers().listIterator();
            iterator.add(observer);
        }

        if (isObserveWifiSignalStrength()) {
            return true;
        }

        ThreadPoolExecutor networkThreadPool = ThreadManager.getInstance().getNetworkThreadPool();
        mObserveWifiSignalLevelTask.setStopObserve(false);
        mWifiLevelFuture = networkThreadPool.submit(mObserveWifiSignalLevelTask);
        return true;
    }

    public boolean isObserveWifiSignalStrength() {
        return mWifiLevelFuture != null && !mWifiLevelFuture.isDone() && !mWifiLevelFuture.isCancelled();
    }

    public void setObserveWifiSignalInvertTime(long time) {
        mObserveWifiSignalLevelTask.setObserveInvertTime(time);
    }

    public void removeWifiSignalObserver(WifiSignalObserver observer) {
        LinkedList<WifiSignalObserver> wifiSignalObservers = mObserveWifiSignalLevelTask.getWifiSignalObservers();
        ListIterator<WifiSignalObserver> iterator = wifiSignalObservers.listIterator();
        while (iterator.hasNext()) {
            WifiSignalObserver next = iterator.next();
            if (next == observer) {
                iterator.remove();
            }
        }
    }

    public void removeAllWifiSignalObservers() {
        LinkedList<WifiSignalObserver> wifiSignalObservers = mObserveWifiSignalLevelTask.getWifiSignalObservers();
        ListIterator<WifiSignalObserver> iterator = wifiSignalObservers.listIterator();
        while (iterator.hasNext()) {
            iterator.remove();
        }
    }

    public void shutDownObserveWifiSignal() {
        if (isObserveWifiSignalStrength()) {
            mObserveWifiSignalLevelTask.setStopObserve(true);
            mWifiLevelFuture.cancel(true);
            mWifiLevelFuture = null;
        }
    }

    public final boolean ping(String url) {
        String result = null;
        try {
            String ip = url;
            Process p = Runtime.getRuntime().exec("ping -c 3 -w 200 " + ip);// ping3次, 每次200毫秒超时限制
            // 读取ping的内容，可以不加
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
                stringBuffer.append("\r\n");
            }
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                return true;
            } else {
            }
        } catch (IOException e) {
            Logger.printStackTrace(e);
        } catch (InterruptedException e) {
            Logger.printStackTrace(e);
        }
        return false;
    }


}
