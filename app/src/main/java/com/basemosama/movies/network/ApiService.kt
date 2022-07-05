package com.basemosama.movies.network

import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.network.PagedResponse
import com.basemosama.movies.network.utils.NetworkResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val BASE_URL: String = "https://api.themoviedb.org/3/"
    }



    @GET("search/movie?include_adult=false")
    suspend fun searchMovies(
        @Query("page") page: Int,
        @Query("query") query: String,
    ): NetworkResult<PagedResponse<Movie>>


    @GET("discover/movie")
    suspend fun getDiscoveredMovies(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String,
    ): NetworkResult<PagedResponse<Movie>>


    @GET("trending/movie/week")
    suspend fun getTrendingMovies(
        @Query("page") page: Int,
    ): NetworkResult<PagedResponse<Movie>>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("page") page: Int,
    ): NetworkResult<PagedResponse<Movie>>

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("page") page: Int,
    ): NetworkResult<PagedResponse<Movie>>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page: Int,
    ): NetworkResult<PagedResponse<Movie>>

    @GET("discover/movie?sort_by=vote_average.desc&vote_count.gte=4000")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int,
    ): NetworkResult<PagedResponse<Movie>>

    @GET("discover/movie?sort_by=original_title.asc")
    suspend fun getMoviesByTitleASC(
        @Query("page") page: Int,
    ): NetworkResult<PagedResponse<Movie>>

    @GET("discover/movie?sort_by=original_title.desc")
    suspend fun getMoviesByTitleDESC(
        @Query("page") page: Int,
    ): NetworkResult<PagedResponse<Movie>>

}