package software.setixx.kaimono.presentation.account.address

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import software.setixx.kaimono.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddressSheet(
    onClose: () -> Unit,
    onAddAddress: () -> Unit,
    viewModel: AddressViewModel = hiltViewModel()
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

    val selectedIndex = remember(state.addresses, state.selectedAddress) {
        val selectedId = state.selectedAddress?.id
        if (selectedId != null) {
            state.addresses.indexOfFirst { it.id == selectedId }
        } else {
            state.addresses.indexOfFirst { it.isDefault }
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
                text = stringResource(R.string.title_addresses),
                style = MaterialTheme.typography.headlineSmall
            )
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clip(MaterialTheme.shapes.large)
                    .animateContentSize(),
                verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap),
            ) {
                if (state.addresses.isEmpty() && !state.isLoading) {
                    item {
                        Text(
                            text = stringResource(R.string.label_no_addresses),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else if (state.isLoading) {
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
                        count = state.addresses.size,
                        key = { index -> state.addresses[index].id }
                    ) { index ->
                        val address = state.addresses[index]
                        val colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceContainer)

                        SegmentedListItem(
                            selected = index == selectedIndex,
                            onClick = { viewModel.setDefaultAddress(address.id) },
                            colors = colors,
                            shapes = ListItemDefaults.segmentedShapes(index = index, count = state.addresses.size),
                            leadingContent = {
                                RadioButton(
                                    selected = index == selectedIndex,
                                    onClick = {
                                        viewModel.setDefaultAddress(address.id)
                                    }
                                )
                            },
                            content = {
                                Text(
                                    text = "${address.city}, ${address.street}, ${address.house}",
                                    style = MaterialTheme.typography.labelLarge,
                                )
                            },
                            trailingContent = {
                                IconButton(
                                    onClick = {
                                        viewModel.deleteAddress(address.id)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = stringResource(R.string.action_delete)
                                    )
                                }
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
                    onClick = onAddAddress
                ) {
                    Text(stringResource(R.string.action_add_address))
                }
            }
        }

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
