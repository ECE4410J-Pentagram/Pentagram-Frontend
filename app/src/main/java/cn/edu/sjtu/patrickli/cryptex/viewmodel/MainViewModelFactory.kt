package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            EncrypterViewModel::class.java -> {
                EncrypterViewModel(context) as T
            }

            DecrypterViewModel::class.java -> {
                DecrypterViewModel() as T
            }

            UserViewModel::class.java -> {
                UserViewModel(context) as T
            }

            RequestViewModel::class.java -> {
                RequestViewModel(context) as T
            }

            KeyViewModel::class.java -> {
                KeyViewModel() as T
            }

            ContactViewModel::class.java -> {
                ContactViewModel(context) as T
            }

            InvitationViewModel::class.java -> {
                InvitationViewModel(context) as T
            }

            else -> {
                throw RuntimeException("Cannot create an instance of ${modelClass}")
            }
        }
    }
}