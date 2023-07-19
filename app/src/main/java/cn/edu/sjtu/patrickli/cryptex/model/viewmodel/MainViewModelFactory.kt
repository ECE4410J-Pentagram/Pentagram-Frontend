package cn.edu.sjtu.patrickli.cryptex.model.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when (modelClass) {
            EncrypterViewModel::class.java -> {
                return EncrypterViewModel(context) as T
            }
            DecrypterViewModel::class.java -> {
                return DecrypterViewModel() as T
            }
            else -> {
                throw RuntimeException("Cannot create an instance of ${modelClass}")
            }
        }
    }
}