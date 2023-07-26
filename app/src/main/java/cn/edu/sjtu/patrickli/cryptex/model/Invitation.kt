package cn.edu.sjtu.patrickli.cryptex.model

import org.json.JSONObject

data class Invitation(
    val id: Int,
    val fromKeyAlias: String,
    val fromDeviceName: String,
    val fromDeviceId: String,
    val toDeviceName: String,
    val toDeviceId: String,
    val type: InvitationType
) {
    companion object {
        fun fromJson(type: InvitationType, jsonObject: JSONObject): Invitation {
            val fromDevice = jsonObject.getJSONObject("from_device").getString("name")
            val toDevice = jsonObject.getJSONObject("to_device").getString("name")
            val fromAtMarkIndex = fromDevice.lastIndexOf("@")
            val toAtMarkIndex = toDevice.lastIndexOf("@")
            return Invitation(
                jsonObject.getInt("id"),
                jsonObject.getJSONObject("from_key").getString("name"),
                fromDevice.substring(0, fromAtMarkIndex),
                fromDevice.substring(fromAtMarkIndex + 1, fromDevice.length),
                toDevice.substring(0, toAtMarkIndex),
                toDevice.substring(toAtMarkIndex + 1, toDevice.length),
                type
            )
        }
    }
}