package setixx.software.kaimono.presentation.navigation

sealed class Routes(val route: String) {
    object Home : Routes("Home")
    object Favorites : Routes("Favorites")
    object AccountGraph : Routes("AccountGraph")
    object Account : Routes("Account")
    object AccountInfo : Routes("AccountInfo")
    object AccountOrders: Routes("AccountOrders")
    object AccountReviews: Routes("AccountReviews")
}