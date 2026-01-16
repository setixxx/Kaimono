package software.setixx.kaimono.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import software.setixx.kaimono.presentation.auth.signin.SignInScreen
import software.setixx.kaimono.presentation.auth.signin.SignInViewModel
import software.setixx.kaimono.presentation.auth.signup.SignUpScreen
import software.setixx.kaimono.presentation.auth.signup.SignUpViewModel

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
            SignInScreen(
                onSignInSuccess = onAuthSuccess,
                onNavigateToSignUp = {
                    navController.navigate(AuthRoutes.SignUp.route)
                },
                viewModel = signInViewModel
            )
        }

        composable(AuthRoutes.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = onAuthSuccess,
                onNavigateToSignIn = {
                    navController.popBackStack()
                },
                viewModel = signUpViewModel
            )
        }
    }
}