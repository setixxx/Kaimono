package setixx.software.kaimono.presentation.account.reviews

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import setixx.software.kaimono.R
import setixx.software.kaimono.presentation.components.ReviewItem
import setixx.software.kaimono.presentation.navigation.Routes

private data class ReviewData(
    val id: Int,
    val productName: String,
    val reviewDate: String,
    val rating: Int,
    val reviewText: String,
    val imageUrl: String?
)

private val mockReviews = listOf(
    ReviewData(
        1,
        "Vintage Denim Jacket",
        "November 10, 2025",
        4,
        "Great jacket! Fits well and the quality is superb for the price. Would definitely recommend.",
        null
    ),
    ReviewData(
        2,
        "Leather Ankle Boots",
        "November 5, 2025",
        5,
        "Absolutely in love with these boots. They are comfortable and look amazing.",
        null
    ),
    ReviewData(
        3,
        "Classic White T-Shirt",
        "October 28, 2025",
        3,
        "It's an okay t-shirt. A bit thin for my liking, but the fit is good.",
        null
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountReviewsScreen(
    navController: NavController
) {
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
                            navController.navigate(Routes.Account.route){
                                popUpTo(Routes.Account.route){ inclusive = true }
                            }                        }
                    ) {
                        Icon(Icons.Outlined.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            itemsIndexed(mockReviews, key = { _, item -> item.id }) { index, item ->
                ReviewItem(
                    productName = item.productName,
                    reviewDate = item.reviewDate,
                    rating = item.rating,
                    reviewText = item.reviewText,
                    imageUrl = item.imageUrl
                )

                if (index < mockReviews.lastIndex) {
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountReviewsPreview() {
    AccountReviewsScreen(navController = rememberNavController())
}