package software.setixx.kaimono.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import software.setixx.kaimono.R

@Composable
fun ProductCard(
    imageUrl: String?,
    contentDescription: String,
    header: String,
    price: Int,
    rating: Double?,
    onClick: () -> Unit,
    isEnable: Boolean = true
){
    Column(
        modifier = Modifier
            .width(176.dp)
            .clip(MaterialTheme.shapes.large)
            .then( if (isEnable) Modifier.clickable { onClick() } else Modifier )
            .then( if (isEnable) Modifier.background(MaterialTheme.colorScheme.surfaceContainer) else Modifier )
    ){
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )
        Text(header,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp, bottom = 4.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = buildString {
            append(price)
            append(stringResource(R.string.label_currency))
            if (rating != null) {
                append("  •  ")
                append(rating)
            }
        },
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = 8.dp)
                .padding(bottom = 4.dp)
        )
    }
}