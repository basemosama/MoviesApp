package com.basemosama.movies.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.basemosama.movies.data.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManger @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }

    val preferencesFlow: Flow<SortOrder> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            SortOrder.valueOf(preferences[SORT_ORDER] ?: SortOrder.POPULAR.name)
        }


     suspend fun saveSortType(type: SortOrder) {
        dataStore.edit { preference ->
            preference[SORT_ORDER] = type.name

        }
    }




}