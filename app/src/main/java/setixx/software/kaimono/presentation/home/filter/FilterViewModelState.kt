package setixx.software.kaimono.presentation.home.filter

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import setixx.software.kaimono.domain.model.Category

@Parcelize
data class FilterViewModelState(
    val categories: List<Category> = emptyList(),
    val selectedCategories: List<Category> = emptyList(),
    val sortBy: List<String> = listOf("Price", "Name", "Date"),
    val selectedSortBy: String = "",
    val sortOrder: List<String> = listOf("Ascending", "Descending"),
    val selectedSortOrder: String = "Ascending",
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val inStockOnly: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
) : Parcelable