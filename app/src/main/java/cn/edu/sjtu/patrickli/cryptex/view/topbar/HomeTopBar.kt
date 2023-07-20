package cn.edu.sjtu.patrickli.cryptex.view.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import cn.edu.sjtu.patrickli.cryptex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavController) {
    TopAppBar(
        title = {
            BarImgComponent()
        },
        actions = {
            IconButton(onClick = { navController.navigate("QrCodeView") }) {
                Icon(
                    Icons.Default.QrCode,
                    contentDescription = stringResource(R.string.myqr)
                )
            }
        }
    )
}