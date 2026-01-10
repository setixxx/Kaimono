package setixx.software.kaimono.presentation.navigation

sealed class Routes(
    val route: String,
    val showBottomBar: Boolean = true
) {
    object Home : Routes("Home")
    object Favorites : Routes("Favorites")
    object Cart : Routes("Cart", showBottomBar = false)
    object AccountGraph : Routes("AccountGraph")
    object Account : Routes("Account")
    object AccountInfo : Routes("AccountInfo", showBottomBar = false)
    object AccountOrders: Routes("AccountOrders", showBottomBar = false)
    object AccountReviews: Routes("AccountReviews", showBottomBar = false)
    object AccountAddCard: Routes("AccountAddCard", showBottomBar = false)
    object AccountAddAddress: Routes("AccountAddAddress", showBottomBar = false)
    object Product: Routes("Product", showBottomBar = false)
    object Search: Routes("Search", showBottomBar = false)
    object ProductReviews: Routes("ProductReviews", showBottomBar = false)
    object Filter: Routes("Filter", showBottomBar = false)


    companion object {
        fun shouldShowBottomBar(route: String?): Boolean {
            return when (route) {
                Home.route -> Home.showBottomBar
                Favorites.route -> Favorites.showBottomBar
                Cart.route -> Cart.showBottomBar
                Account.route -> Account.showBottomBar
                AccountInfo.route -> AccountInfo.showBottomBar
                AccountOrders.route -> AccountOrders.showBottomBar
                AccountReviews.route -> AccountReviews.showBottomBar
                AccountAddCard.route -> AccountAddCard.showBottomBar
                AccountAddAddress.route -> AccountAddAddress.showBottomBar
                Product.route -> Product.showBottomBar
                Search.route -> Search.showBottomBar
                ProductReviews.route -> ProductReviews.showBottomBar
                Filter.route -> Filter.showBottomBar
                else -> true
            }
        }
    }
}