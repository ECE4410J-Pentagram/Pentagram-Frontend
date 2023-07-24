package cn.edu.sjtu.patrickli.cryptex.model

import android.util.Log
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.viewmodel.ContactViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.InvitationViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.KeyViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.UserViewModel
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONArray
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

    fun getSendInvitationRequest(
        viewModelProvider: ViewModelProvider,
        onResponse: ((JSONObject) -> Unit)? = null,
        onError: ((VolleyError) -> Unit)? = null
    ): JsonObjectRequest {
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        val fromKeyAlias = viewModelProvider[KeyViewModel::class.java].keyToShare?.alias
        val toDevice = viewModelProvider[ContactViewModel::class.java].contact
        val toDeviceNameId = toDevice?.name + "@" + toDevice?.id
        val payload = JSONObject(mapOf(
            "from_key" to mapOf("name" to fromKeyAlias),
            "to_device" to mapOf("name" to toDeviceNameId)
        ))
        return object: JsonObjectRequest(
            Request.Method.POST,
            getApi("invitation/sent/"),
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

    fun getFetchSentInvitationRequest(
        userViewModel: UserViewModel,
        onResponse: ((JSONArray) -> Unit)? = null,
        onError: ((VolleyError) -> Unit)? = null
    ): JsonArrayRequest {
        val payload = JSONArray()
        return object : JsonArrayRequest(
            Request.Method.GET,
            getApi("invitation/sent/"),
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

    fun getFetchReceivedInvitationRequest(
        userViewModel: UserViewModel,
        onResponse: ((JSONArray) -> Unit)? = null,
        onError: ((VolleyError) -> Unit)? = null
    ): JsonArrayRequest {
        val payload = JSONArray()
        return object : JsonArrayRequest(
            Request.Method.GET,
            getApi("invitation/received/"),
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

    fun getAcceptInvitationRequest(
        viewModelProvider: ViewModelProvider,
        onResponse: ((JSONObject) -> Unit)? = null,
        onError: ((VolleyError) -> Unit)? = null
    ): JsonObjectRequest {
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        val invitationViewModel = viewModelProvider[InvitationViewModel::class.java]
        val keyViewModelProvider = viewModelProvider[KeyViewModel::class.java]
        val payload = JSONObject(mapOf(
            "id" to invitationViewModel.selectedInvitation?.id,
            "shared_key" to mapOf("name" to keyViewModelProvider.keyToShare?.alias)
        ))
        return object : JsonObjectRequest(
            Request.Method.POST,
            getApi("invitation/received/accept"),
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

    fun getDeclineInvitationRequest(
        viewModelProvider: ViewModelProvider,
        onResponse: ((JSONObject) -> Unit)? = null,
        onError: ((VolleyError) -> Unit)? = null
    ): JsonObjectRequest {
        val userViewModel = viewModelProvider[UserViewModel::class.java]
        val invitationViewModel = viewModelProvider[InvitationViewModel::class.java]
        val payload = JSONObject(mapOf(
            "id" to invitationViewModel.selectedInvitation?.id
        ))
        return object : JsonObjectRequest(
            Request.Method.POST,
            getApi("invitation/received/reject"),
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