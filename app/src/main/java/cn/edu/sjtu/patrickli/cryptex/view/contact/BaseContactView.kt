package cn.edu.sjtu.patrickli.cryptex.view.contact

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import cn.edu.sjtu.patrickli.cryptex.R
import cn.edu.sjtu.patrickli.cryptex.model.Contact
import cn.edu.sjtu.patrickli.cryptex.view.dialog.LoadingDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.NoReceiverDialog
import cn.edu.sjtu.patrickli.cryptex.view.dialog.RemoveContactDialog
import cn.edu.sjtu.patrickli.cryptex.view.tool.BlankPageView
import cn.edu.sjtu.patrickli.cryptex.view.topbar.NavBackBar
import cn.edu.sjtu.patrickli.cryptex.viewmodel.ContactViewModel
import cn.edu.sjtu.patrickli.cryptex.viewmodel.EncrypterViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseContactView(
    context: Context,
    navController: NavHostController,
    viewModelProvider: ViewModelProvider,
    title: String = stringResource(R.string.contacts),
    allowNoUser: Boolean = false,
    onContactClick: (Contact) -> Unit
) {
    val contactViewModel = viewModelProvider[ContactViewModel::class.java]
    val encrypterViewModel = viewModelProvider[EncrypterViewModel::class.java]
    val contacts = contactViewModel.contactList
    var isRefreshing by remember { mutableStateOf(false) }
    var showRemoveContactWarningDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    var selectedContact by remember { mutableStateOf(Contact()) }
    var showNoReceiverWarningDialog by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        contactViewModel.updateContactList(viewModelProvider, true)
    }
    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            contactViewModel.updateContactList(viewModelProvider, true)
            delay(1000)
            isRefreshing = false
        }
    }
    fun onContactDelete(contact: Contact) {
        showRemoveContactWarningDialog = true
        selectedContact = contact
    }
    Scaffold(
        topBar = {
            NavBackBar(navController = navController, title = title)
        },
        content = {
            ConstraintLayout(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = { isRefreshing = true }
                ) {
                    if ((contactViewModel.contactCount == 0) && (!allowNoUser)) {
                        BlankPageView {
                            Text(
                                text = stringResource(R.string.noContacts),
                                fontSize = 17.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            if (allowNoUser) ContactItem(
                                contact = Contact(),
                                onContactClick = { showNoReceiverWarningDialog = true }
                            )
                            contacts.map { contact ->
                                ContactItem(
                                    contact = contact,
                                    onContactClick = onContactClick,
                                    onDelete = ::onContactDelete
                                )
                            }
                        }
                    }
                }
            }
            if (showLoadingDialog) {
                LoadingDialog()
            }
            if (showRemoveContactWarningDialog) {
                RemoveContactDialog(
                    contact = selectedContact,
                    onRemove = {
                        showRemoveContactWarningDialog = false
                        showLoadingDialog = true
                        contactViewModel.contact = selectedContact
                        contactViewModel.deleteContact(viewModelProvider) {
                            showLoadingDialog = false
                        }
                    },
                    onClose = {
                        showRemoveContactWarningDialog = false
                    }
                )
            }
            if (showNoReceiverWarningDialog) {
                NoReceiverDialog(
                    onConfirm = {
                        showNoReceiverWarningDialog = false
                        encrypterViewModel.contact = null
                        showNoReceiverWarningDialog = false
                        navController.navigate("EncryptView")
                    },
                    onClose = { showNoReceiverWarningDialog = false }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.padding(0.dp, 0.dp, 8.dp, 8.dp),
                onClick = {
                    navController.navigate("QrCodeView")
                }
            ) {
                Icon(Icons.Default.Add, stringResource(R.string.addContact))
            }
        }
    )
}
