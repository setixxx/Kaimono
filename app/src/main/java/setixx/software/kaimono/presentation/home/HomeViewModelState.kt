package setixx.software.kaimono.presentation.home

import setixx.software.kaimono.domain.model.Category
import setixx.software.kaimono.domain.model.Product

data class HomeViewModelState(
    val products: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: Category? = null,
    val query: String = "",
)
