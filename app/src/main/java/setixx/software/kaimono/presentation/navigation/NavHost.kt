package setixx.software.kaimono.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import setixx.software.kaimono.presentation.account.info.AccountInfoScreen
import setixx.software.kaimono.presentation.home.HomeScreen
import setixx.software.kaimono.presentation.account.AccountScreen
import setixx.software.kaimono.presentation.account.address.AccountAddAddressScreen
import setixx.software.kaimono.presentation.account.card.AccountAddCardScreen
import setixx.software.kaimono.presentation.account.info.AccountInfoViewModel
import setixx.software.kaimono.presentation.account.orders.AccountOrdersScreen
import setixx.software.kaimono.presentation.account.reviews.AccountReviewsScreen
import setixx.software.kaimono.presentation.cart.CartScreen
import setixx.software.kaimono.presentation.favorites.FavoritesScreen

@Composable
fun NavHost(
    onLogout: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    accountInfoViewModel: AccountInfoViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        composable(Routes.Home.route) { HomeScreen(navController) }
        composable(Routes.Favorites.route) { FavoritesScreen(navController) }
        composable(Routes.Cart.route) { CartScreen(navController) }

        navigation(
            Routes.Account.route,
            Routes.AccountGraph.route
        ) {
            composable(Routes.Account.route) {
                AccountScreen(
                    navController,
                    onLogout = onLogout
                )
            }
            composable(Routes.AccountInfo.route) {
                AccountInfoScreen(
                    navController = navController,
                    viewModel = accountInfoViewModel
                )
            }
            composable(Routes.AccountOrders.route) { AccountOrdersScreen(navController) }
            composable(Routes.AccountReviews.route) { AccountReviewsScreen(navController) }
            composable(Routes.AccountAddCard.route) { AccountAddCardScreen(navController) }
            composable(Routes.AccountAddAddress.route) { AccountAddAddressScreen(navController) }
        }
    }
}