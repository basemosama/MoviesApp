package com.basemosama.movies.network

import com.basemosama.movies.BuildConfig
import com.basemosama.movies.network.utils.FlowCallAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val baseUrl: String = "https://api.themoviedb.org/3/"

    private val gson: Gson = GsonBuilder().setDateFormat("yyyy-mm-dd").create()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private object RequestInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original: Request = chain.request()
            val originalHttpUrl: HttpUrl = original.url

            val url = originalHttpUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()

            // Request customization: add request headers
            val request: Request = original.newBuilder()
                .url(url)
                .build()

            return chain.proceed(request)

        }
    }

    private val client =
        OkHttpClient.Builder().addInterceptor(loggingInterceptor).addInterceptor(RequestInterceptor)
            .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(FlowCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    fun getClient(): ApiService = retrofit.create(ApiService::class.java)


}