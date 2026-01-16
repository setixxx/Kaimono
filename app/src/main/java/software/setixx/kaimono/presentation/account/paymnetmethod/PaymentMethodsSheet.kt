package software.setixx.kaimono.presentation.account.paymnetmethod

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import software.setixx.kaimono.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PaymentMethodsSheet(
    onClose: () -> Unit,
    onAddCard: () -> Unit,
    viewModel: PaymentMethodsViewModel
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

    val selectedIndex = remember(state.paymentMethods, state.selectedPaymentMethod) {
        val selectedId = state.selectedPaymentMethod?.id
        if (selectedId != null) {
            state.paymentMethods.indexOfFirst { it.id == selectedId }
        } else {
            state.paymentMethods.indexOfFirst { it.isDefault }
        }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.title_payment_methods),
                style = MaterialTheme.typography.headlineSmall
            )
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clip(MaterialTheme.shapes.large),
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
                    items(
                        count = state.paymentMethods.size,
                        key = { index -> state.paymentMethods[index].id }
                    ) { index ->
                        val paymentMethod = state.paymentMethods[index]
                        val colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

                        SegmentedListItem(
                            selected = index == selectedIndex,
                            onClick = { viewModel.setDefaultPaymentMethod(paymentMethod.id) },
                            colors = colors,
                            shapes = ListItemDefaults.segmentedShapes(index = index, count = state.paymentMethods.size),
                            leadingContent = {
                                RadioButton(
                                    selected = index == selectedIndex,
                                    onClick = {
                                        viewModel.setDefaultPaymentMethod(paymentMethod.id)
                                    }
                                )
                            },
                            content = {
                                Text(
                                    text = if (paymentMethod.paymentType == "card") {
                                        stringResource(R.string.label_card) + " " + paymentMethod.cardNumberLast4
                                    } else {
                                        stringResource(R.string.label_cash)
                                    },
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            },
                            trailingContent = if (paymentMethod.paymentType == "card"){
                                {
                                    IconButton(
                                        onClick = {
                                            viewModel.deletePaymentMethod(paymentMethod.id)
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.Delete,
                                            contentDescription = stringResource(R.string.action_delete)
                                        )
                                    }
                                }
                            } else {
                                null
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    onClick = onClose,
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.outlineVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(stringResource(R.string.action_close))
                }
                Button(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 4.dp),
                    onClick = onAddCard
                ) {
                    Text(stringResource(R.string.action_add_card))
                }
            }
        }
        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
