package setixx.software.kaimono.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.R

@Composable
fun ProductCard(
    bitmap: ImageBitmap,
    contentDescription: String,
    header: String,
    price: Int,
    onClick: () -> Unit
){
    Column(
        modifier = Modifier
            .width(176.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
    ){
        Image(
            bitmap,
            contentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large),
        )
        Text(header,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp, bottom = 4.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(price.toString() + stringResource(R.string.label_currency) + " • " + " 4.5",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.outline,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .padding(start = 8.dp)
                .padding(bottom = 4.dp)
        )
    }
}

@Preview
@Composable
fun ProductCardPreview(){
    ProductCard(ImageBitmap.imageResource(R.drawable.placeholder),
        "",
        "Product namessssssssssssssssssssssssssssssssssssssssss",
        100,
        onClick = {}
    )
}