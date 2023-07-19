package cn.edu.sjtu.patrickli.cryptex.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.edu.sjtu.patrickli.cryptex.model.ApplicationStart
import cn.edu.sjtu.patrickli.cryptex.model.DecryptState
import cn.edu.sjtu.patrickli.cryptex.model.EncrypterViewModel
import cn.edu.sjtu.patrickli.cryptex.view.HomeView
import cn.edu.sjtu.patrickli.cryptex.view.contact.SelectContactView
import cn.edu.sjtu.patrickli.cryptex.view.key.KeyListView
import cn.edu.sjtu.patrickli.cryptex.view.receiver.DecryptOutputView
import cn.edu.sjtu.patrickli.cryptex.view.receiver.DecryptView
import cn.edu.sjtu.patrickli.cryptex.view.sender.EncryptOutputView
import cn.edu.sjtu.patrickli.cryptex.view.sender.EncryptView
import cn.edu.sjtu.patrickli.cryptex.view.sender.SelectReceiverView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val encrypterViewModel = EncrypterViewModel(this@MainActivity)
        val decryptState = DecryptState()
        ApplicationStart.init(this@MainActivity)
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "HomeView") {
                composable("HomeView") {
                    HomeView(
                        context = this@MainActivity,
                        navController = navController
                    )
                }
                composable("KeyView") {
                    KeyListView(context = this@MainActivity, navController = navController)
                }
                composable("SendView") {
                    SelectReceiverView(
                        context = this@MainActivity,
                        navController = navController,
                        encrypterViewModel = encrypterViewModel
                    )
                }
                composable("SelectContactView") {
                    SelectContactView(
                        context = this@MainActivity,
                        navController = navController,
                        encrypterViewModel = encrypterViewModel
                    )
                }
                composable("EncryptView") {
                    EncryptView(
                        context = this@MainActivity,
                        navController = navController,
                        encrypterViewModel = encrypterViewModel
                    )
                }
                composable("EncryptOutputView") {
                    EncryptOutputView(
                        context = this@MainActivity,
                        navController = navController,
                        encrypterViewModel = encrypterViewModel
                    )
                }
                composable("DecryptView") {
                    DecryptView(
                        context = this@MainActivity,
                        navController = navController,
                        decryptState = decryptState
                    )
                }
                composable("DecryptOutputView") {
                    DecryptOutputView(
                        context = this@MainActivity,
                        navController = navController,
                        decryptState = decryptState
                    )
                }
            }
        }
    }
}