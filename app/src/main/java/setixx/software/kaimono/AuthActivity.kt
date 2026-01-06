package setixx.software.kaimono

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import setixx.software.kaimono.presentation.auth.signin.SignInViewModel
import setixx.software.kaimono.presentation.auth.signup.SignUpViewModel
import setixx.software.kaimono.presentation.navigation.AuthNavHost
import setixx.software.kaimono.presentation.theme.KaimonoTheme

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KaimonoTheme {
                val navController = rememberNavController()
                val signInViewModel = hiltViewModel<SignInViewModel>()
                val signUpViewModel = hiltViewModel<SignUpViewModel>()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AuthNavHost(
                        navController = navController,
                        onAuthSuccess = {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        modifier = Modifier.padding(innerPadding),
                        signInViewModel = signInViewModel,
                        signUpViewModel = signUpViewModel
                    )
                }
            }
        }
    }
}