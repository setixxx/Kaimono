package software.setixx.kaimono.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.model.ProductRequest
import software.setixx.kaimono.domain.usecase.SearchProductsUseCase
import software.setixx.kaimono.presentation.common.ErrorMapper
import software.setixx.kaimono.presentation.home.filter.FilterViewModelState
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _state = MutableStateFlow(HomeViewModelState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        getProducts()
    }

    fun onQueryChange(newQuery: String) {
        _state.update { it.copy(query = newQuery) }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(500)
            getProducts()
        }
    }

    fun onRefreshChange(isRefreshing: Boolean){
        if (isRefreshing) {
            _state.update { it.copy(isRefreshing = true, page = 1, endReached = false) }
            getProducts()
        }
    }

    fun loadNextPage() {
        if (_state.value.isLoading || _state.value.endReached) return
        _state.update { it.copy(page = it.page + 1) }
        getProducts()
    }

    fun applyFilters(filterState: FilterViewModelState){
        _state.update {
            it.copy(
                categories = filterState.categories,
                selectedCategories = filterState.selectedCategories,
                selectedSortBy = filterState.selectedSortBy,
                selectedSortOrder = filterState.selectedSortOrder,
                minPrice = filterState.minPrice,
                maxPrice = filterState.maxPrice,
                inStockOnly = filterState.inStockOnly
            )
        }
        getProducts(filterState)
    }

    fun getProducts(filters: FilterViewModelState? = null) {
        viewModelScope.launch {
            val currentState = _state.value
            _state.update { it.copy(isLoading = true) }

            val request = ProductRequest(
                query = currentState.query.ifEmpty { null },
                categoryIds = filters?.selectedCategories?.joinToString(",") { it.id.toString() },
                minPrice = filters?.minPrice,
                maxPrice = filters?.maxPrice,
                inStockOnly = filters?.inStockOnly,
                sortBy = filters?.selectedSortBy,
                sortOrder = when (filters?.selectedSortOrder){
                    "Ascending" -> "asc"
                    "Descending" -> "desc"
                    else -> null
                },
                page = currentState.page,
                pageSize = 10
            )
            
            when (val result = searchProductsUseCase(request)) {
                is ApiResult.Success -> {
                    val newProducts = result.data.products
                    _state.update {
                        it.copy(
                            products = if (currentState.page == 1) newProducts else it.products + newProducts,
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = null,
                            endReached = newProducts.isEmpty() || newProducts.size < 10
                        )
                    }
                }
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                }
                ApiResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
