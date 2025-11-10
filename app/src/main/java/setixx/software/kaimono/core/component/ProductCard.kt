package setixx.software.kaimono.core.component

import android.graphics.Bitmap
import android.util.Printer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
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
            .height(260.dp)
            .width(176.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick)
            .background(MaterialTheme.colorScheme.surfaceContainer)
    ){
        Image(
            bitmap,
            contentDescription,
            modifier = Modifier
                .clip(MaterialTheme.shapes.large),
        )
        Text(header,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Text(price.toString() + stringResource(R.string.label_currency),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .padding(start = 16.dp)
        )
    }
}

@Preview
@Composable
fun ProductCardPreview(){
    ProductCard(ImageBitmap.imageResource(R.drawable.placeholder),
        "",
        "Product name",
        100,
        onClick = {}
    )
}