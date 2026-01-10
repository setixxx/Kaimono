package setixx.software.kaimono.presentation.product.reviews

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.components.ReviewCardSquare

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductReviewsScreen(
    navController: NavController
){
    var showReviewBottomSheet by remember { mutableStateOf(false) }
    val reviewBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.label_reviews),
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            item(){
                Text(
                    modifier = Modifier
                        .padding(bottom = 8.dp),
                    text = stringResource(R.string.label_your_review),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal
                )
                ReviewCardSquare(
                    username = "talisencrw",
                    productName = "Vintage Denim Jacket",
                    reviewDate = "November 10, 2025",
                    reviewText = "Though not my very favourite movie about the infamous vampire, this is quite beautiful, we...",
                    rating = "9 / 10",
                    withImageAndDate = false,
                    isExpanded = true,
                    isEditable = true,
                    onUpdate = { showReviewBottomSheet = true },
                    onDelete = {}
                )
            }
            item(){
                Text(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    text = stringResource(R.string.label_all_reviews),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal
                )
            }
            items(5){
                ReviewCardSquare(
                    username = "talisencrw",
                    productName = "Vintage Denim Jacket",
                    reviewDate = "November 10, 2025",
                    reviewText = "Though not my very favourite movie about the infamous vampire, this is quite beautiful, we...",
                    rating = "9 / 10",
                    withImageAndDate = false,
                    isExpanded = true,
                    isEditable = false,
                )
            }
        }
        if (showReviewBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showReviewBottomSheet = false },
                sheetState = reviewBottomSheetState
            ) {
                ProductReviewSheet(
                    onClose = { showReviewBottomSheet = false },
                    isUpdate = true
                )
            }
        }
    }
}

@Preview
@Composable
fun ProductReviewsPreview(){
    ProductReviewsScreen(navController = rememberNavController())
}