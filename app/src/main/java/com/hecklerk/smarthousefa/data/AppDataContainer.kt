package com.hecklerk.smarthousefa.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Url

interface AppDataContainer {
    val loginSdk: YandexAuthSdk
    val authRepository: YaAuthRepository
    val iotRepository: YaIotRepository
}

class YaAuthContainer(private val context: Context) : AppDataContainer {
    private val authOptions: YandexAuthOptions = YandexAuthOptions(context, true)
    override val loginSdk: YandexAuthSdk by lazy { YandexAuthSdk(context, authOptions) }

    private val BASE_URL = "https://login.yandex.ru/"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    private val yaAuthApiService: YaAuthApiService by lazy { retrofit.create(YaAuthApiService::class.java) }
    override val authRepository: YaAuthRepository by lazy { YaAuthNetworkRepository(yaAuthApiService) }

    private val BASE_IOT_URL = "https://api.iot.yandex.net/v1.0/"

    private val moshiIot: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofitIot: Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshiIot))
        .baseUrl(BASE_IOT_URL)
        .build()

    private val yaIotApiService: YaIotApiService by lazy { retrofitIot.create(YaIotApiService::class.java) }
    override val iotRepository: YaIotRepository by lazy { YaIotNetworkRepository(yaIotApiService) }
}

interface YaAuthApiService {
    @GET("info")
    suspend fun getUserInfo(@Header("Authorization") authToken: String): UserInfo
}

interface YaIotApiService {
    @GET("user/info")
    suspend fun getUserInfo(@Header("Authorization") authToken: String): IotInfoUser

    @POST("user/devices/action")
    suspend fun setStateDevice(@Header("Authorization") authToken: String, @Body body: SetStateDeviceRequest): SetStateDeviceResponse
}