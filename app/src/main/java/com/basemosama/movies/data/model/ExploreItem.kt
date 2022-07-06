package com.basemosama.movies.data.model

import com.basemosama.movies.data.Movie

data class ExploreItem(val title:String,val sortOrder: SortOrder, val movies:List<Movie>) {

}