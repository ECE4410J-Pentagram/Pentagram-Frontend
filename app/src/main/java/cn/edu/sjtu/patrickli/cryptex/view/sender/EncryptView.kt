package cn.edu.sjtu.patrickli.cryptex.view.sender

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.MediaType
import cn.edu.sjtu.patrickli.cryptex.view.dialog.LoadingDialog
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBarWithDone
import cn.edu.sjtu.patrickli.cryptex.viewmodel.EncrypterViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncryptView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider
) {
    val encrypterViewModel = viewModelProvider[EncrypterViewModel::class.java]
    var inputText by remember { mutableStateOf(encrypterViewModel.plainText?:"") }
    var mediaTypeDropdownMenuExpanded by remember { mutableStateOf(false) }
    val availableMediaTypeNames = stringArrayResource(R.array.mediaTypes)
    var selectedMediaType by remember { mutableStateOf(encrypterViewModel.mediaType) }
    fun onInputDone() {
        encrypterViewModel.plainText = inputText
        encrypterViewModel.mediaType = selectedMediaType
        encrypterViewModel.doEncrypt {
            navController.navigate("EncryptOutputView")
        }
    }
    Scaffold(
        topBar = {
            NavBackBarWithDone(
                navController = navController,
                onDone = { onInputDone() }
            )
        },
        content = { paddingValues ->
            ConstraintLayout(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    encrypterViewModel.contact?.let {
                        Text(
                            text = stringResource(R.string.encryptReceiverTitle),
                            fontSize = 16.sp
                        )
                        Column(
                        ) {
                            Divider()
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp)
                                    .padding(horizontal = 8.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = rememberRipple()
                                    ) { navController.popBackStack() }
                            ) {
                                Icon(Icons.Default.AccountCircle, contentDescription = null)
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = it.name?:"",
                                    fontSize = 17.sp
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(Icons.Default.ChevronRight, contentDescription = null)
                            }
                            Divider()
                        }
                    } ?: let {
                        Text(
                            text = stringResource(R.string.encryptNoReceiverTitle),
                            fontSize = 16.sp
                        )
                    }
                    ExposedDropdownMenuBox(
                        expanded = mediaTypeDropdownMenuExpanded,
                        onExpandedChange = {
                            mediaTypeDropdownMenuExpanded = !mediaTypeDropdownMenuExpanded
                        }
                    ) {
                        TextField(
                            readOnly = true,
                            value = availableMediaTypeNames[selectedMediaType.type],
                            onValueChange = {},
                            label = {
                                Text(text = stringResource(R.string.outputFormat))
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = mediaTypeDropdownMenuExpanded)
                            },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = mediaTypeDropdownMenuExpanded,
                            onDismissRequest = {
                                mediaTypeDropdownMenuExpanded = false
                            }
                        ) {
                            availableMediaTypeNames.forEachIndexed { index, option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        selectedMediaType = MediaType.values()[index]
                                        mediaTypeDropdownMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 15.dp),
                        placeholder = {
                            Text(text = stringResource(R.string.inputTextPlaceHolder))
                        }
                    )
                }
            }
            if (encrypterViewModel.isEncrypting) {
                LoadingDialog()
            }
        }
    )
}