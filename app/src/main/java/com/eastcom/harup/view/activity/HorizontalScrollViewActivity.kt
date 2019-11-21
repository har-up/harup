package com.eastcom.harup.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_horizontal_scroll_view.*

class HorizontalScrollViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horizontal_scroll_view)
        init()
    }

    fun init() {
        var textview: TextView
        for (i in 0..100){
            textview = TextView(this)
            textview.text = "this is $i"
            textview.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
            horizontalScrollView.addView(textview)
        }
    }
}
