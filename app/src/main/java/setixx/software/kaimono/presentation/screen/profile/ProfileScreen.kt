package setixx.software.kaimono.presentation.screen.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.Reviews
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.core.component.ProfileList
import setixx.software.kaimono.R

@Composable
fun ProfileScreen(
){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(shape = MaterialTheme.shapes.large)
        ) {
            ProfileList(Icons.Outlined.Info,
                stringResource(R.string.label_personal_info),
                stringResource(R.string.label_personal_info))
        }
        Column (
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(shape = MaterialTheme.shapes.large)
        ) {
            ProfileList(Icons.Outlined.Collections,
                stringResource(R.string.label_orders),
                stringResource(R.string.label_orders))
            HorizontalDivider(color = MaterialTheme.colorScheme.background)
            ProfileList(Icons.Outlined.Reviews,
                stringResource(R.string.label_reviews),
                stringResource(R.string.label_reviews))
            HorizontalDivider(color = MaterialTheme.colorScheme.background)
            ProfileList(Icons.Outlined.Payment,
                stringResource(R.string.label_payment_methods),
                stringResource(R.string.label_payment_methods))
        }
        Button(
            onClick = {}
        ) {
            Text(text = stringResource(R.string.action_logout))
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfileScreenPreview(){
    ProfileScreen()
}