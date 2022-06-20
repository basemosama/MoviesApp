package com.basemosama.movies.network

import com.basemosama.movies.network.utils.NetworkResult
import com.basemosama.movies.utils.Resource
import kotlinx.coroutines.flow.*

// Helper class to cache the network response and deliver it to the UI
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline fetch: suspend () -> NetworkResult<RequestType>,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { Unit },
    crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
    emit(Resource.Loading(null))
    val data = query().first()

    val flow = if (shouldFetch(data)) {
        emit(Resource.Loading<ResultType>(data))

            when (val networkResult = fetch()) {
                is NetworkResult.Success -> {
                    saveFetchResult(networkResult.data)
                    query().map { Resource.Success(it) }
                }
                is NetworkResult.Error-> {
                    onFetchFailed(Throwable(networkResult.errorMessage))
                    query().map { Resource.Error(networkResult.errorMessage, it) }
                }
            }

    } else {
        query().map { Resource.Success(it) }
    }

    emitAll(flow)
}

