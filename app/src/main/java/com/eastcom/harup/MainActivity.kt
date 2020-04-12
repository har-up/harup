package com.eastcom.harup

import android.content.SyncStatusObserver
import android.media.MediaPlayer
import android.os.Bundle
import android.util.LruCache
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import com.eastcom.harup.adapter.HomeRecyclerViewAdapter
import com.eastcom.harup.view.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbar.setOnMenuItemClickListener{
            when(it.itemId){
            }
        }
        setSupportActionBar(Toolbar(this))
//        setSupportActionBar(toolbar)
        init()
    }

    private fun init(){
        var data = arrayOf("HorizontalScrollView","RemoteView","ShapeDrawable","OtherDrawable"
            ,"Animation","Animation Activity","Animator","Window","ImageLoader",
            "IPC","Fmod","Sound Change","FFmpeg").toList()
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = HomeRecyclerViewAdapter(data)
    }



}
