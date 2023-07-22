package cn.edu.sjtu.patrickli.cryptex.model

import android.security.keystore.KeyProperties
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.UUID

class Key(
    var name: String,
    val alias: String = "key@${UUID.randomUUID()}"
) {
    lateinit var publicKey: PublicKey
    lateinit var privateKey: PrivateKey
    constructor(): this("") {}
    fun init() {
        val keyGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        keyGenerator.initialize(2048, SecureRandom())
        val keyPair = keyGenerator.genKeyPair()
        publicKey = keyPair.public
        privateKey = keyPair.private
    }
    fun setPublicKey(publicKeyByteArray: ByteArray) {
        val keyFactory = KeyFactory.getInstance("RSA")
        publicKey = keyFactory.generatePublic(X509EncodedKeySpec(publicKeyByteArray))
    }
    fun setPrivateKey(privateKeyByteArray: ByteArray) {
        val keyFactory = KeyFactory.getInstance("RSA")
        privateKey = keyFactory.generatePrivate(PKCS8EncodedKeySpec(privateKeyByteArray))
    }
    fun publicKeyIsInitialized(): Boolean {
        return ::publicKey.isInitialized
    }
}