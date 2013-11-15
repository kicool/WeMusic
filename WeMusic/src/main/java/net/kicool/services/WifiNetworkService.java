package net.kicool.services;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import net.kicool.common.utils.LoggerUtil;
import net.kicool.common.utils.NetworkUtil;

/**
 * Created by kicoolzhang on 11/12/13.
 */

public class WifiNetworkService extends BaseService {
    private static final String TAG = "WifiNetworkService";

    WifiManager mWifiManager;
    WifiManager.WifiLock mWifiLock;

    public WifiNetworkService() {

    }

    @Override
    public void init(ServiceProvider provider) {
        super.init(provider);

        mWifiManager = (WifiManager) provider.getContext().getSystemService(Context.WIFI_SERVICE);

        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        LoggerUtil.i(TAG, "Own IP Address: " + NetworkUtil.toBigEndianIpAddressString(wifiInfo.getIpAddress()) + "Network SSID: " + wifiInfo.getSSID() + "Netword ID: " + wifiInfo.getNetworkId());
    }

    public WifiNetworkInfo getWifiInfo() {
        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
        return new WifiNetworkInfo(wifiInfo);
    }

    public void lock() {
        if (mWifiLock == null) {
            mWifiLock = mWifiManager.createWifiLock(
                    android.os.Build.VERSION.SDK_INT >= 12
                            ? WifiManager.WIFI_MODE_FULL_HIGH_PERF
                            : WifiManager.WIFI_MODE_FULL, getClass().getName());
        }

        mWifiLock.acquire();
    }

    public void unlock() {
        mWifiLock.release();
    }

    public static class WifiNetworkInfo {
        WifiInfo wifiInfo;
        String wifiIpAddress;

        public WifiNetworkInfo(WifiInfo wifiInfo) {
            this.wifiInfo = wifiInfo;

            wifiIpAddress = NetworkUtil.toBigEndianIpAddressString(wifiInfo.getIpAddress());
        }

        public String getWifiIpAddress() {
            return wifiIpAddress;
        }

        public String getSSID() {
            return wifiInfo.getSSID();
        }

        public String getNetwordId() {
            return Integer.toString(wifiInfo.getNetworkId());
        }
    }
}
