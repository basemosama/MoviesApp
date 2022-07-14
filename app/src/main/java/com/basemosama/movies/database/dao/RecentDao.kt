package com.basemosama.movies.database.dao

import androidx.room.*
import com.basemosama.movies.data.model.Movie
import com.basemosama.movies.data.model.search.RecentSearch
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface RecentDao {

    @Query("SELECT * FROM recent_searches " +
            "WHERE query != '-1' " +
            "ORDER BY updatedAt DESC LIMIT 25")
    fun getRecentSearches(): Flow<List<RecentSearch>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearch(recentSearch: RecentSearch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecentSearches(items: List<RecentSearch>)

    @Query("DELETE FROM recent_searches")
    suspend fun deleteRecentSearches()


    //RECENT MOVIES
    @Transaction
    @Query("SELECT * FROM movies INNER JOIN recent_searches " +
            "WHERE movies.movieId = recent_searches.movieId " +
            "ORDER BY updatedAt DESC LIMIT 25")
    fun getRecentMovies(): Flow<List<Movie>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: Movie)

    @Transaction
    suspend fun insertRecentMovie(movie: Movie) {
        insertMovie(movie)
        insertRecentSearch(RecentSearch( movieId = movie.id, updatedAt = Date()))
    }

    @Transaction
    suspend fun clearRecent() {
        deleteRecentSearches()
    }

}