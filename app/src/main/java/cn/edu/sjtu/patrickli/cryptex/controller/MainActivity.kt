package cn.edu.sjtu.patrickli.cryptex.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.edu.sjtu.patrickli.cryptex.model.ApplicationStart
import cn.edu.sjtu.patrickli.cryptex.model.viewmodel.MainViewModelFactory
import cn.edu.sjtu.patrickli.cryptex.view.HomeView
import cn.edu.sjtu.patrickli.cryptex.view.contact.SelectContactView
import cn.edu.sjtu.patrickli.cryptex.view.key.KeyListView
import cn.edu.sjtu.patrickli.cryptex.view.receiver.DecryptOutputView
import cn.edu.sjtu.patrickli.cryptex.view.receiver.DecryptView
import cn.edu.sjtu.patrickli.cryptex.view.sender.EncryptOutputView
import cn.edu.sjtu.patrickli.cryptex.view.sender.EncryptView
import cn.edu.sjtu.patrickli.cryptex.view.sender.SelectReceiverView

class MainActivity : ComponentActivity() {
    private lateinit var viewModelProvider: ViewModelProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModelProvider = ViewModelProvider(
            this, MainViewModelFactory(this)
        )
        super.onCreate(savedInstanceState)
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
            }
        }
    }
}