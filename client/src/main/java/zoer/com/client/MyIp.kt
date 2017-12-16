package zoer.com.client

import java.net.NetworkInterface
import java.net.SocketException


object MyIp {
    fun getIpAddress(): CharSequence {
        var ip = ""
        try {
            val enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces()
            while (enumNetworkInterfaces.hasMoreElements()) {
                val networkInterface = enumNetworkInterfaces
                        .nextElement()
                val enumInetAddress = networkInterface
                        .inetAddresses
                while (enumInetAddress.hasMoreElements()) {
                    val inetAddress = enumInetAddress.nextElement()

                    if (inetAddress.isSiteLocalAddress) {
                        ip += ("SiteLocalAddress: "
                                + inetAddress.hostAddress + "\n")
                    }
                }
            }
        } catch (e: SocketException) {
            e.printStackTrace()
            ip += "Something Wrong! " + e.toString() + "\n"
        }
        return ip
    }
}