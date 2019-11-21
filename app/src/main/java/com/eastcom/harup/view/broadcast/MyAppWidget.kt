package com.eastcom.harup.view.broadcast

import android.app.NotificationManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.eastcom.harup.R

class MyAppWidget : AppWidgetProvider() {

    val TAG: String = "MyAppWidget"
    val CLCICK_ACTION = "com.eastcom.harup.broadcast.appwidget.CLICK"
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.i(TAG, "onReceive: action = ${intent?.action}")
        if (CLCICK_ACTION == intent?.action){
            Toast.makeText(context,"clicked it",Toast.LENGTH_SHORT).show()
            Thread {
                val decodeResource = BitmapFactory.decodeResource(
                    context?.resources,
                    R.drawable.ic_launcher_foreground
                )
                val instance = AppWidgetManager.getInstance(context)
                for (i in 0..37){
                    val degree = i * 10 % 360
                    val remoteViews = RemoteViews(context?.packageName, R.layout.widget)
                    remoteViews.setImageViewBitmap(R.id.imageView,rotateBitmap(decodeResource,degree.toFloat()))
                    val intent = Intent()
                    intent.action = CLCICK_ACTION
                    val pendingIntent = PendingIntent.getBroadcast(context,0,intent,0)
                    remoteViews.setOnClickPendingIntent(R.id.imageView,pendingIntent)
                    instance.updateAppWidget(ComponentName(context,MyAppWidget::class.java),remoteViews)
                    SystemClock.sleep(30)
                }
            }.start()
        }
    }

    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.i(TAG,"onUpdate")
        Log.i(TAG,"counter: ${appWidgetIds?.size}")
        for (widgetId in 0..appWidgetIds!!.size){
            onWidgetUpdate(context,appWidgetManager,widgetId)
        }
    }

    fun onWidgetUpdate(context: Context?,appWidgetManager: AppWidgetManager?,widgetId: Int){
        Log.i(TAG,"appWidget: $widgetId")
        val remoteViews = RemoteViews(context?.packageName, R.layout.widget)
        val intent = Intent(CLCICK_ACTION)
        val broadcast = PendingIntent.getBroadcast(context, 0, intent, 0)
        remoteViews.setOnClickPendingIntent(R.id.imageView,broadcast)
        appWidgetManager?.updateAppWidget(widgetId,remoteViews)
    }

    fun rotateBitmap(bitmap: Bitmap, degree:Float): Bitmap{
        val matrix = Matrix()
        matrix.reset()
        matrix.setRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height,matrix,true)
    }
}