package com.basemosama.movies.data

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
    @SerializedName("genre_ids") var genreIds: LongArray?,
    @SerializedName("release_date")
    var releaseDate: Date?,
    var lastUpdatedAt: Date?


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
        null,
        details.releaseDate,
        Date()
    )


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
        if (genreIds != null) {
            if (other.genreIds == null) return false
            if (!genreIds.contentEquals(other.genreIds)) return false
        } else if (other.genreIds != null) return false
        if (releaseDate != other.releaseDate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + (originalTitle?.hashCode() ?: 0)
        result = 31 * result + (overview?.hashCode() ?: 0)
        result = 31 * result + adult.hashCode()
        result = 31 * result + (backdropPath?.hashCode() ?: 0)
        result = 31 * result + (posterPath?.hashCode() ?: 0)
        result = 31 * result + (budget?.hashCode() ?: 0)
        result = 31 * result + (revenue?.hashCode() ?: 0)
        result = 31 * result + (runtime?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (tagline?.hashCode() ?: 0)
        result = 31 * result + (voteAverage?.hashCode() ?: 0)
        result = 31 * result + (voteCount?.hashCode() ?: 0)
        result = 31 * result + (popularity?.hashCode() ?: 0)
        result = 31 * result + (genreIds?.contentHashCode() ?: 0)
        result = 31 * result + (releaseDate?.hashCode() ?: 0)
        return result
    }
}

@Entity(tableName = "genres")
data class Genre(
    @PrimaryKey
    var id: Long,
    var name: String?
)

class ProductionCompany(id: Long, logoPath: String?, name: String?, originCountry: String?)

class ProductionCountry(@SerializedName("iso_3166_1") var code: String?, name: String?)


class SpokenLanguage(englishName: String?, code: String?, name: String?)

enum class ImageType(val width: String) {
    BACKDROP_SMALL("w300"),
    BACKDROP_MEDIUM("w780"),
    BACKDROP_LARGE("w1280"),
    LOGO_SMALL("w45"),
    LOGO_MEDIUM("w92"),
    LOGO_LARGE("w154"),
    POSTER_XSMALL("w92"),
    POSTER_SMALL("w154"),
    POSTER_MEDIUM("w185"),
    POSTER_LARGE("w342"),
    POSTER_XLARGE("w500"),
    POSTER_XXLARGE("w780"),
    PROFILE_SMALL("w45"),
    PROFILE_MEDIUM("w185"),
    PROFILE_LARGE("w632"),
    STILL_SMALL("w92"),
    STILL_MEDIUM("w185"),
    STILL_LARGE("w300"),

}


