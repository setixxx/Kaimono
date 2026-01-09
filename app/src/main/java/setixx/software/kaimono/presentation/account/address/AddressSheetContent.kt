package setixx.software.kaimono.presentation.account.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.components.ListWithRadioAndTrailing

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AddressSheetContent(
    onClose: () -> Unit,
    onAddAddress: () -> Unit,
    viewModel: AddressViewModel
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
            ) {
                items(state.addresses.size) { index ->
                    val address = state.addresses[index]
                    ListWithRadioAndTrailing(
                        index = index,
                        selectedIndex = selectedIndex,
                        header = "${address.city}, ${address.street}, ${address.house}",
                        onSelect = {
                            viewModel.setDefaultAddress(address.id)
                        },
                        onDelete = {
                            viewModel.deleteAddress(address.id)
                        }
                    )
                    HorizontalDivider(color = if (state.addresses.size == 1) MaterialTheme.colorScheme.surfaceContainer
                    else MaterialTheme.colorScheme.background,
                        thickness = 2.dp)
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onClose
                ) {
                    Text(stringResource(R.string.action_close))
                }
                Button(
                    onClick = onAddAddress
                ) {
                    Text(stringResource(R.string.action_add_address))
                }
            }
        }
        if (state.isLoading) ContainedLoadingIndicator(modifier = Modifier.align(Alignment.Center))

        SnackbarHost(
            hostState = snackBarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}
