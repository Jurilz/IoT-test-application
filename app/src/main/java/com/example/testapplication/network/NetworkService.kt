package com.example.testapplication.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.Call;
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

const val UNIKS_WATERFILL_URL = "https://waterfill.uniks.de/api/"

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface Api {
    @GET("{url}")
    suspend fun sendRequest(@Path("url") url: String): Call<String>

    @GET
    suspend fun getApiModel(@Url url: String): ApiModel

    @GET
    suspend fun getMeasurement(@Url url: String): Measurement

    @GET
    suspend fun getFlag(@Url url: String): Boolean

    @GET
    suspend fun getTimeseries(@Url url: String): List<Measurement>

}

object NetworkService {
    val httpClient = OkHttpClient()

    val converterFactory: MoshiConverterFactory = MoshiConverterFactory.create(moshi)

    val API: Api = Retrofit.Builder()
        .baseUrl(UNIKS_WATERFILL_URL)
        .client(httpClient)
        .addConverterFactory(converterFactory)
        .build()
        .create(Api::class.java)
}