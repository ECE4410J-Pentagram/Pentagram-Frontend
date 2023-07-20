package cn.edu.sjtu.patrickli.cryptex.view.user

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.controller.UserController
import cn.edu.sjtu.patrickli.cryptex.model.Util
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel
import cn.edu.sjtu.patrickli.cryptex.view.dialog.LoadingDialog
import cn.edu.sjtu.patrickli.cryptex.view.text.PasswordTextField
import cn.edu.sjtu.patrickli.cryptex.view.text.UsernameTextField
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBarWithDone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val userViewModel = viewModelProvider[UserViewModel::class.java]
    var username by remember { mutableStateOf(userViewModel.accountName) }
    var password by remember { mutableStateOf("") }
    var usernameIsError by remember { mutableStateOf(false) }
    var passwordIsError by remember { mutableStateOf(false) }
    var loginError by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    fun onDoneClick() {
        usernameIsError = !Util.checkUsernameIsLegal(username)
        passwordIsError = !Util.checkPasswordIsLegal(password)
        if (!(usernameIsError || passwordIsError)) {
            UserController.onLogin(username, password, userViewModel) { success ->
                if (success) {
                    userViewModel.isLoggedIn = true
                    navController.popBackStack()
                } else {
                    loginError = true
                }
            }
        }
    }
    Scaffold(
        topBar = {
            NavBackBarWithDone(navController = navController) { onDoneClick() }
        }
    ) {
        ConstraintLayout (
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Image(
                    Icons.Default.AccountCircle,
                    contentDescription = stringResource(R.string.myIcon),
                    modifier = Modifier
                        .size(160.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                UsernameTextField(
                    value = username,
                    onValueChange = { value ->
                        loginError = false
                        username = value.replace("\n", "")
                        usernameIsError = !Util.checkUsernameIsLegal(username)
                    },
                    isError = usernameIsError || loginError,
                    supportingText = {
                        if (usernameIsError) {
                            Text(
                                text = stringResource(R.string.usernameRequirement),
                                color = Color.Red
                            )
                        }
                    }
                )
                PasswordTextField(
                    value = password,
                    onValueChange = { value ->
                        loginError = false
                        password = value.replace("\n", "")
                        passwordIsError = !Util.checkPasswordIsLegal(password)
                    },
                    isError = passwordIsError || loginError,
                    supportingText = {
                        if (passwordIsError) {
                            Text(
                                text = stringResource(R.string.passwordRequirement),
                                color = Color.Red
                            )
                        } else if (loginError) {
                            Text(
                                text = stringResource(R.string.badAccountPassword),
                                color = Color.Red
                            )
                        }
                    },
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChange = { value -> passwordVisible = value },
                    onDoneClick = { onDoneClick() }
                )
            }
        }
        if (userViewModel.isLoggingIn) {
            LoadingDialog(text = stringResource(R.string.logging))
        }
    }
}