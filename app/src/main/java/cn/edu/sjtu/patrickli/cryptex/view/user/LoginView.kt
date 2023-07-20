package cn.edu.sjtu.patrickli.cryptex.view.user

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.controller.UserController
import cn.edu.sjtu.patrickli.cryptex.model.Util
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel
import cn.edu.sjtu.patrickli.cryptex.view.dialog.LoadingDialog
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
                OutlinedTextField(
                    value = username,
                    onValueChange = { value ->
                        loginError = false
                        if (value.length < 25) {
                            username = value.replace("\n", "")
                            usernameIsError = !Util.checkUsernameIsLegal(username)
                        }
                    },
                    label = {
                        Text(text = "User Name")
                    },
                    singleLine = true,
                    isError = usernameIsError || loginError,
                    supportingText = {
                        if (usernameIsError) {
                            Text(
                                text = stringResource(R.string.usernameRequirement),
                                color = Color.Red
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(bottom = 10.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { value ->
                        loginError = false
                        if (value.length < 40) {
                            password = value.replace("\n", "")
                            passwordIsError = !Util.checkPasswordIsLegal(password)
                        }
                    },
                    label = {
                        Text(text = "Password")
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
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth(0.7f),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { onDoneClick() }
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            if (passwordVisible) {
                                Icon(Icons.Default.Visibility, stringResource(R.string.hidePassword))
                            } else {
                                Icon(Icons.Default.VisibilityOff, stringResource(R.string.showPassword))
                            }
                        }
                    }
                )
            }
        }
        if (userViewModel.isLoggingIn) {
            LoadingDialog(text = stringResource(R.string.logging))
        }
    }
}