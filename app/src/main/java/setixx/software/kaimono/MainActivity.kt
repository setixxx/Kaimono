package setixx.software.kaimono

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import setixx.software.kaimono.presentation.account.info.AccountInfoViewModel
import setixx.software.kaimono.presentation.theme.KaimonoTheme
import setixx.software.kaimono.presentation.navigation.BottomNavigationBar
import setixx.software.kaimono.presentation.navigation.NavHost
import setixx.software.kaimono.presentation.navigation.Routes

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KaimonoTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val shouldShowBottomBar = Routes.shouldShowBottomBar(currentRoute)

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (shouldShowBottomBar) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        onLogout = {
                            val intent = Intent(this, AuthActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        navController = navController,
                        modifier = Modifier.fillMaxSize(),
                        innerPadding = innerPadding,
                        shouldShowBottomBar = shouldShowBottomBar
                    )
                }
            }
        }
    }
}