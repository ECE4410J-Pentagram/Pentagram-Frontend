package cn.edu.sjtu.patrickli.cryptex.view.tool

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.edu.sjtu.patrickli.cryptex.R

@Composable
fun ImageWrapper(
    bitmap: Bitmap?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    if (bitmap != null) {
        Image(
            bitmap.asImageBitmap(),
            contentDescription = contentDescription?:stringResource(R.string.defaultImage),
            modifier = modifier
                .size(300.dp)
                .border(3.dp, Color.Black, RoundedCornerShape(16.dp))
                .padding(16.dp)
        )
    } else {
        Image(
            Icons.Outlined.Image,
            contentDescription = stringResource(R.string.defaultImage),
            modifier = modifier
                .size(300.dp)
                .border(3.dp, Color.Black, RoundedCornerShape(16.dp))
                .padding(16.dp)
        )
    }
}