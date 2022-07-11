package com.basemosama.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.data.Movie
import com.basemosama.movies.databinding.ItemExploreMovieBackdropBinding
import com.basemosama.movies.databinding.ItemExploreMovieBinding
import com.basemosama.movies.databinding.ItemExploreMovieLargeBinding

//To use in explore fragment

enum class ExploreViewType(val value: Int) {
    POSTER(1),
    POSTER_LARGE(2),
    BACKDROP(3)
}

class MovieAdapter(
    private val movieClickListener: MovieClickListener,
    var exploreViewType: ExploreViewType
) : ListAdapter<Movie?, RecyclerView.ViewHolder>(diffUtil) {

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<Movie?>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ExploreViewType.POSTER_LARGE.value -> {
                LargeMovieViewHolder.from(parent, movieClickListener)
            }
            ExploreViewType.BACKDROP.value -> {
                BackDropMovieViewHolder.from(parent, movieClickListener)
            }
            else -> {
                MovieViewHolder.from(parent, movieClickListener)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movie = getItem(position)

        when (getItemViewType(position)) {
            ExploreViewType.POSTER_LARGE.value -> {
                (holder as LargeMovieViewHolder).bind(movie)
            }
            ExploreViewType.BACKDROP.value -> {
                (holder as BackDropMovieViewHolder).bind(movie)
            }
            else -> {
                (holder as MovieViewHolder).bind(movie)
            }

        }
    }


    override fun getItemViewType(position: Int): Int {
        return exploreViewType.value
    }

    class MovieViewHolder(
        private val movieBinding: ItemExploreMovieBinding,
        private val movieClickListener: MovieClickListener
    ) :
        RecyclerView.ViewHolder(movieBinding.root), View.OnClickListener {

        init {
            movieBinding.root.setOnClickListener(this)
        }

        fun bind(movie: Movie?) {
            movie.let { movieBinding.movie = movie }
        }

        companion object {
            fun from(parent: ViewGroup, movieClickListener: MovieClickListener): MovieViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val movieBinding = ItemExploreMovieBinding.inflate(inflater, parent, false)
                return MovieViewHolder(movieBinding, movieClickListener)
            }
        }

        override fun onClick(p0: View?) {
            val movie = movieBinding.movie
            movieClickListener.onMovieClickListener(movie)
        }
    }


}

class LargeMovieViewHolder(
    private val movieBinding: ItemExploreMovieLargeBinding,
    private val movieClickListener: MovieClickListener
) :
    RecyclerView.ViewHolder(movieBinding.root), View.OnClickListener {

    init {
        movieBinding.root.setOnClickListener(this)
    }

    fun bind(movie: Movie?) {
        movie.let { movieBinding.movie = movie }
    }

    companion object {
        fun from(parent: ViewGroup, movieClickListener: MovieClickListener): LargeMovieViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val movieBinding = ItemExploreMovieLargeBinding.inflate(inflater, parent, false)
            return LargeMovieViewHolder(movieBinding, movieClickListener)
        }
    }

    override fun onClick(p0: View?) {
        val movie = movieBinding.movie
        movieClickListener.onMovieClickListener(movie)
    }
}


class BackDropMovieViewHolder(
    private val movieBinding: ItemExploreMovieBackdropBinding,
    private val movieClickListener: MovieClickListener
) :
    RecyclerView.ViewHolder(movieBinding.root), View.OnClickListener {

    init {
        movieBinding.root.setOnClickListener(this)
    }

    fun bind(movie: Movie?) {
        movie.let { movieBinding.movie = movie }
    }

    companion object {
        fun from(
            parent: ViewGroup,
            movieClickListener: MovieClickListener
        ): BackDropMovieViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val movieBinding = ItemExploreMovieBackdropBinding.inflate(inflater, parent, false)
            return BackDropMovieViewHolder(movieBinding, movieClickListener)
        }
    }

    override fun onClick(p0: View?) {
        val movie = movieBinding.movie
        movieClickListener.onMovieClickListener(movie)
    }
}




interface MovieClickListener {
    fun onMovieClickListener(movie: Movie?)
}