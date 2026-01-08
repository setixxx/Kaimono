package setixx.software.kaimono.presentation.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import setixx.software.kaimono.presentation.components.ListWithTwoIcons
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.account.address.AddressSheetContent
import setixx.software.kaimono.presentation.account.address.AddressViewModel
import setixx.software.kaimono.presentation.account.paymnetmethod.PaymentMethodsSheetContent
import setixx.software.kaimono.presentation.account.paymnetmethod.PaymentMethodsViewModel
import setixx.software.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    onLogout: () -> Unit,
    viewModel: AccountViewModel
) {
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    var showCardsBottomSheet by remember { mutableStateOf(false) }
    var showAddressBottomSheet by remember { mutableStateOf(false) }
    val cardsSheetState = rememberModalBottomSheetState()
    val addressSheetState = rememberModalBottomSheetState()

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
                    .clip(shape = MaterialTheme.shapes.large)
            ) {
                ListWithTwoIcons(
                    Icons.Outlined.Info,
                    stringResource(R.string.label_personal_info),
                    stringResource(R.string.label_personal_info),
                    onClick = {
                        navController.navigate(Routes.AccountInfo.route)
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(shape = MaterialTheme.shapes.large)
            ) {
                ListWithTwoIcons(
                    Icons.Outlined.Collections,
                    stringResource(R.string.label_orders),
                    stringResource(R.string.label_orders),
                    onClick = {
                        navController.navigate(Routes.AccountOrders.route)
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                ListWithTwoIcons(
                    Icons.Outlined.Reviews,
                    stringResource(R.string.label_reviews),
                    stringResource(R.string.label_reviews),
                    onClick = {
                        navController.navigate(Routes.AccountReviews.route)
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                ListWithTwoIcons(
                    Icons.Outlined.Payment,
                    stringResource(R.string.label_payment_methods),
                    stringResource(R.string.label_payment_methods),
                    onClick = {
                        showCardsBottomSheet = true
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                ListWithTwoIcons(
                    Icons.Outlined.LocationOn,
                    stringResource(R.string.label_address),
                    stringResource(R.string.label_address),
                    onClick = {
                        showAddressBottomSheet = true
                    }
                )
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
                        viewModel.logout(onLogout)
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

        if (showCardsBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCardsBottomSheet = false },
                sheetState = cardsSheetState
            ) {
                val paymentMethodsViewModel: PaymentMethodsViewModel = hiltViewModel()


                LaunchedEffect(Unit) {
                    paymentMethodsViewModel.loadPaymentMethods()
                }

                PaymentMethodsSheetContent(
                    onClose = { showCardsBottomSheet = false },
                    onAddCard = {
                        showCardsBottomSheet = false
                        navController.navigate(Routes.AccountAddCard.route)
                    },
                    viewModel = paymentMethodsViewModel
                )
            }
        }

        if (showAddressBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddressBottomSheet = false },
                sheetState = addressSheetState
            ) {
                val addressViewModel: AddressViewModel = hiltViewModel()

                LaunchedEffect(Unit) {
                    addressViewModel.loadAddresses()
                }

                AddressSheetContent(
                    onClose = { showAddressBottomSheet = false },
                    onAddAddress = {
                        showAddressBottomSheet = false
                        navController.navigate(Routes.AccountAddAddress.route)
                    },
                    viewModel = addressViewModel
                )
            }
        }
    }
}