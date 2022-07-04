package com.basemosama.movies.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName


@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    var id:Long,
    var title: String,
    @SerializedName("poster_path")
    var posterPath: String?,
    )
enum class ImageType {
    BACKDROP,
    LOGO,
    POSTER,
    PROFILE,
    STILL
}