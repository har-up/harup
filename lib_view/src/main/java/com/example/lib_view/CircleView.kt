package com.example.lib_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class CircleView : View{

    private var mPaint = Paint()
    private var mColor : Int = 0

    constructor(context: Context) : super(context){
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        var a = context.obtainStyledAttributes(attrs,R.styleable.CircleView)
        mColor = a.getColor(R.styleable.CircleView_circle_color,Color.BLUE)
        a.recycle()   // 回收资源
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun init(){
        mPaint.color = mColor
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getDefaultSize(getMinWidth(),widthMeasureSpec), getDefaultSize(getMinHeight(),heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        requestLayout()
        invalidate()
        var width = width - paddingLeft - paddingRight
        var height = height - paddingTop - paddingBottom
        var list = with(ArrayList<String>()){
                add("none")
                print("$size")
        }

        canvas?.drawCircle(((width + 2*paddingLeft)/2).toFloat(), ((height + 2*paddingTop)/2).toFloat(),
            (Math.min(width,height)/2).toFloat(),mPaint)
    }

    protected fun getMinWidth(): Int{
        return  200
    }

    protected fun getMinHeight(): Int{
        return  200
    }

    companion object {
        fun getDefaultSize(size: Int, measureSpec: Int): Int{
            var result = size
            var specMode = MeasureSpec.getMode(measureSpec)
            var specSize = MeasureSpec.getSize(measureSpec)
            when(specMode){
                MeasureSpec.UNSPECIFIED -> {
                    result = size
                }
                MeasureSpec.AT_MOST -> { //处理属性是wrap_content时的情况
                    result = size
                }
                MeasureSpec.EXACTLY -> {
                    result = specSize
                }
            }
            return result
        }
    }
}