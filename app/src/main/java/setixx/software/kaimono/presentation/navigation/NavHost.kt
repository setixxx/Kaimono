package setixx.software.kaimono.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import setixx.software.kaimono.presentation.screen.auth.SignInScreen
import setixx.software.kaimono.presentation.screen.favorites.FavouritesScreen
import setixx.software.kaimono.presentation.screen.home.HomeScreen
import setixx.software.kaimono.presentation.screen.profile.ProfileScreen

@Composable
fun NavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        composable(Routes.Home.route) { HomeScreen() }
        composable(Routes.Favorites.route) { FavouritesScreen() }
        composable(Routes.Profile.route) { ProfileScreen() }
    }
}
