package com.basemosama.movies

import android.app.Application
import com.basemosama.movies.database.AppDatabase

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val database :AppDatabase by lazy { AppDatabase.getDatabase(this) }

    }
}