package com.basemosama.movies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.basemosama.movies.data.Genre
import com.basemosama.movies.data.Movie
import com.basemosama.movies.data.model.details.*
import com.basemosama.movies.data.model.explore.ExploreInfo
import com.basemosama.movies.data.model.explore.ExploreMovieCrossRef
import com.basemosama.movies.data.model.search.RecentMovie
import com.basemosama.movies.data.model.search.RecentSearch
import com.basemosama.movies.database.dao.ExploreDao
import com.basemosama.movies.database.dao.MovieDao
import com.basemosama.movies.database.dao.MovieRemoteKeyDao
import com.basemosama.movies.database.dao.RecentDao
import com.basemosama.movies.database.typeconverters.DateTypeConverter
import com.basemosama.movies.database.typeconverters.LongArrayTypeConverter
import com.basemosama.movies.pagination.MovieRemoteKey

@Database(
    entities = [Movie::class, MovieRemoteKey::class,
        ExploreInfo::class, ExploreMovieCrossRef::class,
        RecentMovie::class, RecentSearch::class,
        Genre::class,GenreMovieCrossRef::class,
        SimilarMovieCrossRef::class, RecommendedMovieCrossRef::class,
        Artist::class, MovieCastCrossRef::class,
        Video::class,MovieVideoCrossRef::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTypeConverter::class, LongArrayTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    abstract fun movieRemoteKeyDao(): MovieRemoteKeyDao

    abstract fun exploreDao(): ExploreDao

    abstract fun recentDao(): RecentDao
}