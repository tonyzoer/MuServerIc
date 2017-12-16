package com.zoer.servicetest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.net.NetworkInterface
import java.net.SocketException

class ServerActivity : AppCompatActivity() {


    companion object {
        val TAG="ServerActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()
        infoip.text = getIpAddress()
        var serverIntent = Intent(this@ServerActivity, ServerSocket::class.java)
        startService(serverIntent)
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show()
    }


    private fun getIpAddress(): CharSequence {
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
