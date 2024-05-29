package com.hecklerk.smarthousefa.data

import retrofit2.http.Body

interface YaIotRepository {
    suspend fun getUserInfo(authToken: String): IotInfoUser

    suspend fun setDeviceState(authToken: String, body: SetStateDeviceRequest): SetStateDeviceResponse
}

class YaIotNetworkRepository(private val iotApiService: YaIotApiService) : YaIotRepository {
    override suspend fun getUserInfo(authToken: String): IotInfoUser {
        val headerAuthToken = "Bearer ${authToken}"
        val data = iotApiService.getUserInfo(headerAuthToken)
        return data
    }

    override suspend fun setDeviceState(authToken: String, body: SetStateDeviceRequest): SetStateDeviceResponse {
        val headerAuthToken = "Bearer ${authToken}"
        val data = iotApiService.setStateDevice(headerAuthToken, body)
        return data
    }
}