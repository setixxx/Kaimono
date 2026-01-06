package setixx.software.kaimono.presentation.account.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import setixx.software.kaimono.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountAddCardScreen(
    navController: NavController,
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_add_card),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {  paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = "",
                onValueChange = {  },
                label = { Text(stringResource(R.string.hint_card_number)) }
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 16.dp)
            ){
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    value = "",
                    onValueChange = {  },
                    label = { Text(stringResource(R.string.hint_card_date)) },
                )
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    value = "",
                    onValueChange = {  },
                    label = { Text(stringResource(R.string.hint_card_code)) }
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = "",
                onValueChange = {  },
                label = { Text(stringResource(R.string.hint_card_holder_name)) }
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {}
                ) {
                    Text(
                        stringResource(R.string.action_save_card)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AddCardScreenPreview(){
    AccountAddCardScreen(navController = NavController(LocalContext.current))
}