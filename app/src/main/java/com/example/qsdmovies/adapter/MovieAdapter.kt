package com.example.qsdmovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qsdmovies.databinding.MovieItemBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.util.Constants

class MovieAdapter(val onMovieTap: (Movie) -> Unit) :
    PagingDataAdapter<Movie, MovieAdapter.MovieViewHolder>(MoviesDiffCallback) {

    inner class MovieViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindMovie(posterPath: Movie) {
            Glide.with(itemView).load((Constants.IMAGE_BASE + posterPath.poster))
                .into(binding.moviePoster)

            binding.moviePoster.setOnClickListener {
                onMovieTap.invoke(posterPath)
            }
        }
    }

    private companion object {
        val MoviesDiffCallback = object : DiffUtil.ItemCallback<Movie>() {
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(getItem(position) as Movie)
    }

}