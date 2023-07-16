package cn.edu.sjtu.patrickli.cryptex.view.sender

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
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
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.ContactViewModel
import cn.edu.sjtu.patrickli.cryptex.view.button.BaseWideButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptOutputView(context: Context, navController: NavHostController, contactViewModel: ContactViewModel) {
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Image(
                        Icons.Outlined.Image,
                        contentDescription = stringResource(R.string.defaultImage),
                        modifier = Modifier
                            .size(300.dp)
                            .border(1.dp, Color.Black)
                    )
                    Text(
                        text = stringResource(R.string.encryptDone) + " " + (
                                contactViewModel.selectedContact
                                    ?. let { stringResource(R.string.encryptOutputShareTo, it.name?:{"noname"}) }
                                    ?: let { stringResource(R.string.encryptOutputShareAny) }
                                ),
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(16.dp, 10.dp)
                    )
                    BaseWideButton(onClick = { shareContent(context) }) {
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
    )
}

fun shareContent(context: Context) {
    val type = "text/plain"
    val subject = "Your subject"
    val extraText = "My shared content"
    val shareWith = "ShareWith"

    val intent = Intent(Intent.ACTION_SEND)
    intent.type = type
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, extraText)

    ContextCompat.startActivity(
        context,
        Intent.createChooser(intent, shareWith),
        null
    )
}