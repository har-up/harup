package com.eastcom.harup.view.activity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.widget.Button
import androidx.core.graphics.BitmapCompat
import com.eastcom.harup.R
import kotlinx.android.synthetic.main.activity_bitmap.*
import java.io.File
import java.lang.NullPointerException

class BitmapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bitmap)

        load.setOnClickListener {
            iv_bitmap.setImageResource(R.drawable.image_love)
            printBitmapSize(iv_bitmap.drawable)
        }
        Button(this)
    }

    private fun printBitmapSize(drawable: Drawable) {
        drawable as BitmapDrawable
        if (drawable == null) return
        Log.d(this.javaClass.name,"count ${drawable.bitmap.byteCount}")
        Log.d(this.javaClass.name,"width ${drawable.bitmap.width}")
        Log.d(this.javaClass.name,"height ${drawable.bitmap.height}")
        var displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics);
        displayMetrics.apply {
            Log.d(this@BitmapActivity.javaClass.name,"densityDpi ${this.densityDpi}")
            Log.d(this@BitmapActivity.javaClass.name,"heightPixels ${this.heightPixels}")
            Log.d(this@BitmapActivity.javaClass.name,"widthPixels ${this.widthPixels}")
            Log.d(this@BitmapActivity.javaClass.name,"scaledDensity ${this.scaledDensity}")
        }
    }

}
