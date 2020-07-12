package com.eastcom.harup.view.activity

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.SparseArray
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.eastcom.harup.R
import com.eastcom.harup.utils.FixDexUtils
import com.eastcom.harup.utils.FixUtil
import com.eastcom.harup.utils.MathUtil
import kotlinx.android.synthetic.main.activity_hot_fix.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

class HotFixActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        var sparseArray = SparseArray<Any>(10)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hot_fix)

        tv_result.text = "0"

        bt_compute.setOnClickListener {
            var result = compute();
            tv_result.text = result.toString()
        }

        registerReceiver(object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                TODO("Not yet implemented")
            }
        }, IntentFilter())
        bt_fix.setOnClickListener {
            FixUtil.loadDex(this)
            FixDexUtils.loadFixedDex(this)
            var okHttpClient = OkHttpClient()
            var newCall = okHttpClient.newCall(Request.Builder().build())
            newCall.execute()
            newCall.enqueue(null)
        }
    }

    private fun compute():Int{
        return MathUtil.div();
    }
}
