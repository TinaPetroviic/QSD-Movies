package com.example.qsdmovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.qsdmovies.databinding.MovieItemBinding
import com.example.qsdmovies.util.Constants

class MovieAdapter(private val moviePosters: List<String?>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    class MovieViewHolder(val binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindMovie(posterPath: String?) {
            Glide.with(itemView).load(Constants.IMAGE_BASE + posterPath).into(binding.moviePoster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(moviePosters[position])
    }

    override fun getItemCount(): Int = moviePosters.size
}