package com.example.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.databinding.ItemMovieBinding
import com.example.movieapp.network.Movie

class MovieAdapter(
    private val movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit,
    private val onMovieLongClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    inner class MovieViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            binding.tvMovieTitle.text = movie.title


            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500${movie.poster_path}")
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(binding.ivMoviePoster)


            binding.root.setOnClickListener {
                onMovieClick(movie)
            }


            binding.root.setOnLongClickListener {
                onMovieLongClick(movie)
                true
            }
        }
    }
}