package com.hecklerk.smarthousefa.data

interface YaAuthRepository {
    suspend fun getUserInfo(authToken: String): UserInfo
}

class YaAuthNetworkRepository(private val authApiService: YaAuthApiService) : YaAuthRepository {
    override suspend fun getUserInfo(authToken: String): UserInfo {
        val headerAuthToken = "OAuth ${authToken}"
        return authApiService.getUserInfo(headerAuthToken)
    }
}
