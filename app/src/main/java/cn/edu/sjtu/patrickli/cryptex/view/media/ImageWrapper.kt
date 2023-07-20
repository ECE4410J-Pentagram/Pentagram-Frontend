package cn.edu.sjtu.patrickli.cryptex.view.media

import android.content.Context
import android.util.Log
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import cn.edu.sjtu.patrickli.cryptex.R
import coil.compose.AsyncImage
import java.io.File

@Composable
fun ImageWrapper(
    context: Context,
    file: File?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val imgUri = try {
        file?.let {
            FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                it
            )
        }
    } catch (err: Exception) {
        Log.e("imgLoader", "cipherImgFile load error")
        err.printStackTrace()
        null
    }
    if (imgUri != null) {
        AsyncImage(
            model = imgUri,
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