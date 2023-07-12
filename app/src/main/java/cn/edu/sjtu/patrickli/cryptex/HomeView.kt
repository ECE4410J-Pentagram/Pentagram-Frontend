package cn.edu.sjtu.patrickli.cryptex

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController

@Composable
fun HomeViewBtn(text: String, navController: NavHostController, targetActivity: String) {
    Button(
        onClick = {
                  navController.navigate(targetActivity)
                  },
        modifier = Modifier.padding(1.dp)
        // Assign reference "button" to the Button composable
        // and constrain it to the top of the ConstraintLayout
    ) {
        Text(text)
    }
}

@Composable
fun ConstraintLayoutContent(paddingValues: PaddingValues, navController: NavHostController) {
    ConstraintLayout (
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            ) {
        // Create references for the composables to constrain
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
                ){
            Image(
                painter = painterResource(id = R.drawable.cryptex),
                contentDescription = stringResource(id = R.string.logo_name),
                contentScale = ContentScale.Fit,
                modifier = imageModifier.padding(10.dp)
            )
            HomeViewBtn(text = "Send", navController, "")
            HomeViewBtn(text = "Receive", navController, "")
            HomeViewBtn(text = "Keys", navController, "")
        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(context: Context, navController: NavHostController) {
    Scaffold(
        topBar = {
            BaseTopBar()
        },
        content = { padding -> ConstraintLayoutContent(padding, navController = navController)
        }
    )
}