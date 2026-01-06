package setixx.software.kaimono.presentation.account.orders

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.components.OrderCard
import setixx.software.kaimono.presentation.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountOrdersScreen(
    navController: NavController
){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.label_orders),
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
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) { index ->
                val orderId = "#${12345 + index}"
                val orderDate = "November ${10 - index}, 2025"
                val orderStatus = if (index % 2 == 0) "Delivered" else "In processing"
                val orderTotal = "${(index + 1) * 37}.50 ₽"

                OrderCard(
                    orderId = orderId,
                    orderDate = orderDate,
                    orderStatus = orderStatus,
                    orderTotal = orderTotal
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountOrdersPreview(){
    AccountOrdersScreen(navController = rememberNavController())
}