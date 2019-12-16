package com.eastcom.module_login

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import org.junit.Test

import org.junit.Assert.*
import java.security.MessageDigest
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    companion object {
        //    @Test
            @JvmStatic
            fun  main(args:Array<String>){
                var threadLocal = ThreadLocal<String>()
                threadLocal.set("hello")
                var get = threadLocal.get()
                println(get)
                get = "hello world"
                println(get)
                assertEquals("hello world",get)
                Looper.prepare()
            var handler = Handler(Looper.myLooper())
            handler.sendEmptyMessage(2)
            MessageDigest.getInstance("MD5")
        }
    }


}
