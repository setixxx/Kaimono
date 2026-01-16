package software.setixx.kaimono

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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.usecase.IsLoggedInUseCase
import software.setixx.kaimono.presentation.auth.signin.SignInViewModel
import software.setixx.kaimono.presentation.auth.signup.SignUpViewModel
import software.setixx.kaimono.presentation.navigation.AuthNavHost
import software.setixx.kaimono.presentation.theme.KaimonoTheme
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : ComponentActivity() {

    @Inject
    lateinit var isLoggedInUseCase: IsLoggedInUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            if (isLoggedInUseCase()) {
                val intent = Intent(this@AuthActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
                return@launch
            }
        }

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