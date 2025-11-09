package setixx.software.kaimono.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import setixx.software.kaimono.presentation.navigation.BottomNavigationBar
import setixx.software.kaimono.presentation.navigation.NavHost


@Composable
fun Main() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}