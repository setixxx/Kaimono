package setixx.software.kaimono.presentation.screen.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import setixx.software.kaimono.R
import setixx.software.kaimono.core.component.FavoriteItemCard
import setixx.software.kaimono.core.component.ProductSheetContent

private data class FavoriteItem(
    val id: Int,
    val imageUrl: String?,
    val name: String,
    val size: String,
    val price: String
)

private val mockFavorites = listOf(
    FavoriteItem(1, null, "Vintage Denim Jacket", "L", "$75.00"),
    FavoriteItem(2, null, "Silk Blend Scarf", "One Size", "$45.50"),
    FavoriteItem(3, null, "Leather Ankle Boots", "9", "$120.00"),
    FavoriteItem(4, null, "Classic White T-Shirt", "M", "$25.00")
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FavoritesScreen(
    navController: NavController
) {
    var showProductBottomSheet by remember { mutableStateOf(false) }
    val productSheetState = rememberModalBottomSheetState(true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.label_favorites),
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockFavorites, key = { it.id }) { item ->
                FavoriteItemCard(
                    ImageBitmap.imageResource(R.drawable.placeholder),
                    name = item.name,
                    size = item.size,
                    price = item.price,
                    onClick = { showProductBottomSheet = true}
                )
            }
        }
        if (showProductBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showProductBottomSheet = false },
                sheetState = productSheetState
            ) {
                ProductSheetContent(
                    images = listOf(
                        ImageBitmap.imageResource(R.drawable.placeholder),
                        ImageBitmap.imageResource(R.drawable.placeholder),
                        ImageBitmap.imageResource(R.drawable.placeholder),
                    ),                    contentDescription = "",
                    header = "Product name",
                    price = 100,
                    onClose = { showProductBottomSheet = false }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoritesScreenPreview() {
    FavoritesScreen(navController = rememberNavController())
}