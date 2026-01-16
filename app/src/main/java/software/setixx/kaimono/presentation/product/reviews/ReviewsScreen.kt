package software.setixx.kaimono.presentation.product.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import software.setixx.kaimono.R
import software.setixx.kaimono.presentation.common.DateUtils
import software.setixx.kaimono.presentation.components.ReviewCardSquare

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewsScreen(
    navController: NavController,
    viewModel: ReviewsScreenViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    var showReviewBottomSheet by remember { mutableStateOf(false) }
    val reviewBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    LaunchedEffect(state.errorMessage) {
        state.errorMessage?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.label_reviews),
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
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.ownReview?.let { ownReview ->
                    item {
                        Text(
                            modifier = Modifier.padding(bottom = 8.dp),
                            text = stringResource(R.string.label_your_review),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Normal
                        )
                        ReviewCardSquare(
                            username = ownReview.userName,
                            productName = "",
                            reviewDate = DateUtils.formatTimestamp(ownReview.createdAt),
                            reviewText = ownReview.comment ?: "",
                            rating = ownReview.rating.toString(),
                            withImageAndDate = false,
                            isExpanded = true,
                            isEditable = true,
                            onUpdate = { showReviewBottomSheet = true },
                            onDelete = { viewModel.deleteReview(ownReview.publicId) },
                            imageUrl = null
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = stringResource(R.string.label_all_reviews),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Normal
                        )
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = stringResource(R.string.label_total_reviews, " ${state.reviews.size}"),
                        )
                    }
                }

                items(state.reviews) { review ->
                    ReviewCardSquare(
                        username = review.userName,
                        productName = "",
                        reviewDate = review.createdAt,
                        reviewText = review.comment ?: "",
                        rating = review.rating.toString(),
                        withImageAndDate = false,
                        isExpanded = true,
                        isEditable = false,
                        imageUrl = null
                    )
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            state.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
        }

        if (showReviewBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showReviewBottomSheet = false },
                sheetState = reviewBottomSheetState
            ) {
                ProductReviewSheet(
                    review = state.ownReview,
                    onClose = { 
                        showReviewBottomSheet = false
                        viewModel.loadReviews()
                    },
                    isUpdate = true,
                )
            }
        }
    }
}
