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
fun ImageView.setImage(path: String?, type: ImageType) {

    if (!path.isNullOrEmpty()) {
        val width = type.width
        val url = BASE_IMAGE_URL + width + path
        Glide.with(this).load(url)
            .fallback(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .into(this)
    }

}

@BindingAdapter("sliderImage", "type", requireAll = true)
fun ImageView.setSliderImage(path: String?, type: ImageType) {

    if (!path.isNullOrEmpty()) {
        val width = type.width
        val url = BASE_IMAGE_URL + width + path
        Glide.with(this).load(url)
            .fallback(R.drawable.ic_placeholder)
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .into(this)
    }
}

@BindingAdapter("image", "type", requireAll = true)
fun ImageView.setMovieImage(movie: Movie?, type: ImageType) {
    movie?.posterPath?.let { path ->
        val width = type.width
        val url = BASE_IMAGE_URL + width + path

        Glide.with(this).load(url)
            .error(R.drawable.ic_placeholder)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop().into(this)
    }
}

@BindingAdapter("text")
fun TextView.setText(_text: String?) {
    text = _text ?: ""
}

@BindingAdapter("year")
fun TextView.setYear(movie: Movie?) {
    movie?.releaseDate?.let {
        val date = movie.releaseDate?.let {
            SimpleDateFormat("yyyy", Locale.US).format(it)
        }
        text = date ?: ""
    }
}

@BindingAdapter("date")
fun TextView.setDate(movie: Movie?) {
    movie?.releaseDate?.let {
        val date = movie.releaseDate?.let {
            SimpleDateFormat("yyyy-mm-dd", Locale.US).format(it)
        }
        text = date
    }
}

@BindingAdapter("runtime")
fun TextView.setRuntime(runtime: Long?) {
    var runtimeText = "00h"
    runtime?.let {
        val hour = runtime / 60
        val min = runtime % 60
        runtimeText = "${hour}h ${min}m"
    }
    text = runtimeText
}

@BindingAdapter("rating")
fun TextView.setRating(movie: Movie?) {
    var ratingText ="0"
    movie?.voteAverage?.let {
        ratingText = "$it"
    }
    text = ratingText
}



