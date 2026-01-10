package setixx.software.kaimono.presentation.account.paymnetmethod

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.components.ListWithRadioAndTrailing

@Composable
fun PaymentMethodsSheetContent(
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.title_payment_methods),
                style = MaterialTheme.typography.headlineSmall
            )
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clip(MaterialTheme.shapes.large)
            ) {
                items(
                    count = state.paymentMethods.size,
                    key = { index -> state.paymentMethods[index].id }
                ) { index ->
                    Box(Modifier.animateItem().animateContentSize()){
                        val paymentMethod = state.paymentMethods[index]
                        val month = paymentMethod.expiryMonth.toString().padStart(2, '0')
                        val year = paymentMethod.expiryYear.toString().padStart(2, '0')

                        ListWithRadioAndTrailing(
                            index = index,
                            selectedIndex = selectedIndex,
                            header = "${stringResource(R.string.label_credit_card)} *${paymentMethod.cardNumberLast4} ($month/$year)",
                            onSelect = {
                                viewModel.setDefaultPaymentMethod(paymentMethod.id)
                            },
                            onDelete = {
                                viewModel.deletePaymentMethod(paymentMethod.id)
                            }
                        )
                        HorizontalDivider(
                            color = Color.Transparent,
                            thickness = 2.dp
                        )
                    }
                }
                item(key = "cash_method") {
                    HorizontalDivider(
                        color = Color.Transparent, thickness = 2.dp
                    )
                    Box(Modifier.animateItem()) {
                        ListWithRadioAndTrailing(
                            index = -1,
                            selectedIndex = if (state.selectedPaymentMethod == null) -1 else -2,
                            header = stringResource(R.string.label_cash_method),
                            onSelect = {
                                /*viewModel.setDefaultPaymentMethod(null)*/
                            },
                            onDelete = {},
                            isTrailingIconVisible = false
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
