package com.eastcom.harup.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eastcom.harup.R

class EnterAndExitAnimActivity : AppCompatActivity() {


    override fun onStart() {
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.animation_activity_enter,R.anim.animation_alpha)
        setContentView(R.layout.activity_enter_and_exit_anim)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(android.R.anim.fade_in,R.anim.animation_activity_exit)
    }
}
