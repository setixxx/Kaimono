package software.setixx.kaimono.presentation.navigation

sealed class AuthRoutes(val route: String) {
    object SignIn : AuthRoutes("SignIn")
    object SignUp : AuthRoutes("SignUp")
}