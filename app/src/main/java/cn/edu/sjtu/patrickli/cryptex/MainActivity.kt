package cn.edu.sjtu.patrickli.cryptex

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModelProvider
import cn.edu.sjtu.patrickli.cryptex.model.ApplicationStart
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.ui.theme.AppTheme
import cn.edu.sjtu.patrickli.cryptex.view.HomeView
import cn.edu.sjtu.patrickli.cryptex.view.contact.AcceptInvitationView
import cn.edu.sjtu.patrickli.cryptex.view.contact.ContactListView
import cn.edu.sjtu.patrickli.cryptex.view.contact.InvitationListView
import cn.edu.sjtu.patrickli.cryptex.view.contact.SelectContactView
import cn.edu.sjtu.patrickli.cryptex.view.contact.SendInvitationView
import cn.edu.sjtu.patrickli.cryptex.view.key.KeyView
import cn.edu.sjtu.patrickli.cryptex.view.qrcode.QrCodeScanView
import cn.edu.sjtu.patrickli.cryptex.view.qrcode.QrCodeView
import cn.edu.sjtu.patrickli.cryptex.view.receiver.DecryptOutputView
import cn.edu.sjtu.patrickli.cryptex.view.sender.EncryptOutputView
import cn.edu.sjtu.patrickli.cryptex.view.sender.EncryptView
import cn.edu.sjtu.patrickli.cryptex.view.tool.SettingView
import cn.edu.sjtu.patrickli.cryptex.viewmodel.InvitationViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.KeyViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.MainViewModelFactory
import cn.edu.sjtu.patrickli.cryptex.viewmodel.UserViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var databaseProvider: DatabaseProvider
    private var mainHandler: Handler? = null
    private val updateInvitationListTask = object : Runnable {
        override fun run() {
            viewModelProvider[InvitationViewModel::class.java].updateInvitationList(viewModelProvider)
            mainHandler?.postDelayed(this, 10000)
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelProvider = ViewModelProvider(
            this, MainViewModelFactory(this)
        )
        databaseProvider = DatabaseProvider(this)
        ApplicationStart.init(this@MainActivity, viewModelProvider, databaseProvider) {
            mainHandler = Handler(Looper.getMainLooper())
            mainHandler!!.post(updateInvitationListTask)
        }
        setContent {
            val navController = rememberAnimatedNavController()
            val animationSpec = tween<IntOffset>(
                durationMillis = 200,
                easing = FastOutSlowInEasing
            )
            AppTheme(viewModelProvider[UserViewModel::class.java]) {
                AnimatedNavHost(
                    navController,
                    startDestination = "HomeView",
                    enterTransition = {
                        slideIn(animationSpec) { fullSize -> IntOffset(fullSize.width, 0) }
                    },
                    popEnterTransition = {
                        slideIn(animationSpec) { fullSize -> IntOffset(-fullSize.width, 0) }
                    },
                    exitTransition = {
                        slideOut(animationSpec) { fullSize -> IntOffset(-fullSize.width, 0) }
                    },
                    popExitTransition = {
                        slideOut(animationSpec) { fullSize -> IntOffset(fullSize.width, 0) }
                    }
                ) {
                    composable("HomeView") {
                        HomeView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("KeyView") {
                        KeyView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider,
                            databaseProvider = databaseProvider
                        )
                    }
                    composable("SelectKeyView") {
                        KeyView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider,
                            databaseProvider = databaseProvider,
                            title = stringResource(R.string.selectKey),
                            onKeyClick = { key ->
                                viewModelProvider[KeyViewModel::class.java].keyToShare = key
                                navController.popBackStack()
                            }
                        )
                    }
                    composable("ContactListView") {
                        ContactListView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("SelectContactView") {
                        SelectContactView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("EncryptView") {
                        EncryptView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("EncryptOutputView") {
                        EncryptOutputView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("DecryptOutputView") {
                        DecryptOutputView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider,
                            databaseProvider = databaseProvider
                        )
                    }
                    composable("QrCodeView") {
                        QrCodeView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("QrCodeScanView") {
                        QrCodeScanView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("SendInvitationView") {
                        SendInvitationView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("AcceptInvitationView") {
                        AcceptInvitationView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("InvitationView") {
                        InvitationListView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                    composable("SettingView") {
                        SettingView(
                            context = this@MainActivity,
                            navController = navController,
                            viewModelProvider = viewModelProvider
                        )
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        mainHandler?.removeCallbacks(updateInvitationListTask)
    }

    override fun onResume() {
        super.onResume()
        mainHandler?.post(updateInvitationListTask)
    }

}