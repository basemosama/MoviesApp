package com.basemosama.movies.network.utils

import com.basemosama.movies.utils.DataState
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
                                when (val result: ApiResponseHandler<T> = ApiResponseHandler.create(response)) {
                                    is ApiSuccessResponse -> continuation.resume(result.data)
                                    is ApiErrorResponse -> continuation.resumeWithException(
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

class StateBodyCallAdapter<T>(private val responseType: Type) : CallAdapter<T, Flow<DataState<T>>> {
    override fun adapt(call: Call<T>): Flow<DataState<T>> {
        return flow {

            emit(DataState.Loading)

            emit(
                suspendCancellableCoroutine { continuation ->
                    call.enqueue(object : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            val result: ApiErrorResponse<T> = ApiResponseHandler.create(t)
                            continuation.resume(DataState.Error(result.errorMessage))
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            try {
                                when (val result: ApiResponseHandler<T> = ApiResponseHandler.create(response)) {

                                    is ApiSuccessResponse -> continuation.resume(
                                        DataState.Success(
                                            result.data
                                        )
                                    )
                                    is ApiErrorResponse -> continuation.resume(
                                        DataState.Error(result.errorMessage)
                                    )
                                }

                            } catch (e: Exception) {
                                continuation.resume(
                                    DataState.Error(
                                        e.localizedMessage ?: "Unknown Error"
                                    )
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
