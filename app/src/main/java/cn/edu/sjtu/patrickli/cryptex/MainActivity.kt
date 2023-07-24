package cn.edu.sjtu.patrickli.cryptex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.edu.sjtu.patrickli.cryptex.model.ApplicationStart
import cn.edu.sjtu.patrickli.cryptex.model.database.DatabaseProvider
import cn.edu.sjtu.patrickli.cryptex.view.HomeView
import cn.edu.sjtu.patrickli.cryptex.view.contact.AcceptInvitationView
import cn.edu.sjtu.patrickli.cryptex.view.contact.InvitationListView
import cn.edu.sjtu.patrickli.cryptex.view.contact.SelectContactView
import cn.edu.sjtu.patrickli.cryptex.view.contact.SendInvitationView
import cn.edu.sjtu.patrickli.cryptex.view.key.KeyView
import cn.edu.sjtu.patrickli.cryptex.view.qrcode.QrCodeScanView
import cn.edu.sjtu.patrickli.cryptex.view.qrcode.QrCodeView
import cn.edu.sjtu.patrickli.cryptex.view.receiver.DecryptOutputView
import cn.edu.sjtu.patrickli.cryptex.view.receiver.DecryptView
import cn.edu.sjtu.patrickli.cryptex.view.sender.EncryptOutputView
import cn.edu.sjtu.patrickli.cryptex.view.sender.EncryptView
import cn.edu.sjtu.patrickli.cryptex.view.sender.SelectReceiverView
import cn.edu.sjtu.patrickli.cryptex.viewmodel.KeyViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModelProvider: ViewModelProvider
    private lateinit var databaseProvider: DatabaseProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelProvider = ViewModelProvider(
            this, MainViewModelFactory(this)
        )
        databaseProvider = DatabaseProvider(this)
        ApplicationStart.init(this@MainActivity, viewModelProvider, databaseProvider)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "HomeView") {
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
                        onKeyClick = { key ->
                            viewModelProvider[KeyViewModel::class.java].keyToShare = key
                            navController.popBackStack()
                        }
                    )
                }
                composable("SendView") {
                    SelectReceiverView(
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
                composable("DecryptView") {
                    DecryptView(
                        context = this@MainActivity,
                        navController = navController,
                        viewModelProvider = viewModelProvider
                    )
                }
                composable("DecryptOutputView") {
                    DecryptOutputView(
                        context = this@MainActivity,
                        navController = navController,
                        viewModelProvider = viewModelProvider
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
            }
        }
    }
}