package software.setixx.kaimono.presentation.account.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import software.setixx.kaimono.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreen(
    navController: NavController,
    viewModel: AddAddressViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val updateSuccessMessage = stringResource(R.string.hint_address_added)

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            snackBarHostState.showSnackbar(
                message = updateSuccessMessage,
                duration = SnackbarDuration.Short
            )
            viewModel.onSuccessShown()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_add_address),
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
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) {  paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.city,
                onValueChange = viewModel::onCityChange,
                label = { Text(stringResource(R.string.hint_city)) },
                isError = state.cityError != null,
                supportingText = { state.cityError?.let { Text(it) } },
                singleLine = true,
                enabled = !state.isLoading
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = state.street,
                onValueChange = viewModel::onStreetChange,
                label = { Text(stringResource(R.string.hint_street)) },
                isError = state.streetError != null,
                supportingText = { state.streetError?.let { Text(it) } },
                singleLine = true,
                enabled = !state.isLoading
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ){
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    value = state.house,
                    onValueChange = viewModel::onHouseChange,
                    label = { Text(stringResource(R.string.hint_house)) },
                    isError = state.houseError != null,
                    supportingText = { state.houseError?.let { Text(it) } },
                    singleLine = true,
                    enabled = !state.isLoading
                )
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    value = state.apartment,
                    onValueChange = viewModel::onApartmentChange,
                    label = { Text(stringResource(R.string.hint_apartment)) },
                    isError = state.apartmentError != null,
                    supportingText = { state.apartmentError?.let { Text(it) } },
                    singleLine = true,
                    enabled = !state.isLoading
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.zipCode,
                onValueChange = viewModel::onZipCodeChange,
                label = { Text(stringResource(R.string.hint_postal_code)) },
                isError = state.zipCodeError != null,
                supportingText = { state.zipCodeError?.let { Text(it) } },
                singleLine = true,
                enabled = !state.isLoading
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = state.additionalInfo,
                onValueChange = viewModel::onAdditionalInfoChange,
                label = { Text(stringResource(R.string.hint_additional_info)) },
                supportingText = { Text(stringResource(R.string.hint_additional_info_description)) },
                minLines = 3,
                maxLines = 5,
                enabled = !state.isLoading
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        viewModel.addAddress()
                    },
                    enabled = !state.isLoading
                ) {
                    Text(
                        stringResource(R.string.action_save_address)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AddAddressScreenPreview(){
    AddAddressScreen(navController = NavController(LocalContext.current))
}