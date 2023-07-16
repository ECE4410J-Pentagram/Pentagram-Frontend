package cn.edu.sjtu.patrickli.cryptex.model

import androidx.compose.runtime.mutableStateListOf

class Key(var sk: String, var pk: String, var name: String)

var testKeys = mutableStateListOf<Key>(
    Key(sk = "sk1", pk = "asdfjlaskdjfljkaslddddddddddddddddddddddflkasdjflkasjdlfkjasldjflkasjdflkjasdlfjkasldkjflaksjdflkasjdflkjasdlkjflaskdjflkajsdlfjaslkdjf", name = "key1"),
    Key(sk = "sk2", pk = "pk2", name = "key2")
)

fun createKey(name: String): Key {
    return Key(sk = "security key", pk = "public key", name = name)
}