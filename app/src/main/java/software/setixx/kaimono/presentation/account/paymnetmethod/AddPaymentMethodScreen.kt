package software.setixx.kaimono.presentation.account.paymnetmethod

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import software.setixx.kaimono.R
import software.setixx.kaimono.presentation.common.CardNumberTransformation
import software.setixx.kaimono.presentation.common.CardDateTransformation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentMethodScreen(
    navController: NavController,
    viewModel: AddPaymentMethodViewModel = hiltViewModel()
){
    val state by viewModel.state.collectAsState()
    val snackBarHostState = remember { SnackbarHostState() }
    val updateSuccessMessage = stringResource(R.string.hint_card_added)

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
                        stringResource(R.string.title_add_card),
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
                value = state.cardNumber,
                onValueChange = viewModel::onCardNumberChange,
                label = { Text(stringResource(R.string.hint_card_number)) },
                isError = state.cardNumberError != null,
                supportingText = { state.cardNumberError?.let { Text(it) } },
                singleLine = true,
                visualTransformation = CardNumberTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = !state.isLoading
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ){
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    value = state.expiryDate,
                    onValueChange = viewModel::onExpiryDateChange,
                    label = { Text(stringResource(R.string.hint_card_date)) },
                    isError = state.expiryDateError != null,
                    supportingText = { state.expiryDateError?.let { Text(it) } },
                    visualTransformation = CardDateTransformation(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    enabled = !state.isLoading
                )
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    value = state.cvv,
                    onValueChange = viewModel::onCvvChange,
                    label = { Text(stringResource(R.string.hint_card_code)) },
                    isError = state.cvvError != null,
                    supportingText = { state.cvvError?.let { Text(it) } },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    enabled = !state.isLoading
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.cardHolderName,
                onValueChange = viewModel::onCardHolderNameChange,
                label = { Text(stringResource(R.string.hint_card_holder_name)) },
                isError = state.cardHolderNameError != null,
                supportingText = { state.cardHolderNameError?.let { Text(it) } },
                singleLine = true,
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
                        viewModel.savePaymentMethod()
                    },
                    enabled = !state.isLoading
                ) {
                    Text(
                        stringResource(R.string.action_save_card)
                    )
                }
            }
        }
    }
}
