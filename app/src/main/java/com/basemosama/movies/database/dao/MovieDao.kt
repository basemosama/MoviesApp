package com.basemosama.movies.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.model.details.*
import com.basemosama.movies.data.model.utils.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies ORDER BY movieId DESC")
    fun getMovies(): Flow<List<Movie>>

    @Transaction
    @Query(
        "SELECT * FROM movies" +
                " INNER JOIN movie_remote_key_table on movies.movieId = movie_remote_key_table.movieId" +
                " WHERE searchQuery = :searchQuery AND sortOrder = :sortOrder" +
                " ORDER BY movie_remote_key_table.id"
    )
    fun getPagedMovies(sortOrder: SortOrder, searchQuery: String): PagingSource<Int, Movie>

    @Query("SELECT * FROM movies WHERE movieId = :id")
    fun getMovieById(id: Long): Flow<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Query("DELETE FROM movies WHERE movieId = :id")
    fun deleteMovieById(id: Int)

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
        val videosCrossRef = videos?.map { video ->
            MovieVideoCrossRef(currentMovieId, video.id)
        }

        val similarMovies = details.similar?.results
        val similarMovieCrossRef = similarMovies?.map { movie ->
            SimilarMovieCrossRef(currentMovieId, movie.id)
        }

        val recommendedMovies = details.recommendations?.results
        val recommendedMovieCrossRef = recommendedMovies?.map { movie ->
            RecommendedMovieCrossRef(currentMovieId, movie.id)
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