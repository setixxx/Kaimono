package setixx.software.kaimono.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.components.CustomSearchBar
import setixx.software.kaimono.presentation.components.ProductCard
import setixx.software.kaimono.presentation.home.filter.FilterViewModelState
import setixx.software.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    val backStackEntry = navController.currentBackStackEntry
    val filterResult = backStackEntry?.savedStateHandle?.getStateFlow<FilterViewModelState?>("filter_state", null)
        ?.collectAsStateWithLifecycle()

    val listState = rememberLazyGridState()
    var previousIndex by remember { mutableStateOf(0) }
    var previousScrollOffset by remember { mutableStateOf(0) }

    val isScrollingUp by remember {
        derivedStateOf {
            val scrollingUp = if (listState.firstVisibleItemIndex != previousIndex) {
                listState.firstVisibleItemIndex < previousIndex
            } else {
                listState.firstVisibleItemScrollOffset < previousScrollOffset
            }

            previousIndex = listState.firstVisibleItemIndex
            previousScrollOffset = listState.firstVisibleItemScrollOffset

            scrollingUp
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

    LaunchedEffect(filterResult?.value) {
        filterResult?.value?.let { filters ->
            viewModel.applyFilters(filters)
            backStackEntry.savedStateHandle.remove<FilterViewModelState>("filter_state")
        }
    }

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CustomSearchBar(
                        query = state.query,
                        onQueryChange = { viewModel.onQueryChange(it) },
                        onSearch = { viewModel.onQueryChange(it) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 8.dp)
                    )

                    FilledIconButton(
                        onClick = {
                            val currentFilters = FilterViewModelState(
                                categories = state.categories,
                                selectedCategories = state.selectedCategories,
                                selectedSortBy = state.selectedSortBy,
                                selectedSortOrder = state.selectedSortOrder,
                                minPrice = state.minPrice,
                                maxPrice = state.maxPrice,
                                inStockOnly = state.inStockOnly
                            )
                            navController.navigate(Routes.Filter.route)
                            navController.getBackStackEntry(Routes.Filter.route)
                                .savedStateHandle["filter_state"] = currentFilters
                        },
                        Modifier
                            .size(
                                IconButtonDefaults.mediumContainerSize(
                                    IconButtonDefaults.IconButtonWidthOption.Uniform
                                )
                            ),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Tune,
                            contentDescription = null,
                            modifier = Modifier.size(IconButtonDefaults.mediumIconSize),
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Routes.Cart.route) },
                expanded = isScrollingUp,
                icon = { Icon(Icons.Filled.ShoppingCart,
                    stringResource(R.string.action_checkout)) },
                text = { Text(text = stringResource(R.string.action_checkout)) },
            )
        },
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Adaptive(minSize = 160.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                state = listState,
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 80.dp
                )
            ) {
                items(state.products.size) { index ->
                    if (index >= state.products.size - 1 && !state.isLoading && !state.endReached) {
                        LaunchedEffect(Unit) {
                            viewModel.loadNextPage()
                        }
                    }

                    ProductCard(
                        imageUrl = state.products[index].images.firstOrNull()?.imageUrl,
                        contentDescription = state.products[index].name,
                        header = state.products[index].name,
                        price = state.products[index].basePrice.toDouble().toInt(),
                        onClick = {
                            val productId = state.products[index].publicId
                            navController.navigate(Routes.Product.createRoute(productId))
                        },
                        rating = state.products[index].averageRating
                    )
                }
            }

            if (state.isLoading && state.products.isEmpty()) {
                ContainedLoadingIndicator()
            }
        }
    }
}
