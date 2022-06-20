/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.basemosama.movies.network.utils

import retrofit2.Response

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class NetworkResult<T> {

    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val errorMessage: String) : NetworkResult<T>()


    companion object {
        fun <T> create(error: Throwable): Error<T> {
            return Error(error.message ?: "unknown error")
        }

        fun <T> create(response: Response<T>): NetworkResult<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    Error(  "Empty Response error")
                } else {
                    Success( body)
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty())  response.message() else  msg
                Error(errorMsg ?: "unknown error")
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make Success's body non-null.
 */
//class ApiEmptyResponse<T> : NetworkResult<T>()

