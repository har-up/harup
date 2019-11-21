package com.eastcom.harup.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.eastcom.harup.R
import com.eastcom.harup.view.activity.HorizontalScrollViewActivity
import com.eastcom.harup.view.activity.RemoteViewActivity
import com.eastcom.harup.view.activity.ShapeDrawableActivity

class HomeRecyclerViewAdapter(var datas: List<String>) : RecyclerView.Adapter<HomeItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.item_function, parent, false)
        return HomeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: HomeItemViewHolder, position: Int) {
        holder.infoText.text = datas[position]
        holder.cardView.setOnClickListener {
            var intent : Intent? = null

            when(datas[position]){
                "HorizontalScrollView" -> intent = Intent(holder.itemView.context,HorizontalScrollViewActivity::class.java)
                "RemoteView" -> intent = Intent(holder.itemView.context,RemoteViewActivity::class.java)
                "ShapeDrawable" -> intent = Intent(holder.itemView.context,ShapeDrawableActivity::class.java)
            }
            if(intent != null) holder.itemView.context.startActivity(intent)
        }
    }
}



class HomeItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    var infoText: TextView = itemView.findViewById(R.id.info_text)
    var cardView: CardView = itemView.findViewById(R.id.card_view)
}