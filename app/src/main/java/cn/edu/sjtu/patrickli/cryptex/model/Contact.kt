package cn.edu.sjtu.patrickli.cryptex.model

import androidx.compose.runtime.mutableStateListOf

class Contact (
    val name: String? = null,
    val publicKey: String? = null
) {}

val testContacts = mutableStateListOf<Contact>(
    Contact(name = "Contact 0"),
    Contact(name = "Contact 1"),
    Contact(name = "Contact 2"),
    Contact(name = "Contact 3"),
    Contact(name = "Contact 4"),
    Contact(name = "Contact 5"),
    Contact(name = "Contact 6"),
    Contact(name = "Contact 7"),
    Contact(name = "Contact 8"),
    Contact(name = "Contact 9"),
    Contact(name = "Contact 10"),
    Contact(name = "Contact 11"),
    Contact(name = "Contact 12"),
)