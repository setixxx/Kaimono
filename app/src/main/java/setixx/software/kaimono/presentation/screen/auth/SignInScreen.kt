package setixx.software.kaimono.presentation.screen.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.R
@Composable
fun SignInScreen(
    modifier: Modifier = Modifier,
    onAction: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.label_sign_in),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(bottom = 24.dp),
            text = stringResource(R.string.sublabel_sign_in),
            style = MaterialTheme.typography.titleLarge,
        )
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.hint_email)) },
        )
        OutlinedTextField(
            modifier = Modifier.padding(vertical = 8.dp),
            value = "",
            onValueChange = {},
            label = { Text(stringResource(R.string.hint_password)) },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                onClick = {
                    onAction
                }
            ) {
                Text(text = stringResource(R.string.action_login))
            }
            Button(
                onClick = {
                    onAction
                }
            ) {
                Text(text = stringResource(R.string.action_register))
            }
        }
    }
}