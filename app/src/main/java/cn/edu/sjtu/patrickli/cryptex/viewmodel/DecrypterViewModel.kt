package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.lifecycle.ViewModel
import cn.edu.sjtu.patrickli.cryptex.model.Key
import cn.edu.sjtu.patrickli.cryptex.model.core.ImageDecrypter
import cn.edu.sjtu.patrickli.cryptex.model.core.TextDecrypter
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.model.security.KeyDecrypter
import java.security.PrivateKey

class DecrypterViewModel: ViewModel() {
    var pickImageLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>? = null
    var cipherByteArray: ByteArray? = null
    private fun getPrivateKey(databaseProvider: DatabaseProvider, alias: String): PrivateKey {
        val query = "SELECT encryptedPrivateKey, encryptedPrivateKeyIv FROM [key] WHERE privateKeyAlias=?"
        val cur = databaseProvider.userDatabase.rawQuery(query, arrayOf(alias))
        cur.moveToFirst()
        val encryptedPrivateKey = cur.getBlob(0)
        val encryptedPrivateKeyIv = cur.getBlob(1)
        cur.close()
        val keyDecrypter = KeyDecrypter()
        val privateKeyBytes = keyDecrypter.doFinal(alias, encryptedPrivateKey, encryptedPrivateKeyIv)
        return Key.getPrivateKey(privateKeyBytes)
    }
    fun doDecrypt(databaseProvider: DatabaseProvider, onFinished: (String) -> Unit = {}) {
        val (textByteArray, keyAliasByteArray) = ImageDecrypter.doFinal(cipherByteArray!!)
        if (keyAliasByteArray != null) {
            val privateKeyAlias = keyAliasByteArray.toString(Charsets.UTF_8)
            val privateKey = getPrivateKey(databaseProvider, privateKeyAlias)
            val plainText = TextDecrypter.doFinal(textByteArray, privateKey)
            onFinished(plainText)
        } else {
            onFinished(textByteArray.toString(Charsets.UTF_8))
        }
    }
}