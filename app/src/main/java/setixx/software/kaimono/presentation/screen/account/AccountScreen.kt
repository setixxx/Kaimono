package setixx.software.kaimono.presentation.screen.account

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import setixx.software.kaimono.core.component.ListWithTwoIcons
import setixx.software.kaimono.R
import setixx.software.kaimono.core.component.AddressSheetContent
import setixx.software.kaimono.core.component.PaymentMethodsSheetContent
import setixx.software.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController,
    onLogout: () -> Unit
) {
    var showCardsBottomSheet by remember { mutableStateOf(false) }
    var showAddressBottomSheet by remember { mutableStateOf(false) }
    val cardsSheetState = rememberModalBottomSheetState()
    val addressSheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(
                            R.string.label_greeting,
                            "Maxim"),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(shape = MaterialTheme.shapes.large)
            ) {
                ListWithTwoIcons(
                    Icons.Outlined.Info,
                    stringResource(R.string.label_personal_info),
                    stringResource(R.string.label_personal_info),
                    onClick = {
                        navController.navigate(Routes.AccountInfo.route)
                    }
                )
            }

            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(shape = MaterialTheme.shapes.large)
            ) {
                ListWithTwoIcons(
                    Icons.Outlined.Collections,
                    stringResource(R.string.label_orders),
                    stringResource(R.string.label_orders),
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                ListWithTwoIcons(
                    Icons.Outlined.Reviews,
                    stringResource(R.string.label_reviews),
                    stringResource(R.string.label_reviews),
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                ListWithTwoIcons(
                    Icons.Outlined.Payment,
                    stringResource(R.string.label_payment_methods),
                    stringResource(R.string.label_payment_methods),
                    onClick = {
                        showCardsBottomSheet = true
                    }
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                ListWithTwoIcons(
                    Icons.Outlined.LocationOn,
                    stringResource(R.string.label_address),
                    stringResource(R.string.label_address),
                    onClick = {
                        showAddressBottomSheet = true
                    }
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = onLogout
                ){
                    Text(
                        stringResource(R.string.action_logout)
                    )
                }
            }
        }

        if (showCardsBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCardsBottomSheet = false },
                sheetState = cardsSheetState
            ) {
                PaymentMethodsSheetContent(
                    onClose = { showCardsBottomSheet = false },
                    onAddCard = {
                        navController.navigate(Routes.AccountAddCard.route)
                    }
                )
            }
        }

        if (showAddressBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAddressBottomSheet = false },
                sheetState = addressSheetState
            ) {
                AddressSheetContent(
                    onClose = { showAddressBottomSheet = false }
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    AccountScreen(navController = NavController(LocalContext.current), onLogout = {})
}