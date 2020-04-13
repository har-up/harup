package com.eastcom.harup.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eastcom.harup.R
import com.eastcom.harup.utils.PosixUtil
import kotlinx.android.synthetic.main.activity_posix.*

class PosixActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posix)

        init()
    }

    fun init(){
        button.setOnClickListener {
            PosixUtil.runOtherThread()
        }
    }
}
