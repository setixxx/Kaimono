package setixx.software.kaimono.presentation.product.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import setixx.software.kaimono.R
import setixx.software.kaimono.domain.model.Review

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductReviewSheet(
    review: Review? = null,
    productPublicId: String? = null,
    orderPublicId: String? = null,
    onClose: () -> Unit = {},
    isUpdate: Boolean = false,
    viewModel: ReviewsSheetViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val options = listOf("1", "2", "3", "4", "5")

    LaunchedEffect(review) {
        review?.let { viewModel.setReview(it) }
    }

    LaunchedEffect(productPublicId, orderPublicId) {
        if (!isUpdate && productPublicId != null && orderPublicId != null) {
            viewModel.setInitialData(productPublicId, orderPublicId)
        }
    }

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) {
            onClose()
            viewModel.resetSuccess()
        }
    }

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let { error ->
            snackBarHostState.showSnackbar(
                message = error,
                duration = SnackbarDuration.Short
            )
            viewModel.clearError()
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
                text = if (isUpdate) stringResource(R.string.label_edit_review) else stringResource(R.string.label_create_review),
                style = MaterialTheme.typography.headlineSmall
            )

            state.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                value = state.reviewText,
                minLines = 5,
                maxLines = 5,
                onValueChange = { viewModel.onReviewTextChange(it) },
                label = { Text(stringResource(R.string.hint_text_of_review)) },
                singleLine = false,
                enabled = !state.isLoading
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            ) {
                options.forEachIndexed { index, label ->
                    val ratingValue = index + 1
                    ToggleButton(
                        checked = state.rating == ratingValue,
                        onCheckedChange = { viewModel.onRatingChange(ratingValue) },
                        modifier = Modifier.weight(1f),
                        shapes = when (index) {
                            0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                            options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                            else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                        },
                        enabled = !state.isLoading
                    ) {
                        if (state.rating == ratingValue) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                        }
                        Text(label)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    onClick = {
                        onClose()
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.outlineVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    enabled = !state.isLoading
                ) {
                    Text(text = stringResource(R.string.action_cancel))
                }
                Button(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 4.dp),
                    onClick = {
                        if (isUpdate) {
                            viewModel.updateReview()
                        } else {
                            viewModel.createReview()
                        }
                    },
                    enabled = !state.isLoading
                ) {
                    if (state.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(text = if (isUpdate) stringResource(R.string.acton_update) else stringResource(R.string.acton_create))
                    }
                }
            }
        }
        SnackbarHost(snackBarHostState)
    }
}
