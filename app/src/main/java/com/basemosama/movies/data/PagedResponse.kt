package com.basemosama.movies.data

import com.google.gson.annotations.SerializedName

class PagedResponse<T> (
    val page:Long,
    val results:List<T>?,
    @SerializedName("total_pages")
    val totalPages :Long,
    @SerializedName("total_results")
    val totalResults :Long
)