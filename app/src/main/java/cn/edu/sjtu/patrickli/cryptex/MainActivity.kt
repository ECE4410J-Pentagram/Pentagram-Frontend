package cn.edu.sjtu.patrickli.cryptex

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
import cn.edu.sjtu.patrickli.cryptex.ContactViews.SelectContactView
import cn.edu.sjtu.patrickli.cryptex.KeyViews.KeyListView
import cn.edu.sjtu.patrickli.cryptex.SendViews.SelectReceiverView
import cn.edu.sjtu.patrickli.cryptex.ui.theme.CryptexTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    SelectReceiverView(context = this@MainActivity, navController = navController)
                }
                composable("SelectContactView") {
                    SelectContactView(context = this@MainActivity, navController = navController)
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