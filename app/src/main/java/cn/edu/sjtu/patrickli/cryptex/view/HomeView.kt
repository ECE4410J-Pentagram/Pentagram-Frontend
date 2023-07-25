package cn.edu.sjtu.patrickli.cryptex.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.FileUpload
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Key
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.view.button.HomeViewButton
import cn.edu.sjtu.patrickli.cryptex.view.topbar.HomeTopBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.ContactViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConstraintLayoutContent(
    paddingValues: PaddingValues,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    ConstraintLayout (
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            ) {
        val (col) = createRefs()
        val imageModifier = Modifier

        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(col) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.cryptex),
                contentDescription = stringResource(id = R.string.logo_name),
                contentScale = ContentScale.Fit,
                modifier = imageModifier.padding(bottom = 70.dp)
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterVertically),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HomeViewButton(
                        Icons.Outlined.FileUpload,
                        "Send",
                        navController,
                        "SelectContactView"
                    )
                    HomeViewButton(
                        Icons.Outlined.FileDownload,
                        "Receive",
                        navController,
                        "DecryptView"
                    )
                }
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(50.dp, Alignment.CenterHorizontally),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HomeViewButton(
                        Icons.Outlined.Group,
                        "Contacts",
                        navController,
                        "ContactListView",
                        badge = {
                            if (viewModelProvider[ContactViewModel::class.java].hasNewContact) {
                                Badge(
                                    modifier = Modifier.offset((-15).dp, 15.dp)
                                ) { Text(text = "") }
                            }
                        }
                    )
                    HomeViewButton(
                        Icons.Outlined.Key,
                        "Keys",
                        navController,
                        "KeyView"
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val notificationPermissionState = rememberPermissionState(android.Manifest.permission.POST_NOTIFICATIONS)
    LaunchedEffect(Unit) {
        if (!notificationPermissionState.status.isGranted) {
            notificationPermissionState.launchPermissionRequest()
        }
    }
    Scaffold(
        topBar = {
            HomeTopBar(navController, viewModelProvider)
        },
        content = { padding ->
            ConstraintLayoutContent(padding, navController, viewModelProvider)
        }
    )
}