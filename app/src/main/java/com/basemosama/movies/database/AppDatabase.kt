package com.basemosama.movies.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.basemosama.movies.data.Movie

@Database(entities = [Movie::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    companion object{
        private const val DATABASE_NAME = "movies"
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    DATABASE_NAME)
                    .build()
                INSTANCE = instance

                instance
            }
        }

    }

    abstract fun movieDao(): MovieDao

}