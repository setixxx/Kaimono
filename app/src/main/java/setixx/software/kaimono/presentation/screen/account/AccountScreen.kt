package setixx.software.kaimono.presentation.screen.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Reviews
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import setixx.software.kaimono.core.component.CustomList
import setixx.software.kaimono.R
import setixx.software.kaimono.core.component.PaymentMethodsSheetContent
import setixx.software.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavController
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        modifier = Modifier
            .padding(horizontal = 16.dp),
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
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clip(shape = MaterialTheme.shapes.large)
            ) {
                CustomList(
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
                CustomList(
                    Icons.Outlined.Collections,
                    stringResource(R.string.label_orders),
                    stringResource(R.string.label_orders),
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                CustomList(
                    Icons.Outlined.Reviews,
                    stringResource(R.string.label_reviews),
                    stringResource(R.string.label_reviews),
                    onClick = {}
                )
                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
                CustomList(
                    Icons.Outlined.Payment,
                    stringResource(R.string.label_payment_methods),
                    stringResource(R.string.label_payment_methods),
                    onClick = {
                        showBottomSheet = true
                    }
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                PaymentMethodsSheetContent(
                    onClose = { showBottomSheet = false }
                )
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    AccountScreen(navController = NavController(LocalContext.current))
}