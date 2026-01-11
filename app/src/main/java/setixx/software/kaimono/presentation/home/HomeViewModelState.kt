package setixx.software.kaimono.presentation.home

import setixx.software.kaimono.domain.model.Category
import setixx.software.kaimono.domain.model.Product

data class HomeViewModelState(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategories: List<Category> = emptyList(),
    val query: String = "",
    val selectedSortBy: String = "",
    val selectedSortOrder: String = "",
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val inStockOnly: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRefreshing: Boolean = false,
    val page: Int = 1,
    val endReached: Boolean = false
)
