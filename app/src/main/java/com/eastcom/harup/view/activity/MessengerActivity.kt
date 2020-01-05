package com.eastcom.harup.view.activity

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_messenager.*

const val WHAT:Int = 0x11
class MessengerActivity : AppCompatActivity() {

    private var mHandler = Handler{
        if (it.what == WHAT){
            val bundle = it.obj as Bundle
            Toast.makeText(this, "${Process.myPid()}:" + bundle.get("reply"),Toast.LENGTH_SHORT).show()
        }
        true}

    private val replyMessenger:Messenger? = Messenger(mHandler)

    private var mMessenger: Messenger ?= null
    private val connection = object: ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mMessenger = Messenger(service)
            Toast.makeText(this@MessengerActivity,"connected",Toast.LENGTH_SHORT).show()
        }
    }




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messenager)
        init()
    }

    private fun init() {
        connect.setOnClickListener {
            bindService(Intent(this,MessengerService::class.java),connection,Service.BIND_AUTO_CREATE)
        }

        send.setOnClickListener {
            val message = Message()
            message.apply {
                what = WHAT
                var bundle = Bundle()
                bundle.putCharSequence("edit",editText.text.toString())
                obj = bundle
                replyTo = replyMessenger
            }

            mMessenger?.send(message)
        }
    }

    override fun onDestroy() {
        if (mMessenger != null){
            unbindService(connection)
        }
        super.onDestroy()
    }


}

class MessengerService: Service() {

    private var mHandler = Handler{
        if (it.what == WHAT){
            val bundle = it.obj as Bundle
            Toast.makeText(this, "${Process.myPid()}:" + bundle.get("edit"),Toast.LENGTH_SHORT).show()
            val client = it.replyTo
            val replyMessage = Message()
            var replyBundle = Bundle()
            replyBundle.putCharSequence("reply","${Process.myPid()} got it")
            replyMessage.apply {
                what = WHAT
                obj = replyBundle
            }
            client.send(replyMessage)
        }
        true}

    private val messenger:Messenger? = Messenger(mHandler)
        override fun onBind(intent: Intent?): IBinder? {
            return messenger?.binder
    }
}

class MessengerHandler : Handler() {
    override fun handleMessage(msg: Message?) {
        if (msg?.what == WHAT){

        }
    }
}