package com.basemosama.movies.database.dao

import androidx.room.*
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.model.details.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDetailsDao {


    //Details
    @Transaction
    @Query("SELECT * FROM movies WHERE movieId = :id")
    fun getMovieDetailsById(id: Long): Flow<MovieDetails>


    @Transaction
    fun insertMovieDetails(details: MovieDetailsResponse) {
        val movie = Movie(details)
        val currentMovieId = movie.id

        val genres = details.genres
        val genresCrossRef: List<GenreMovieCrossRef>? = genres?.map { genre ->
            GenreMovieCrossRef(currentMovieId, genre.id)
        }

        val cast: List<Artist>? = details.credits?.cast
        val castCrossRef = cast?.map { artist ->
            MovieCastCrossRef(currentMovieId, artist.id)
        }

        val videos:List<Video>? = details.videos?.results
        val videosCrossRef = videos?.map {
            MovieVideoCrossRef(currentMovieId, it.id)
        }

        val similarMovies = details.similar?.results
        val similarMovieCrossRef = similarMovies?.map {
            SimilarMovieCrossRef(currentMovieId, it.id)
        }

        val recommendedMovies = details.recommendations?.results
        val recommendedMovieCrossRef = recommendedMovies?.map {
            RecommendedMovieCrossRef(currentMovieId, it.id)
        }

        insertMovie(movie)
        genres?.let { insertGenres(it) }
        genresCrossRef?.let { insertGenresCrossRef(it) }
        cast?.let { insertCast(it) }
        castCrossRef?.let { insertCastCrossRef(it) }
        videos?.let { insertVideos(it) }
        videosCrossRef?.let { insertVideoCrossRef(it) }
        similarMovies?.let { insertSimilarMovies(it) }
        similarMovieCrossRef?.let { insertSimilarMoviesCrossRef(it) }
        recommendedMovies?.let { insertRecommendedMovies(it) }
        recommendedMovieCrossRef?.let { insertRecommendedMoviesCrossRef(it) }



    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenres(genres: List<Genre>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenresCrossRef(genresCrossRef: List<GenreMovieCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCast(cast: List<Artist>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCastCrossRef(castCrossRef: List<MovieCastCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideos(videos: List<Video>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertVideoCrossRef(videosCrossRef: List<MovieVideoCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSimilarMovies(similarMovies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSimilarMoviesCrossRef(similarMovieCrossRef: List<SimilarMovieCrossRef>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecommendedMovies(recommendedMovies: List<Movie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRecommendedMoviesCrossRef(recommendedMovieCrossRef: List<RecommendedMovieCrossRef>)


}