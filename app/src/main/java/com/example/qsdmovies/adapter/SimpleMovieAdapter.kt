package com.example.qsdmovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.qsdmovies.databinding.MovieItemBinding
import com.example.qsdmovies.models.Movie
import com.example.qsdmovies.util.Constants

class SimpleMovieAdapter(
    var list: List<Movie>,
    val onMovieTap: (Movie) -> Unit,
    val onLikeTap: (Movie) -> Unit
) : Adapter<SimpleMovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindMovie(posterPath: Movie) {
            Glide.with(itemView).load((Constants.IMAGE_BASE + posterPath.poster_path))
                .into(binding.moviePoster)
            binding.tvName.isVisible = false
            binding.moviePoster.setOnClickListener {
                onMovieTap.invoke(posterPath)
            }

            binding.checkBox.isChecked = posterPath.favorite

            binding.checkBox.setOnClickListener {
                posterPath.favorite = !posterPath.favorite
                onLikeTap.invoke(posterPath)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SimpleMovieAdapter.MovieViewHolder {
        return MovieViewHolder(
            MovieItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(list[position])
    }

    override fun getItemCount() = list.size

    fun setMovies(list: List<Movie>) {
        this.list = list
        notifyDataSetChanged()
    }
}