package setixx.software.kaimono.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import setixx.software.kaimono.presentation.account.AccountScreen
import setixx.software.kaimono.presentation.account.AccountViewModel
import setixx.software.kaimono.presentation.account.info.AccountInfoScreen
import setixx.software.kaimono.presentation.home.HomeScreen
import setixx.software.kaimono.presentation.account.address.AddAddressScreen
import setixx.software.kaimono.presentation.account.paymnetmethod.AddPaymentMethodScreen
import setixx.software.kaimono.presentation.account.info.AccountInfoViewModel
import setixx.software.kaimono.presentation.account.orders.AccountOrdersScreen
import setixx.software.kaimono.presentation.account.reviews.AccountReviewsScreen
import setixx.software.kaimono.presentation.cart.CartScreen
import setixx.software.kaimono.presentation.favorites.FavoritesScreen
import setixx.software.kaimono.presentation.filter.FilterScreen
import setixx.software.kaimono.presentation.product.ProductScreen
import setixx.software.kaimono.presentation.product.reviews.ProductReviewsScreen

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavHost(
    onLogout: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues,
    shouldShowBottomBar: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = if (shouldShowBottomBar) {
            modifier.padding(
                PaddingValues(bottom = innerPadding.calculateBottomPadding())
            )
        } else {
            modifier
        }
    ) {
        composable(Routes.Home.route) { HomeScreen(navController) }
        composable(Routes.Favorites.route) { FavoritesScreen(navController) }
        composable(Routes.Cart.route) { CartScreen(navController) }

        navigation(
            Routes.Account.route,
            Routes.AccountGraph.route
        ) {
            composable(Routes.Account.route) {
                val accountViewModel = hiltViewModel<AccountViewModel>()
                AccountScreen(
                    navController,
                    onLogout = onLogout,
                    viewModel = accountViewModel
                )
            }
            composable(Routes.AccountInfo.route) {
                val accountInfoViewModel = hiltViewModel<AccountInfoViewModel>()
                AccountInfoScreen(
                    navController = navController,
                    viewModel = accountInfoViewModel
                )
            }
            composable(Routes.AccountOrders.route) { AccountOrdersScreen(navController) }
            composable(Routes.AccountReviews.route) { AccountReviewsScreen(navController) }
            composable(Routes.AccountAddCard.route) { AddPaymentMethodScreen(navController) }
            composable(Routes.AccountAddAddress.route) { AddAddressScreen(navController) }
            composable(Routes.Product.route) { ProductScreen(navController) }
            composable(Routes.ProductReviews.route) { ProductReviewsScreen(navController) }
            composable(Routes.Filter.route) { FilterScreen(navController) }
        }
    }
}