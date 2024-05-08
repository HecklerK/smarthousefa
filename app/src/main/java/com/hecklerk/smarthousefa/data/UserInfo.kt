package com.hecklerk.smarthousefa.data

import com.squareup.moshi.Json

data class UserInfo(
    @Json(name = "real_name") val realName: String
)
