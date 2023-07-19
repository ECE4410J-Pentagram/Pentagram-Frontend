package cn.edu.sjtu.patrickli.cryptex.model

import java.io.File

class Encrypter(
    val encrypterViewModel: EncrypterViewModel
) {

    private val imgDir = File(encrypterViewModel.context.filesDir, "images")

    fun doFinal(): File {
        return File(imgDir, "test.jpg")
    }

}