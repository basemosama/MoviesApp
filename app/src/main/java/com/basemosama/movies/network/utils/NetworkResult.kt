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
import timber.log.Timber
import java.io.IOException

/**
 * Common class used by API responses.
 * @param <T> the type of the response object
</T> */
@Suppress("unused") // T is used in extending classes
sealed class NetworkResult<T> {

    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val errorMessage: String) : NetworkResult<T>()


    companion object {
        private const val DEFAULT_ERROR = "Sorry, Something went wrong"
        fun <T> create(error: Throwable): Error<T> {
            var  errorMessage = error.message ?: DEFAULT_ERROR
            if(error is IOException){
                errorMessage = "Please check your internet connection and try again "
            }
            Timber.d("NetworkError : $errorMessage")

            return Error(errorMessage)
        }

        fun <T> create(response: Response<T>): NetworkResult<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    Timber.d("NetworkError : Empty String")

                    Error(  DEFAULT_ERROR)
                } else {
                    Timber.d("NetworkError : Success")
                    Success( body)

                }
            } else {
                val msg = if(!response.message().isNullOrEmpty()) response.message() else DEFAULT_ERROR
                Timber.d("NetworkError response: $msg")
                Error(msg)
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make Success's body non-null.
 */
//class ApiEmptyResponse<T> : NetworkResult<T>()

