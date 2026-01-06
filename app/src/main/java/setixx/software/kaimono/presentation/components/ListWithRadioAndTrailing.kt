package setixx.software.kaimono.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.R

@Composable
fun ListWithRadioAndTrailing(
    index: Int,
    selectedIndex: Int,
    header: String,
    modifier: Modifier = Modifier,
    onSelect: (Int) -> Unit
){
    var selected by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .clickable { onSelect(index) }
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = index == selectedIndex,
            onClick = { selected = !selected }
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            text = header,
            style = MaterialTheme.typography.labelLarge
        )
        IconButton(
            onClick = {}
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp),
                imageVector = Icons.Outlined.Close,
                contentDescription = stringResource(R.string.action_add_card)
            )
        }
    }
}

@Preview
@Composable
fun PaymentListPreview(){
    ListWithRadioAndTrailing(
        index = 0,
        selectedIndex = 0,
        header = "Credit card",
        onSelect = {}
    )
}