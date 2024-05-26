package com.hecklerk.smarthousefa.data

import com.squareup.moshi.Json

data class IotInfoUser (
    @Json(name = "status") val status: String,
    @Json(name = "request_id") val requestId: String,
    @Json(name = "devices") val devices: List<Device>
)

data class Device (
    @Json(name = "id") val id: String,
    @Json(name = "name") val name: String,
    @Json(name = "capabilities") val capabilities: List<Capability>
)

data class Capability (
    @Json(name = "type") val type: String
)