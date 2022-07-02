package com.basemosama.movies.database

import androidx.paging.PagingSource
import androidx.room.*
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.SortType
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    fun getSortedMovies(sortType: SortType, searchQuery: String) : Flow<List<Movie>> =
        when(sortType){
            SortType.ASC ->  getSortedMoviesASC(searchQuery)
            SortType.DESC -> getSortedMoviesDESC(searchQuery)
            SortType.DEFAULT -> getMovies()
        }

    fun getPagedMovies(sortType: SortType, searchQuery: String) : PagingSource<Int,Movie> =
        when(sortType){
            SortType.ASC ->  getPagedSortedMoviesASC(searchQuery)
            SortType.DESC -> getPagedSortedMoviesDESC(searchQuery)
            SortType.DEFAULT -> getDefaultPagedMovies()
        }


    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun getMovies(): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%'" +
            " OR originalTitle LIKE :search" +
            " ORDER BY title ASC")
    fun getSortedMoviesASC(search:String): Flow<List<Movie>>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%'" +
            " OR originalTitle LIKE :search" +
            " ORDER BY title DESC")
    fun getSortedMoviesDESC(search:String): Flow<List<Movie>>





    @Transaction
    @Query("SELECT * FROM movies" +
            " ORDER BY popularity DESC")
    fun getDefaultPagedMovies(): PagingSource<Int,Movie>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%'" +
            " OR originalTitle LIKE :search" +
            " ORDER BY title ASC")
    fun getPagedSortedMoviesASC(search:String): PagingSource<Int,Movie>

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :search || '%'" +
            " OR originalTitle LIKE :search" +
            " ORDER BY title DESC")
    fun getPagedSortedMoviesDESC(search:String): PagingSource<Int,Movie>



    @Query("SELECT * FROM movies WHERE id = :id")
    fun getMovieById(id: Int): Flow<Movie>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<Movie>)

    @Query("DELETE FROM movies")
    fun deleteAllMovies()

    @Query("DELETE FROM movies WHERE id = :id")
    fun deleteMovieById(id: Int)

}