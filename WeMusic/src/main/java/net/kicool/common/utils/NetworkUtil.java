package net.kicool.common.utils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

/**
 * Created by kicoolzhang on 11/12/13.
 */

public class NetworkUtil {

    private static final String TAG = "NetworkUtil";

    public static String toBigEndianIpAddressString(int ipAddress) {
        // Convert little-endian to big-endianif needed
        if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] ipByteArray = BigInteger.valueOf(ipAddress).toByteArray();

        String ipAddressString;
        try {
            ipAddressString = InetAddress.getByAddress(ipByteArray).getHostAddress();
        } catch (UnknownHostException ex) {
            LoggerUtil.e(TAG, "Unable to get host address.");
            ipAddressString = null;
        }

        LoggerUtil.i(TAG, "IP:" + ipAddressString);
        return ipAddressString;
    }

}
