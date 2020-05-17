package com.eastcom.harup.view.activity

import android.app.IntentService
import android.app.Service
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.eastcom.harup.R
import com.eastcom.harup.utils.FileUtil
import com.example.ffmpeg.FFmpegUtil
import kotlinx.android.synthetic.main.activity_ffmpeg.*
import java.io.File
import java.lang.NullPointerException

const val TAG = "FFmpegActivity"
class FFmpegActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg)
        FileUtil.verifyStoragePermissions(this)
        initView()
    }

    private fun initView() {
        btn_decode.setOnClickListener {
            startService(Intent(this,MyService::class.java))
        }
    }


}

class MyService : IntentService("service"){

    override fun onHandleIntent(intent: Intent?) {
        val input =
            File(Environment.getExternalStorageDirectory(), "处理静态文件.mp4")
        if (!input.exists()){
            Log.i(TAG,"file not exist")
        }

        val output = File(
            Environment.getExternalStorageDirectory(),
            "output_1280x720_yuv420p.yuv"
        )
        if (!output.exists()){
           output.createNewFile();
        }
        FFmpegUtil.test(input.absolutePath, output.absolutePath)
    }
}

