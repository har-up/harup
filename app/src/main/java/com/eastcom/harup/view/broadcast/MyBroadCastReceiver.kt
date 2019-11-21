package com.eastcom.harup.view.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.eastcom.harup.view.activity.RemoteViewActivity

class MyBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (RemoteViewActivity.ACTION_SNOOZE == intent?.action){
            println("点击了   snooze")
        }
    }
}