package com.eastcom.harup.view.activity

import android.graphics.PixelFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Display
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import android.widget.Button
import android.widget.Toast
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_window.*

class WindowActivity : AppCompatActivity() {

    var button: Button? = null
    var layoutParams: LayoutParams? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_window)
        init()
    }


    private fun init(){
        addButton.setOnClickListener {
            addFloatButton()
        }
    }

    private fun addFloatButton(){
        layoutParams = WindowManager.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            1999,
            LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSPARENT
        )
        layoutParams?.x = 100
        layoutParams?.y = 100
        button = Button(this)
        button?.setOnClickListener {
            windowManager.removeView(it)
        }
        button?.setText("来自widowmaager 添加")
        windowManager.addView(button,layoutParams)
        Toast.makeText(this,"this",Toast.LENGTH_SHORT).show()
//        startActivity()
//        startService()
    }



    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_MOVE){
            layoutParams?.apply {
                gravity = Gravity.TOP
                x = event.x.toInt()
                y = event.y.toInt()
            }
            windowManager.updateViewLayout(button,layoutParams)
        }
        return false
    }
}
