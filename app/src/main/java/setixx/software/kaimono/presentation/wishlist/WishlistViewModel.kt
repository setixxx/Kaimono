package setixx.software.kaimono.presentation.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import setixx.software.kaimono.domain.model.ApiResult
import setixx.software.kaimono.domain.usecase.ClearWishListUseCase
import setixx.software.kaimono.domain.usecase.GetUserWishListUseCase
import setixx.software.kaimono.presentation.common.ErrorMapper
import setixx.software.kaimono.presentation.home.HomeViewModelState
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlistUseCase: GetUserWishListUseCase,
    private val clearWishListUseCase: ClearWishListUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {
    private val _state = MutableStateFlow(WishlistViewModelState())
    val state = _state.asStateFlow()

    init {
        getWishlistItems()
    }

    fun getWishlistItems(){
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = getWishlistUseCase()){
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(
                        wishlist = result.data,
                        wishlistItem = result.data.wishListItem,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                }
                ApiResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    fun clearWishlist(){
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            when (val result = clearWishListUseCase()){
                is ApiResult.Success -> {
                    getWishlistItems()
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = errorMapper.mapToMessage(result.error)
                    )
                }
                ApiResult.Loading -> {
                    _state.value = _state.value.copy(isLoading = true)
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }

}