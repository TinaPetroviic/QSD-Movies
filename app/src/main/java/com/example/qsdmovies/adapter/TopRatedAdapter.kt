package com.example.qsdmovies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qsdmovies.R
import com.example.qsdmovies.models.TopRated
import kotlinx.android.synthetic.main.movie_item.view.*

class TopRatedAdapter(
    private val toprated: List<TopRated>
) : RecyclerView.Adapter<TopRatedAdapter.TopRatedViewHolder>() {

    class TopRatedViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
        fun bindTopRated(toprated: TopRated) {
            Glide.with(itemView).load(IMAGE_BASE + toprated.poster).into(itemView.movie_poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopRatedViewHolder {
        return TopRatedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TopRatedViewHolder, position: Int) {
        holder.bindTopRated(toprated.get(position))
    }

    override fun getItemCount(): Int = toprated.size
}