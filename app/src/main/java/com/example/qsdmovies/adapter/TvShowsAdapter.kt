package com.example.qsdmovies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qsdmovies.R
import com.example.qsdmovies.models.TvShows
import kotlinx.android.synthetic.main.movie_item.view.*

class TvShowsAdapter(
    private val tvshows: List<TvShows>
) : RecyclerView.Adapter<TvShowsAdapter.TvShowsViewHolder>() {

    class TvShowsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"
        fun bindTvShows(tvshows: TvShows) {
            Glide.with(itemView).load(IMAGE_BASE + tvshows.poster).into(itemView.movie_poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvShowsViewHolder {
        return TvShowsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TvShowsViewHolder, position: Int) {
        holder.bindTvShows(tvshows.get(position))
    }

    override fun getItemCount(): Int = tvshows.size
}