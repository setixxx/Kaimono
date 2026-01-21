package software.setixx.kaimono.presentation.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import software.setixx.kaimono.R
import software.setixx.kaimono.presentation.account.address.AddressSheet
import software.setixx.kaimono.presentation.account.address.AddressViewModel
import software.setixx.kaimono.presentation.account.paymnetmethod.PaymentMethodsSheet
import software.setixx.kaimono.presentation.account.paymnetmethod.PaymentMethodsViewModel
import software.setixx.kaimono.presentation.navigation.Routes


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AccountScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: AccountViewModel
) {
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    val cardsSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val addressSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    if (state.isDialogOpen){
        AlertDialog(
            onDismissRequest = { viewModel.onIsDialogOpen(false) },
            title = { Text(text = stringResource(R.string.label_logout)) },
            text = { Text(text = stringResource(R.string.label_logout_description)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onIsDialogOpen(false)
                        viewModel.logout(onLogout)
                    }
                ) {
                    Text(text = stringResource(R.string.action_logout))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onIsDialogOpen(false) }
                ) {
                    Text(text = stringResource(R.string.action_cancel))
                }
            }
        )
    }

    data class AccountItems(
        val leadingContent: ImageVector,
        val contentDescription: String,
        val content: String,
        val onClick: () -> Unit
    )

    val accountItems = listOf(
        AccountItems(
            Icons.Outlined.Collections,
            stringResource(R.string.label_orders),
            stringResource(R.string.label_orders),
            onClick = {
                navController.navigate(Routes.AccountOrders.route)
            }
        ),
        AccountItems(
            Icons.Outlined.Reviews,
            stringResource(R.string.label_reviews),
            stringResource(R.string.label_reviews),
            onClick = {
                navController.navigate(Routes.AccountReviews.route)
            }
        ),
        AccountItems(
            Icons.Outlined.Payment,
            stringResource(R.string.label_payment_methods),
            stringResource(R.string.label_payment_methods),
            onClick = { viewModel.onIsPaymentMethodsSheetOpen(true) }
        ),
        AccountItems(
            Icons.Outlined.LocationOn,
            stringResource(R.string.label_addresses),
            stringResource(R.string.label_addresses),
            onClick = { viewModel.onIsAddressesSheetOpen(true) }
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.label_greeting, state.name),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(MaterialTheme.shapes.large)
            ) {
                SegmentedListItem(
                    onClick = {
                        navController.navigate(Routes.AccountInfo.route)
                    },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(R.string.label_personal_info)
                        )
                    },
                    content = {
                        Text(
                            text = stringResource(R.string.label_personal_info),
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    colors = ListItemDefaults.colors(MaterialTheme.colorScheme.surfaceContainer),
                    shapes = ListItemDefaults.segmentedShapes(1, 1)
                )
            }

            Column(
                modifier = Modifier
                    .selectableGroup()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                val count = accountItems.size
                val colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                repeat(count) { index ->
                    SegmentedListItem(
                        onClick = { accountItems[index].onClick() },
                        colors = colors,
                        shapes = ListItemDefaults.segmentedShapes(index = index, count = count),
                        leadingContent = {
                            Icon(
                                imageVector = accountItems[index].leadingContent,
                                contentDescription = accountItems[index].contentDescription)
                        },
                        content = {
                            Text(
                                modifier = Modifier,
                                text = accountItems[index].content,
                                style = MaterialTheme.typography.labelLarge,
                            )
                        }
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.onIsDialogOpen(true)
                    },
                    enabled = !state.isLoggingOut
                ){
                    if (state.isLoggingOut) {
                        CircularProgressIndicator()
                    } else {
                        Text(stringResource(R.string.action_logout))
                    }
                }
            }
        }

        if (state.isPaymentMethodsSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onIsPaymentMethodsSheetOpen(false) },
                sheetState = cardsSheetState
            ) {
                val paymentMethodsViewModel: PaymentMethodsViewModel = hiltViewModel()


                LaunchedEffect(Unit) {
                    paymentMethodsViewModel.loadPaymentMethods()
                }

                PaymentMethodsSheet(
                    onClose = { viewModel.onIsPaymentMethodsSheetOpen(false) },
                    onAddCard = {
                        viewModel.onIsPaymentMethodsSheetOpen(false)
                        navController.navigate(Routes.AccountAddCard.route)
                    },
                    viewModel = paymentMethodsViewModel
                )
            }
        }

        if (state.isAddressesSheetOpen) {
            ModalBottomSheet(
                onDismissRequest = { viewModel.onIsAddressesSheetOpen(false) },
                sheetState = addressSheetState
            ) {
                val addressViewModel: AddressViewModel = hiltViewModel()

                LaunchedEffect(Unit) {
                    addressViewModel.loadAddresses()
                }

                AddressSheet(
                    onClose = { viewModel.onIsAddressesSheetOpen(false) },
                    onAddAddress = {
                        viewModel.onIsAddressesSheetOpen(false)
                        navController.navigate(Routes.AccountAddAddress.route)
                    },
                    viewModel = addressViewModel
                )
            }
        }
    }
}