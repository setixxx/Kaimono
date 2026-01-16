package software.setixx.kaimono.presentation.cart

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import software.setixx.kaimono.R
import software.setixx.kaimono.presentation.account.address.AddressSheet
import software.setixx.kaimono.presentation.account.address.AddressViewModel
import software.setixx.kaimono.presentation.account.paymnetmethod.PaymentMethodsSheet
import software.setixx.kaimono.presentation.account.paymnetmethod.PaymentMethodsViewModel
import software.setixx.kaimono.presentation.components.ListWithTwoIcons
import software.setixx.kaimono.presentation.components.SwipeableListWithPriceAndQuantity
import software.setixx.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val haptics = LocalHapticFeedback.current

    var showCardsBottomSheet by remember { mutableStateOf(false) }
    var showAddressBottomSheet by remember { mutableStateOf(false) }
    val cardsSheetState = rememberModalBottomSheetState()
    val addressSheetState = rememberModalBottomSheetState()

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.isOrderCreated) {
        if (state.isOrderCreated) {
            viewModel.resetOrderCreated()
            navController.navigate(Routes.AccountOrders.route) {
                popUpTo(Routes.Cart.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            if (state.items.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.createOrder() },
                    expanded = true,
                    icon = {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            stringResource(R.string.action_checkout)
                        )
                    },
                    text = { Text(text = stringResource(R.string.action_checkout)) },
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.action_checkout),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (state.items.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.clearCart() },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            items(
                count = state.items.size,
                key = { index -> state.items[index].id }
            ) { index ->
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .animateItem()
                ) {
                    SwipeableListWithPriceAndQuantity(
                        query = state.items[index],
                        onDismiss = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.deleteCartItem(state.items[index].productPublicId, state.items[index].size)
                        },
                        onOpen = {
                            navController.navigate(Routes.Product.createRoute(state.items[index].productPublicId))
                        }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.background,
                        thickness = 2.dp
                    )
                }
            }

            if (state.items.isNotEmpty() && !state.isLoading) {
                item {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clip(shape = MaterialTheme.shapes.large)
                            .animateItem()
                            .animateContentSize()
                    ) {
                        val paymentLabel = state.selectedPaymentMethod?.let {
                            if (it.paymentType == "card") {
                                stringResource(R.string.label_card) + " " + it.cardNumberLast4
                            } else {
                                stringResource(R.string.label_cash)
                            }
                        }
                            ?: stringResource(R.string.label_payment_methods)

                        ListWithTwoIcons(
                            icon = Icons.Outlined.CreditCard,
                            contentDescription = stringResource(R.string.label_payment_methods),
                            header = paymentLabel,
                            onClick = {
                                showCardsBottomSheet = true
                            },
                            trailingIcon = Icons.Outlined.ChevronRight
                        )
                    }

                    Column(
                        modifier = Modifier
                            .clip(MaterialTheme.shapes.large)
                            .animateItem()
                            .animateContentSize()
                    ) {
                        val addressLabel = state.selectedAddress?.let { "${it.city}, ${it.street} ${it.house}" }
                            ?: stringResource(R.string.label_addresses)

                        ListWithTwoIcons(
                            icon = Icons.Outlined.LocationOn,
                            contentDescription = stringResource(R.string.label_addresses),
                            header = addressLabel,
                            onClick = {
                                showAddressBottomSheet = true
                            },
                            trailingIcon = Icons.Outlined.ChevronRight
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(top = 16.dp, bottom = 80.dp)
                            .animateItem()
                            .animateContentSize(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(R.string.label_total),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            state.totalPrice + stringResource(R.string.label_currency),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else if (state.isLoading){
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        ContainedLoadingIndicator()
                    }
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = stringResource(R.string.label_empty_cart),
                        )
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

                PaymentMethodsSheet(
                    onClose = { 
                        showCardsBottomSheet = false
                        viewModel.loadPaymentMethods()
                    },
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

                AddressSheet(
                    onClose = { 
                        showAddressBottomSheet = false
                        viewModel.loadAddresses()
                    },
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

@Preview
@Composable
fun CartScreenPreview() {
    CartScreen(navController = NavController(LocalContext.current))
}
