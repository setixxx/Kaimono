package software.setixx.kaimono.presentation.account.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import software.setixx.kaimono.domain.model.ApiResult
import software.setixx.kaimono.domain.usecase.GetOrdersUseCase
import software.setixx.kaimono.presentation.common.ErrorMapper
import javax.inject.Inject

@HiltViewModel
class AccountOrdersViewModel @Inject constructor(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val errorMapper: ErrorMapper
) : ViewModel() {

    private val _state = MutableStateFlow(AccountOrdersViewModelState())
    val state: StateFlow<AccountOrdersViewModelState> = _state.asStateFlow()

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getOrdersUseCase()) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(
                            orders = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
                is ApiResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMapper.mapToMessage(result.error)
                        )
                    }
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun clearError() {
        _state.update { it.copy(errorMessage = null) }
    }
}
