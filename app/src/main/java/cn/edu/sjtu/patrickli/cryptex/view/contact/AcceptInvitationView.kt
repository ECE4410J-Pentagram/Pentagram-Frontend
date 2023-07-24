package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.view.button.BaseWideButton

@Composable
fun AcceptInvitationView(
    context: Context,
    viewModelProvider: ViewModelProvider,
    navController: NavHostController
) {
    BaseInvitationView(
        context = context,
        viewModelProvider = viewModelProvider,
        navController = navController
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        BaseWideButton(onClick = { navController.navigate("HomeView") { popUpTo(0) } }) {
            Text(text = stringResource(R.string.acceptFriend))
        }
    }
}