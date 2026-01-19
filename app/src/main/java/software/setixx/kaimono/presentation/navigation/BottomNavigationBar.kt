package software.setixx.kaimono.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    NavigationBar {
        val backStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry?.destination

        NavBarItems.BarItems.forEach { navItem ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == navItem.route.route } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(navItem.route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = false
                        }
                        launchSingleTop = true
                        restoreState = false
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) navItem.enabledImage else navItem.disabledImage,
                        contentDescription = navItem.route.titleRes?.let { stringResource(it) } ?: navItem.route.route
                    )
                },
                label = {
                    navItem.route.titleRes?.let {
                        Text(text = stringResource(it))
                    }
                }
            )
        }
    }
}


object NavBarItems {
    val BarItems = listOf(
        BarItem(
            enabledImage = Icons.Filled.Home,
            disabledImage = Icons.Outlined.Home,
            route = Routes.Home
        ),
        BarItem(
            enabledImage = Icons.Filled.Favorite,
            disabledImage = Icons.Outlined.FavoriteBorder,
            route = Routes.Wishlist
        ),
        BarItem(
            enabledImage = Icons.Filled.Person,
            disabledImage = Icons.Outlined.Person,
            route = Routes.Account
        )
    )
}

data class BarItem(
    val enabledImage: ImageVector,
    val disabledImage: ImageVector,
    val route: Routes
)