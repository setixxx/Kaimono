package setixx.software.kaimono

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import setixx.software.kaimono.presentation.Main
import setixx.software.kaimono.core.ui.theme.KaimonoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KaimonoTheme {
                Main()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun BottomNavigationBarPreview() {
    Main()
}