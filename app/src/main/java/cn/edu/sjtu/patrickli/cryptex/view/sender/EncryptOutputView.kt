package cn.edu.sjtu.patrickli.cryptex.view.sender

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.EncrypterViewModel
import cn.edu.sjtu.patrickli.cryptex.model.FileHandler.saveImgToPublicDownload
import cn.edu.sjtu.patrickli.cryptex.model.MediaType
import cn.edu.sjtu.patrickli.cryptex.view.button.BaseWideButton
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import coil.compose.AsyncImage
import java.io.File

@Composable
fun OutputPreviewLoader(
    context: Context,
    encrypterViewModel: EncrypterViewModel
) {
    val imgUri = try {
        encrypterViewModel.cipherImgFile?.let {
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
            contentDescription = stringResource(R.string.defaultImage),
            modifier = Modifier
                .size(300.dp)
                .border(1.dp, Color.Black)
        )
    } else {
        Image(
            Icons.Outlined.Image,
            contentDescription = stringResource(R.string.defaultImage),
            modifier = Modifier
                .size(300.dp)
                .border(1.dp, Color.Black)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptOutputView(
    context: Context,
    navController: NavHostController,
    encrypterViewModel: EncrypterViewModel
) {
    Scaffold(
        topBar = {
            NavBackBar(navController = navController)
        },
        content = { paddingValues ->
            ConstraintLayout (
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                if (encrypterViewModel.isEncrypting) {
                    CircularProgressIndicator()
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    ) {
                        OutputPreviewLoader(context, encrypterViewModel)
                        Text(
                            text = stringResource(R.string.encryptDone) + " " + (
                                    encrypterViewModel.contact
                                        ?.let {
                                            stringResource(
                                                R.string.encryptOutputShareTo,
                                                it.name ?: { "noname" })
                                        }
                                        ?: let { stringResource(R.string.encryptOutputShareAny) }
                                    ),
                            fontSize = 16.sp,
                            modifier = Modifier
                                .padding(16.dp, 10.dp)
                        )
                        BaseWideButton(onClick = {
                            saveImgToPublicDownload(context, encrypterViewModel)
                        }) {
                            Text(text = stringResource(R.string.download))
                        }
                        BaseWideButton(onClick = {
                            shareContent(context, MediaType.IMAGE, encrypterViewModel.cipherImgFile)
                        }) {
                            Text(text = stringResource(R.string.share))
                        }
                        BaseWideButton(onClick = {
                            navController.navigate("HomeView") { popUpTo(0) }
                        }) {
                            Text(text = stringResource(R.string.navHome))
                        }
                    }
                }
            }
        }
    )
}

fun shareContent(context: Context, type: MediaType, file: File? = null) {
    val intent = Intent(Intent.ACTION_SEND)
    val shareWith = "ShareWith"
    when (type) {
        MediaType.TEXT -> {
            val subject = "Your subject"
            val extraText = "My shared content"
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, extraText)
        }
        MediaType.IMAGE -> {
            if (file != null) {
                val uri = FileProvider.getUriForFile(
                    context, context.packageName + ".provider", file
                )
                intent.type = "image/jpeg"
                intent.clipData = ClipData.newRawUri("", uri)
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                intent.putExtra(Intent.EXTRA_STREAM, uri)
            }
        }
    }
    ContextCompat.startActivity(
        context,
        Intent.createChooser(intent, shareWith),
        null
    )
}