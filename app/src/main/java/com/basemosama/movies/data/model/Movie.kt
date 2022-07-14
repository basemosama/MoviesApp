package com.basemosama.movies.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.basemosama.movies.data.model.details.MovieDetailsResponse
import com.google.gson.annotations.SerializedName
import java.util.*


@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "movieId")
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
    var voteAverage: Double?,
    @SerializedName("vote_count")
    var voteCount: Long?,
    var popularity: Double?,
    @SerializedName("release_date")
    var releaseDate: Date?,
    var lastUpdatedAt: Date? = null

) {


    constructor(details: MovieDetailsResponse) : this(
        details.id,
        details.title,
        details.originalTitle,
        details.overview,
        details.adult,
        details.backdropPath,
        details.posterPath,
        details.budget,
        details.revenue,
        details.runtime,
        details.status,
        details.tagline,
        details.voteAverage,
        details.voteCount,
        details.popularity,
        details.releaseDate,
        Date())


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Movie

        if (id != other.id) return false
        if (title != other.title) return false
        if (originalTitle != other.originalTitle) return false
        if (overview != other.overview) return false
        if (adult != other.adult) return false
        if (backdropPath != other.backdropPath) return false
        if (posterPath != other.posterPath) return false
        if (budget != other.budget) return false
        if (revenue != other.revenue) return false
        if (runtime != other.runtime) return false
        if (status != other.status) return false
        if (tagline != other.tagline) return false
        if (voteAverage != other.voteAverage) return false
        if (voteCount != other.voteCount) return false
        if (popularity != other.popularity) return false
        if (releaseDate != other.releaseDate) return false

        return true
    }
}


class ProductionCompany(id: Long, logoPath: String?, name: String?, originCountry: String?)

class ProductionCountry(@SerializedName("iso_3166_1") var code: String?, name: String?)


class SpokenLanguage(englishName: String?, code: String?, name: String?)


