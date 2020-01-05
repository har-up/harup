package com.eastcom.harup.view.activity

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eastcom.harup.R
import com.eastcom.harup.aidl.Book
import com.eastcom.harup.aidl.IBookCallback
import com.eastcom.harup.aidl.IBookManager
import kotlinx.android.synthetic.main.activity_aidl.*
import java.util.concurrent.CopyOnWriteArrayList

class AidlActivity : AppCompatActivity() {

    private var mBookManager: IBookManager? = null

    private val mHandler = Handler{
        if (it.what == 1 && it.obj != null){
            val books = it.obj as List<Book>
            text.text = books.joinToString { it.toString() }
        }
        true
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aidl)
        init()
    }


    private fun init() {
        val intent = Intent(this, AidlService::class.java)
        bindService(intent,aidlServiceConnection, Context.BIND_AUTO_CREATE)

        add.setOnClickListener {
            Thread(Runnable {
                mBookManager?.addBook(Book("Android",36))
            }).start()
        }

        get.setOnClickListener {
            Thread(Runnable {
                try {
                    val bookList = mBookManager?.bookList
                    val message = Message()
                    message.what = 1
                    message.obj = bookList
                    mHandler.sendMessage(message)
                }catch (e: RemoteException){
                    e.printStackTrace()
                }

            }).start()
        }
    }

    private var callback = object: IBookCallback.Stub() {
        override fun onAdd(book: Book?) {
            mHandler.post {
                Toast.makeText(this@AidlActivity,"书库有更新",Toast.LENGTH_SHORT).show()
                val bookList = mBookManager?.bookList
                val message = Message()
                message.what = 1
                message.obj = bookList
                mHandler.sendMessage(message)
            }

        }
    }

    private val aidlServiceConnection = object : ServiceConnection{
        override fun onServiceDisconnected(name: ComponentName?) {
            Toast.makeText(this@AidlActivity,"已断开aidlService",Toast.LENGTH_SHORT).show()
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mBookManager = IBookManager.Stub.asInterface(service)
            mBookManager?.registerListener(callback)
            Toast.makeText(this@AidlActivity,"已连接aidlService",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        unbindService(aidlServiceConnection)
        mBookManager?.unRegisterListener(callback)
        super.onDestroy()
    }
}




class AidlService : Service(){

     val mBooks: CopyOnWriteArrayList<Book> =  CopyOnWriteArrayList()
     val mListener: RemoteCallbackList<IBookCallback> = RemoteCallbackList()

    private var bookManager = object : IBookManager.Stub(){
        override fun registerListener(callback: IBookCallback?){
            mListener.register(callback)
        }

        override fun unRegisterListener(callback: IBookCallback?) {
            mListener.unregister(callback)
        }

        override fun addBook(book: Book?) {
            mBooks.add(book)
            for ( i in 0 until mListener.beginBroadcast()){
                try {
                    mListener.getBroadcastItem(i).onAdd(book)
                }catch (e: RemoteException){
                    e.printStackTrace()
                }
            }
            mListener.finishBroadcast()

        }

        override fun getBookList(): MutableList<Book> {
            return mBooks
        }
    }

    override fun onCreate() {
        super.onCreate()
        mBooks.add(Book("java",11))
    }

    override fun onBind(intent: Intent?): IBinder? {
        return bookManager
    }

}