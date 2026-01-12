package setixx.software.kaimono.presentation.account.orders

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import setixx.software.kaimono.R
import setixx.software.kaimono.domain.model.Address
import setixx.software.kaimono.domain.model.DeliveryInfo
import setixx.software.kaimono.domain.model.Order
import setixx.software.kaimono.domain.model.OrderItem
import setixx.software.kaimono.domain.model.PaymentInfo
import setixx.software.kaimono.domain.model.PaymentMethodInfo
import setixx.software.kaimono.presentation.common.DateUtils
import setixx.software.kaimono.presentation.components.ListWithTwoIcons
import setixx.software.kaimono.presentation.product.reviews.ProductReviewSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    
    var showReviewSheet by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        state.order?.let { stringResource(R.string.label_order) + " #${it.publicId}" } ?: "",
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                state.order?.let { order ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = DateUtils.formatTimestamp(order.createdAt),
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = order.status,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (order.status == stringResource(R.string.label_delivered))
                                        MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.secondary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${order.totalAmount} ${stringResource(R.string.label_currency)}",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.outlineVariant)

                        Text(
                            text = stringResource(R.string.label_orders),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        Column(
                            modifier = Modifier
                                .clip(MaterialTheme.shapes.large)
                        ) {
                            order.items.forEachIndexed { index, item ->
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceContainer)
                                        .clickable(onClick = {})
                                        .padding(horizontal = 16.dp, vertical = 12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = item.productName,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = stringResource(R.string.label_size, item.size) + " • x${item.quantity}",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                        Text(
                                            text = "${item.subtotal} ${stringResource(R.string.label_currency)}",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    }

                                    if (order.status == "Доставлен" || order.status == "Delivered"){
                                        TextButton(
                                            onClick = { showReviewSheet = true },
                                            modifier = Modifier.align(Alignment.End),
                                            contentPadding = PaddingValues(0.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Outlined.RateReview,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(Modifier.width(8.dp))
                                            Text(
                                                text = stringResource(R.string.label_create_review),
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }
                                    }
                                }
                                if (index < order.items.size - 1) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                                }
                            }
                        }

                        order.deliveryInfo?.let { delivery ->
                            Text(
                                text = stringResource(R.string.label_address),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            Column(
                                modifier = Modifier.clip(MaterialTheme.shapes.large)
                            ) {
                                val address = delivery.address
                                val addressText = "${address.city}, ${address.street} ${address.house}${if (address.apartment.isNullOrEmpty()) "" else ", " + address.apartment}"
                                
                                ListWithTwoIcons(
                                    icon = Icons.Outlined.LocationOn,
                                    contentDescription = stringResource(R.string.label_address),
                                    header = addressText,
                                    onClick = {}
                                )
                                
                                delivery.trackingNumber?.let { tracking ->
                                    HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                                    ListWithTwoIcons(
                                        icon = Icons.Outlined.LocalShipping,
                                        contentDescription = "Tracking",
                                        header = tracking,
                                        onClick = {}
                                    )
                                }
                            }
                        }

                        order.paymentInfo?.let { payment ->
                            Text(
                                text = stringResource(R.string.label_payment_methods),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            
                            Column(
                                modifier = Modifier.clip(MaterialTheme.shapes.large)
                            ) {
                                val paymentMethodText = if (payment.paymentMethod != null) {
                                    "${
                                        if (payment.paymentType == "card") {
                                            stringResource(R.string.label_credit_card)
                                        } else {
                                            stringResource(R.string.label_cash)
                                        }
                                    } *${payment.paymentMethod.cardNumberLast4}"
                                } else {
                                    payment.paymentType
                                }
                                
                                ListWithTwoIcons(
                                    icon = Icons.Outlined.Payment,
                                    contentDescription = stringResource(R.string.label_payment_methods),
                                    header = paymentMethodText,
                                    onClick = {}
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }

        if (showReviewSheet) {
            ModalBottomSheet(
                onDismissRequest = { showReviewSheet = false },
                sheetState = sheetState
            ) {
                ProductReviewSheet(
                    onClose = { showReviewSheet = false }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OrderScreenPreview() {
    val mockOrder = Order(
        publicId = "ORD-12345",
        status = "Delivered",
        totalAmount = "1500.00",
        createdAt = "2023-10-27T10:00:00Z",
        items = listOf(
            OrderItem(
                productName = "T-Shirt",
                size = "L",
                quantity = 2,
                priceAtPurchase = "500.00",
                subtotal = "1000.00"
            ),
            OrderItem(
                productName = "Jeans",
                size = "32",
                quantity = 1,
                priceAtPurchase = "500.00",
                subtotal = "500.00"
            )
        ),
        deliveryInfo = DeliveryInfo(
            trackingNumber = "TRK987654321",
            status = "Delivered",
            estimatedDate = "2023-10-30",
            address = Address(
                id = 1,
                city = "New York",
                street = "5th Ave",
                house = "101",
                apartment = "12A",
                code = "10001",
                additionalInfo = "Leave at the door",
                isDefault = true
            )
        ),
        paymentInfo = PaymentInfo(
            id = 1,
            amount = "1500.00",
            status = "Paid",
            paymentType = "Card",
            transactionId = "TXN555",
            paidAt = "2023-10-27T10:05:00Z",
            paymentMethod = PaymentMethodInfo(
                cardNumberLast4 = "1234",
                cardHolderName = "John Doe"
            )
        )
    )

    OrderScreen(
        navController = NavController(LocalContext.current)
    )
}
