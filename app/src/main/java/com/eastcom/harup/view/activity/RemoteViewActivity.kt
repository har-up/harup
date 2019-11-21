package com.eastcom.harup.view.activity

import android.app.Notification
import android.app.Notification.EXTRA_NOTIFICATION_ID
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.eastcom.harup.MainActivity
import com.eastcom.harup.R
import com.eastcom.harup.view.broadcast.MyBroadCastReceiver
import kotlinx.android.synthetic.main.activity_remote_view.*

class RemoteViewActivity : AppCompatActivity() {


    companion object {
        const val NOTIFICATION_NORMAL_ID = "har-up notification"
        const val ACTION_SNOOZE = "my broadcast receiver action"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_remote_view)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            init()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun init(){
        createNotificationChannel() //首先创建app的notification channel,
        // app内的所有通知都可以使用该channel，官方推荐在app开始时创建

        notificationNormal.setOnClickListener {
            NotificationManagerCompat.from(this).notify(2,getNormalNotification())
        }

        notification.setOnClickListener {
            NotificationManagerCompat.from(this).notify(1,getRemoteViewNotification())
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getRemoteViewNotification(): Notification{
        return NotificationCompat.Builder(this, NOTIFICATION_NORMAL_ID).run {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setTicker("hello world")
//            setContentTitle("harup test")
//            setContentText("notification hello world")
            setContentIntent(getPendingIntent())
            setChannelId(NOTIFICATION_NORMAL_ID)
            addAction(R.drawable.ic_launcher_foreground,"snooze",getActionPendingIntent())
            setCustomContentView(getRemoteView())
            build()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getNormalNotification(): Notification{
        val builder = NotificationCompat.Builder(this, NOTIFICATION_NORMAL_ID)
        return builder.run {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setTicker("hello world")
            setContentTitle("harup test")
            setContentText("notification hello world")
            setContentIntent(getPendingIntent())
            setChannelId(NOTIFICATION_NORMAL_ID)
            addAction(R.drawable.ic_launcher_foreground,"snooze",getActionPendingIntent())
            build()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(){
        val channel =  NotificationChannel(NOTIFICATION_NORMAL_ID,getString(R.string.channel),NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = getString(R.string.channel)
        }
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun getPendingIntent(): PendingIntent{
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(this, 0, intent, 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getActionPendingIntent(): PendingIntent{
        val snoozeIntent = Intent(this, MyBroadCastReceiver::class.java).apply {
            action = ACTION_SNOOZE
            putExtra(EXTRA_NOTIFICATION_ID, 0)
        }

        return PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)
    }

    fun getRemoteView():RemoteViews{
        val remoteViews = RemoteViews(this.packageName, R.layout.layout_notification)
        remoteViews.setTextViewText(R.id.notification_text_one,"remote_view")
        remoteViews.setTextViewText(R.id.notification_text_two,"this is remoteView test")
        remoteViews.setImageViewResource(R.id.notification_icon,R.drawable.ic_launcher_foreground)
        remoteViews.setOnClickPendingIntent(R.id.remoteView,getPendingIntent())
        return remoteViews
    }
}
