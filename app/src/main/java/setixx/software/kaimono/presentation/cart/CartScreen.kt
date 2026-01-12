package setixx.software.kaimono.presentation.cart

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.key
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
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.account.address.AddressSheetContent
import setixx.software.kaimono.presentation.account.address.AddressViewModel
import setixx.software.kaimono.presentation.account.paymnetmethod.PaymentMethodsSheetContent
import setixx.software.kaimono.presentation.account.paymnetmethod.PaymentMethodsViewModel
import setixx.software.kaimono.presentation.components.ListWithTwoIcons
import setixx.software.kaimono.presentation.components.SwipeableListWithPriceAndQuantity
import setixx.software.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    deliveryFee: Double = 0.00,
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
                            navController.navigate(Routes.Home.route) {
                                popUpTo(Routes.Home.route) { inclusive = true }
                            }
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
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.large)
                        .animateContentSize()
                ) {
                    state.items.forEach { item ->
                        key(item.id) {
                            SwipeableListWithPriceAndQuantity(
                                query = item,
                                onDismiss = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.deleteCartItem(item.productPublicId, item.size)
                                },
                                onOpen = {
                                    navController.navigate(Routes.Product.createRoute(item.productPublicId))
                                }
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.background,
                                thickness = 2.dp
                            )
                        }
                    }
                }

                if (state.items.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clip(shape = MaterialTheme.shapes.large)
                    ) {
                        val paymentLabel = state.selectedPaymentMethod?.let {
                            "${
                                if (it.paymentType == "card") {
                                    stringResource(R.string.label_credit_card)
                                } else {
                                    stringResource(R.string.label_cash)
                                }
                            } *${it.cardNumberLast4} (${it.expiryMonth}/${it.expiryYear})"
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
                    ) {
                        val addressLabel = state.selectedAddress?.let { "${it.city}, ${it.street} ${it.house}" }
                            ?: stringResource(R.string.label_address)
                            
                        ListWithTwoIcons(
                            icon = Icons.Outlined.LocationOn,
                            contentDescription = stringResource(R.string.label_address),
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
                            .padding(top = 24.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            stringResource(R.string.label_delivery_fee),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            deliveryFee.toString() + stringResource(R.string.label_currency),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .padding(bottom = 80.dp),
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
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Your cart is empty",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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

                AddressSheetContent(
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
