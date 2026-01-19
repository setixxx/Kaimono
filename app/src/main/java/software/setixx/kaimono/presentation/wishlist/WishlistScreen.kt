package software.setixx.kaimono.presentation.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import software.setixx.kaimono.R
import software.setixx.kaimono.presentation.components.ProductCard
import software.setixx.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WishlistScreen(
    navController: NavController,
    viewModel: WishlistViewModel = hiltViewModel()
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

    if (state.isDialogOpen){
        AlertDialog(
            onDismissRequest = { viewModel.onIsDialogOpen(false) },
            title = { Text(text = stringResource(R.string.label_clear_wishlist)) },
            text = { Text(text = stringResource(R.string.label_clear_wishlist_description)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.clearWishlist()
                        viewModel.onIsDialogOpen(false)
                    }
                ) {
                    Text(text = stringResource(R.string.action_clear))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.onIsDialogOpen(false) }
                ) {
                    Text(text = stringResource(R.string.action_cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.label_favorites),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                actions = {
                    if (!state.wishlistItem.isEmpty() && !state.isLoading){
                        IconButton(
                            onClick = { viewModel.onIsDialogOpen(true) },
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null,
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
    ) { paddingValues ->
        if (state.wishlistItem.isEmpty() && !state.isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = stringResource(R.string.label_empty_wishlist),
                )
            }
        } else if (state.isLoading){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ){
                ContainedLoadingIndicator()
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 160.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = paddingValues.calculateBottomPadding()
                ),
            ) {
                items(state.wishlistItem.size) { index ->
                    ProductCard(
                        imageUrl = state.wishlistItem[index].productImage,
                        contentDescription = state.wishlistItem[index].productName,
                        header = state.wishlistItem[index].productName,
                        price = state.wishlistItem[index].basePrice.toDouble().toInt(),
                        onClick = {
                            val productId = state.wishlistItem[index].productPublicId
                            navController.navigate(Routes.Product.createRoute(productId))
                        },
                        rating = null
                    )
                }

            }
        }
    }
}