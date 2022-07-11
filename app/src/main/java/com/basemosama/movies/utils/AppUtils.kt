package com.basemosama.movies.utils

import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.basemosama.movies.data.model.SortOrder
import com.basemosama.movies.data.model.explore.ExploreInfo
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import java.lang.reflect.Type
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

inline fun SearchView.onQueryTextChanged(crossinline listener: (String) -> Unit) {
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(query: String?): Boolean {
            listener(query.orEmpty())
            return true
        }

    })
}


fun <T> Fragment.repeatOnLifeCycle(flow: Flow<T>, collector: suspend (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collector)
        }
    }
}


object DateDeserializer : JsonDeserializer<Date> {
    val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    override fun deserialize(
        json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?
    ): Date? {
        return try {
            dateFormat.parse(json!!.asString) as Date
        } catch (e: ParseException) {
            null
        }
    }

}


fun getExploreData(): List<ExploreInfo> {
    return listOf(
        ExploreInfo(
            1,
            "Popular",
            SortOrder.POPULAR
        ), ExploreInfo(
            2,
            "Top Rated",
            SortOrder.TOP_RATED
        ), ExploreInfo(
            3,
            "Upcoming",
            SortOrder.UPCOMING
        ), ExploreInfo(
            5,
            "Now Playing",
            SortOrder.NOW_PLAYING
        ), ExploreInfo(
            4,
            "Trending",
            SortOrder.TRENDING
        ), ExploreInfo(
            6,
            "Sorted By Title ASC",
            SortOrder.BY_TITLE_ASC
        ), ExploreInfo(
            7,
            "Sorted By Title DESC",
            SortOrder.BY_TITLE_DESC
        )
    )
}
