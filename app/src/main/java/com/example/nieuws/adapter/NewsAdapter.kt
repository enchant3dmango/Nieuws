package com.example.nieuws.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.nieuws.R

class NewsAdapter(
    private var titles: List<String>,
    private var descriptions: List<String>,
    private var authors: List<String>,
    private var links: List<String>,
    private var images: List<String>

    ) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemTitle: TextView = itemView.findViewById(R.id.tv_title)
        val itemAuthor: TextView = itemView.findViewById(R.id.tv_author)
        val itemDescription: TextView = itemView.findViewById(R.id.tv_description)
        val itemImage: ImageView = itemView.findViewById(R.id.iv_image)

        init {
            itemView.setOnClickListener {
                val position: Int = adapterPosition

                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(links[position])
                startActivity(itemView.context, i, null)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        return ViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = titles.size

    override fun onBindViewHolder(holder: NewsAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemDescription.text = descriptions[position]
        holder.itemAuthor.text = authors[position]


        holder.itemDescription.maxLines = 2

        Glide.with(holder.itemImage)
            .load(images[position])
            .into(holder.itemImage)
    }
}