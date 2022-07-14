package com.basemosama.movies.data.model.details

import androidx.room.Entity

//Movie having many to many relationship with Genre, similar, recommended movies

@Entity(tableName = "genre_movies_cross_ref_table", primaryKeys = ["genreId", "movieId"])
data class GenreMovieCrossRef(
    val movieId: Long,
    val genreId: Long
)

@Entity(tableName = "similar_movies_cross_ref_table", primaryKeys = ["parentId", "movieId"])
data class SimilarMovieCrossRef(
    val parentId: Long,
    val movieId: Long
)


@Entity(tableName = "recommended_movies_cross_ref_table", primaryKeys = ["parentId", "movieId"])
data class RecommendedMovieCrossRef(
    val parentId: Long,
    val movieId: Long
)

@Entity(tableName = "movie_cast_cross_ref_table", primaryKeys = ["artistId", "movieId"])
data class MovieCastCrossRef(
    val movieId: Long,
    val artistId: Long
)

@Entity(tableName = "movie_videos_cross_ref_table", primaryKeys = ["videoId", "movieId"])
data class MovieVideoCrossRef(
    val movieId: Long,
    val videoId: String
)



