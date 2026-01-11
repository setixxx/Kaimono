package setixx.software.kaimono.presentation.home.filter

import androidx.compose.animation.core.copy
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.model.Category
import setixx.software.kaimono.domain.usecase.GetCategoriesUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val errorMapper: ErrorMapper,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private fun getInitialState(): FilterViewModelState {
        return savedStateHandle.get<FilterViewModelState>("filter_state") ?: FilterViewModelState()
    }
    private val _state = MutableStateFlow(getInitialState())
    val state: StateFlow<FilterViewModelState> = _state.asStateFlow()

    init {
        loadCategories()
        observeExternalChanges()
    }

    private fun observeExternalChanges() {
        viewModelScope.launch {
            savedStateHandle.getStateFlow<FilterViewModelState?>("filter_state", null)
                .filterNotNull()
                .collect { externalState ->
                    _state.update { currentState ->
                        externalState.copy(
                            categories = if (currentState.categories.isNotEmpty())
                                currentState.categories
                            else externalState.categories,
                            isLoading = currentState.isLoading,
                            errorMessage = currentState.errorMessage
                        )
                    }
                }
        }
    }

    private fun updateState(update: (FilterViewModelState) -> FilterViewModelState) {
        _state.update { 
            val newState = update(it)
            savedStateHandle["filter_state"] = newState
            newState
        }
    }

    fun loadCategories(){
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }

            when (val result = getCategoriesUseCase()) {
                is ApiResult.Success -> {
                    updateState {
                        it.copy(
                            isLoading = false,
                            categories = result.data,
                            errorMessage = null
                        )
                    }
                }
                is ApiResult.Error -> {
                    updateState {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                }
                is ApiResult.Loading -> {
                    updateState { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun onSortByChange(sortBy: String) {
        updateState { it.copy(selectedSortBy = sortBy) }
    }

    fun onSortOrderChange(sortOrder: String) {
        updateState { it.copy(selectedSortOrder = sortOrder) }
    }

    fun onMinPriceChange(minPrice: Int?) {
        updateState { it.copy(minPrice = minPrice) }
    }

    fun onMaxPriceChange(maxPrice: Int?) {
        updateState { it.copy(maxPrice = maxPrice) }
    }

    fun onInStockOnlyChange(inStockOnly: Boolean) {
        updateState { it.copy(inStockOnly = inStockOnly) }
    }

    fun onCategorySelected(category: Category) {
        val currentCategories = _state.value.selectedCategories
        val updatedCategories = if (currentCategories.any { it.id == category.id }) {
            currentCategories.filterNot { it.id == category.id }
        } else {
            currentCategories + category
        }
        updateState { it.copy(selectedCategories = updatedCategories) }
    }

    fun clearFilters(){
        updateState {
            it.copy(
                selectedCategories = emptyList(),
                selectedSortBy = "",
                selectedSortOrder = "Ascending",
                minPrice = null,
                maxPrice = null,
                inStockOnly = false
            )
        }
    }

    fun clearError() {
        updateState { it.copy(errorMessage = null) }
    }
}
