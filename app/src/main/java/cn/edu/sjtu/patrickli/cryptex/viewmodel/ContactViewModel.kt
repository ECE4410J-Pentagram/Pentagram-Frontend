package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.model.Contact

class ContactViewModel: ViewModel() {
    var contactList = mutableStateListOf<Contact>()
    var contactCount by mutableStateOf(0)
    var contact: Contact? = null
    var hasNewContact by mutableStateOf(false)
    fun updateContactList(
        viewModelProvider: ViewModelProvider,
        resetNewContactIndicator: Boolean = false,
        onFinished: () -> Unit = {}
    ) {
        val requestViewModel = viewModelProvider[RequestViewModel::class.java]
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getFetchContactsRequest(
            onResponse = {
                val oldContactCount = contactCount
                contactList.clear()
                for (i in 0 until it.length()) {
                    contactList.add(Contact.fromJson(it.getJSONObject(i)))
                }
                contactCount = contactList.size
                hasNewContact = (contactCount > oldContactCount) && (!resetNewContactIndicator)
                Log.d("UpdateContact", "Success fetched ${contactCount} contacts, hasNewContact=${hasNewContact}")
                onFinished()
            },
            onError = {
                Log.e("UpdateContact", "Failed")
                it.printStackTrace()
                onFinished()
            }
        ))
    }
    fun deleteContact(viewModelProvider: ViewModelProvider, onFinished: () -> Unit = {}) {
        val requestViewModel = viewModelProvider[RequestViewModel::class.java]
        requestViewModel.requestQueue.add(requestViewModel.requestStore.getDeleteContactRequest(
            contact,
            onResponse = {
                Log.d("DeleteContact", "Success")
                updateContactList(viewModelProvider, true, onFinished)
            },
            onError = {
                Log.e("DeleteContact", "Failed")
                it.printStackTrace()
                onFinished()
            }
        ))
    }
}