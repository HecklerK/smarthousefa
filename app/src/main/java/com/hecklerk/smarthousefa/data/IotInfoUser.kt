package com.hecklerk.smarthousefa.data

import com.squareup.moshi.Json
import org.json.JSONObject

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
    @Json(name = "type") val type: String,
    @Json(name = "state") val state: StateCapability
)

data class StateCapability (
    @Json(name = "instance") val instance: String,
    @Json(name = "value") var value: Any
)

data class SetStateDeviceRequest(
    @Json(name = "devices") var device: List<SetStateDevice>
)

data class SetStateDevice(
    @Json(name = "id") var string: String,
    @Json(name = "actions") var actions: List<Capability>
)

data class SetStateDeviceResponse(
    @Json(name = "request_id") val requestId: String,
    @Json(name = "devices") val devices: List<DeviceSetState>
)

data class DeviceSetState(
    @Json(name = "id") var id: String,
    @Json(name = "capabilities") var capabilities: List<CapabilitySetState>
)

data class CapabilitySetState(
    @Json(name = "type") var type: String,
    @Json(name = "state") var state: SetStateCapability
)

data class SetStateCapability(
    @Json(name = "instance") val instance: String,
    @Json(name = "action_result") val actionResult: ActionResult
)

data class ActionResult(
    @Json(name = "status") val status: String
)