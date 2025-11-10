package setixx.software.kaimono.core.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.CreditCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AccountList(
    icon: ImageVector,
    contentDescription: String,
    header: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    trailingIcon: ImageVector? = null
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onClick)
            .padding(top = 12.dp, bottom = 12.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ){
            Icon(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .size(24.dp),
                imageVector = icon,
                contentDescription = contentDescription)
            Text(
                modifier = Modifier
                    .padding(vertical = 8.dp),
                text = header,
                style = MaterialTheme.typography.labelLarge
            )
        }
        trailingIcon?.let {
            Icon(
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .size(24.dp),
                imageVector = it,
                contentDescription = "Credit card"
            )
        }
    }
}

@Preview
@Composable
fun CustomListPreview(){
    AccountList(
        icon = Icons.Outlined.CreditCard,
        contentDescription = "Credit card",
        header = "Credit card",
        onClick = {}
    )
}
