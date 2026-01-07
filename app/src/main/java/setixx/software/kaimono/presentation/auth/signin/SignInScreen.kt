package setixx.software.kaimono.presentation.auth.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.R
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onSignInSuccess: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: SignInViewModel
){
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
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                stringResource(R.string.label_sign_in),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                modifier = Modifier.padding(bottom = 24.dp),
                text = stringResource(R.string.sublabel_sign_in),
                style = MaterialTheme.typography.titleLarge,
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text(stringResource(R.string.hint_email)) },
                singleLine = true,
                isError = state.emailError != null,
                supportingText = {
                    state.emailError?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                enabled = !state.isLoading

            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                label = { Text(stringResource(R.string.hint_password)) },
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                singleLine = true,
                isError = state.passwordError != null,
                supportingText = {
                    state.passwordError?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                enabled = !state.isLoading,
                visualTransformation = if (state.isPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = viewModel::togglePasswordVisibility) {
                        Icon(
                            imageVector = if (state.isPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (state.isPasswordVisible)
                                stringResource(R.string.hint_hide_password)
                            else
                                stringResource(R.string.hint_show_password)
                        )
                    }
                },
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    viewModel.onSignInClick(onSignInSuccess)
                },
                enabled = !state.isLoading
            ) {
                when (state.isLoading){
                    true -> CircularProgressIndicator()
                    false -> Text(text = stringResource(R.string.action_sign_in))
                }
            }
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = onNavigateToSignUp
            ) {
                Text(text = stringResource(R.string.action_sign_up))
            }
        }
    }
}

/*
@Preview(showSystemUi = true)
@Composable
fun LoginScreenPreview(){
    LoginScreen(
        onSignInSuccess = {},
        onNavigateToSignUp = {},
        viewModel = SignInViewModel())
}*/
