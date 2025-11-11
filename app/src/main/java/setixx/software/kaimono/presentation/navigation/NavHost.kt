package setixx.software.kaimono.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import setixx.software.kaimono.presentation.screen.account.AccountInfoScreen
import setixx.software.kaimono.presentation.screen.favorites.FavouritesScreen
import setixx.software.kaimono.presentation.screen.home.HomeScreen
import setixx.software.kaimono.presentation.screen.account.AccountScreen
import setixx.software.kaimono.presentation.screen.account.AccountAddAddressScreen
import setixx.software.kaimono.presentation.screen.account.AccountAddCartScreen
import setixx.software.kaimono.presentation.screen.account.AccountOrdersScreen
import setixx.software.kaimono.presentation.screen.account.AccountReviewsScreen
import setixx.software.kaimono.presentation.screen.cart.CartScreen

@Composable
fun NavHost(
    onLogout: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        composable(Routes.Home.route) { HomeScreen(navController) }
        composable(Routes.Favorites.route) { FavouritesScreen() }
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
            composable(Routes.AccountInfo.route) { AccountInfoScreen(navController) }
            composable(Routes.AccountOrders.route) { AccountOrdersScreen(navController) }
            composable(Routes.AccountReviews.route) { AccountReviewsScreen(navController) }
            composable(Routes.AccountAddCard.route) { AccountAddCartScreen(navController) }
            composable(Routes.AccountAddAddress.route) { AccountAddAddressScreen(navController) }
        }
    }
}