package cn.edu.sjtu.patrickli.cryptex.KeyViews

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.R
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import java.util.function.Predicate


@Composable
fun KeyView(key: Key, index: Int, onRemove: (name: String) -> Unit) {
    val deleteKey = SwipeAction(
        icon = rememberVectorPainter(image = Icons.TwoTone.Delete),
        background = Color.Red,
        onSwipe = { onRemove(key.name) }
    )
    SwipeableActionsBox (
        endActions = listOf(deleteKey),
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp, 8.dp, 4.dp, 4.dp),
        ) {
            Text(
                text = key.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(4.dp, 8.dp, 4.dp, 0.dp),
                maxLines = 1
            )
            Text(
                text = key.pk,
                fontSize = 14.sp,
                textAlign = TextAlign.End,
                modifier = Modifier.padding(4.dp, 8.dp, 4.dp, 0.dp),
                maxLines = 2
            )
        }
    }
}

@Composable
fun KeyViewWithDiv(key: Key, index: Int, onRemove: (name: String) -> Unit) {
    Column (
        modifier = Modifier.fillMaxWidth()
            )
    {
        KeyView(key = key, index = index, onRemove = onRemove)
        Divider()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeyListView(context: Context, navController: NavHostController) {
    var keyList = testKeys
    var addDialogOpen by remember {
        mutableStateOf<Boolean>(false)
    }
    fun onRemove(name: String) {
        Log.d("KeyView","Remove Called: " + keyList.size)
        keyList.removeIf(Predicate { key -> key.name == name })
        Log.d("KeyView","Remove Called: " + keyList.size)

    }
    Scaffold(
        topBar = {
            NavBackBar(navController = navController)
        },
        content = { padding ->
            LazyColumn(
                verticalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .padding(padding)
            ) {
                items(count = keyList.size) { index ->
                    KeyViewWithDiv(keyList[index], index, ::onRemove)
                }
            }
            if (addDialogOpen) {
                Dialog(onDismissRequest = { addDialogOpen = false }) {
                    Card (
                        Modifier
                            .fillMaxWidth(1F)
                            .defaultMinSize(minHeight = 300.dp)) {
                        Text(text = "Under Construction")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 8.dp),
                onClick = {
                    // navigate to PostView
                    addDialogOpen = true
                }
            ) {
                Icon(Icons.Default.Add, stringResource(R.string.addKey))
            }
        },
    )
}