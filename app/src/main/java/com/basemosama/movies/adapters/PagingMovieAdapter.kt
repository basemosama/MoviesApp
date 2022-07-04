package com.basemosama.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.data.Movie
import com.basemosama.movies.databinding.ItemMovieBinding

class PagingMovieAdapter(private val movieClickListener: MovieClickListener)
    : PagingDataAdapter<Movie, PagingMovieAdapter.PagingMovieViewHolder>(diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
               // Timber.d("areItemsTheSame: oldItem : ${oldItem.id} (${oldItem.title}  " +
                       // " newItem : ${newItem.id} (${newItem.title} are: ${oldItem.id == newItem.id}")
                return oldItem.id == newItem.id
            }
            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
           //     Timber.d("areContentsTheSame: oldItem : ${oldItem.id} (${oldItem.title}  " +
                   //     " newItem : ${newItem.id} (${newItem.title} are: ${oldItem.id == newItem.id}")

                return oldItem == newItem
            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingMovieViewHolder {
        return PagingMovieViewHolder.from(parent,movieClickListener)
    }

    override fun onBindViewHolder(holder: PagingMovieViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)

    }


    class PagingMovieViewHolder(private val movieBinding: ItemMovieBinding,private val movieClickListener: MovieClickListener) :
        RecyclerView.ViewHolder(movieBinding.root) , View.OnClickListener{

        init {
            movieBinding.root.setOnClickListener(this)
        }

        fun bind(movie: Movie?)  {
            movie.let { movieBinding.movie = movie }
        }

        companion object {
            fun from(parent: ViewGroup, movieClickListener: MovieClickListener): PagingMovieViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val movieBinding = ItemMovieBinding.inflate(inflater, parent, false)
                return PagingMovieViewHolder(movieBinding,movieClickListener)
            }
        }

        override fun onClick(p0: View?) {
            val  movie = movieBinding.movie
            movieClickListener.onMovieClickListener(movie)
        }
    }


}

