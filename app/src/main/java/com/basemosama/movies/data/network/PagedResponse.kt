package com.basemosama.movies.data.network

import com.google.gson.annotations.SerializedName

class PagedResponse<T> (
    val page:Int,
    val results:List<T>?,
    @SerializedName("total_pages")
    val totalPages :Int,
    @SerializedName("total_results")
    val totalResults :Long
)