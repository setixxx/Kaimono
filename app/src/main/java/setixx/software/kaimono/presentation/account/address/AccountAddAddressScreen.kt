package setixx.software.kaimono.presentation.account.address

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
fun AccountAddAddressScreen(
    navController: NavController,
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_add_address),
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
                label = { Text(stringResource(R.string.hint_city)) }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = "",
                onValueChange = {  },
                label = { Text(stringResource(R.string.hint_street)) }
            )
            Row(
                modifier = Modifier
                    .padding(vertical = 8.dp)
            ){
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    value = "",
                    onValueChange = {  },
                    label = { Text(stringResource(R.string.hint_house)) },
                )
                OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    value = "",
                    onValueChange = {  },
                    label = { Text(stringResource(R.string.hint_apartment)) }
                )
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = "",
                onValueChange = {  },
                label = { Text(stringResource(R.string.hint_postal_code)) }
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                value = "",
                onValueChange = {  },
                label = { Text(stringResource(R.string.hint_additional_info)) },
                supportingText = { Text(stringResource(R.string.hint_additional_info_description)) },
                minLines = 3,
                maxLines = 5
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        // Сохранение адреса
                        navController.popBackStack()
                    }
                ) {
                    Text(
                        stringResource(R.string.action_save_address)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun AddAddressScreenPreview(){
    AccountAddAddressScreen(navController = NavController(LocalContext.current))
}