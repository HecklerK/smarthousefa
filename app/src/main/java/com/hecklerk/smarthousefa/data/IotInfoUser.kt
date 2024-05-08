package com.hecklerk.smarthousefa.data

import com.squareup.moshi.Json

data class IotInfoUser (
    @Json(name = "status") val status: String,
    @Json(name = "request_id") val requestId: String
)