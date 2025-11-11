package setixx.software.kaimono.core.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import setixx.software.kaimono.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductImageCarousel(
    images: List<ImageBitmap>,
    modifier: Modifier = Modifier
) {
    if (images.isEmpty()) return

    HorizontalCenteredHeroCarousel(
        state = rememberCarouselState { images.size },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp),
        maxItemWidth = 270.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { i ->
        Image(
            modifier = Modifier
                .height(270.dp)
                .maskClip(MaterialTheme.shapes.extraLarge),
            bitmap = images[i],
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun ProductImageCarouselPreview() {
    ProductImageCarousel(
        images = listOf(
            ImageBitmap.imageResource(R.drawable.placeholder),
            ImageBitmap.imageResource(R.drawable.placeholder),
            ImageBitmap.imageResource(R.drawable.placeholder),
        )
    )
}
