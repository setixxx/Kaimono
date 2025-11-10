package setixx.software.kaimono.presentation.screen.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import setixx.software.kaimono.presentation.navigation.Routes
import setixx.software.kaimono.R
import setixx.software.kaimono.core.component.AccountList
import setixx.software.kaimono.core.component.AddressSheetContent
import setixx.software.kaimono.core.component.CartList
import setixx.software.kaimono.core.component.PaymentMethodsSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    deliveryFee: Double = 0.00,
    total: Double = 0.00
){
    var showCardsBottomSheet by remember { mutableStateOf(false) }
    var showAddressBottomSheet by remember { mutableStateOf(false) }
    val cardsSheetState = rememberModalBottomSheetState()
    val addressSheetState = rememberModalBottomSheetState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* do something */ },
                expanded = true,
                icon = { Icon(Icons.Outlined.ArrowBack,
                    stringResource(R.string.action_checkout)) },
                text = { Text(text = stringResource(R.string.action_checkout)) },
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.action_checkout),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.Home.route){
                                popUpTo(Routes.Home.route){ inclusive = true }
                            }
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {  innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState(0))
        ) {
            Column(
                Modifier
                    .clip(MaterialTheme.shapes.large)
            ) {
                CartList(
                    quantity = 1,
                    product = "Product name",
                    price = 100.00,
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                CartList(
                    quantity = 1,
                    product = "Product name",
                    price = 100.00,
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                CartList(
                    quantity = 1,
                    product = "Product name",
                    price = 100.00,
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                CartList(
                    quantity = 1,
                    product = "Product name",
                    price = 100.00,
                    onClick = {}
                )
            }
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(MaterialTheme.shapes.large)
            ) {
                AccountList(
                    icon = Icons.Outlined.CreditCard,
                    contentDescription = stringResource(R.string.label_credit_card),
                    header = stringResource(R.string.label_credit_card),
                    onClick = {
                        showCardsBottomSheet = true
                    },
                    trailingIcon = Icons.Outlined.ChevronRight
                )
            }
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
            ) {
                AccountList(
                    icon = Icons.Outlined.LocationOn,
                    contentDescription = stringResource(R.string.label_address),
                    header = stringResource(R.string.label_address),
                    onClick = {
                        showAddressBottomSheet = true
                    },
                    trailingIcon = Icons.Outlined.ChevronRight
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.label_delivery_fee),
                    style = MaterialTheme.typography.labelLarge
                )
                Text(
                    deliveryFee.toString() + stringResource(R.string.label_currency),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 80.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.label_total),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    total.toString() + stringResource(R.string.label_currency),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (showCardsBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCardsBottomSheet = false },
                sheetState = cardsSheetState
            ) {
                PaymentMethodsSheetContent(
                    onClose = { showCardsBottomSheet = false }
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
fun CartScreenPreview(){
    CartScreen(navController = NavController(LocalContext.current))
}