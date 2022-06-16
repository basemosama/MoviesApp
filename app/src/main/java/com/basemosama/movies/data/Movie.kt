package com.basemosama.movies.data

import com.google.gson.annotations.SerializedName
import java.util.*

data class Movie(
    val id: Long?,
    val title: String?,
    @SerializedName("original_title")
    val originalTitle: String?,
    val overview: String?,
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    @SerializedName("poster_path")
    var posterPath: String?,
    val budget: Long?,
    val genres: List<Genre>?,
    @SerializedName("release_date")
    val releaseDate: Date?,
    val revenue: Long?,
    val runtime: Long?,
    val status: String?,
    val tagline: String?,
    @SerializedName("vote_average")
    val vote_average: Double?
)

class Genre(id: Int?, name: String?)

class ProductionCompany(id: Long, logoPath: String?, name: String?, originCountry: String?)

class ProductionCountry(@SerializedName("iso_3166_1") val code: String?, name: String?)


class SpokenLanguage(englishName: String?, code: String?, name: String?)

enum class ImageType{
    BACKDROP,
    LOGO,
   POSTER,
    PROFILE,
    STILL
}