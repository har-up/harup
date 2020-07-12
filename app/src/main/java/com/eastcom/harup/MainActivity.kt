package com.eastcom.harup

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.content.SyncStatusObserver
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.text.Editable
import android.util.Log
import android.util.LruCache
import androidx.annotation.RequiresApi
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.eastcom.harup.adapter.HomeRecyclerViewAdapter
import com.eastcom.harup.view.activity.BaseActivity
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.IoScheduler
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : BaseActivity() {

    private val TAG = javaClass.name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.setOnMenuItemClickListener{
            when(it.itemId){
            }
            true
        }
        setSupportActionBar(Toolbar(this))
//        setSupportActionBar(toolbar)
        init()
    }

    @SuppressLint("CheckResult")
    private fun init(){
        var data = arrayOf("HorizontalScrollView","RemoteView","ShapeDrawable","OtherDrawable"
            ,"Animation","Animation Activity","Animator","Window","ImageLoader",
            "IPC","Fmod","Sound Change","FFmpeg","Posix","Bitmap","HotFix","ChangeTheme","hello","hello","hello","hello","hello").toList()
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = HomeRecyclerViewAdapter(data)
        var observable = Observable.create(ObservableOnSubscribe<Int> {
            it.onNext(1);
        })
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

        var arr : ArrayList<Int>  = ArrayList()
        var just = Observable.fromArray(arr)
        var empty = Observable.empty<Int>()

        var observer = object: Observer<Int> {
            override fun onComplete() {
                Log.d(TAG,"onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG,"onSubscribe")
                d.dispose();
            }

            override fun onNext(t: Int) {
                Log.d(TAG,"onNext")
            }

            override fun onError(e: Throwable) {
                Log.d(TAG,"onError")
            }
        }
        observable.subscribe(observer)
        observable.filter {
            it > 3;
        }.subscribe {
            Log.d(TAG,it.toString())
        }


    }

}

