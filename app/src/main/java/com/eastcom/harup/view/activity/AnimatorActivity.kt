package com.eastcom.harup.view.activity

import android.animation.*
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_animation.*
import kotlinx.android.synthetic.main.activity_animator.*

class AnimatorActivity : AppCompatActivity() {

    private  var colorAnimator : ObjectAnimator? = null
    private var animatorSet : AnimatorSet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animator)
        init()
    }

    private fun init(){
        backgroundColor.setOnClickListener {
            endAimatorSet()
            startColorAnimator(animator,"backgroundColor", 0xFFFF8080.toInt(), 0xFF8080FF.toInt())
        }

        set.setOnClickListener {
            endColorAnimator()
            startAnimatorSet(animator)
        }

        loadAnimator.setOnClickListener {
            loadAnimator(ViewWrapper(animator))
        }
    }

    private fun startColorAnimator(target: Any,propertyName: String, vararg values: Int){
        colorAnimator = ObjectAnimator.ofInt(target, propertyName, values[0],values[1])
        colorAnimator?.apply {
            duration = 3000
            setEvaluator(ArgbEvaluator())
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        colorAnimator?.start()
    }


    @SuppressLint("ObjectAnimatorBinding")
    private fun startAnimatorSet(target: Any){
        animatorSet = AnimatorSet()
        animatorSet?.playTogether(
            ObjectAnimator.ofFloat(target,"rotationX",0f,360f),
            ObjectAnimator.ofFloat(target,"rotationY",0F,180F),
            ObjectAnimator.ofFloat(target,"rotation",0f,-90f),
            ObjectAnimator.ofFloat(target,"translationX",0f,90f),
            ObjectAnimator.ofFloat(target,"translationY",0f,90f),
            ObjectAnimator.ofFloat(target,"scaleX",1f,1.5f),
            ObjectAnimator.ofFloat(target,"scaleY",1f,1.5f),
            ObjectAnimator.ofFloat(target,"ALPHA",1f,0.25f,1F)
        )
        animatorSet?.duration = 5*1000
        animatorSet?.start()
    }

    private fun loadAnimator(target: Any){
        val loadAnimator = AnimatorInflater.loadAnimator(this, R.animator.animator_property)
        loadAnimator.setTarget(target)
        loadAnimator.start()
    }

    private fun endColorAnimator(){
            colorAnimator?.end()
    }

    private fun endAimatorSet(){
        animatorSet?.end()
    }

    override fun finish() {
        endColorAnimator()
        endAimatorSet()
        AccelerateDecelerateInterpolator()
        super.finish()
    }
}

class ViewWrapper{
    var view:View

    constructor (view : View){
        this.view = view
    }

    fun setX(x:Int){
        this.view.x = x.toFloat()
    }

    fun getX():Float{
        return this.view.x
    }

}


