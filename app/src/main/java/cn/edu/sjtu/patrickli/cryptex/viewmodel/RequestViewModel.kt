package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import cn.edu.sjtu.patrickli.cryptex.model.CustomHurlStack
import cn.edu.sjtu.patrickli.cryptex.model.RequestStore
import com.android.volley.toolbox.Volley

class RequestViewModel(
    private val context: Context
): ViewModel() {
    val requestStore = RequestStore()
    val requestQueue = Volley.newRequestQueue(context, CustomHurlStack())
}