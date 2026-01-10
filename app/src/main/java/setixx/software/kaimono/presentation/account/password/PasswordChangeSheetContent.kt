package setixx.software.kaimono.presentation.account.password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PasswordChangeSheetContent(
    onClose: () -> Unit,
    viewModel: PasswordChangeViewModel
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

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onClose()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.label_password_change),
                style = MaterialTheme.typography.headlineSmall
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                value = state.currentPassword,
                onValueChange = viewModel::onCurrentPasswordChange,
                label = { Text(stringResource(R.string.hint_current_password)) },
                isError = state.currentPasswordError != null,
                supportingText = { state.currentPasswordError?.let { Text(text = it) } },
                singleLine = true,
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = if (state.isOldPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = viewModel::toggleOldPasswordVisibility) {
                        Icon(
                            imageVector = if (state.isOldPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (state.isOldPasswordVisible)
                                stringResource(R.string.hint_hide_password)
                            else
                                stringResource(R.string.hint_show_password)
                        )
                    }
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = state.newPassword,
                onValueChange = viewModel::onNewPasswordChange,
                label = { Text(stringResource(R.string.hint_new_password)) },
                isError = state.newPasswordError != null,
                supportingText = { state.newPasswordError?.let { Text(text = it) } },
                singleLine = true,
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = if (state.isNewPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = viewModel::toggleNewPasswordVisibility) {
                        Icon(
                            imageVector = if (state.isNewPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (state.isNewPasswordVisible)
                                stringResource(R.string.hint_hide_password)
                            else
                                stringResource(R.string.hint_show_password)
                        )
                    }
                },
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                value = state.confirmPassword,
                onValueChange = viewModel::onConfirmPasswordChange,
                label = { Text(stringResource(R.string.hint_password_confirmation)) },
                isError = state.confirmPasswordError != null,
                supportingText = { state.confirmPasswordError?.let { Text(text = it) } },
                singleLine = true,
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = if (state.isConfirmPasswordVisible)
                    VisualTransformation.None
                else
                    PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = viewModel::toggleConfirmPasswordVisibility) {
                        Icon(
                            imageVector = if (state.isConfirmPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = if (state.isConfirmPasswordVisible)
                                stringResource(R.string.hint_hide_password)
                            else
                                stringResource(R.string.hint_show_password)
                        )
                    }
                },
            )

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
                    Text(stringResource(R.string.action_cancel))
                }
                Button(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 4.dp),
                    onClick = {
                        viewModel.changePassword()
                    }
                ) {
                    Text(stringResource(R.string.action_change_password))
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
