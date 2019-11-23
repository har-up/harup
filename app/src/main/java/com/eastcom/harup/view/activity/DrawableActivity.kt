package com.eastcom.harup.view.activity

import android.graphics.drawable.ClipDrawable
import android.graphics.drawable.ScaleDrawable
import android.graphics.drawable.TransitionDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_drawable.*
import kotlin.math.abs

class DrawableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawable)
        init()
    }

    private fun init(){
        levelButton.setOnClickListener{
            it.background.level = abs(it.background.level - 1)
        }
        transitionButton.setOnClickListener {
            var transitionDrawable = it.background as TransitionDrawable
            transitionDrawable.level = abs(transitionDrawable.level - 1)
            if (transitionDrawable.level > 0){
                transitionDrawable.startTransition(1000)
            }else{
                transitionDrawable.reverseTransition(1000)
            }
        }
        scaleButton.setOnClickListener {
            var scaleDrawable = it.background as ScaleDrawable
            scaleDrawable.level = if (scaleDrawable.level == 0) 1000 else 0
        }

        var drawable = clip.background as ClipDrawable
        drawable.level = 10000
        clip.setOnClickListener {
            drawable.level -= 50
        }
    }
}
