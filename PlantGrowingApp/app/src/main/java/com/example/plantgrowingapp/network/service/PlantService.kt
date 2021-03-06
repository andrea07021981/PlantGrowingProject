package com.example.plantgrowingapp.network.service

import com.example.plantgrowingapp.network.datatransferobject.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BASE_URL = "http://10.0.2.2:3000/" // Depend on your local port

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClientBuilder = OkHttpClient.Builder()
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(getInterceptior())

private fun getInterceptior(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BASIC
    return logging
}

/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .client(okHttpClientBuilder.build())
    .build()

interface PlantService {

    @GET("data/?")
    fun getPlantData(@Query("plantId") plantId: Long): Deferred<NetworkPlantDataContainer>

    @GET("user/?")
    fun getUser(@Query("email") email: String, @Query("password") password: String): Deferred<NetworkUser>

    @POST("user/")
    @FormUrlEncoded
    fun postUser(@Field("email") email: String, @Field("password") password: String): Deferred<NetworkUser>

    @GET("plant/?")
    fun getPlantsByUser(@Query("userId") plantId: Long): Deferred<NetworkPlantContainer>

    @POST("command/")
    @FormUrlEncoded
    suspend fun postCommand(@Field("plantId") plantId: Long, @Field("commandType") commandType: Int): NetworkCommand
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object PlantApi {
    val retrofitService : PlantService by lazy { retrofit.create(PlantService::class.java) }
}