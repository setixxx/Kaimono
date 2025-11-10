package setixx.software.kaimono.presentation.screen.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import setixx.software.kaimono.presentation.navigation.Routes
import setixx.software.kaimono.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInfoScreen(
    navController: NavController
){
    var nameFieldState by remember { mutableStateOf(false) }
    var surnameFieldState by remember { mutableStateOf(false) }
    var phoneFieldState by remember { mutableStateOf(false) }
    var emailFieldState by remember { mutableStateOf(false) }
    var dateOfBirthFieldState by remember { mutableStateOf(false) }

    val states = listOf(
        nameFieldState,
        surnameFieldState,
        phoneFieldState,
        emailFieldState,
        dateOfBirthFieldState)


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Account Info",
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.Account.route){
                                popUpTo(Routes.Account.route){ inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Routes.Cart.route) },
                expanded = states.any { it },
                icon = { Icon(Icons.Filled.Save,
                    stringResource(R.string.action_save)) },
                text = { Text(text = stringResource(R.string.action_save)) },
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .padding(end = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 24.dp),
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Person",
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = "",
                        onValueChange = { nameFieldState = true },
                        label = { Text(stringResource(R.string.hint_name)) }
                    )
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 48.dp, top = 8.dp, bottom = 8.dp),
                    value = "",
                    onValueChange = { surnameFieldState = true },
                    label = { Text(stringResource(R.string.hint_surname)) }
                )
                Row(

                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(
                        modifier = Modifier
                            .padding(end = 24.dp),
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "Person",
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = "",
                        onValueChange = { phoneFieldState = true },
                        label = { Text(stringResource(R.string.hint_phone)) }
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(
                        modifier = Modifier
                            .padding(end = 24.dp),
                        imageVector = Icons.Outlined.Email,
                        contentDescription = "Person",
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = "",
                        onValueChange = { emailFieldState = true },
                        label = { Text(stringResource(R.string.hint_email)) }
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Icon(
                        modifier = Modifier
                            .padding(end = 24.dp),
                        imageVector = Icons.Outlined.DateRange,
                        contentDescription = "Person",
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        value = "",
                        onValueChange = { dateOfBirthFieldState = true },
                        label = { Text(stringResource(R.string.hint_date_of_birth)) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountInfoScreenPreview(){
    AccountInfoScreen(navController = NavController(LocalContext.current))
}