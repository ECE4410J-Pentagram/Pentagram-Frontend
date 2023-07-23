package cn.edu.sjtu.patrickli.cryptex.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import cn.edu.sjtu.patrickli.cryptex.model.Contact

class ContactViewModel: ViewModel() {
    var contactList = mutableStateListOf<Contact>()
    var scannedContact: Contact? = null
}