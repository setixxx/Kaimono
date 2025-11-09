package setixx.software.kaimono.presentation.navigation

sealed class Routes(val route: String) {
    object Home : Routes("Home")
    object Favorites : Routes("Favorites")
    object Profile : Routes("Profile")
}