package cn.edu.sjtu.patrickli.cryptex.model

import cn.edu.sjtu.patrickli.cryptex.R

class TextEncrypter(
    val encrypterViewModel: EncrypterViewModel
) {

    fun doFinal(): String {
        return encrypterViewModel.context.getString(R.string.lipsum)
    }

}