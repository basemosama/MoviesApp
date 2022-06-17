package com.basemosama.movies.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*


@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    var id: Long,
    var title: String,
    @SerializedName("original_title")
    var originalTitle: String?,
    var overview: String?,
    var adult: Boolean,
    @SerializedName("backdrop_path")
    var backdropPath: String?,
    @SerializedName("poster_path")
    var posterPath: String?,
    var budget: Long?,
    var revenue: Long?,
    var runtime: Long?,
    var status: String?,
    var tagline: String?,
    @SerializedName("vote_average")
    var vote_average: Double?,
   @SerializedName("genre_ids") var genreIds: LongArray?,
    @SerializedName("release_date")
    var releaseDate: Date?

)

data class Genre(var id: Int?, var name: String?)

class ProductionCompany(id: Long, logoPath: String?, name: String?, originCountry: String?)

class ProductionCountry(@SerializedName("iso_3166_1") var code: String?, name: String?)


class SpokenLanguage(englishName: String?, code: String?, name: String?)

enum class ImageType {
    BACKDROP,
    LOGO,
    POSTER,
    PROFILE,
    STILL
}