package com.basemosama.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.data.Movie
import com.basemosama.movies.databinding.ItemMovieBinding

class MovieAdapter(private val movieClickListener: MovieClickListener) : ListAdapter<Movie?, MovieAdapter.MovieViewHolder>(diffUtil) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder.from(parent,movieClickListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)

    }


    class MovieViewHolder(private val movieBinding: ItemMovieBinding,private val movieClickListener: MovieClickListener) :
        RecyclerView.ViewHolder(movieBinding.root) , View.OnClickListener{

        init {
            movieBinding.root.setOnClickListener(this)
        }

        fun bind(movie: Movie?)  {
            movie.let { movieBinding.movie = movie }
        }

        companion object {
            fun from(parent: ViewGroup, movieClickListener: MovieClickListener): MovieViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val movieBinding = ItemMovieBinding.inflate(inflater, parent, false)
                return MovieViewHolder(movieBinding,movieClickListener)
            }
        }

        override fun onClick(p0: View?) {
            val  movie = movieBinding.movie
            movieClickListener.onMovieClickListener(movie)
        }
    }
}
val diffUtil = object : DiffUtil.ItemCallback<Movie?>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }

}

interface MovieClickListener{
    fun onMovieClickListener(movie: Movie?)
}