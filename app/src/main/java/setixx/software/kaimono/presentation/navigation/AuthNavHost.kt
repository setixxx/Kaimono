package setixx.software.kaimono.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import setixx.software.kaimono.presentation.screen.auth.SignInScreen
import setixx.software.kaimono.presentation.screen.auth.SignUpScreen

@Composable
fun AuthNavHost(
    navController: NavHostController,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.SignIn.route,
        modifier = modifier
    ) {
        composable(AuthRoutes.SignIn.route) {
            SignInScreen(
                onSignInSuccess = onAuthSuccess,
                onNavigateToSignUp = {
                    navController.navigate(AuthRoutes.SignUp.route)
                }
            )
        }

        composable(AuthRoutes.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = onAuthSuccess,
                onNavigateToSignIn = {
                    navController.popBackStack()
                }
            )
        }
    }
}