package cn.edu.sjtu.patrickli.cryptex.view.tool

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.ThemePreference
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.UserViewModel

@Composable
fun ThemeSelectionRow(
    context: Context,
    text: String,
    userViewModel: UserViewModel,
    themePreference: ThemePreference
) {
    fun onClick() {
        userViewModel.themePreference = themePreference
        userViewModel.writeToConfigFile(context)
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { onClick() }
    ) {
        RadioButton(
            selected = (userViewModel.themePreference == themePreference),
            onClick = { onClick() }
        )
        Text(text = text)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val userViewModel = viewModelProvider[UserViewModel::class.java]
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            NavBackBar(navController = navController, title = stringResource(R.string.setting))
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.theme),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp, 10.dp)
                )
                ThemeSelectionRow(context, stringResource(R.string.auto), userViewModel, ThemePreference.AUTO)
                ThemeSelectionRow(context, stringResource(R.string.light), userViewModel, ThemePreference.LIGHT)
                ThemeSelectionRow(context, stringResource(R.string.dark), userViewModel, ThemePreference.DARK)
                Divider()
                Text(
                    text = stringResource(R.string.about),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp, 10.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = rememberRipple()
                        ) {
                            uriHandler.openUri(context.getString(R.string.teamGithubLink))
                        }
                ) {
                    Icon(
                        painterResource(R.drawable.icons8_github),
                        contentDescription = stringResource(R.string.github),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Text(
                        text = stringResource(R.string.teamName),
                        fontWeight = FontWeight.Bold
                    )
                }
                Divider()
                Image(
                    painterResource(R.drawable.cryptex),
                    contentDescription = stringResource(R.string.logo_name),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .size(120.dp)
                )
                Text(
                    text = stringResource(R.string.appVersion),
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .offset(y = (-30).dp)
                )
            }
        }
    }
}