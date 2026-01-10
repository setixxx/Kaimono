package setixx.software.kaimono.presentation.product.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.R

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductReviewSheet(
    onClose: () -> Unit = {},
    isUpdate: Boolean = false
){
    var reviewText by remember { mutableStateOf("") }
    val options = listOf("1", "2", "3", "4", "5")
    var selectedIndex by remember { mutableIntStateOf(0) }


    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isUpdate) stringResource(R.string.label_edit_review) else stringResource(R.string.label_create_review),
                style = MaterialTheme.typography.headlineSmall
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                value = reviewText,
                minLines = 5,
                maxLines = 5,
                onValueChange = { reviewText = it },
                label = { Text(stringResource(R.string.hint_text_of_review)) },
                singleLine = false,
                enabled = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
            ) {
                options.forEachIndexed { index, label ->
                    ToggleButton(
                        checked = selectedIndex == index,
                        onCheckedChange = { selectedIndex = index },
                        modifier = Modifier.weight(1f),
                        shapes =
                            when (index) {
                                0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                options.lastIndex -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                            },
                        enabled = true
                    ) {
                        if (selectedIndex == index) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                        }
                        Text(label)
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(top = 8.dp),
            ) {
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp),
                    onClick = {
                        onClose()
                    },
                    colors = ButtonColors(
                        containerColor = MaterialTheme.colorScheme.outlineVariant,
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.outlineVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text(text = stringResource(R.string.action_cancel))
                }
                Button(
                    modifier = Modifier
                        .weight(2f)
                        .padding(start = 4.dp),
                    onClick = {
                        onClose()
                    }
                ) {
                    Text(text = if (isUpdate) stringResource(R.string.acton_update) else stringResource(R.string.acton_create))
                }
            }
        }
    }
}

@Preview
@Composable
fun ProductReviewSheetPreview(){
    ProductReviewSheet()
}