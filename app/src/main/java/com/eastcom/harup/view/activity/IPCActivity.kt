package com.eastcom.harup.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_ipc.*

class IPCActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ipc)
        initView()
    }

    private fun initView() {
        bundle.setOnClickListener {
            Toast.makeText(this,"Using in four base Components",Toast.LENGTH_SHORT).show()
        }
        file.setOnClickListener {
            startActivity(Intent(this,FileShareActivity::class.java))
        }
        messenger.setOnClickListener {
            startActivity(Intent(this,MessengerActivity::class.java))
        }
        aidl.setOnClickListener {
            startActivity(Intent(this,AidlActivity::class.java))
        }
        contentProvider.setOnClickListener {
            startActivity(Intent(this,ContentProviderActivity::class.java))
        }
        socket.setOnClickListener {
            startActivity(Intent(this,SocketActivity::class.java))
        }
    }
}
