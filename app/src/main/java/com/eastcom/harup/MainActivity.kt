package com.eastcom.harup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init(){
        var textview: TextView
        for (i in 0..100){
            textview = TextView(this)
            textview.text = "this is $i"
            horizontalScrollView.addView(textview)
        }
    }
}
