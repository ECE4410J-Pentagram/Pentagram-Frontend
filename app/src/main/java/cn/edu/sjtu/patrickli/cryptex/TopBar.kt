package cn.edu.sjtu.patrickli.cryptex

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun BarImgComponent() {
    Image(
        painter = painterResource(id = R.drawable.cryptex),
        contentDescription = stringResource(id = R.string.logo_name),
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .padding(10.dp)
            .size(100.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar() {
    TopAppBar(
        title = {
            BarImgComponent()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBackBar(navController: NavHostController) {
    TopAppBar (
        title = {
            BarImgComponent()

        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    painter = painterResource(com.google.android.material.R.drawable.abc_ic_ab_back_material),
                    contentDescription = stringResource(id = R.string.app_name)
                )
            }
        }
    )
}