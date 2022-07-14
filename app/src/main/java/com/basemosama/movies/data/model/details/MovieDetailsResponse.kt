package com.basemosama.movies.data.model.details

import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.network.PagedResponse
import com.google.gson.annotations.SerializedName
import java.util.*

//Api Response for Movie Details
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


data class Credits(
    val cast: List<Artist>?,
    val crew: List<Crew>?
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