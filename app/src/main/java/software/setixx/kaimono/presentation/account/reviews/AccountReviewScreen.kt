package software.setixx.kaimono.presentation.account.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import software.setixx.kaimono.R
import software.setixx.kaimono.presentation.common.DateUtils
import software.setixx.kaimono.presentation.components.ReviewCardSquare
import software.setixx.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountReviewScreen(
    navController: NavController,
    viewModel: AccountReviewViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
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
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.label_your_reviews),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.Account.route){
                                popUpTo(Routes.Account.route){ inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(state.reviews) { review ->
                val productName = state.productNames[review.productPublicId] ?: ""
                val productImage = review.productPublicId?.let { state.productImages[it] }
                
                ReviewCardSquare(
                    username = review.userName,
                    productName = productName,
                    reviewDate = DateUtils.formatTimestamp(review.createdAt),
                    reviewText = review.comment,
                    rating = review.rating.toString(),
                    withImageAndDate = true,
                    isExpanded = true,
                    isEditable = false,
                    imageUrl = productImage,
                    onNavigateToReviews = {
                        val productId = review.productPublicId
                        if (productId != null) {
                            navController.navigate(Routes.Reviews.createRoute(productId))
                        }
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountReviewPreview() {
    AccountReviewScreen(navController = rememberNavController())
}
