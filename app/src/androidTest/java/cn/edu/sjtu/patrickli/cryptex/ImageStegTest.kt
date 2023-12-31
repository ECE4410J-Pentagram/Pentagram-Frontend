package cn.edu.sjtu.patrickli.cryptex

import android.security.keystore.KeyProperties
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import cn.edu.sjtu.patrickli.cryptex.model.FileHandler
import cn.edu.sjtu.patrickli.cryptex.model.Util
import cn.edu.sjtu.patrickli.cryptex.model.core.ImageDecrypter
import cn.edu.sjtu.patrickli.cryptex.model.core.ImageEncrypter
import cn.edu.sjtu.patrickli.cryptex.model.core.IntDecrypter
import cn.edu.sjtu.patrickli.cryptex.model.core.IntEncrypter
import cn.edu.sjtu.patrickli.cryptex.model.core.TextDecrypter
import cn.edu.sjtu.patrickli.cryptex.model.core.TextEncrypter
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import java.nio.charset.Charset
import java.nio.file.Paths
import java.security.KeyPairGenerator
import java.security.SecureRandom

@RunWith(AndroidJUnit4::class)
class ImageStegTest {
    @Test
    fun anonymousTest() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val content = "Hello world"
//        val content = context.getString(R.string.lipsum).repeat(2)
        val plainFile = Paths.get(context.filesDir.toString(), "images", "test.jpg").toFile()
        assertEquals(plainFile.exists(), true)
        val plainFileBytes = plainFile.readBytes()
        val bitmap = ImageEncrypter.doFinal(content.toByteArray(), plainFileBytes)
        val cipherFile = FileHandler.saveImageToCache(bitmap, "test")
        assertEquals(cipherFile.exists(), true)
        val cipherFileBytes = cipherFile.readBytes()
        val textByteArray = ImageDecrypter.doFinal(cipherFileBytes)
        assertEquals(content, textByteArray.toString(Charsets.UTF_8))
    }
    @Test
    fun wholeTest() {
        anonymousTest()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val keyGenerator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA)
        keyGenerator.initialize(2048, SecureRandom())
        val keyPair = keyGenerator.genKeyPair()
        val publicKey = keyPair.public
        val privateKey = keyPair.private
        val content = "Hello world"
//        val content = context.getString(R.string.lipsum).repeat(2)
        val keyAlias = "key@d7736dd5-1031-4d9d-a44f-8cad6b88f458"
        val cipherByteArray = TextEncrypter.doFinal(content, publicKey)
        val plainFile = Paths.get(context.filesDir.toString(), "images", "test.jpg").toFile()
        assertEquals(plainFile.exists(), true)
        val plainFileBytes = plainFile.readBytes()
        val shuffleSeed = 3289
        val shuffleSeedArray = IntEncrypter.doFinal(shuffleSeed, publicKey)
        val cipherByteSizeArray = IntEncrypter.doFinal(cipherByteArray.size, publicKey)
        val bitmap = ImageEncrypter.doFinal(
            textBytes = cipherByteArray,
            imgBytes = plainFileBytes,
            isAnonymous = false,
            textSizeBytes = cipherByteSizeArray,
            shuffleSeed = shuffleSeed,
            shuffleSeedBytes = shuffleSeedArray,
            keyAlias = keyAlias
        )
        val cipherFile = FileHandler.saveImageToCache(bitmap, "test")
        assertEquals(cipherFile.exists(), true)
        val cipherFileBytes = cipherFile.readBytes()

        val keyAliasByteArray = ImageDecrypter.extractKeyAlias(cipherFileBytes)
        val privateKeyAlias = keyAliasByteArray!!.toString(Charsets.UTF_8)

        assertEquals(privateKeyAlias, keyAlias)

        val textByteArray = ImageDecrypter.doFinal(cipherFileBytes, privateKey)
        val plainText = TextDecrypter.doFinal(textByteArray, privateKey)
        assertEquals(content, plainText)
        assertEquals(keyAlias, keyAliasByteArray.toString(Charsets.UTF_8))
    }
}