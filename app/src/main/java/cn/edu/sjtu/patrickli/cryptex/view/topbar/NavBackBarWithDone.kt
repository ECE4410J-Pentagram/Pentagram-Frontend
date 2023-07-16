package cn.edu.sjtu.patrickli.cryptex.view.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBackBarWithDone(navController: NavHostController, onDone: () -> Unit) {
    TopAppBar (
        title = {
            BarImgComponent()
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.app_name)
                )
            }
        },
        actions = {
            IconButton(onClick = { onDone() }) {
                Icon(
                    Icons.Default.Done,
                    contentDescription = stringResource(R.string.done)
                )
            }
        }
    )
}