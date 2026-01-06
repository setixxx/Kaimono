package setixx.software.kaimono.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.theme.KaimonoTheme

@Composable
fun ReviewItem(
    modifier: Modifier = Modifier,
    productName: String,
    reviewDate: String,
    rating: Int,
    reviewText: String,
    imageUrl: String?
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                bitmap = ImageBitmap.imageResource(R.drawable.placeholder),
                contentDescription = productName,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.large)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = productName,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = reviewDate,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            repeat(5) { index ->
                val (icon, tint) = if (index < rating) {
                    Pair(Icons.Filled.Star, MaterialTheme.colorScheme.tertiary)
                } else {
                    Pair(Icons.Outlined.Star, MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = reviewText,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Preview(showBackground = true, widthDp = 360)
@Composable
fun ReviewItemPreview() {
    KaimonoTheme {
        ReviewItem(
            productName = "Vintage Denim Jacket",
            reviewDate = "November 10, 2025",
            rating = 4,
            reviewText = "Great jacket! Fits well and the quality is superb for the price. Would definitely recommend.",
            imageUrl = null,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}