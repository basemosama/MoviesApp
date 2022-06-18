package com.basemosama.movies.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.basemosama.movies.data.Movie
import com.basemosama.movies.database.typeconverters.DateTypeConverter
import com.basemosama.movies.database.typeconverters.LongArrayTypeConverter

@Database(entities = [Movie::class], version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter::class,LongArrayTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun movieDao(): MovieDao

}