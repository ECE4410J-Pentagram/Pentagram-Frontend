package cn.edu.sjtu.patrickli.cryptex.SendViews

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectReceiverView(context: Context, navController: NavHostController) {
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
                val (col) = createRefs()
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(col) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Text(
                        text = stringResource(R.string.chooseReceiver),
                        fontSize = 30.sp,
                        modifier = Modifier.padding(bottom = 50.dp)
                    )
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(R.string.importReceiver))
                    }
                    Button(onClick = {
                        navController.navigate("SelectContactView")
                    }) {
                        Text(text = stringResource(R.string.contact))
                    }
                    Button(onClick = { /*TODO*/ }) {
                        Text(text = stringResource(R.string.noneReceiver))
                    }
                }
            }
        }
    )
}