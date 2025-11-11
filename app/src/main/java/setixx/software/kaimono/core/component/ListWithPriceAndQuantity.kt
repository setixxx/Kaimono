package setixx.software.kaimono.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ListWithPriceAndQuantity(
    quantity: Int,
    product: String,
    price: Double,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "$quantity",
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
            text = product,
            style = MaterialTheme.typography.labelLarge
        )
        Text(
            modifier = Modifier
                .padding(vertical = 8.dp),
            text = "$price",
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
fun CartListPreview() {
    ListWithPriceAndQuantity(
        quantity = 1,
        product = "Product name",
        price = 100.00,
        onClick = {}
    )
}

