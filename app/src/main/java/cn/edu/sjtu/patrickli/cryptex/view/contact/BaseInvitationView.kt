package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.ContactViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.KeyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseInvitationView(
    context: Context,
    viewModelProvider: ViewModelProvider,
    navController: NavHostController,
    content: @Composable() () -> Unit
) {
    val contactViewModel = viewModelProvider[ContactViewModel::class.java]
    val keyViewModel = viewModelProvider[KeyViewModel::class.java]
    if (keyViewModel.keyToShare == null) {
        keyViewModel.keyToShare =
            keyViewModel.myKeys.find { it.alias == keyViewModel.defaultKeyAlias }
    }
    Scaffold(
        topBar = {
            NavBackBar(navController = navController, title = stringResource(R.string.newFriend))
        }
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp, 4.dp)
            ) {
                contactViewModel.contact?.let { contact ->
                    ContactCard(contact)
                }
                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 30.dp)
                ) {
                    Divider()
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .padding(horizontal = 8.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple()
                            ) { navController.navigate("SelectKeyView") }
                    ) {
                        Icon(Icons.Default.Key, contentDescription = null)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = keyViewModel.keyToShare?.name?:stringResource(R.string.selectKey),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(Icons.Default.ChevronRight, contentDescription = null)
                    }
                    Divider()
                }
                content()
            }
        }
    }
}