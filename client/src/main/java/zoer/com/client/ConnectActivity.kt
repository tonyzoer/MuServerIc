package zoer.com.client

import android.annotation.SuppressLint
import android.content.Context
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_connect.*
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.*

class ConnectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)
        connect.setOnClickListener({
            val myClientTask = ConnectTask(
                    address.text.toString(),
                    Integer.parseInt(port.text.toString()))
            myClientTask.execute()
        })
        val serversearch = SearchServerTask()
        serversearch.execute()
        clear.setOnClickListener({ response_text.text = "" })
    }

    inner class SearchServerTask : AsyncTask<String, String, String>() {
        @SuppressLint("WifiManagerLeak", "MissingPermission")
        override fun doInBackground(vararg p0: String?): String {
            val ip = MyIp.getIpAddress().toString()
            val subnet = ip.removeRange(ip.lastIndexOf("."), ip.length)
            val timeout = 100
            var wifii:WifiManager=getSystemService(Context.WIFI_SERVICE) as WifiManager
            var d=wifii.dhcpInfo
            var host:InetAddress= InetAddress.getByName(longToIp(d.dns1.toLong()))
            var iparr: ByteArray = host.address
            for (i in 1..254) {
//                var host = subnet + ".$i"
                iparr[0]= i.toByte()
                var address=InetAddress.getByAddress(iparr.reversedArray())
                try {
                    if (address.isReachable(timeout)) {
                        Log.d("SearchServer", address.toString() + "is reached")
                    }
// else if (!address.getHostAddress().equals(address.getHostName())){
//                        Log.d("SearchServer", address.toString()+" known")
//                    }
                } catch (e: Exception) {
//                    Log.e("EXEPTION ", e.message)
                }
            }
            return ""
        }

        fun longToIp(ip: Long): String {
            var ip = ip
            val result = StringBuilder(15)

            for (i in 0..3) {

                result.insert(0, java.lang.Long.toString(ip and 0xff))

                if (i < 3) {
                    result.insert(0, '.')
                }

                ip = ip shr 8
            }
            return result.toString()
        }

    }


    inner class ConnectTask internal constructor(private var dstAddress: String, private var dstPort: Int) : AsyncTask<String, String, String>() {
        private var response = ""

        override fun doInBackground(vararg arg0: String): String? {

            var socket: Socket? = null

            try {
                socket = Socket(dstAddress, dstPort)

                val byteArrayOutputStream = ByteArrayOutputStream(1024)
                val buffer = ByteArray(1024)

                var bytesRead: Int
                val inputStream = socket.getInputStream()


                bytesRead = inputStream.read(buffer)
                while (bytesRead != -1) {
                    byteArrayOutputStream.write(buffer, 0, bytesRead)
                    response += byteArrayOutputStream.toString("UTF-8")
                    bytesRead = inputStream.read(buffer)
                }

            } catch (e: UnknownHostException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                response = "UnknownHostException: " + e.toString()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
                response = "IOException: " + e.toString()
            } finally {
                if (socket != null) {
                    try {
                        socket.close()
                    } catch (e: IOException) {
                        // TODO Auto-generated catch block
                        e.printStackTrace()
                    }

                }
            }
            return response
        }

        override fun onPostExecute(result: String) {
            response_text.text = result
            super.onPostExecute(result)
            this@ConnectActivity.runOnUiThread({ Toast.makeText(this@ConnectActivity, response, Toast.LENGTH_SHORT).show() })
        }

    }
}
