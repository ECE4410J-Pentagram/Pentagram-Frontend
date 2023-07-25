package cn.edu.sjtu.patrickli.cryptex.model

import org.json.JSONObject

data class Contact (
    val name: String? = null,
    val id: String? = null,
    val keyAlias: String? = null,
    val publicKey: String? = null
) {
    companion object {
        fun fromInvitation(invitation: Invitation): Contact {
            return Contact(invitation.fromDeviceName, invitation.fromDeviceId)
        }
        fun fromJson(jsonObject: JSONObject): Contact {
            val device = jsonObject.getJSONObject("owner").getString("name")
            val atMarkIndex = device.lastIndexOf("@")
            return Contact(
                device.substring(0, atMarkIndex),
                device.substring(atMarkIndex + 1, device.length),
                jsonObject.getString("name"),
                jsonObject.getString("pk")
            )
        }
    }
}