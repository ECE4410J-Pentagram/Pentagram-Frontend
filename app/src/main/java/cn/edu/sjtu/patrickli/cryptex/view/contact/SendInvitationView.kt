package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.view.button.BaseWideButton
import cn.edu.sjtu.patrickli.cryptex.view.dialog.LoadingDialog
import cn.edu.sjtu.patrickli.cryptex.viewmodel.InvitationViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.KeyViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.RequestViewModel

@Composable
fun SendInvitationView(
    context: Context,
    viewModelProvider: ViewModelProvider,
    navController: NavHostController
) {
    val requestViewModel = viewModelProvider[RequestViewModel::class.java]
    val keyViewModel = viewModelProvider[KeyViewModel::class.java]
    var showLoadingDialog by remember { mutableStateOf(false) }
    val keyIsNotNull by remember { mutableStateOf(
        (keyViewModel.keyToShare != null) || (keyViewModel.defaultKeyAlias != null)
    ) }
    BaseInvitationView(
        context = context,
        viewModelProvider = viewModelProvider,
        navController = navController
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        BaseWideButton(
            enabled = keyIsNotNull,
            onClick = {
                showLoadingDialog = true
                requestViewModel.requestQueue.add(requestViewModel.requestStore.getSendInvitationRequest(
                    viewModelProvider,
                    onResponse = {
                        viewModelProvider[InvitationViewModel::class.java].updateInvitationList(viewModelProvider)
                        viewModelProvider[KeyViewModel::class.java].keyToShare = null
                        Log.d("FriendRequest", "Success")
                        Toast.makeText(context, context.getString(R.string.addFriendSuccess), Toast.LENGTH_SHORT).show()
                        showLoadingDialog = false
                        navController.navigate("HomeView") { popUpTo(0) }
                    },
                    onError = {
                        Log.d("FriendRequest", "Fail")
                        it.printStackTrace()
                        Toast.makeText(context, context.getString(R.string.unknownError), Toast.LENGTH_SHORT).show()
                        showLoadingDialog = false
                        navController.navigate("HomeView") { popUpTo(0) }
                    }
                ))
            }
        ) {
            Text(text = stringResource(R.string.addFriend))
        }
        if (showLoadingDialog) {
            LoadingDialog()
        }
    }
}