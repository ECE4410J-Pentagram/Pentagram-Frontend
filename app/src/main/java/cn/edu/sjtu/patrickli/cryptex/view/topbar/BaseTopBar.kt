package cn.edu.sjtu.patrickli.cryptex.view.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar() {
    TopAppBar(
        title = {
            BarImgComponent()
        }
    )
}