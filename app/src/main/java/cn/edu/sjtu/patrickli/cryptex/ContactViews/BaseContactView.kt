package cn.edu.sjtu.patrickli.cryptex.ContactViews

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.testContacts

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseContactView(context: Context, navController: NavHostController) {
    val contacts = testContacts
    Scaffold(
        topBar = {
            NavBackBar(navController = navController)
        },
        content = { paddingSize ->
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(paddingSize)
            ) {
                items(count = contacts.size, key = { index -> index }) {
                    index -> ContactItem(index = index, contact = contacts[index])
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 8.dp),
                onClick = {
                }
            ) {
                Icon(Icons.Default.Add, stringResource(R.string.addContact))
            }
        },
    )
}
