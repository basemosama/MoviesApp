package com.basemosama.movies.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*


@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @ColumnInfo(name = "movieId")
    var id:Long,
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
    var releaseDate: Date?



) {
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