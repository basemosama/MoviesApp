package com.basemosama.movies.di

import android.content.Context
import androidx.room.Room
import com.basemosama.movies.database.AppDatabase
import com.basemosama.movies.database.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "movies"

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .createFromAsset("movies.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao {
        return database.movieDao()
    }

    @Singleton
    @Provides
    fun provideRemoteMovieKeyDao(database: AppDatabase): MovieRemoteKeyDao {
        return database.movieRemoteKeyDao()
    }


    @Singleton
    @Provides
    fun provideExploreDao(database: AppDatabase): ExploreDao {
        return database.exploreDao()
    }

    @Singleton
    @Provides
    fun provideRecentDao(database: AppDatabase): RecentDao {
        return database.recentDao()
    }


    @Singleton
    @Provides
    fun provideMovieDetailsDao(database: AppDatabase): MovieDetailsDao {
        return database.detailsDao()
    }
}

