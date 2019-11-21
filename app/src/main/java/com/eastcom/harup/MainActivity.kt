package com.eastcom.harup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.eastcom.harup.adapter.HomeRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init(){
        var data = arrayOf("HorizontalScrollView","RemoteView","ShapeDrawable","next").toList()
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = HomeRecyclerViewAdapter(data)
    }
}
