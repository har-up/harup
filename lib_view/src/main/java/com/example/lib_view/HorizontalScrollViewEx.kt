package com.example.lib_view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import android.widget.Scroller
import kotlin.math.abs

class HorizontalScrollViewEx : ViewGroup{


    private var lastX = 0f
    private var lastY= 0f
    private var lastInterceptX = 0f
    private var lastInterceptY = 0f
    private var mScroller: Scroller? = null
    private var mVelocityTracker: VelocityTracker? = null


    constructor(context:Context): super(context){}

    constructor(context: Context, attributeSet: AttributeSet?): super(context,attributeSet)

    constructor(context: Context,attributeSet: AttributeSet?,defStyleAttr: Int): super(context,attributeSet,defStyleAttr)

    constructor(context: Context,attributeSet: AttributeSet?,defStyleAttr: Int,defStyleRes: Int): super(context,attributeSet,defStyleAttr,defStyleRes)

    init {
        if (mScroller == null){
            mScroller = Scroller(context)
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec,heightMeasureSpec)

        if (childCount == 0) {
            setMeasuredDimension(0,0)
            return
        }
        var widthMode = MeasureSpec.getMode(widthMeasureSpec)
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var heigthMode = MeasureSpec.getMode(widthMeasureSpec)
        var heigthSize = MeasureSpec.getSize(widthMeasureSpec)
        var measureWidth = 0
        var measureHeight = 0
        val childView = getChildAt(0)
        if (widthMode == heigthMode && widthMode == MeasureSpec.AT_MOST){
            measureWidth = childView.measuredWidth * childCount
            measureHeight = childView.measuredHeight
        }else if(widthMode == MeasureSpec.AT_MOST){
            measureWidth = childView.measuredWidth * childCount
            measureHeight= heigthSize
        }else if(heigthMode == MeasureSpec.AT_MOST){
            measureHeight = childView.measuredHeight
            measureWidth = widthSize
        }
        setMeasuredDimension(measuredWidth,measuredHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var childLeft = 0
        if (childCount == 0){
            return
        }
        for ( i in 0 until childCount){
            val childView = getChildAt(i)
            if (childView.visibility != View.GONE){
                childView.layout(childLeft,0,childLeft + childView.measuredWidth,childView.measuredHeight)
                childLeft += childView.measuredWidth
            }
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var intercepted = false
        when(ev?.action){
            MotionEvent.ACTION_DOWN -> {
                intercepted = false
            }
            MotionEvent.ACTION_MOVE -> {
                var deltaX = ev.x - lastInterceptX
                var deltaY = ev.y - lastInterceptY
                intercepted = abs(deltaX) > abs(deltaY)
            }
            MotionEvent.ACTION_UP -> {
                intercepted = false
            }
        }
        if (ev != null) {
            lastInterceptX = ev.x
            lastInterceptY = ev.y
        }
        return intercepted
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {
                scrollBy(-(event.x - lastX).toInt(),0)
            }
            MotionEvent.ACTION_UP -> {

            }
        }
        if (event != null){
            lastX = event.x
            lastY = event.y
        }
        return true
    }
}
