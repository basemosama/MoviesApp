package com.basemosama.movies.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*


@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    var  id: Long,
    var  title: String,
    @SerializedName("original_title")
    var  originalTitle: String?,
    var  overview: String?,
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    @SerializedName("poster_path")
    var posterPath: String?,
    var  budget: Long?,
    var  revenue: Long?,
    var  runtime: Long?,
    var  status: String?,
    var  tagline: String?,
    @SerializedName("vote_average")
    var  vote_average: Double?,
    @Ignore var  genres: List<Genre>?,
    @SerializedName("release_date")
    @Ignore  var  releaseDate: Date?

) {
    // need the constructor for Room to be able to create the object while ignoring the genres and releaseDate
    constructor() : this(0, "", "", "", false, "",
        "", 0, 0, 0, "", "", 0.0,
        null, null)
}
data class Genre(var id: Int?,var name: String?)

class ProductionCompany(id: Long, logoPath: String?, name: String?, originCountry: String?)

class ProductionCountry(@SerializedName("iso_3166_1") var code: String?, name: String?)


class SpokenLanguage(englishName: String?, code: String?, name: String?)

enum class ImageType{
    BACKDROP,
    LOGO,
   POSTER,
    PROFILE,
    STILL
}