package com.example.qsdmovies.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.bumptech.glide.Glide
import com.example.qsdmovies.databinding.MovieItemBinding
import com.example.qsdmovies.models.Cast
import com.example.qsdmovies.util.Constants

class CastAdapter(
    private var list: List<Cast>,
    private val onCrewTap: (Cast) -> Unit
) : Adapter<CastAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(val binding: MovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindMovie(posterPath: Cast) {
            Glide.with(itemView).load((Constants.IMAGE_BASE + posterPath.profilePath))
                .into(binding.moviePoster)
            binding.checkBox.isVisible = false
            binding.moviePoster.setOnClickListener {
                onCrewTap.invoke(posterPath)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MovieViewHolder(
        MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(list[position])
    }

    override fun getItemCount() = list.size
}