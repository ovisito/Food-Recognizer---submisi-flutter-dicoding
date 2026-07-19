package com.example.data.network

import com.example.data.model.MealResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MealDbApiService {
    @GET("api/json/v1/1/search.php")
    suspend fun searchMealByName(@Query("s") name: String): MealResponse

    @GET("api/json/v1/1/lookup.php")
    suspend fun lookupMealById(@Query("i") id: String): MealResponse
}

object MealDbRetrofitClient {
    private const val BASE_URL = "https://www.themealdb.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val service: MealDbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(MealDbApiService::class.java)
    }
}
