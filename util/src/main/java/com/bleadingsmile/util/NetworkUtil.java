package com.bleadingsmile.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Created by Larry Hsiao on 2016/6/15.
 */
public class NetworkUtil {
    /**
     * According Internet Assigned Numbers Authority (IANA)
     * Class A:10.0.0.0 - 10.255.255.255
     * Class B:172.16.0.0 - 172.31.255.255
     * Class C:192.168.0.0 - 192.168.255.255
     */
    public static boolean isInPrivateNetwork(long hostAddress) {
        byte[] addressBytes = {
                (byte) (0xff & hostAddress),
                (byte) (0xff & (hostAddress >> 8)),
                (byte) (0xff & (hostAddress >> 16)),
                (byte) (0xff & (hostAddress >> 24))};
        return isInPrivateNetwork(addressBytes);

    }

    /**
     * According Internet Assigned Numbers Authority (IANA)
     * Class A:10.0.0.0 - 10.255.255.255
     * Class B:172.16.0.0 - 172.31.255.255
     * Class C:192.168.0.0 - 192.168.255.255
     */
    public static boolean isInPrivateNetwork(byte[] addressBytes) {
        try {
            InetAddress ip = InetAddress.getByAddress(addressBytes);
            byte[] octets = ip.getAddress();
            long result = 0;
            for (byte octet : octets) {
                result <<= 8;
                result |= octet & 0xff;
            }
            boolean classA = result >= 167772160L && result <= 184549375L;
            boolean classB = result >= 2886729728L && result <= 2887778303L;
            boolean classC = result >= 3232235520L && result <= 3232301055L;
            return classA || classB || classC;
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public static InetAddress getWLANInterfaceIp() throws UnknownHostException {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr =
                         intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && (inetAddress.getAddress().length == 4)) {
                            return inetAddress;
                        }
                    }
                }
            }
            throw new UnknownHostException("WLAN Interface ip not exist");
        } catch (Exception ex) {
            throw new UnknownHostException("WLAN Interface ip not exist," + ex);
        }
    }
}
