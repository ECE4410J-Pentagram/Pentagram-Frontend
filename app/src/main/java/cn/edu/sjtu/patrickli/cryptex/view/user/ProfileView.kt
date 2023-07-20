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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.UserViewModel
import cn.edu.sjtu.patrickli.cryptex.view.button.BaseWideButton
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val userViewModel = viewModelProvider[UserViewModel::class.java]
    Scaffold(
        topBar = {
            NavBackBar(navController = navController)
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
                Text(text = userViewModel.accountName, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text(text = userViewModel.deviceName)
                Text(text = userViewModel.deviceKey?:"Empty Device Key")
                Spacer(modifier = Modifier.height(30.dp))
                BaseWideButton(onClick = { navController.navigate(("LoginView")) }) {
                    Text(text = stringResource(R.string.login))
                }
                BaseWideButton(onClick = { /*TODO*/ }) {
                    Text(text = stringResource(R.string.signup))
                }
                BaseWideButton(onClick = { navController.navigate("QrCodeView") }) {
                    Text(text = stringResource(R.string.myqr))
                }
            }
        }
    }
}