package com.eastcom.harup.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.eastcom.harup.R
import com.example.lib_imageloader.ImageLoader

class ImageAdapter(datas: List<String>) : RecyclerView.Adapter<ImageViewHolder>() {
    val datas = datas
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image,parent,false))
    }

    override fun getItemCount(): Int {
        return datas.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        ImageLoader.build(holder.image.context)?.bindBitmap(datas[position],holder.image,300,300)
    }
}

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val image: ImageView = itemView.findViewById(R.id.item_imageView)
}