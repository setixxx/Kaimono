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
    object Product: Routes("Product/{productId}", showBottomBar = false){
        fun createRoute(productId: String) = "Product/$productId"
    }
    object Search: Routes("Search", showBottomBar = false)
    object ProductReviews: Routes("ProductReviews", showBottomBar = false)
    object Filter: Routes("Filter", showBottomBar = false)


    companion object {
        fun shouldShowBottomBar(route: String?): Boolean {
            if (route == null) return true

            if (route.startsWith("Product")) {
                return Product.showBottomBar
            }

            val screens = listOf(
                Home, Favorites, Cart, Account, AccountInfo,
                AccountOrders, AccountReviews, AccountAddCard,
                AccountAddAddress, Search, ProductReviews, Filter
            )

            return screens.find { it.route == route }?.showBottomBar ?: true
        }

    }
}