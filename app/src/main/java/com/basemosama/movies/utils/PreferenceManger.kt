package com.basemosama.movies.utils

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.basemosama.movies.data.model.SortOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManger @Inject constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        val SORT_ORDER = stringPreferencesKey("sort_order")
        val EXPLORE_UPDATE_CURRENT_DAY = intPreferencesKey("explore_update_current_day")
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

    val explore: Flow<SortOrder> = dataStore.data
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

    suspend fun saveExploreCurrentUpdateDay(day: Int) {
        dataStore.edit { preference ->
            preference[EXPLORE_UPDATE_CURRENT_DAY] = day
        }
    }


    suspend fun getExploreLastUpdateDay(): Int {
        return dataStore.data.firstOrNull()?.get(EXPLORE_UPDATE_CURRENT_DAY) ?: -1
    }


}