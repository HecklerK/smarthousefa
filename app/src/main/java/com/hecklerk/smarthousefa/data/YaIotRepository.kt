package com.hecklerk.smarthousefa.data

interface YaIotRepository {
    suspend fun getUserInfo(authToken: String): IotInfoUser
}

class YaIotNetworkRepository(private val iotApiService: YaIotApiService) : YaIotRepository {
    override suspend fun getUserInfo(authToken: String): IotInfoUser {
        val headerAuthToken = "Bearer ${authToken}"
        val data = iotApiService.getUserInfo(headerAuthToken)
        return data
    }
}