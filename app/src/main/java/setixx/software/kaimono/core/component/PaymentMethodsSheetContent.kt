package setixx.software.kaimono.core.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.R

@Composable
fun PaymentMethodsSheetContent(
    onClose: () -> Unit
) {
    var selectedIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.title_payment_methods),
            style = MaterialTheme.typography.headlineSmall
        )
        Column(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .clip(MaterialTheme.shapes.large)
        ) {
            PaymentList(
                index = 0,
                selectedIndex = selectedIndex,
                header = stringResource(R.string.label_credit_card),
                onSelect = { selectedIndex = it }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
            PaymentList(
                index = 1,
                selectedIndex = selectedIndex,
                header = stringResource(R.string.label_credit_card),
                onSelect = { selectedIndex = it }
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 2.dp)
            PaymentList(
                index = 2,
                selectedIndex = selectedIndex,
                header = stringResource(R.string.label_credit_card),
                onSelect = { selectedIndex = it }
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onClose
            ) {
                Text(stringResource(R.string.action_close))
            }
            Button(
                onClick = onClose
            ) {
                Text(stringResource(R.string.action_add_card))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentMethodsSheetContentPreview(){
    PaymentMethodsSheetContent(onClose = {})
}