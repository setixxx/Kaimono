package setixx.software.kaimono.presentation.navigation

sealed class Routes(val route: String) {
    object Home : Routes("Home")
    object Favorites : Routes("Favorites")
    object Cart : Routes("Cart")
    object AccountGraph : Routes("AccountGraph")
    object Account : Routes("Account")
    object AccountInfo : Routes("AccountInfo")
    object AccountOrders: Routes("AccountOrders")
    object AccountReviews: Routes("AccountReviews")
    object AccountAddCard: Routes("AccountAddCard")
}