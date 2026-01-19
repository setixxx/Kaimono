package software.setixx.kaimono.presentation.navigation

import androidx.annotation.StringRes
import software.setixx.kaimono.R

sealed class Routes(
    val route: String,
    val showBottomBar: Boolean = true,
    @StringRes val titleRes: Int? = null
) {
    object Home : Routes("Home", titleRes = R.string.label_home)
    object Wishlist : Routes("Wishlist", titleRes = R.string.label_wishlist)
    object Cart : Routes("Cart", showBottomBar = false)
    object AccountGraph : Routes("AccountGraph")
    object Account : Routes("Account", titleRes = R.string.label_account)
    object AccountInfo : Routes("AccountInfo", showBottomBar = false)
    object AccountOrders: Routes("AccountOrders", showBottomBar = false)
    object AccountReviews: Routes("AccountReviews", showBottomBar = false)
    object AccountAddCard: Routes("AccountAddCard", showBottomBar = false)
    object AccountAddAddress: Routes("AccountAddAddress", showBottomBar = false)
    object Product: Routes("Product/{productId}", showBottomBar = false){
        fun createRoute(productId: String) = "Product/$productId"
    }
    object Search: Routes("Search", showBottomBar = false)
    object Reviews: Routes("Reviews/{productId}", showBottomBar = false){
        fun createRoute(productId: String?) = "Reviews/$productId"
    }
    object Filter: Routes("Filter", showBottomBar = false)
    object OrderDetails: Routes("OrderDetails/{orderId}", showBottomBar = false){
        fun createRoute(orderId: String) = "OrderDetails/$orderId"
    }


    companion object {
        fun shouldShowBottomBar(route: String?): Boolean {
            if (route == null) return true

            if (route.startsWith("Product") || route.startsWith("Reviews") || route.startsWith("OrderDetails")) {
                return Product.showBottomBar
            }

            val screens = listOf(
                Home, Wishlist, Cart, Account, AccountInfo,
                AccountOrders, AccountReviews, AccountAddCard,
                AccountAddAddress, Search, Reviews, Filter
            )

            return screens.find { it.route == route }?.showBottomBar ?: true
        }

    }
}