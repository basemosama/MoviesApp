package com.basemosama.movies.data.model.details

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.basemosama.movies.data.model.Movie

class MovieDetails(
    @Embedded
    var movie: Movie?,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "id",
        associateBy = Junction(GenreMovieCrossRef::class, "movieId", "genreId")
    )
    var genres: List<Genre>?,
    @Relation(
        parentColumn = "movieId",
        entityColumn = "id",
        associateBy = Junction(MovieCastCrossRef::class, "movieId", "artistId")
    )
    var cast: List<Artist>?,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "id",
        associateBy = Junction(MovieVideoCrossRef::class, "movieId", "videoId")
    )
    var videos: List<Video>?,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "movieId",
        associateBy = Junction(SimilarMovieCrossRef::class, "parentId", "movieId")
    )
    var similarMovies: List<Movie>?,

    @Relation(
        parentColumn = "movieId",
        entityColumn = "movieId",
        associateBy = Junction(RecommendedMovieCrossRef::class, "parentId", "movieId")
    )
    var recommendedMovies: List<Movie>?

)