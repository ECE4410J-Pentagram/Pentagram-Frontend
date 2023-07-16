package cn.edu.sjtu.patrickli.cryptex.view.topbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.edu.sjtu.patrickli.cryptex.R

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