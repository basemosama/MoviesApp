package com.basemosama.movies.data.model.details

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.basemosama.movies.data.Genre
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.network.PagedResponse
import com.google.gson.annotations.SerializedName
import java.util.*

class MovieDetailsResponse(
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
    @SerializedName("release_date")
    var releaseDate: Date?,
    var genres: List<Genre>?,
    var credits: Credits?,
    var videos: VideosResponse?,
    var similar: PagedResponse<Movie>?,
    var recommendations: PagedResponse<Movie>?

)





data class VideosResponse(
    val results: List<Video>
)

@Entity(tableName = "videos")
data class Video(
    @PrimaryKey
    val id: String,
    val iso_3166_1: String,
    val iso_639_1: String,
    val key: String,
    val name: String,
    val official: Boolean,
    val published_at: String,
    val site: String,
    val size: Int,
    val type: String
)





data class Credits(
    val cast: List<Artist>?,
    val crew: List<Crew>?
)

@Entity(tableName = "cast")
data class Artist(
    @PrimaryKey
    val id: Long,
    val adult: Boolean,
    val cast_id: Int,
    val character: String,
    val credit_id: String,
    val gender: Int?,
    val known_for_department: String,
    val name: String,
    val order: Int,
    val original_name: String,
    val popularity: Double,
    val profile_path: String?
)



data class Crew(
    val adult: Boolean,
    val credit_id: String,
    val department: String,
    val gender: Int,
    val id: Int,
    val job: String,
    val known_for_department: String,
    val name: String,
    val original_name: String,
    val popularity: Double,
    val profile_path: String
)