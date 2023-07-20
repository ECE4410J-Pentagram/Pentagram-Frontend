package cn.edu.sjtu.patrickli.cryptex.controller

import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel

object UserController {
    fun onLogin(
        username: String,
        password: String,
        userViewModel: UserViewModel,
        onFinished: (Boolean) -> Unit
    ) {
        userViewModel.accountName = username
        userViewModel.login() {
            onFinished(it)
        }
    }
}