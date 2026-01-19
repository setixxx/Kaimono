package software.setixx.kaimono.presentation.account.orders

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.RateReview
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedListItem
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
import software.setixx.kaimono.R
import software.setixx.kaimono.domain.model.Address
import software.setixx.kaimono.domain.model.DeliveryInfo
import software.setixx.kaimono.domain.model.Order
import software.setixx.kaimono.domain.model.OrderItem
import software.setixx.kaimono.domain.model.PaymentInfo
import software.setixx.kaimono.domain.model.PaymentMethodInfo
import software.setixx.kaimono.presentation.common.DateUtils
import software.setixx.kaimono.presentation.components.ListWithTwoIcons
import software.setixx.kaimono.presentation.product.reviews.ProductReviewSheet

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OrderScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    
    var showReviewSheet by remember { mutableStateOf(false) }
    var selectedProductPublicId by remember { mutableStateOf<String?>(null) }
    var selectedOrderPublicId by remember { mutableStateOf<String?>(null) }

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
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
        ) {
            if (state.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        ContainedLoadingIndicator()
                    }
                }
            } else {
                state.order?.let { order ->
                    item {
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
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            Text(
                                modifier = Modifier
                                    .padding(top = 12.dp, bottom = 4.dp),
                                text = stringResource(R.string.label_order_structure),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    items(
                        count = order.items.size
                    ) { index ->
                        val orderItem = order.items[index]
                        val colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
                        SegmentedListItem(
                            onClick = {
                                navController.navigate("product/${orderItem.productPublicId}")
                            },
                            colors = colors,
                            shapes = ListItemDefaults.segmentedShapes(
                                index = index,
                                count = order.items.size
                            ),
                            content = {
                                Text(
                                    modifier = Modifier
                                        .padding(top = 4.dp),
                                    text = orderItem.productName,
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            },
                            supportingContent = {
                                Text(
                                    modifier = Modifier
                                        .padding(bottom = 4.dp),
                                    text = stringResource(
                                        R.string.label_size,
                                        orderItem.size
                                    ) + " • x${orderItem.quantity}",
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            },
                            trailingContent = if ((order.status == "Доставлен" || order.status == "Delivered")
                                && state.writtenProductReviews[orderItem] == false) {
                                {
                                    Column(
                                        horizontalAlignment = Alignment.End,
                                        verticalArrangement = Arrangement.Center
                                    ){
                                        Text(
                                            modifier = Modifier
                                                .padding(top = 4.dp),
                                            text = orderItem.subtotal,
                                            style = MaterialTheme.typography.labelLarge,
                                        )
                                        TextButton(
                                            onClick = {
                                                selectedProductPublicId = orderItem.productPublicId
                                                selectedOrderPublicId = order.publicId
                                                showReviewSheet = true
                                            },
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
                            } else {
                                {
                                    Text(
                                        text = orderItem.subtotal,
                                        style = MaterialTheme.typography.labelLarge,
                                    )
                                }
                            }
                        )
                    }
                    item {
                        order.deliveryInfo?.let { delivery ->
                            Text(
                                modifier = Modifier
                                    .padding(top = 12.dp, bottom = 8.dp),
                                text = stringResource(R.string.label_delivery_info),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            Column(
                                modifier = Modifier.clip(MaterialTheme.shapes.large)
                            ) {
                                val address = delivery.address
                                val addressText =
                                    "${address.city}, ${address.street} ${address.house}${if (address.apartment.isNullOrEmpty()) "" else ", " + address.apartment}"

                                ListWithTwoIcons(
                                    icon = Icons.Outlined.LocationOn,
                                    contentDescription = stringResource(R.string.label_addresses),
                                    header = addressText,
                                    onClick = {}
                                )

                                delivery.trackingNumber?.let { tracking ->
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.background,
                                        thickness = 2.dp
                                    )
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
                                modifier = Modifier
                                    .padding(top = 12.dp, bottom = 8.dp),
                                text = stringResource(R.string.label_payment_method),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            Column(
                                modifier = Modifier.clip(MaterialTheme.shapes.large)
                            ) {
                                val paymentMethodText = if (payment.paymentMethod != null) {
                                    if (payment.paymentType == "card") {
                                        stringResource(R.string.label_card) + " " + payment.paymentMethod.cardNumberLast4
                                    } else {
                                        stringResource(R.string.label_cash)
                                    }
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
                    }
                }
            }
        }
        if (showReviewSheet) {
            ModalBottomSheet(
                onDismissRequest = { 
                    showReviewSheet = false
                    selectedProductPublicId = null
                    selectedOrderPublicId = null
                },
                sheetState = sheetState
            ) {
                ProductReviewSheet(
                    productPublicId = selectedProductPublicId,
                    orderPublicId = selectedOrderPublicId,
                    onClose = { 
                        showReviewSheet = false
                        selectedProductPublicId = null
                        selectedOrderPublicId = null
                        viewModel.refresh()
                    }
                )
            }
        }
    }
}