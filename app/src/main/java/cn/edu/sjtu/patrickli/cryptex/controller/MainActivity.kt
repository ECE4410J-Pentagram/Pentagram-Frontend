package cn.edu.sjtu.patrickli.cryptex.controller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cn.edu.sjtu.patrickli.cryptex.model.ContactViewModel
import cn.edu.sjtu.patrickli.cryptex.model.DecryptState
import cn.edu.sjtu.patrickli.cryptex.ui.theme.CryptexTheme
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
        val contactViewModel = ContactViewModel()
        val decryptState = DecryptState()
        setContent {
            val navController = rememberNavController()
            NavHost(navController, startDestination = "HomeView") {
                composable("HomeView") {
                    HomeView(this@MainActivity, navController)
                }
                composable("KeyView") {
                    KeyListView(context = this@MainActivity, navController = navController)
                }
                composable("SendView") {
                    SelectReceiverView(
                        context = this@MainActivity,
                        navController = navController,
                        contactViewModel = contactViewModel
                    )
                }
                composable("SelectContactView") {
                    SelectContactView(
                        context = this@MainActivity,
                        navController = navController,
                        contactViewModel = contactViewModel
                    )
                }
                composable("EncryptView") {
                    EncryptView(
                        context = this@MainActivity,
                        navController = navController,
                        contactViewModel = contactViewModel
                    )
                }
                composable("EncryptOutputView") {
                    EncryptOutputView(
                        context = this@MainActivity,
                        navController = navController,
                        contactViewModel = contactViewModel
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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CryptexTheme {
        Greeting("Android")
    }
}