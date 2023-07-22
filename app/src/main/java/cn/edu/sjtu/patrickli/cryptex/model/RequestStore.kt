package cn.edu.sjtu.patrickli.cryptex.model

import android.util.Log
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject

class RequestStore {
    private val apiUrl = "https://cryptex.software/api/"
    private fun getApi(path: String): String {
        return apiUrl + path
    }

    fun getCreateDeviceRequest(
        userViewModel: UserViewModel,
        onResponse: ((JSONObject) -> Unit)? = null,
        onError: ((VolleyError) -> Unit)? = null
    ): JsonObjectRequest {
        val payload = JSONObject(mapOf(
            "name" to "${userViewModel.deviceName}@${userViewModel.deviceId}",
            "key" to userViewModel.deviceKey
        ))
        return JsonObjectRequest(
            Request.Method.POST,
            getApi("device/"),
            payload,
            onResponse ?: {},
            onError ?: {}
        )
    }

    fun getLoginRequest(
        userViewModel: UserViewModel,
        onResponse: ((JSONObject) -> Unit)? = null,
        onError: ((VolleyError) -> Unit)? = null
    ): JsonObjectRequest {
        val payload = JSONObject(mapOf(
            "name" to "${userViewModel.deviceName}@${userViewModel.deviceId}",
            "key" to userViewModel.deviceKey
        ))
        return JsonObjectRequest(
            Request.Method.POST,
            getApi("login/"),
            payload,
            onResponse ?: {
                userViewModel.authorization = it.getString("Authorization")
                // Print out the authorization string for debug use
                Log.d("Auth", "Authorization: ${userViewModel.authorization}")
                Unit
            },
            onError ?: {}
        )
    }

    fun getAddKeyRequest(
        key: Key,
        userViewModel: UserViewModel,
        onResponse: ((JSONObject) -> Unit)? = null,
        onError: ((VolleyError) -> Unit)? = null
    ): JsonObjectRequest {
        val payload = JSONObject(mapOf(
            "name" to key.alias,
            "pk" to Util.publicKeyToString(key.publicKey)
        ))
        return object: JsonObjectRequest(
            Request.Method.POST,
            getApi("key/"),
            payload,
            onResponse ?: {},
            onError ?: {}
        ) {
            override fun getHeaders(): Map<String, String> {
                return mapOf(
                    "Content-Type" to "application/json",
                    "Authorization" to (userViewModel.authorization ?: "")
                )
            }
        }
    }

    fun getRemoveKeyRequest(
        key: Key,
        userViewModel: UserViewModel,
        onResponse: ((JSONObject) -> Unit)? = null,
        onError: ((VolleyError) -> Unit)? = null
    ): JsonObjectRequest {
        val payload = JSONObject()
        return object: JsonObjectRequest(
            Request.Method.DELETE,
            getApi("key/${key.alias}"),
            payload,
            onResponse ?: {},
            onError ?: {}
        ) {
            override fun getHeaders(): Map<String, String> {
                return mapOf(
                    "Content-Type" to "application/json",
                    "Authorization" to (userViewModel.authorization ?: "")
                )
            }
        }
    }

}