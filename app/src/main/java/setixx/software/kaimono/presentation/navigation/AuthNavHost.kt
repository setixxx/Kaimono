package setixx.software.kaimono.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import setixx.software.kaimono.presentation.auth.signin.LoginScreen
import setixx.software.kaimono.presentation.auth.signin.SignInViewModel
import setixx.software.kaimono.presentation.auth.signup.RegisterScreen
import setixx.software.kaimono.presentation.auth.signup.SignUpViewModel

@Composable
fun AuthNavHost(
    navController: NavHostController,
    onAuthSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    signInViewModel: SignInViewModel,
    signUpViewModel: SignUpViewModel
) {
    NavHost(
        navController = navController,
        startDestination = AuthRoutes.SignIn.route,
        modifier = modifier
    ) {
        composable(AuthRoutes.SignIn.route) {
            LoginScreen(
                onSignInSuccess = onAuthSuccess,
                onNavigateToSignUp = {
                    navController.navigate(AuthRoutes.SignUp.route)
                },
                viewModel = signInViewModel
            )
        }

        composable(AuthRoutes.SignUp.route) {
            RegisterScreen(
                onSignUpSuccess = onAuthSuccess,
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                viewModel = signUpViewModel
            )
        }
    }
}