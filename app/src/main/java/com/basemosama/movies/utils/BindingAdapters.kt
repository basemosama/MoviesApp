package com.basemosama.movies.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.basemosama.movies.R
import com.basemosama.movies.data.ImageType
import com.basemosama.movies.data.Movie
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"

@BindingAdapter("image", "type", requireAll = true)
fun setImage(view: ImageView, path: String?, type: ImageType) {

    if (!path.isNullOrEmpty()) {
        val width = type.width
        val url = BASE_IMAGE_URL + width + path
        Glide.with(view).load(url)
            .fallback(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .into(view)
    }

}

@BindingAdapter("sliderImage", "type", requireAll = true)
fun setSliderImage(view: ImageView, path: String?, type: ImageType) {

    val width = type.width
    val url = BASE_IMAGE_URL + width + path
    Glide.with(view).load(url)
        .fallback(R.drawable.ic_placeholder)
        .error(R.drawable.ic_placeholder)
        .placeholder(R.drawable.ic_placeholder)
        .centerCrop()
        .into(view)
}

@BindingAdapter("image", "type", requireAll = true)
fun setMovieImage(view: ImageView, movie: Movie?, type: ImageType) {
    movie?.posterPath?.let {
        val width = type.width
        val url = BASE_IMAGE_URL + width + movie.posterPath

        Glide.with(view).load(url)
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop().into(view)
    }
}

@BindingAdapter("date")
fun setDate(view: TextView, movie: Movie?) {
    movie?.releaseDate?.let {
        val date = movie.releaseDate?.let {
            SimpleDateFormat("yyyy-mm-dd", Locale.US).format(it)
        }
        view.text = date
    }
}

@BindingAdapter("runtime")
fun setRuntime(view: TextView, runtime: Long?) {
    var text = "unavailable"
    runtime?.let {
        text = "$runtime min"
    }
    view.text = text
}




