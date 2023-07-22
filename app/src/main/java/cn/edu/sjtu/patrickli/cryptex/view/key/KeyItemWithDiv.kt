package cn.edu.sjtu.patrickli.cryptex.view.key

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cn.edu.sjtu.patrickli.cryptex.model.Key

@Composable
fun KeyItemWithDiv(
    key: Key,
    onRemove: () -> Unit,
    onRename: (Key) -> Unit
) {
    Column (
        modifier = Modifier.fillMaxWidth()
    )
    {
        KeyItem(key = key, onRemove = onRemove, onRename = onRename)
        Divider()
    }
}
