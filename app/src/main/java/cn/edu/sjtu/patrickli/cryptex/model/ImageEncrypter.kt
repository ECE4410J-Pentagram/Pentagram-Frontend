package cn.edu.sjtu.patrickli.cryptex.model

import cn.edu.sjtu.patrickli.cryptex.viewmodel.EncrypterViewModel
import java.io.File

class ImageEncrypter(
    val encrypterViewModel: EncrypterViewModel
) {

    private val imgDir = File(encrypterViewModel.context.filesDir, "images")

    fun doFinal(): File {
        return File(imgDir, "test.jpg")
    }

}