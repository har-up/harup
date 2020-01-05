package com.eastcom.harup.view.activity

import android.app.Service
import android.app.admin.DevicePolicyManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_socket.*
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.text.SimpleDateFormat
import java.util.*

class SocketActivity : AppCompatActivity() {
    private val TAG: String = javaClass.simpleName
    private val SERVER_MESSAGE = 0x11
    private val SERVER_CONNECTED = "connected flag"
    var mSocket: Socket? = null
    private var mPrintWriter: PrintWriter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_socket)
        startService(Intent(this, SocketServer::class.java))
        Thread(mRunnable).start()
        init()
    }

    private fun init() {
        send.setOnClickListener {
            val text = editText.text
            sendTo(text.toString())
            record.text = StringBuilder(record.text).append("self${formatDateTime(System.currentTimeMillis())}$text\n").toString()
        }
    }

    private val mRunnable = Runnable {
        while (mSocket == null) {
            try {
                mSocket = Socket("localhost", 8080)
                mPrintWriter = PrintWriter(OutputStreamWriter(mSocket?.getOutputStream()), true)
                Log.d(TAG, "连接到8080服务器")
                sendTo(SERVER_CONNECTED)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.d(TAG, "连接服务异常")
                SystemClock.sleep(1000)
                Log.d(TAG, "connect tcp server failed,retry...")
            }
        }

        try {
            var inputReader = BufferedReader(InputStreamReader(mSocket?.getInputStream()))
            while (!this@SocketActivity.isFinishing) {
                var msg = inputReader.readLine()
                Log.d(TAG, "接收到服务端消息： $msg")
                if (msg!=null){
                    mHandler.obtainMessage(SERVER_MESSAGE, msg).sendToTarget()
                }
            }
            Log.d(TAG, "self 退出")
            inputReader.close()
            mPrintWriter?.close()
            mSocket?.close()
        } catch (e: IOException) {
            Log.d(TAG, "接受数据异常")
        }

    }

    private fun sendTo(msg: String){
        Thread(Runnable {
            mPrintWriter?.println(msg)
        }).start()
    }

    private val mHandler = Handler {
        if (it.what == SERVER_MESSAGE) {
            var msg: String = it.obj as String
            var split = msg.split("|")
            if (split.size == 2){
                msg = formatDateTime(split[0].toLong()) + split[1]
            }
            record.text = StringBuilder(record.text).append("server$msg\n").toString()
        }
        true
    }

    private fun formatDateTime(time: Long):String{
        return SimpleDateFormat("(HH:mm:ss) ：").format(Date(time))
    }

}


class SocketServer : Service() {

    val TAG = javaClass.simpleName

    private var destroyed = false
    private val mDefineMessage = arrayOf("你好！", "请问你叫什么", "我很好", "再见")

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        Thread(Runnable {
            var serverSocket: ServerSocket
            try {
                serverSocket = ServerSocket(8080)
            } catch (e: IOException) {
                Log.d(TAG, "establish tcp server failed, port: 8080")
                e.printStackTrace()
                return@Runnable
            }
            while (!destroyed) {
                val socket = serverSocket.accept()
                Thread(Runnable {
                    Log.d(TAG, "server accept")
                    try {
                        var inputReader = BufferedReader(InputStreamReader(socket.getInputStream()))
                        var printWriter =
                            PrintWriter(
                                BufferedWriter(OutputStreamWriter(socket.getOutputStream())),
                                true
                            )
                        while (!destroyed) {
                            var str = inputReader.readLine()
                            Log.d(TAG, "接收到信息：$str")
                            if (str == null) {
                                Log.d(TAG, "断开连接")
                                break
                            }
                            if ("connected flag" == str){
                                printWriter.println("${System.currentTimeMillis()}|欢迎来到聊天室")
                            }else{
                                var random = Random()
                                var nextInt = random.nextInt(mDefineMessage.size-1)
                                Log.d(TAG, "回复： ${mDefineMessage[nextInt]}")
                                printWriter.println("${System.currentTimeMillis()}|${mDefineMessage[nextInt]}")
                            }
                        }
                        Log.d(TAG, "客户端**退出连接")
                        printWriter.close()
                        inputReader.close()
                        socket.close()
                    } catch (e: IOException) {
                        Log.d(TAG, "与客户端通信异常")
                        e.printStackTrace()
                    }
                }).start()
            }
        }).start()
    }

    override fun onDestroy() {
        destroyed = true
        super.onDestroy()
    }
}
