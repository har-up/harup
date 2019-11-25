package com.eastcom.harup.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_animation.*
import kotlinx.android.synthetic.main.activity_animation.view.*
import kotlinx.android.synthetic.main.activity_remote_view.*

class AnimationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animation)
        init()
    }

    private fun init(){
        val animationObject = animation
        translate.setOnClickListener {
            val animationTranslate = AnimationUtils.loadAnimation(this, R.anim.animation_translate)
            animationTranslate.duration = 1000
            animationObject.startAnimation(animationTranslate)
        }

        scale.setOnClickListener {
            val animationScale = AnimationUtils.loadAnimation(this, R.anim.animation_scale)
            animationScale.duration = 1000
            animationObject.startAnimation(animationScale)
        }

        rotate.setOnClickListener {
            val rotateAnimation = RotateAnimation(0F,270F,animationObject.pivotX,animationObject.pivotY)
            rotateAnimation.duration = 1000
            animationObject.startAnimation(rotateAnimation)
        }

        alpha.setOnClickListener {
            val animationAlpha = AnimationUtils.loadAnimation(this, R.anim.animation_alpha)
            animationAlpha.duration = 1000
            animationObject.startAnimation(animationAlpha)
        }

        animationSet.setOnClickListener {
            val animationSet = AnimationUtils.loadAnimation(this, R.anim.animation_set) as AnimationSet
            val rotateAnimation = RotateAnimation(0F,270F,animationObject.pivotX,animationObject.pivotY)
            animationSet.addAnimation(rotateAnimation)
            animationSet.duration = 1000
            animationObject.startAnimation(animationSet)
        }
    }
}
