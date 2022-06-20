package com.basemosama.movies.network.utils.flowAdapter

import com.basemosama.movies.network.utils.NetworkResult
import com.basemosama.movies.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ResponseCallAdapter<T>(
    private val responseType: Type
) : CallAdapter<T, Flow<Response<T>>> {
    override fun adapt(call: Call<T>): Flow<Response<T>> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            continuation.resume(response)
                        }
                    })
                    continuation.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType
}


class BodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<T>> {
    override fun adapt(call: Call<T>): Flow<T> {
        return flow {
            emit(
                suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            continuation.resumeWithException(t)
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            try {
                                when (val result: NetworkResult<T> =
                                    NetworkResult.create(response)) {
                                    is NetworkResult.Success -> continuation.resume(result.data)
                                    is NetworkResult.Error -> continuation.resumeWithException(
                                        Exception(result.errorMessage)
                                    )
                                }

                            } catch (e: Exception) {
                                continuation.resumeWithException(e)
                            }
                        }
                    })
                    continuation.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType
}

class StateBodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<Resource<T>>> {
    override fun adapt(call: Call<T>): Flow<Resource<T>> {
        return flow {

            emit(Resource.Loading(null))

            emit(
                suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            val result: NetworkResult.Error<T> = NetworkResult.create(t)
                            continuation.resume(Resource.Error(result.errorMessage,null))
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            try {
                                when (val result: NetworkResult<T> =
                                    NetworkResult.create(response)) {

                                    is NetworkResult.Success -> continuation.resume(
                                        Resource.Success(
                                            result.data
                                        )
                                    )
                                    is NetworkResult.Error -> continuation.resume(
                                        Resource.Error(result.errorMessage,null)
                                    )
                                }

                            } catch (e: Exception) {
                                continuation.resume(
                                    Resource.Error(
                                        e.localizedMessage ?: "Unknown Error",null)
                                    )
                            }
                        }
                    })
                    continuation.invokeOnCancellation { call.cancel() }
                }
            )
        }
    }

    override fun responseType() = responseType
}
