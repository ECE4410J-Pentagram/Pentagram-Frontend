package cn.edu.sjtu.patrickli.cryptex.view.key

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cn.edu.sjtu.patrickli.cryptex.model.Key

@Composable
fun KeyItemWithDiv(
    key: Key,
    onRemove: () -> Unit,
    onRename: (Key) -> Unit,
    onClick: () -> Unit = {}
) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple()
            ) { onClick() }
    )
    {
        KeyItem(key = key, onRemove = onRemove, onRename = onRename)
        Divider()
    }
}
