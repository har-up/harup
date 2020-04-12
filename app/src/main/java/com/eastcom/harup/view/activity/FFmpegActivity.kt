package com.eastcom.harup.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.eastcom.harup.R
import com.example.ffmpeg.FFmpegUtil
import kotlinx.android.synthetic.main.activity_ffmpeg.*
import java.io.File

const val TAG = "FFmpegActivity"
class FFmpegActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg)
        initView()
    }

    private fun initView() {
        btn_decode.setOnClickListener {
            val input =
                File(Environment.getExternalStorageDirectory(), "input.mp4").getAbsolutePath()
            val output = File(
                Environment.getExternalStorageDirectory(),
                "output_1280x720_yuv420p.yuv"
            ).getAbsolutePath()
            Log.d(TAG,input)
            Log.d(TAG,output)
            FFmpegUtil.test(input, output)
        }
    }


}
