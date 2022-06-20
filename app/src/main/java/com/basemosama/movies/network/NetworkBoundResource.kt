package com.basemosama.movies.network

import com.basemosama.movies.utils.Resource
import kotlinx.coroutines.flow.*

// Helper class to cache the network response and deliver it to the UI
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { Unit },
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    emit(Resource.Loading(null))
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading<ResultType>(data))
        try {
            saveFetchResult(fetch())
            query().map { Resource.Success(it) }
        } catch (throwable: Throwable) {
            onFetchFailed(throwable)
            query().map { Resource.Error(throwable.message ?: "Unknown Error", it) }
        }
    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}

