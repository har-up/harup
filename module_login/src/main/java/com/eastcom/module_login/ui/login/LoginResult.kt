package com.eastcom.module_login.ui.login

import java.util.concurrent.CountDownLatch

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: LoggedInUserView? = null,
    val error: Int? = null
)
var runnable1 = Runnable {
    var i = 0
    while (i < 10){
        Thread.sleep(1000);
        println("thread1:$i")

        i++
    }
}

var runnable2 = Runnable {
    var i = 0
    while (i < 10){
        Thread.sleep(1000);
        System.out.println("thread2:$i")
        i++
    }
}

var count = CountDownLatch(2);

fun main(args:Array<String>){
    var thread1 = Thread(runnable1)
    var thread2 = Thread(runnable2)
    thread1.start()
    thread2.start()
    thread1.join()
    thread2.join()
    println("执行完毕")
}