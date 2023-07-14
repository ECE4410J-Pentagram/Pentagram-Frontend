package cn.edu.sjtu.patrickli.cryptex.ContactViews

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Contact

@Composable
fun ContactItem(index: Int, contact: Contact) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth(1f)
            .padding(8.dp, 0.dp, 8.dp, 0.dp)
            .background(
                color = colorResource(if (index % 2 == 0) R.color.deepGray else R.color.lightGray),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {}
    ) {
        Icon(
            Icons.Rounded.AccountBox,
            contentDescription = stringResource(R.string.defaultContactIconCircle),
            modifier = Modifier
                .size(64.dp)
                .padding(4.dp, 2.dp)
        )
        contact.name?.let {
            Text(it, fontSize = 17.sp, modifier = Modifier.padding(horizontal = 4.dp))
        }
    }
}