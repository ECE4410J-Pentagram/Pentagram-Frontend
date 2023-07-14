package cn.edu.sjtu.patrickli.cryptex.SendViews

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.ContactViews.ContactItem
import cn.edu.sjtu.patrickli.cryptex.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.ContactViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptView(context: Context, navController: NavHostController, contactViewModel: ContactViewModel) {
    var inputText by remember { mutableStateOf("") }
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
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    contactViewModel.selectedContact?.let {
                        Text(
                            text = stringResource(R.string.encryptReceiverTitle),
                            fontSize = 16.sp
                        )
                        ContactItem(
                            index = 0,
                            contact = it,
                            onContactClick = {
                                navController.popBackStack()
                            }
                        )
                    } ?: let {
                        Text(
                            text = stringResource(R.string.encryptNoReceiverTitle),
                            fontSize = 16.sp
                        )
                    }
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 15.dp)
                        ,
                        placeholder = {
                            Text(text = stringResource(R.string.inputTextPlaceHolder))
                        }
                    )
                }
            }
        }
    )
}