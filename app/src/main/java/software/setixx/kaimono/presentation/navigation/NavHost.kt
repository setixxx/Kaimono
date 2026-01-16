package software.setixx.kaimono.presentation.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import software.setixx.kaimono.presentation.account.AccountScreen
import software.setixx.kaimono.presentation.account.info.AccountInfoScreen
import software.setixx.kaimono.presentation.home.HomeScreen
import software.setixx.kaimono.presentation.account.address.AddAddressScreen
import software.setixx.kaimono.presentation.account.paymnetmethod.AddPaymentMethodScreen
import software.setixx.kaimono.presentation.account.orders.AccountOrdersScreen
import software.setixx.kaimono.presentation.account.orders.OrderScreen
import software.setixx.kaimono.presentation.account.reviews.AccountReviewScreen
import software.setixx.kaimono.presentation.cart.CartScreen
import software.setixx.kaimono.presentation.wishlist.WishlistScreen
import software.setixx.kaimono.presentation.home.filter.FilterScreen
import software.setixx.kaimono.presentation.product.ProductScreen
import software.setixx.kaimono.presentation.product.reviews.ReviewsScreen

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
        composable(Routes.Home.route) {
            HomeScreen(
                navController = navController,
                viewModel = hiltViewModel()
            )
        }
        composable(Routes.Favorites.route) {
            WishlistScreen(
                navController = navController
            )
        }
        composable(Routes.Cart.route) { CartScreen(navController) }

        navigation(
            Routes.Account.route,
            Routes.AccountGraph.route
        ) {
            composable(Routes.Account.route) {
                AccountScreen(
                    navController,
                    onLogout = onLogout,
                    viewModel = hiltViewModel()
                )
            }
            composable(Routes.AccountInfo.route) {
                AccountInfoScreen(
                    navController = navController,
                    viewModel = hiltViewModel()
                )
            }
            composable(Routes.AccountOrders.route) { AccountOrdersScreen(navController) }
            composable(Routes.AccountReviews.route) { AccountReviewScreen(navController) }
            composable(Routes.AccountAddCard.route) { AddPaymentMethodScreen(navController) }
            composable(Routes.AccountAddAddress.route) { AddAddressScreen(navController) }
            composable(
                route = Routes.Product.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) {
                ProductScreen(navController = navController, viewModel = hiltViewModel())
            }
            composable(
                route = Routes.Reviews.route,
                arguments = listOf(navArgument("productId") { type = NavType.StringType })
            ) {
                ReviewsScreen(navController, viewModel = hiltViewModel())
            }
            composable(
                route = Routes.OrderDetails.route,
                arguments = listOf(navArgument("orderId") { type = NavType.StringType })
            ) {
                OrderScreen(navController, viewModel = hiltViewModel())
            }
            composable(Routes.Filter.route) { FilterScreen(navController) }
        }
    }
}