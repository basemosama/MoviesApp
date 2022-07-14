package com.basemosama.movies.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.databinding.ItemSliderBinding

class SliderAdapter(var movies: List<Movie?>? = emptyList(), private val sliderItemClickListener: SliderItemClickListener) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    interface SliderItemClickListener {
        fun onSliderMovieClickListener(movie: Movie?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder.from(parent,sliderItemClickListener)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(movies?.get(position))
    }

    override fun getItemCount(): Int {
        return movies?.size ?:0
    }

    class SliderViewHolder(private val itemSliderBinding: ItemSliderBinding, private val itemClickListener: SliderItemClickListener) :
        RecyclerView.ViewHolder(itemSliderBinding.root), View.OnClickListener {

         init{
            itemSliderBinding.root.setOnClickListener(this)
        }

        fun bind(movie: Movie?) {
            movie?.let {
                itemSliderBinding.movie = movie
            }
        }

        companion object {
            fun from(parent: ViewGroup,itemClickListener: SliderItemClickListener): SliderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val itemSliderBinding = ItemSliderBinding.inflate(layoutInflater, parent, false)
                return SliderViewHolder(itemSliderBinding,itemClickListener)
            }
        }

        override fun onClick(p0: View?) {
            val movie = itemSliderBinding.movie
            itemClickListener.onSliderMovieClickListener(movie)
        }


    }

    fun updateList(newMovies: List<Movie?>?) {
        movies = newMovies
        notifyDataSetChanged()
    }

}