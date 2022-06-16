package com.basemosama.movies.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.basemosama.movies.data.ImageType
import com.basemosama.movies.data.Movie
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

const val BASE_IMAGE_URL = "https://image.tmdb.org/t/p/"

@BindingAdapter("image", "type", requireAll = true)
fun setImage(view: ImageView, path: String?,type: ImageType) {
    path?.let {
        val width = getImageWidth(type)
        val url = BASE_IMAGE_URL+ width + path
        Glide.with(view).load(url).centerCrop().into(view)
    }

}

@BindingAdapter("image", "type", requireAll = true)
fun setMovieImage(view: ImageView, movie: Movie?, type: ImageType) {
    movie?.posterPath?.let {
        val width = getImageWidth(type)
        val url = BASE_IMAGE_URL+ width + movie.posterPath

        Glide.with(view).load(url).centerCrop().into(view)
    }
}

@BindingAdapter("date")
fun setDate(view: TextView, movie: Movie?) {
    movie?.releaseDate?.let {
        val date = SimpleDateFormat("yyyy-mm-dd", Locale.US).format(movie.releaseDate)
        view.text = date
    }
}


fun getImageWidth(type: ImageType):String = when (type) {
        ImageType.BACKDROP -> "w780"
        ImageType.LOGO -> "w92"
        ImageType.POSTER -> "w154"
        ImageType.PROFILE -> "w185"
        ImageType.STILL -> "w185"
    }
