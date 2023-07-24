package cn.edu.sjtu.patrickli.cryptex.view

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
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

@Composable
fun ConstraintLayoutContent(
    paddingValues: PaddingValues,
    navController: NavHostController
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
                modifier = imageModifier.padding(bottom = 50.dp)
            )
            HomeViewButton(text = "Send", navController, "SendView")
            HomeViewButton(text = "Receive", navController, "DecryptView")
            HomeViewButton(text = "Keys", navController, "KeyView")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    Scaffold(
        topBar = {
            HomeTopBar(navController, viewModelProvider)
        },
        content = { padding ->
            ConstraintLayoutContent(padding, navController)
        }
    )
}